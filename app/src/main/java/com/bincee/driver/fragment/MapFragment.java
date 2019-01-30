package com.bincee.driver.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.bincee.driver.HomeActivity;
import com.bincee.driver.R;
import com.bincee.driver.api.firestore.Ride;
import com.bincee.driver.api.model.GetSchoolResponce;
import com.bincee.driver.api.model.Student;
import com.bincee.driver.base.BFragment;
import com.bincee.driver.helper.ImageBinder;
import com.bincee.driver.helper.LatLngHelper;
import com.google.android.gms.maps.model.Marker;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.core.constants.Constants;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.Source;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends BFragment implements OnMapReadyCallback {


    public static final String MAPBOX_TOKEN = "pk.eyJ1IjoiZmluZHhhaW4iLCJhIjoiY2pxOTY1bjY3MTMwYjQzbDEwN3h2aTdsbCJ9.fKLD1_UzlMIWhXfUZ3aRYQ";
    private static MapFragment mapFragment;
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
    private Marker mylocationMarker;

    private static final String ROUTE_LAYER_ID = "route-layer-id";
    private static final String ROUTE_SOURCE_ID = "route-source-id";
    private static final String ICON_LAYER_ID = "icon-layer-id";
    private static final String ICON_SOURCE_ID = "icon-source-id";
    private static final String RED_PIN_ICON_ID = "red-pin-icon-id";


    private MapboxMap mapboxMap;
    //    private Point origin;
    private List<com.mapbox.mapboxsdk.annotations.Marker> markers = new ArrayList<>();
    private Unbinder bind;
    //    private DirectionsRoute currentRoute;
//    private Point destination;

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment getInstance() {
        if (mapFragment == null) {
            mapFragment = new MapFragment();
        }
        return new MapFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Mapbox.getInstance(getContext(), MAPBOX_TOKEN);

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        bind = ButterKnife.bind(this, view);


        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        HomeActivity homeActivity = getHomeActivity();

        homeActivity.liveData.ride.observe(this, new Observer<Ride>() {
            @Override
            public void onChanged(Ride ride) {

                if (ride == null || ride.students == null) return;

                for (Student student : ride.students) {

                    if (ride.shift == Ride.SHIFT_MORNING) {

                        //SHow Student which attandance is not marked
                        if (student.present == Student.UNKNOWN) {
                            setStudent(student);
                            return;
                        }
                    } else {

                        //SHow Student which is in the bus state

                        if (student.status == Student.STATUS_AFTERNOON_INTHEBUS) {
                            setStudent(student);
                            return;
                        }


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
        textViewTime.setText(student.address);
        if (student.distance != null) {
            textViewNameKM.setText("ETA: " + Math.round(student.duration) + " Minutes");
        }

        ImageBinder.setImageUrl(imageView9, student.photo);
    }

    private void setupRoute(DirectionsRoute currentRoute) {
        HomeActivity homeActivity = getHomeActivity();
//


        MutableLiveData<Location> myLocaton = homeActivity.liveData.myLocaton;
        if (currentRoute != null && myLocaton.getValue() != null) {


            List<Student> students = homeActivity.liveData.ride.getValue().students;

            List<Point> points = LineString.fromPolyline(currentRoute.geometry(), Constants.PRECISION_6).coordinates();


            List<LatLng> pointsLtLng = new ArrayList<>();

            for (Point point : points) {
                pointsLtLng.add(LatLngHelper.toLatLng(point));
            }


            Student lastStudent = students.get((students.size() - 1));
            Point mylocation = Point.fromLngLat(myLocaton.getValue().getLongitude(), myLocaton.getValue().getLatitude());


            GetSchoolResponce school = homeActivity.liveData.schoolResponce.getValue();
            Point lastLocation = Point.fromLngLat(school.lng, school.lat);


//            Polyline polyline = mapboxMap.addPolyline(new PolylineOptions()
//                    .addAll(pointsLtLng)
//                    .color(Color.BLACK).width(5f));

//
            List<Feature> directionsRouteFeatureList = new ArrayList<>();
            for (int i = 0; i < points.size(); i++) {
                directionsRouteFeatureList.add(Feature.fromGeometry(LineString.fromLngLats(points)));
            }

            FeatureCollection dashedLineDirectionsFeatureCollection = FeatureCollection.fromFeatures(directionsRouteFeatureList);


            LineString lineString = LineString.fromLngLats(points);
            FeatureCollection featureCollection =
                    FeatureCollection.fromFeatures(new Feature[]{Feature.fromGeometry(lineString)});
            Source geoJsonSource = new GeoJsonSource("line-source", featureCollection);
            mapboxMap.addSource(geoJsonSource);
            LineLayer lineLayer = new LineLayer("linelayer", "line-source");


            lineLayer.setProperties(
                    PropertyFactory.lineDasharray(new Float[]{0.01f, 3f}),
                    PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                    PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                    PropertyFactory.lineWidth(3f),
                    PropertyFactory.lineColor(Color.parseColor("#e55e5e"))
            );
            mapboxMap.addLayer(lineLayer);


            if (markers != null && markers.size() > 0) {
                for (com.mapbox.mapboxsdk.annotations.Marker marker : markers) {
                    mapboxMap.removeMarker(marker);
                }
                markers.clear();
            }

            IconFactory getInstance = IconFactory.getInstance(getContext());
            Icon busIcon = getInstance.fromResource(R.drawable.map_icon_bus);


//            com.mapbox.mapboxsdk.annotations.Marker marker = mapboxMap.addMarker(new MarkerOptions()
//                    .setPosition(new com.mapbox.mapboxsdk.geometry.LatLng(mylocation.latitude(),
//                            mylocation.longitude()))
//                    .setTitle("Start")
//            );
            com.mapbox.mapboxsdk.annotations.Marker marker1 = mapboxMap.addMarker(new MarkerOptions()
                    .setPosition(new com.mapbox.mapboxsdk.geometry.LatLng(lastLocation.latitude()
                            , lastLocation.longitude()))
                    .setTitle("School")

            );

//            markers.add(marker);
            markers.add(marker1);

            Icon icon = getInstance.fromResource(R.drawable.map_icon_home);

            for (Student student : students) {


                com.mapbox.mapboxsdk.annotations.Marker markerStudent = mapboxMap.addMarker(new MarkerOptions().setPosition(new com.mapbox.mapboxsdk.geometry.LatLng(student.lat,
                        student.lng))
                        .setIcon(icon)
                        .setTitle(student.fullname));

                markers.add(markerStudent);


            }

        }
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

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            return;
        }
        setupMyLocation(mapboxMap);

//        origin = Point.fromLngLat(-3.588098, 37.176164);

        initSource();
        initLayers();


        getHomeActivity().liveData.currentRoute.observe(getViewLifecycleOwner(), new Observer<DirectionsRoute>() {
            @Override
            public void onChanged(DirectionsRoute directionsRoute) {
                setupRoute(directionsRoute);
                getHomeActivity().liveData.currentRoute.removeObserver(this);
            }
        });


    }


    @SuppressLint("MissingPermission")
    private void setupMyLocation(MapboxMap mapboxMap) {
        LocationComponent locationComponent = mapboxMap.getLocationComponent();
        locationComponent.activateLocationComponent(getContext());
        locationComponent.setLocationComponentEnabled(true);
//        locationComponent.applyStyle(LocationComponentOptions.builder(getContext())
//                .gpsDrawable(R.drawable.map_marker_dark)
//                .foregroundDrawable(R.drawable.bus_marker)
//                .bearingDrawable(R.drawable.map_marker_dark)
//                .backgroundDrawable(R.drawable.bus_marker)
//                .build());
        locationComponent.setCameraMode(CameraMode.TRACKING);
        locationComponent.zoomWhileTracking((mapboxMap.getMaxZoomLevel() - 5));


    }

    /**
     * Add the route and marker sources to the map
     */
    private void initSource() {
        GeoJsonSource routeGeoJsonSource = new GeoJsonSource(ROUTE_SOURCE_ID,
                FeatureCollection.fromFeatures(new Feature[]{}));
        mapboxMap.addSource(routeGeoJsonSource);

        FeatureCollection iconFeatureCollection = FeatureCollection.fromFeatures(new Feature[]{
//                Feature.fromGeometry(Point.fromLngLat(origin.longitude(), origin.latitude())),
//                Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude()))
        });

        GeoJsonSource iconGeoJsonSource = new GeoJsonSource(ICON_SOURCE_ID, iconFeatureCollection);
        mapboxMap.addSource(iconGeoJsonSource);
    }

    /**
     * Add the route and maker icon layers to the map
     */
    private void initLayers() {
        LineLayer routeLayer = new LineLayer(ROUTE_LAYER_ID, ROUTE_SOURCE_ID);

        // Add the LineLayer to the map. This layer will display the directions route.
        routeLayer.setProperties(
                lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
                lineWidth(5f),
                lineColor(Color.parseColor("#009688"))
        );
        mapboxMap.addLayer(routeLayer);

//        // Add the red marker icon image to the map
//        mapboxMap.addImage(RED_PIN_ICON_ID, BitmapUtils.getBitmapFromDrawable(
//                getResources().getDrawable(R.drawable.red_marker)));
//
//        // Add the red marker icon SymbolLayer to the map
//        SymbolLayer startEndIconLayer = new SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID);
//        startEndIconLayer.setProperties(
//                iconImage(RED_PIN_ICON_ID),
//                iconIgnorePlacement(true),
//                iconIgnorePlacement(true));
//
//        mapboxMap.addLayer(startEndIconLayer);
    }


    public void setMyLocation(Location myLocaton) {
//        this.myLocaton = myLocaton;
//        if (mylocationMarker != null) {
//            mylocationMarker.setPosition(new LatLng(myLocaton.getLatitude(), myLocaton.getLongitude()));
//
//        }
    }

    @OnClick(R.id.textViewFinishRide)
    public void onViewClicked() {

        (getHomeActivity()).finishRide();

    }
}
