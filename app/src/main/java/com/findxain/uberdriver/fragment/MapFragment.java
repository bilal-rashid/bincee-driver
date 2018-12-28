package com.findxain.uberdriver.fragment;


import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.findxain.uberdriver.HomeActivity;
import com.findxain.uberdriver.R;
import com.findxain.uberdriver.base.BFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends BFragment implements OnMapReadyCallback {


    private static MapFragment mapFragment;
    private Location myLocaton;
    private Marker mylocationMarker;
    private GoogleMap googleMap;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        new SendNotificationDialog(getContext()).show();
//        new SendAlertDialog(getContext()).show();
//        new MarkStudentAbdentDialog(getContext()).show();
//        new FinishRideDialog(getContext()).show();
//        new SelectRouteDialog(getContext()).show();
//        new MarkAttendanceDialog(getContext()).show();
//        new LocateMeDialog(getContext()).show();
//        new SendNotificationToAll(getContext()).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        setActivityTitle("ROUTE");

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.myLocaton = ((HomeActivity) getActivity()).myLocaton;
        this.googleMap = googleMap;
        if (myLocaton != null) {
            LatLng latLng = new LatLng(myLocaton.getLatitude(), myLocaton.getLongitude());
            mylocationMarker = googleMap.addMarker(new MarkerOptions().draggable(false).position(latLng));

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f));

        }

    }

    public void setMyLocation(Location myLocaton) {
        this.myLocaton = myLocaton;
        if (mylocationMarker != null) {
            mylocationMarker.setPosition(new LatLng(myLocaton.getLatitude(), myLocaton.getLongitude()));

        }
    }

    @OnClick(R.id.textViewFinishRide)
    public void onViewClicked() {

        ((HomeActivity)getActivity()).finishRide();

    }
}
