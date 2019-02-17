package com.bincee.driver.helper;

import android.location.Location;

import com.bincee.driver.api.model.Student;
import com.google.firebase.firestore.GeoPoint;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.List;

public class LatLngHelper {
    public static Location toLocation(Student student) {
        Location location = new Location("student");

        location.setLatitude(student.lat);
        location.setLongitude(student.lng);

        return location;
    }

    public static Location toLocation(Point point) {
        Location location = new Location("student");

        location.setLatitude(point.latitude());
        location.setLongitude(point.longitude());

        return location;
    }

    public static LatLng toLatLng(Point point) {
        LatLng location = new LatLng(point.latitude(), point.longitude(), point.altitude());

        location.setLatitude(point.latitude());
        location.setLongitude(point.longitude());

        return location;
    }

    public static Location toLocation(GeoPoint schoolLatLng) {
        Location location = new Location("student");

        location.setLatitude(schoolLatLng.getLatitude());
        location.setLongitude(schoolLatLng.getLongitude());

        return location;
    }

    public static LatLng toLatLng(Location loc) {
        LatLng location = new LatLng();

        location.setLatitude(loc.getLatitude());
        location.setLongitude(loc.getLongitude());

        return location;
    }

    public static List<LatLng> toLatLng(List<Point> coordinates) {
        List<LatLng> list = new ArrayList<>();

        for (Point point :
                coordinates) {
            list.add(toLatLng(point));
        }

        return list;
    }

    public static GeoPoint toGeoPoint(Location value) {
        if (value == null) return null;
        return new GeoPoint(value.getLatitude(), value.getLongitude());
    }
}
