package com.example.locationlogger.UI;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.locationlogger.R;
import com.example.locationlogger.Utils.Constants;
import com.example.locationlogger.databinding.ActivityLocationShowBinding;

public class LocationShowActivity extends AppCompatActivity {

    private ActivityLocationShowBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLocationShowBinding.inflate(getLayoutInflater());
        sharedPreferences = getSharedPreferences(Constants.LOCATION_PREF, MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String userLocation = sharedPreferences.getString(Constants.LOCATION, "null");
        binding.userLocation.setText(userLocation);
    }
}