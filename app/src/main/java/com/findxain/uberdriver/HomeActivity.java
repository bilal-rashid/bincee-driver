package com.findxain.uberdriver;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;

import com.findxain.uberdriver.activity.ContectUsActivity;
import com.findxain.uberdriver.activity.ProfileActivity;
import com.findxain.uberdriver.api.firestore.Ride;
import com.findxain.uberdriver.api.model.Student;
import com.findxain.uberdriver.api.model.LoginResponse;
import com.findxain.uberdriver.api.model.MyResponse;
import com.findxain.uberdriver.base.BA;
import com.findxain.uberdriver.databinding.ActivityHomeBinding;
import com.findxain.uberdriver.dialog.FinishRideDialog;
import com.findxain.uberdriver.fragment.AttendanceFragemnt;
import com.findxain.uberdriver.fragment.HomeFragment;
import com.findxain.uberdriver.fragment.MapFragment;
import com.findxain.uberdriver.fragment.MyPowerFragemnt;
import com.findxain.uberdriver.helper.PermissionHelper;
import com.findxain.uberdriver.observer.EndpointObserver;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class HomeActivity extends BA {

    public static final String MY_POWER = "- My Power";
    public static final String HOME = "- Home";
    public static final String MY_PROFILE = "- My Profile";
    public static final String CONTACT_US = "- Contact Bincee";
    @BindView(R.id.imageViewProfilePic)
    ImageView imageViewProfilePic;
    @BindView(R.id.textViewUsername)
    TextView textViewUsername;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.navigationView)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.textViewTitle)
    public TextView textViewTitle;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.bottomNavigationView)
    BottomNavigationView bottomNavigationView;
    private List<String> menuItem;

    public LiveData liveData;
    ActivityHomeBinding binding;
    private FirebaseFirestore db;
    private LocationCallback mLocationCallback;
    private FusedLocationProviderClient mFusedLocationClient;
    private boolean mRequestingLocationUpdates = true;
    private PermissionHelper permissionHelper;

    public Ride ride;
    private String TAG = HomeActivity.class.getSimpleName();
    public Location myLocaton;
    public DocumentReference userDocument;
    private boolean fetchingFromFS = false;
    private ProgressDialog progressDialog;
    private CollectionReference history;


    public static void start(Context context) {
        context.startActivity(new Intent(context, HomeActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home);
        liveData = ViewModelProviders.of(this).get(LiveData.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        binding.setVm(liveData);
        liveData.user.observe(this, user -> binding.userLayout.textViewUsername.setText(user.username));

        db = FirebaseFirestore.getInstance();
        userDocument = db.collection("user").document(MyApp.instance.user.id + "");


        history = db.collection("history");


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Wait...");
        progressDialog.show();

        fetchingFromFS = true;
        userDocument.get().addOnCompleteListener(this, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                fetchingFromFS = false;
                progressDialog.dismiss();

                if (task.isSuccessful()) {

                    DocumentSnapshot result = task.getResult();
                    if (result != null) {
                        ride = result.toObject(Ride.class);
                    }
                    setupBottemNavListner();


                } else {
                    task.getException().printStackTrace();

                }

            }
        });


        textViewTitle.setText("Home");
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.threel_line);


        menuItem = new ArrayList<String>();

        menuItem.add(MY_POWER);
        menuItem.add(MY_PROFILE);
        menuItem.add("- Route Designer");
        menuItem.add("- FAQ");
        menuItem.add(CONTACT_US);

        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setLayoutFrozen(true);
        recycleView.setAdapter(new RecyclerView.Adapter<NavigationVH>() {
            @NonNull
            @Override
            public NavigationVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new NavigationVH(LayoutInflater.from(HomeActivity.this).inflate(R.layout.naviation_row, parent, false));
            }

            @Override
            public void onBindViewHolder(@NonNull NavigationVH holder, int position) {
                holder.bind();

            }

            @Override
            public int getItemCount() {
                return menuItem.size();
            }
        });

        bottomNavigationView.setItemIconTintList(null);


        liveData.students.observe(this, new Observer<List<Student>>() {
            @Override
            public void onChanged(List<Student> students) {
                if (students == null || students.size() == 0) return;
                MyApp.showToast(students.size() + " Items");
                bottomNavigationView.setSelectedItemId(R.id.bottomNavigationAttendance);

                if (ride == null) {
                    ride = new Ride();
                }
                ride.rideId = UUID.randomUUID().toString();
                ride.startTime = Timestamp.now();
                ride.rideInProgress = true;
                ride.students = students;
                ride.driverId = MyApp.instance.user.id;


                userDocument.set(ride)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Log.d(TAG, "onSuccess: User Ride Stated Succefully");
                            }
                        })
                        .addOnFailureListener(HomeActivity.this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();

                            }
                        });


            }
        });

        liveData.createRideListner.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                binding.progressBar.setVisibility(aBoolean ? View.VISIBLE : View.GONE);

            }
        });


        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                List<Location> locations = locationResult.getLocations();
                for (Location location : locations) {
                    // Update UI with location data
                    // ...
                }

                if (locations.size() > 0 && locations.get(0) != null) {
                    Location location = locations.get(0);
                    HomeActivity.this.myLocaton = location;

                    MapFragment.getInstance().setMyLocation(myLocaton);

                    if (ride != null && ride.rideInProgress) {

                        ride.latLng = new GeoPoint(location.getLatitude(), location.getLongitude());
                        userDocument.set(ride).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                Log.d(TAG, "Location Updated");
                            }
                        });


                    }


                }
            }


        };

        mFusedLocationClient = new FusedLocationProviderClient(this);

    }

    private void setupBottemNavListner() {
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {

            switch (menuItem.getItemId()) {
                case R.id.bottomNavigationMap:
//                    List<Student> studentList = liveData.students.getValue();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, ride == null ? HomeFragment.getInstance() : (ride.rideInProgress ? MapFragment.getInstance() : HomeFragment.getInstance()))
                            .commit();
                    break;
                case R.id.bottomNavigationAttendance:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, AttendanceFragemnt.getInstance())
                            .commit();
                    break;
                case R.id.bottomNavigationPowerScreen:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, MyPowerFragemnt.getInstance())
                            .commit();
                    break;
            }
            return true;
        });
        bottomNavigationView.setSelectedItemId(R.id.bottomNavigationMap);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(Gravity.LEFT, true);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void finishRide() {
        ride.rideInProgress = false;
        ride.endTime = Timestamp.now();
        userDocument.set(ride).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {


            }
        });
        userDocument.delete();

        history.add(ride).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {

                ride = null;
            }
        });
        liveData.students.setValue(new ArrayList<>());


        new FinishRideDialog(this).setListner(new FinishRideDialog.Listner() {
            @Override
            public void startNewShft() {
                recreate();


            }

            @Override
            public void logOut() {
                finish();

            }
        }).show();


    }

    public void updateAttendance() {
        userDocument.set(ride);
    }

    public class NavigationVH extends RecyclerView.ViewHolder {
        @BindView(R.id.textView)
        TextView textView;

        public NavigationVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind() {
            textView.setText(menuItem.get(getAdapterPosition()));
        }

        @OnClick(R.id.textView)
        public void onMenuItemClicked() {

            drawerLayout.closeDrawer(Gravity.LEFT);
            if (textView.getText().toString().equalsIgnoreCase(MY_POWER)) {


                bottomNavigationView.setSelectedItemId(R.id.bottomNavigationPowerScreen);
            } else if (textView.getText().toString().equalsIgnoreCase(HOME)) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, HomeFragment.getInstance())
                        .commit();
            } else if (textView.getText().toString().equalsIgnoreCase(MY_POWER)) {
                ProfileActivity.start(HomeActivity.this);

            } else if (textView.getText().toString().equalsIgnoreCase(CONTACT_US)) {
                ContectUsActivity.start(HomeActivity.this);
            } else if (textView.getText().toString().equalsIgnoreCase(MY_PROFILE)) {
                ProfileActivity.start(HomeActivity.this);
            }


        }
    }


    public static class LiveData extends ViewModel {

        public MutableLiveData<LoginResponse.User> user = new MutableLiveData<>();
        private CompositeDisposable compositeDisposable;


        public MutableLiveData<List<Student>> students = new MutableLiveData<>();
        public MutableLiveData<Boolean> createRideListner = new MutableLiveData<>();


        public LiveData() {
            compositeDisposable = new CompositeDisposable();
            user.setValue(MyApp.instance.user);
        }


        public void startRide(String shiftId) {
            createRideListner.setValue(true);
            EndpointObserver<MyResponse<List<Student>>> endpointObserver = MyApp.endPoints
                    .createRide(MyApp.instance.user.id + "", shiftId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new EndpointObserver<MyResponse<List<Student>>>() {
                        @Override
                        public void onComplete() {

                            createRideListner.setValue(false);

                        }

                        @Override
                        public void onData(MyResponse<List<Student>> data) throws Exception {

                            if (data.status == 200) {

                                LiveData.this.students.setValue(data.data);


                            } else {
                                throw new Exception(data.status + "");
                            }

                        }

                        @Override
                        public void onHandledError(Throwable e) {
                            e.printStackTrace();
                            MyApp.showToast(e.getMessage());

                        }
                    });

            compositeDisposable.add(endpointObserver);

        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRequestingLocationUpdates) {
            permissionHelper = new PermissionHelper();
            permissionHelper
                    .with(this)
                    .permissionId(12)
                    .requiredPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION})
                    .setListner(new PermissionHelper.PermissionCallback() {
                        @Override
                        public void onPermissionGranted() {

                            startLocationUpdates();

                        }

                        @Override
                        public void onPermissionFailed() {

                            MyApp.showToast("Location Permission Failed");
                        }
                    }).request();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        mFusedLocationClient.requestLocationUpdates(getLocatonRequest(),
                mLocationCallback,
                null /* Looper */);
    }

    private LocationRequest getLocatonRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(1000);
        return locationRequest;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionHelper != null) {
            permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
