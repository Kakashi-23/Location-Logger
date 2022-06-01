package com.example.locationlogger.Utils;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

public class UserLocation implements LocationListener {

    private Context context;
    private String userCurrentLocation;

    public UserLocation(Context context) {
        this.context = context;
    }

    private LocationManager locationManager;
    int LOCATION_REFRESH_TIME = 300000; // 15 seconds to update
    int LOCATION_REFRESH_DISTANCE = 500; // 500 meters to update
    private SharedPreferences sharedPreferences;

    private void getUserLocation() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE,
                this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        /*log to file */
        saveToUserLocationLogFile();
        saveUserLocationToPref();
    }

    private void saveUserLocationToPref() {
        if (sharedPreferences==null){
           sharedPreferences = context.getSharedPreferences(Constants.LOCATION_PREF,Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.LOCATION,userCurrentLocation);
        editor.apply();
        editor.commit();
    }

    private void saveToUserLocationLogFile() {

    }
}
