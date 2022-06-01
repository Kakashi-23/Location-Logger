package com.example.locationlogger.Utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Environment;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class UserLocation implements LocationListener {

    private Context context;
    private String userCurrentLocation;

    public UserLocation(Context context) {
        this.context = context;
        getUserLocation();
    }

    public String getUserCurrentLocation() {
        return userCurrentLocation;
    }

    private LocationManager locationManager;
    int LOCATION_REFRESH_TIME = 1000; // 3sec to update
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
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE,
                this,Looper.getMainLooper());
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        /*log to file */
        userCurrentLocation = "Latitude = " + location.getLatitude() + "Longitude = " + location.getLongitude();
        saveToUserLocationLogFile();
        saveUserLocationToPref();
        sendBroadcast();
    }

    private void sendBroadcast() {
        Intent intent = new Intent(Constants.LOCATION_UPDATED_BROADCAST);
        context.sendBroadcast(intent);
    }

    private void saveUserLocationToPref() {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(Constants.LOCATION_PREF, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.LOCATION, userCurrentLocation);
        editor.apply();
        editor.commit();
    }

    private void saveToUserLocationLogFile() {

        // Get the directory for the user's public pictures directory.
        final File path =
                Environment.getExternalStoragePublicDirectory
                        (
                                //Environment.DIRECTORY_PICTURES
                                Environment.DIRECTORY_DOWNLOADS + "/LocationLogger/"
                        );

        // Make sure the path directory exists.
        if (!path.exists()) {
            // Make it, if it doesn't exit
            path.mkdirs();
        }

        final File file = new File(path, "location.txt");

        // Save your stream, don't forget to flush() it before closing it.

        try {
            if (!file.exists()) file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(userCurrentLocation).append(" Time : ").append(String.valueOf(System.currentTimeMillis())).append("\n");
            myOutWriter.close();

            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            System.out.print(e);
        }


    }

    public void updateLocation() {
        getUserLocation();
    }
}
