package com.bincee.driver.fragment;


import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.bincee.driver.HomeActivity;
import com.bincee.driver.MyApp;
import com.bincee.driver.R;
import com.bincee.driver.api.firestore.Ride;
import com.bincee.driver.api.model.AddNotificationBackednBody;
import com.bincee.driver.api.model.GetSchoolResponce;
import com.bincee.driver.api.model.Student;
import com.bincee.driver.base.BFragment;
import com.bincee.driver.dialog.SendAlertDialog;
import com.bincee.driver.helper.DateHelper;
import com.bincee.driver.helper.ImageBinder;
import com.bincee.driver.helper.LatLngHelper;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.core.constants.Constants;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.MarkerView;
import com.mapbox.mapboxsdk.annotations.MarkerViewManager;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Projection;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.mapbox.services.android.navigation.v5.location.replay.ReplayRouteLocationEngine;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconRotate;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends BFragment implements OnMapReadyCallback {


    //    public static final String MAPBOX_TOKEN = "pk.eyJ1IjoiZmluZHhhaW4iLCJhIjoiY2pxOTY1bjY3MTMwYjQzbDEwN3h2aTdsbCJ9.fKLD1_UzlMIWhXfUZ3aRYQ";
    public static final int DURATION = 3000;
    private static MapFragment mapFragment;
    private DirectionsRoute directionRoute;
    @BindView(R.id.mapView)
    MapView mapView;
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
    //    private Location myLocaton;


    private MapboxMap mapboxMap;
    //    private Point origin;
    private List<com.mapbox.mapboxsdk.annotations.Marker> markers = new ArrayList<>();
    private Unbinder bind;
    String LINE_SOURCE = "line-source";
    String LINE_LAYER = "linelayer";
    private Icon iconBusMyLoc;
    private Marker markerView;
    private String BUS_ICON_LAYER = "bus-icon-layer";
    private String BUS_ICON_SOURCE = "bus-icon_source";
    private String BUS_ICON = "bus-icon";
    private LatLng oldLocation;
    private Location currentLocation;
    private double lastBearing = 0;
    private int padding = 5;
    private Observer<Location> mylocationObserver;
    //    private DirectionsRoute currentRoute;
//    private Point destination;

    public void enableFinishRide (){
        textViewFinishRide.setEnabled(true);
        textViewFinishRide.setBackgroundColor(ContextCompat.getColor(Objects.requireNonNull(getContext()),R.color.sky_blue));
    }

    public void disableFinishRide(){
        textViewFinishRide.setEnabled(false);
        textViewFinishRide.setBackgroundColor(ContextCompat.getColor(Objects.requireNonNull(getContext()),R.color.color_grey));
    }
    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment getInstance() {
        if (mapFragment == null) {
            mapFragment = new MapFragment();
        }
        return mapFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Mapbox.getInstance(getContext(), NavigationFragment.getToken());

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        bind = ButterKnife.bind(this, view);

        iconBusMyLoc = IconFactory.getInstance(getContext()).fromResource(R.drawable.bus_icon_green);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


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

        if (student == null) {
            textViewName.setText("");
            textViewTime.setText("");
//            if (student.distance != null) {
            textViewNameKM.setText("");
//            }

//            ImageBinder.roundedCornerCenterCorpKid(imageView9, null);
            imageView9.setImageBitmap(null);
        } else {
            textViewName.setText(student.fullname);
            textViewTime.setText(student.address);
            if (student.distance != null) {
                textViewNameKM.setText("ETA: " + Math.round(student.duration) + " Minutes");
            }

            ImageBinder.roundedCornerCenterCorpKid(imageView9, student.photo);
        }
    }

    private void setupRoute(DirectionsRoute currentRoute) {
        HomeActivity homeActivity = getHomeActivity();

        if (homeActivity.liveData.ride.getValue() == null) return;

        MutableLiveData<Location> myLocaton = homeActivity.liveData.myLocaton;
        if (currentRoute != null && myLocaton.getValue() != null) {

            Ride ride = homeActivity.liveData.ride.getValue();
            List<Student> students = ride.students;

            List<Point> points = LineString.fromPolyline(currentRoute.geometry(), Constants.PRECISION_6).coordinates();

//            List<LatLng> pointsLtLng = new ArrayList<>();
//
//            for (Point point : points) {
//                pointsLtLng.add(LatLngHelper.toLatLng(point));
//            }


//            Polyline polyline = mapboxMap.addPolyline(new PolylineOptions()
//                    .addAll(pointsLtLng)
//                    .color(Color.BLACK).width(5f));

//
//            List<Feature> directionsRouteFeatureList = new ArrayList<>();
//            for (int i = 0; i < points.size(); i++) {
//                directionsRouteFeatureList.add(Feature.fromGeometry(LineString.fromLngLats(points)));
//            }


            LineString lineString = LineString.fromLngLats(points);
            FeatureCollection featureCollection =
                    FeatureCollection.fromFeatures(new Feature[]{Feature.fromGeometry(lineString)});


//            Source geoJsonSource = new GeoJsonSource("line-source", featureCollection);
//            mapboxMap.addSource(geoJsonSource);
//            LineLayer lineLayer = new LineLayer("linelayer", "line-source");
//
//
//            lineLayer.setProperties(
//                    PropertyFactory.lineDasharray(new Float[]{0.01f, 3f}),
//                    PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
//                    PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
//                    PropertyFactory.lineWidth(3f),
//                    PropertyFactory.lineColor(Color.parseColor("#e55e5e"))
//            );
//            mapboxMap.addLayer(lineLayer);


            GeoJsonSource lineSource = mapboxMap.getSourceAs(LINE_SOURCE);
            if(lineSource != null)
                lineSource.setGeoJson(featureCollection);


            if (markers != null && markers.size() > 0) {
                for (com.mapbox.mapboxsdk.annotations.Marker marker : markers) {
                    mapboxMap.removeMarker(marker);
                }
                markers.clear();
            }

            IconFactory getInstance = IconFactory.getInstance(getContext());


            if (ride.shift.equalsIgnoreCase(Ride.SHIFT_MORNING)) {
                //dont show school marker in evenign
                GetSchoolResponce school = homeActivity.liveData.schoolResponce.getValue();
                Point schoolLocation = Point.fromLngLat(school.lng, school.lat);
                com.mapbox.mapboxsdk.annotations.Marker schoolMarker = mapboxMap.addMarker(new MarkerOptions()
                        .setPosition(new com.mapbox.mapboxsdk.geometry.LatLng(schoolLocation.latitude()
                                , schoolLocation.longitude()))
                        .setTitle("School")
                );
                markers.add(schoolMarker);
            }


            for (Student student : students) {

                if (ride.shift.equalsIgnoreCase(Ride.SHIFT_MORNING)) {

                    if (student.present == Student.UNKNOWN) {
                        addMarkerStudent(student.lat, student.lng, student.fullname);
                    }


                } else {

                    if (student.present == Student.PRESENT && student.status < Student.STATUS_AFTERNOON_ATYOURDOORSTEP) {

                        addMarkerStudent(student.lat, student.lng, student.fullname);
                    }


                }

            }


        }
    }

    private void addMarkerStudent(double lat, double lng, String fullname) {
        Icon icon = IconFactory.getInstance(getContext()).fromResource(R.drawable.map_icon_home);

        Marker markerStudent = mapboxMap.addMarker(new MarkerOptions().setPosition(new LatLng(lat,
                lng))
                .setIcon(icon)
                .setTitle(fullname));

        markers.add(markerStudent);
    }

    private HomeActivity getHomeActivity() {
        return (HomeActivity) getActivity();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        setActivityTitle("ROUTE");

    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        mapView.onStop();

        super.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        bind.unbind();
        if (mylocationObserver != null) {
            getHomeActivity().liveData.myLocaton.removeObserver(mylocationObserver);
        }
        if (markerView != null) {
            markerView.remove();
            markerView = null;
        }
        mapView.onDestroy();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        this.mapboxMap.getUiSettings().setRotateGesturesEnabled(false);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            return;
        }
//        setupMyLocation(mapboxMap);


        FeatureCollection featureCollection =
                FeatureCollection.fromFeatures(new Feature[]{});

        Source geoJsonSource = new GeoJsonSource(LINE_SOURCE, featureCollection);
        mapboxMap.addSource(geoJsonSource);
        LineLayer lineLayer = new LineLayer(LINE_LAYER, LINE_SOURCE);


        lineLayer.setProperties(
                PropertyFactory.lineDasharray(new Float[]{0.01f, 3f}),
                PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                PropertyFactory.lineWidth(3f),
                PropertyFactory.lineColor(getResources().getColor(R.color.sky_blue)));


        mapboxMap.addLayer(lineLayer);


        FeatureCollection iconFeatureCollection = FeatureCollection.fromFeatures(new Feature[]{
        });

        GeoJsonSource iconGeoJsonSource = new GeoJsonSource(BUS_ICON_SOURCE, iconFeatureCollection);
        mapboxMap.addSource(iconGeoJsonSource);

        mapboxMap.addImage(BUS_ICON, BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.bus_icon_green)));


        SymbolLayer startEndIconLayer = new SymbolLayer(BUS_ICON_LAYER, BUS_ICON_SOURCE);
        startEndIconLayer.setProperties(
                iconImage(BUS_ICON),
                iconIgnorePlacement(true),
                iconIgnorePlacement(true));

        mapboxMap.addLayer(startEndIconLayer);


        mylocationObserver = new Observer<Location>() {
            @Override
            public void onChanged(Location location) {


                LatLng nowLocation = LatLngHelper.toLatLng(location);
                currentLocation = location;

                FeatureCollection busCollectionSource = FeatureCollection.fromFeatures(new Feature[]{
                        Feature.fromGeometry(Point.fromLngLat(nowLocation.getLongitude(), nowLocation.getLatitude())),
//                Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude()))

                });

                GeoJsonSource busSource = mapboxMap.getSourceAs(BUS_ICON_SOURCE);

                SymbolLayer busLayer = mapboxMap.getLayerAs(BUS_ICON_LAYER);

                ;

                if (oldLocation != null && markerView != null) {

//                    double displacemnet = oldLocation.distanceTo(nowLocation);

//                    if (displacemnet > 1) {
//                    double nowBearing = bearingBetweenLocations(oldLocation, nowLocation);

//                    smothRotation(busLayer, nowBearing, lastBearing);
//                    animateMarker(oldLocation, nowLocation, busSource);

//                        markerView.setPosition(nowLocation);
//                        markerView.setRotation((float) nowBearing);


//                        ValueAnimator rotationAnimator = new ValueAnimator();
//                        rotationAnimator.setObjectValues(Float.parseFloat(lastBearing + ""), nowBearing);
//                        rotationAnimator.setDuration(DURATION);
//                        rotationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//
//                            @Override
//                            public void onAnimationUpdate(ValueAnimator animator) {
//
//                                markerView.setRotation((Float) animator.getAnimatedValue());
//
//                            }
//                        });
//                        rotationAnimator.start();


                    ValueAnimator markerAnimator = ValueAnimator.ofObject(new LatLngEvaluator(), (Object[]) new LatLng[]{oldLocation, nowLocation});
                    markerAnimator.setDuration(DURATION);
                    markerAnimator.setRepeatCount(0);
                    markerAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                    markerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            if (markerView != null) {

                                markerView.setPosition((LatLng) animation.getAnimatedValue());
//                                mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerView.getPosition(), mapboxMap.getCameraPosition().zoom));

                            }
                        }
                    });
                    markerAnimator.start();
//                    markerView.setPosition(nowLocation);


//                    lastBearing = nowBearing;
//                    }


                } else {
//                    busSource.setGeoJson(busCollectionSource);
                    markerView = MapFragment.this.mapboxMap.addMarker(new MarkerOptions()
                            .icon(iconBusMyLoc).position(nowLocation));
                    mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerView.getPosition(), mapboxMap.getCameraPosition().zoom));

                }
                oldLocation = nowLocation;

//                animateMapToRoute(directionRoute, mapboxMap, padding);


            }
        };

        HomeActivity homeActivity = getHomeActivity();

        homeActivity.liveData.ride.observe(this, new Observer<Ride>() {
            @Override
            public void onChanged(Ride ride) {

                if (ride == null || ride.students == null) return;

                if (directionRoute != null) {
                    setupRoute(directionRoute);
                }

//                for (Student student : ride.students) {

                if (ride.shift.equalsIgnoreCase(Ride.SHIFT_MORNING)) {

                    Student student = getFirstNotPickedStudentMorning(ride.students);
                    setStudent(student);
                    if(currentLocation!=null) {
                        Double distanceFromSchool = distance(currentLocation.getLatitude(),
                                currentLocation.getLongitude(),
                                ride.schoolLatLng.getLatitude(),
                                ride.schoolLatLng.getLongitude());
                        if(distanceFromSchool < 150){
                            enableFinishRide();
                        }else {
                            disableFinishRide();
                        }
                    }

                    //SHow Student which attandance is not marked


                } else if (ride.shift.equalsIgnoreCase(Ride.SHIFT_AFTERNOON)) {

                    //SHow Student which is in the bus state
                    enableFinishRide();

                    Student student = getFirstUnDropedStudentEvening(ride.students);
                    setStudent(student);
                    if (getPresentStudents(ride.students).size()==1) {
                        if(currentLocation!=null) {
                            List<Student> lastStudentList = getPresentStudents(ride.students);
                            Double distanceFromLastStudent = distance(currentLocation.getLatitude(),
                                    currentLocation.getLongitude(),
                                    lastStudentList.get(0).lat,
                                    lastStudentList.get(0).lng);
                            if(distanceFromLastStudent < 150){
                                enableFinishRide();
                            }else {
                                enableFinishRide();
                            }
                        }
                    }



                }

            }
//                setStudent(null);


//            }
        });

        getHomeActivity().liveData.myLocaton.observe(MapFragment.this, mylocationObserver);

        getHomeActivity().liveData.currentRoute.observe(getViewLifecycleOwner(), new Observer<DirectionsRoute>() {
            @Override
            public void onChanged(DirectionsRoute directionsRoute) {
//                mapboxMap.removeAnnotations();
                setupRoute(directionsRoute);
//                getHomeActivity().liveData.currentRoute.removeObserver(this);
//                mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mylocationMarker.getPosition(),14));

                MapFragment.this.directionRoute = directionsRoute;
                animateMapToRoute(directionsRoute, mapboxMap, padding);

            }
        });


    }

    private List<Student> getPresentStudents(List<Student> students) {
        List<Student> result = new ArrayList<>();
        for(int i = 0;i<students.size();i++){
            if(students.get(i).present == Student.PRESENT){
                result.add(students.get(i));
            }
        }
        return result;
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist*1000);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private Student getFirstUnDropedStudentEvening(List<Student> students) {

        for (Student student : students) {
            if (student.present == Student.PRESENT && student.status < Student.STATUS_AFTERNOON_ATYOURDOORSTEP) {
                return student;
            }

        }

        return null;

    }

    private Student getFirstNotPickedStudentMorning(List<Student> students) {
        for (Student student : students) {

            if (student.present == Student.UNKNOWN) {
                return student;
            }
        }
        return null;
    }

    public void animateMapToRoute(DirectionsRoute directionsRoute, MapboxMap mapboxMap, int padding) {
        if (directionsRoute != null) {
            mapboxMap.animateCamera(CameraUpdateFactory
                    .newLatLngBounds(new LatLngBounds.Builder()
                            .includes(LatLngHelper.toLatLng(directionsRoute.routeOptions().coordinates()))
                            .build(), padding));
        }
    }

//    public void animateMarker(final LatLng marker, final LatLng toPosition, GeoJsonSource busSource) {
//        final Handler handler = new Handler();
//        final long start = SystemClock.uptimeMillis();
//
//
//        final LatLng startLatLng = marker;
//        final long duration = DURATION;
//
//        final Interpolator interpolator = new LinearInterpolator();
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                long elapsed = SystemClock.uptimeMillis() - start;
//                float t = interpolator.getInterpolation((float) elapsed
//                        / duration);
//                double lng = t * toPosition.getLongitude() + (1 - t)
//                        * startLatLng.getLongitude();
//                double lat = t * toPosition.getLatitude() + (1 - t)
//                        * startLatLng.getLatitude();
//
//
//                FeatureCollection busCollectionSource = FeatureCollection.fromFeatures(new Feature[]{
//                        Feature.fromGeometry(Point.fromLngLat(lng, lat)),
//
//                });
//
//                busSource.setGeoJson(busCollectionSource);
////                marker.setPosition(new LatLng(lat, lng));
//
//
//                if (t < 1.0) {
//                    // Post again 16ms later.
//                    handler.postDelayed(this, 16);
//
//                } else {
////                    if (hideMarker) {
////                        marker.setVisible(false);
////                    } else {
////                        marker.setVisible(true);
////                    }
//                }
//            }
//        });
//    }
//
//    private void smothRotation(final SymbolLayer marker, double nowBearing, double lastBearing) {
//        ValueAnimator markerAnimator = new ValueAnimator();
//        markerAnimator.setObjectValues(Float.parseFloat(lastBearing + ""), nowBearing);
//        markerAnimator.setDuration(DURATION);
//        markerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//
//            @Override
//            public void onAnimationUpdate(ValueAnimator animator) {
////                marker.setProperties(
////                        PropertyFactory.iconSize((float) animator.getAnimatedValue())
////                );
//
//
//                marker.setProperties(
//                        iconImage(BUS_ICON),
//                        iconIgnorePlacement(true),
//                        iconIgnorePlacement(true),
//                        iconRotate((float) animator.getAnimatedValue())
//                );
//
//            }
//        });
//        markerAnimator.start();
//    }
//
//    private double bearingBetweenLocations(LatLng latLng1, LatLng latLng2) {
//
//        double PI = 3.14159;
//        double lat1 = latLng1.getLatitude() * PI / 180;
//        double long1 = latLng1.getLongitude() * PI / 180;
//        double lat2 = latLng2.getLatitude() * PI / 180;
//        double long2 = latLng2.getLongitude() * PI / 180;
//
//        double dLon = (long2 - long1);
//
//        double y = Math.sin(dLon) * Math.cos(lat2);
//        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
//                * Math.cos(lat2) * Math.cos(dLon);
//
//        double brng = Math.atan2(y, x);
//
//        brng = Math.toDegrees(brng);
//        brng = (brng + 360) % 360;
//
//        return brng;
//    }
//
//    @SuppressLint("MissingPermission")
//    private void setupMyLocation(MapboxMap mapboxMap) {
//        LocationComponent locationComponent = mapboxMap.getLocationComponent();
//
//        LocationComponentOptions locationComponentOptions = locationComponent.getLocationComponentOptions();
//
//        locationComponent.activateLocationComponent(getContext());
//        locationComponent.setLocationComponentEnabled(true);
//        LocationComponentOptions.Builder builder = LocationComponentOptions.builder(getContext());
//        locationComponent.applyStyle(builder
//                .gpsDrawable(R.drawable.bus_marker)
//
////                .foregroundDrawable(R.drawable.map_icon_bus)
////                .backgroundDrawable(R.drawable.map_icon_bus)
//
//                .bearingDrawable(R.drawable.bus_marker)
//
//                .compassAnimationEnabled(true)
//                .accuracyAnimationEnabled(false)
//
//
//                .build());
//        locationComponent.setCameraMode(CameraMode.TRACKING);
//        locationComponent.zoomWhileTracking((mapboxMap.getMaxZoomLevel() - 5));
//
//        locationComponent.activateLocationComponent(getContext(), builder.build());
//        locationComponent.setRenderMode(RenderMode.GPS);
//    }
//
//
//    @SuppressLint("MissingPermission")
//    private void setupMyLocation(MapboxMap mapboxMap, DirectionsRoute directionsRoute) {
//        LocationComponent locationComponent = mapboxMap.getLocationComponent();
//
//        LocationComponentOptions locationComponentOptions = locationComponent.getLocationComponentOptions();
//
//        locationComponent.activateLocationComponent(getContext());
//        locationComponent.setLocationComponentEnabled(true);
//        LocationComponentOptions.Builder builder = LocationComponentOptions.builder(getContext());
//        locationComponent.applyStyle(builder
//                .gpsDrawable(R.drawable.bus_marker)
//
////                .foregroundDrawable(R.drawable.map_icon_bus)
////                .backgroundDrawable(R.drawable.map_icon_bus)
//
//                .bearingDrawable(R.drawable.bus_marker)
//
//                .compassAnimationEnabled(true)
//                .accuracyAnimationEnabled(false)
//
//
//                .build());
//        locationComponent.setCameraMode(CameraMode.TRACKING);
//        locationComponent.zoomWhileTracking((mapboxMap.getMaxZoomLevel() - 5));
//
//        locationComponent.activateLocationComponent(getContext(), builder.build());
//        locationComponent.setRenderMode(RenderMode.GPS);
//
//
//    }


    @OnClick(R.id.textViewFinishRide)
    public void onViewClicked() {

        (getHomeActivity()).finishRide();

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.map_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.map_menu_notification) {

            new SendAlertDialog(getContext())
                    .setListner(new SendAlertDialog.Listner() {
                        @Override
                        public void send(String text) {

                            getHomeActivity().liveData.sendNotificationTOALlPresentStudents(text, true);


                        }

                        @Override
                        public void cancel() {

                        }
                    })
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private static class LatLngEvaluator implements TypeEvaluator<LatLng> {

        // Method is used to interpolate the marker animation.

        private LatLng latLng = new LatLng();

        @Override
        public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
            latLng.setLatitude(startValue.getLatitude()
                    + ((endValue.getLatitude() - startValue.getLatitude()) * fraction));
            latLng.setLongitude(startValue.getLongitude()
                    + ((endValue.getLongitude() - startValue.getLongitude()) * fraction));
            return latLng;
        }
    }
}
