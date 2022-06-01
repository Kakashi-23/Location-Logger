package com.example.locationlogger.UI;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.locationlogger.Utils.Constants;
import com.example.locationlogger.Services.GetLocationService;
import com.example.locationlogger.Utils.ToastClass;
import com.example.locationlogger.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainBinding;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
        showProgressBar(true);
        mainBinding.loginButton.setOnClickListener(onCLick -> getLoginInfo());
    }

    private void showProgressBar(boolean show) {
        if (show) mainBinding.progressBar.setVisibility(View.VISIBLE);
        else mainBinding.progressBar.setVisibility(View.GONE);
    }

    private void getLoginInfo() {
        if (!mainBinding.mobileNumber.getText().toString().isEmpty()) {
            if (!mainBinding.enterName.getText().toString().isEmpty()) {
                showProgressBar(true);
                saveUserLogin();
            } else new ToastClass(this, "Please enter name!!");
        } else new ToastClass(this, "Please enter mobile number!!");
    }

    private void saveUserLogin() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.ALREADY_LOGGED_IN, true);
        editor.apply();
        editor.commit();
        moveToShowLocation();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onResume() {
        super.onResume();
        checkLocationPermission();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void checkLocationPermission() {
        showProgressBar(false);
        if ( ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, 1);
        } else {
            checkUserLogin();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    /*check user login*/

                    checkUserLogin();
                } else {
                    new ToastClass(this, "Permission Not granted!!");
                    finish();
                }
                return;
            }
        }
    }

    private void checkUserLogin() {
        showProgressBar(false);
        if (sharedPreferences.getBoolean(Constants.ALREADY_LOGGED_IN, false)) {
            /*user already logged in*/
            moveToShowLocation();
        }
    }

    private void moveToShowLocation() {
        showProgressBar(false);
        Intent intent = new Intent(this, LocationShowActivity.class);
        startActivity(intent);
        startBackgroundService();
    }

    private void startBackgroundService() {
        Thread t = new Thread() {
            public void run() {
                startService(new Intent(MainActivity.this,
                        GetLocationService.class));
            }
        };
        t.start();
    }
}