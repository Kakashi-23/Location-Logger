package com.example.locationlogger.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.locationlogger.Utils.Constants;
import com.example.locationlogger.Utils.UserLocation;
import com.example.locationlogger.databinding.ActivityLocationShowBinding;

public class LocationShowActivity extends AppCompatActivity {

    private ActivityLocationShowBinding binding;
    private SharedPreferences sharedPreferences;
    private UserLocation userLocation;
    private BroadcastReceiver locationUpdatedBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showLocation();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLocationShowBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPreferences = getSharedPreferences(Constants.LOCATION_PREF, MODE_PRIVATE);
        userLocation = new UserLocation(this);
        IntentFilter intentFilter = new IntentFilter(Constants.LOCATION_UPDATED_BROADCAST);
        registerReceiver(locationUpdatedBroadCastReceiver, intentFilter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(locationUpdatedBroadCastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showLocation();
    }

    private void showLocation() {
        String userLocationFromPref = sharedPreferences.getString(Constants.LOCATION, "null");
        if (!userLocationFromPref.isEmpty()) binding.userLocation.setText(userLocationFromPref);
        else {
            String userLocationFromGps = this.userLocation.getUserCurrentLocation();
            binding.userLocation.setText(userLocationFromGps);
        }
    }
}