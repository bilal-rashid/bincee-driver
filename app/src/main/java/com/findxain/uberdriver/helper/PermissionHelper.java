package com.findxain.uberdriver.helper;

import android.Manifest;
import android.app.Activity;

import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;


public class PermissionHelper {
    private PermissionCallback listner;
    private String[] requiredPermissions;
    private int permissionId;
    private Activity context;
    private Fragment fragment;

    public PermissionHelper setListner(PermissionCallback listner) {
        this.listner = listner;
        return this;
    }

    public PermissionHelper requiredPermissions(String[] camera_permissios) {
        this.requiredPermissions = camera_permissios;
        return this;
    }

    public PermissionHelper permissionId(int permissionId) {
        this.permissionId = permissionId;
        return this;
    }

    public void request() {

        for (String permison : requiredPermissions) {

            int permissionStatus = ContextCompat.checkSelfPermission(context == null ? fragment.getContext() : context, permison);

            if (permissionStatus == PermissionChecker.PERMISSION_DENIED) {
                if (fragment != null) {
                    fragment.requestPermissions(requiredPermissions, permissionId);
                    return;
                } else {
                    ActivityCompat.requestPermissions(context, requiredPermissions, permissionId);
                    return;
                }
            }
        }

        listner.onPermissionGranted();
    }

    public PermissionHelper with(Activity context) {
        this.context = context;
        return this;
    }

    public PermissionHelper with(Fragment context) {
        this.fragment = context;
        return this;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == this.permissionId) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                listner.onPermissionGranted();

            } else {
                listner.onPermissionFailed();

            }

        }
    }

    public interface PermissionCallback {
        void onPermissionGranted();

        void onPermissionFailed();

    }
}
