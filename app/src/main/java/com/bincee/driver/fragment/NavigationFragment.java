package com.bincee.driver.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.bincee.driver.BuildConfig;
import com.bincee.driver.HomeActivity;
import com.bincee.driver.R;
import com.bincee.driver.api.firestore.Ride;
import com.bincee.driver.api.model.Student;
import com.bincee.driver.base.BFragment;
import com.bincee.driver.helper.ImageBinder;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationFragment extends BFragment

//        implements  OnNavigationReadyCallback,
//        NavigationListener, RouteListener, ProgressChangeListener
{


    public static String getToken() {
        if (BuildConfig.FLAVOR.equalsIgnoreCase("client")) {
            //client
            return "pk.eyJ1IjoiYmluY2VlIiwiYSI6ImNqc2E2Nm0wYjAwaGM0OXFjd3kxazBnNmYifQ.Wnu7rjFfU_qpl1Pmi062vg";
        } else {
            return "pk.eyJ1IjoiZmluZHhhaW4iLCJhIjoiY2pxOTY1bjY3MTMwYjQzbDEwN3h2aTdsbCJ9.fKLD1_UzlMIWhXfUZ3aRYQ";

        }


    }

    private static NavigationFragment mapFragment;
    //    @BindView(R.id.mapView)
//    NavigationView mapView;
    @BindView(R.id.textViewFinishRide)
    TextView textViewFinishRide;
    @BindView(R.id.constraintLayout)
    ConstraintLayout constraintLayout;
    @BindView(R.id.imageView9)
    ImageView imageView9;
    @BindView(R.id.textViewName)
    TextView textViewName;
    @BindView(R.id.textViewNameKM)
    TextView textViewNameKM;
    @BindView(R.id.textViewTime)
    TextView textViewTime;

    private Unbinder bind;
    private Location lastKnownLocation;


    private List<Point> points = new ArrayList<>();
    private boolean dropoffDialogShown;


    public NavigationFragment() {
        // Required empty public constructor
    }

    public static NavigationFragment getInstance() {
        if (mapFragment == null) {
            mapFragment = new NavigationFragment();
        }
        return new NavigationFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        points.add(Point.fromLngLat(-77.04012393951416, 38.9111117447887));
        points.add(Point.fromLngLat(-77.03847169876099, 38.91113678979344));
        points.add(Point.fromLngLat(-77.03848242759705, 38.91040213277608));
        points.add(Point.fromLngLat(-77.03850388526917, 38.909650771013034));
        points.add(Point.fromLngLat(-77.03651905059814, 38.90894949285854));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Mapbox.getInstance(getContext(), getToken());

        View view = inflater.inflate(R.layout.fragment_navigation, container, false);
        bind = ButterKnife.bind(this, view);


//        mapView.onCreate(savedInstanceState);
//        mapView.initialize(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        HomeActivity homeActivity = (HomeActivity) getActivity();

        homeActivity.liveData.ride.observe(this, new Observer<Ride>() {
            @Override
            public void onChanged(Ride ride) {

                if (ride == null || ride.students == null) return;

                for (Student student : ride.students) {
                    if (student.present == Student.UNKNOWN) {

                        setStudent(student);

                        return;
                    }
                }

            }
        });

//        getRoute(mylocation, lastLocation, students);


//        new SendNotificationDialog(getContext()).show();
//        new SendAlertDialog(getContext()).show();
//        new MarkStudentAbdentDialog(getContext()).show();
//        new FinishRideDialog(getContext()).show();
//        new SelectRouteDialog(getContext()).show();
//        new MarkAttendanceDialog(getContext()).show();
//        new LocateMeDialog(getContext()).show();
//        new SendNotificationToAll(getContext()).show();
    }

    private void setStudent(Student student) {

        textViewName.setText(student.fullname);
        ImageBinder.setImageUrl(imageView9, student.photo);
    }


    @Override
    public void onStart() {
        super.onStart();
//        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
//        mapView.onResume();
        setActivityTitle("ROUTE");

    }

    @Override
    public void onPause() {
        super.onPause();
//        mapView.onPause();
    }

    @Override
    public void onStop() {
//        mapView.onStop();

        super.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
//        mapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        bind.unbind();
//        mapView.onDestroy();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        mapView.onSaveInstanceState(outState);
    }


    @OnClick(R.id.textViewFinishRide)
    public void onViewClicked() {

        ((HomeActivity) getActivity()).finishRide();

    }


//    @Override
//    public void onNavigationReady(boolean isRunning) {
////        fetchRoute(points.remove(0), points.remove(0));
//
//
//        HomeActivity homeActivity = (HomeActivity) getActivity();
//        startNavigation(homeActivity.navigationRoute);
//
//
//    }

//    @Override
//    public void onCancelNavigation() {
//// Navigation canceled, finish the activity
////        finish();
//    }
//
//    @Override
//    public void onNavigationFinished() {
//// Intentionally empty
//    }
//
//    @Override
//    public void onNavigationRunning() {
//// Intentionally empty
//    }
//
//    @Override
//    public boolean allowRerouteFrom(Point offRoutePoint) {
//        return true;
//    }
//
//    @Override
//    public void onOffRoute(Point offRoutePoint) {
//
//    }
//
//    @Override
//    public void onRerouteAlong(DirectionsRoute directionsRoute) {
//
//    }
//
//    @Override
//    public void onFailedReroute(String errorMessage) {
//
//    }
//
//    @Override
//    public void onArrival() {
////        if (!dropoffDialogShown && !points.isEmpty()) {
////            showDropoffDialog();
////            dropoffDialogShown = true; // Accounts for multiple arrival events
////        }
//    }
//
//    @Override
//    public void onProgressChange(Location location, RouteProgress routeProgress) {
//        lastKnownLocation = location;
//    }
//
//    private void startNavigation(DirectionsRoute directionsRoute) {
//        NavigationViewOptions navigationViewOptions = setupOptions(directionsRoute);
//        mapView.startNavigation(navigationViewOptions);
//
//    }

//    private void showDropoffDialog() {
//        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
//        alertDialog.setMessage(getString(R.string.dropoff_dialog_text));
//        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.dropoff_dialog_positive_text),
//                (dialogInterface, in) -> fetchRoute(getLastKnownLocation(), points.remove(0)));
//        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.dropoff_dialog_negative_text),
//                (dialogInterface, in) -> {
//// Do nothing
//                });
//
//        alertDialog.show();
//    }


//    private NavigationViewOptions setupOptions(DirectionsRoute directionsRoute) {
//        dropoffDialogShown = false;
//
//        NavigationViewOptions.Builder options = NavigationViewOptions.builder();
//        options.directionsRoute(directionsRoute)
//                .navigationListener(this)
//                .progressChangeListener(this)
//                .waynameChipEnabled(true)
//                .routeListener(this)
//                .shouldSimulateRoute(false);
//
//        return options.build();
//    }

    private Point getLastKnownLocation() {
        return Point.fromLngLat(lastKnownLocation.getLongitude(), lastKnownLocation.getLatitude());
    }


}
