package com.findxain.uberdriver.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.findxain.uberdriver.R;
import com.findxain.uberdriver.base.BFragment;
import com.findxain.uberdriver.dialog.FinishRideDialog;
import com.findxain.uberdriver.dialog.LocateMeDialog;
import com.findxain.uberdriver.dialog.MarkAttendanceDialog;
import com.findxain.uberdriver.dialog.MarkStudentAbdentDialog;
import com.findxain.uberdriver.dialog.SelectRouteDialog;
import com.findxain.uberdriver.dialog.SendAlertDialog;
import com.findxain.uberdriver.dialog.SendNotificationDialog;
import com.findxain.uberdriver.dialog.SendNotificationToAll;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends BFragment implements OnMapReadyCallback {


    private static MapFragment mapFragment;

    public MapFragment() {
        // Required empty public constructor
    }

    public static Fragment getInstance() {
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

        new SendNotificationDialog(getContext()).show();
        new SendAlertDialog(getContext()).show();
        new MarkStudentAbdentDialog(getContext()).show();
        new FinishRideDialog(getContext()).show();
        new SelectRouteDialog(getContext()).show();
        new MarkAttendanceDialog(getContext()).show();
        new LocateMeDialog(getContext()).show();
        new SendNotificationToAll(getContext()).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        setActivityTitle("ROUTE");

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
