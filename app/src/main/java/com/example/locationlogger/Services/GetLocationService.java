package com.example.locationlogger.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.locationlogger.R;
import com.example.locationlogger.Utils.UserLocation;

import java.util.Timer;
import java.util.TimerTask;

public class GetLocationService extends Service {

    private UserLocation userLocation;
    private static final int NOTIF_ID = 1;
    private static final String NOTIF_CHANNEL_ID = "Channel_Id";
    private static final String NOTI_CHANNEL_NAME = "NOTIFICATION_CHANNEL";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground();
        updateLocationAfterInterval();
        //getUserLocation();
        return START_STICKY;

    }

    private void updateLocationAfterInterval() {
        Timer myTimer = new Timer();
        TimerTask myTask = new TimerTask() {
            @Override
            public void run() {
                getUserLocation();
            }
        };

         myTimer.scheduleAtFixedRate(myTask , 0l, 60000); // Runs every 5 mins
       // myTimer.scheduleAtFixedRate(myTask, 0l, 2000); // Runs every 2 sec
    }

    private void getUserLocation() {
        if (userLocation == null) {
            userLocation = new UserLocation(getApplicationContext());
        } else userLocation.updateLocation();
    }

    private void startForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel chan = new NotificationChannel(NOTIF_CHANNEL_ID, NOTI_CHANNEL_NAME, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

        }
        Notification notification = new NotificationCompat.Builder(this,
                NOTIF_CHANNEL_ID) // don't forget create a notification channel first
                .setOngoing(true)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Location is accessed in background")
                .setPriority(Notification.PRIORITY_HIGH)
                .build();
        startForeground((int) System.currentTimeMillis(), notification);
    }


}
