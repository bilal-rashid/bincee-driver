package com.bincee.driver.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.bincee.driver.HomeActivity;
import com.bincee.driver.R;
import com.bincee.driver.api.firestore.Driver;
import com.bincee.driver.api.firestore.FireStoreHelper;
import com.bincee.driver.api.firestore.Ride;
import com.bincee.driver.helper.MyPref;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.GeoPoint;

public class MyService extends Service {
    static final int NOTIFICATION_ID = 543;

    public static boolean isServiceRunning = false;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 1000;  /* 1 sec */
    private long FASTEST_INTERVAL = 500; /* 1/2 sec */
    private Location myLocation;
    private Location myLastLocation;
    private Float direction=0f;
    LocationManager manager;
    public DocumentReference rideDocument;

    @Override
    public void onCreate() {
        super.onCreate();
        startServiceWithNotification();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        myLastLocation = null;
        locationCallback=new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        };
        try {
            rideDocument = FireStoreHelper.getRealTimeDriver();
        }catch (Exception e){}
        startLocationUpdates();
        return START_STICKY;
    }
    private android.os.Handler mHandler;



    public LocationCallback locationCallback;
    protected void startLocationUpdates() {
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return;
        }
        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        final LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, locationCallback,
                Looper.myLooper());
    }

    public void onLocationChanged(Location location) {
        // New location has now been determined
        myLocation = location;
        if(myLastLocation!=null){
            float bearTo=myLastLocation.bearingTo(location);
            if (bearTo < 0)
                bearTo = bearTo + 360;
            direction = bearTo;
        }
        myLastLocation = location;
        sendToFirebase();


    }

    private void sendToFirebase() {
        Driver driver = new Driver();
        driver.latLng = new GeoPoint(myLocation.getLatitude(),myLocation.getLongitude());
        driver.driverDirection = direction;
        Ride ride = MyPref.getRide(this);
        if(ride!=null){
            driver.rideId = ride.rideId;
            driver.shift = ride.shift;
        }else {
            driver.rideId = null;
            driver.shift = null;
        }
        try {
            rideDocument.set(driver);
        }catch (Exception h){}
    }


    public void onResume() {
    }

    @Override
    public void onDestroy() {
        LocationServices.getFusedLocationProviderClient(getApplicationContext()).removeLocationUpdates(locationCallback);
        isServiceRunning = false;
        super.onDestroy();
        try{
        }catch (Exception e){

        }
    }
    void startServiceWithNotification() {
        if (isServiceRunning) return;
        isServiceRunning = true;

        Intent notificationIntent = new Intent(getApplicationContext(), HomeActivity.class);
        notificationIntent.setAction("C.ACTION_MAIN");  // A string containing the action name
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_circle);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setTicker(getResources().getString(R.string.app_name))
                .setContentText("Location")
                .setSmallIcon(R.drawable.ic_circle)
                .setContentIntent(contentPendingIntent)
                .setOngoing(true)
//                .setDeleteIntent(contentPendingIntent)  // if needed
                .build();
        notification.flags = notification.flags | Notification.FLAG_NO_CLEAR;     // NO_CLEAR makes the notification stay when the user performs a "delete all" command
        startForeground(NOTIFICATION_ID, notification);
    }

}
