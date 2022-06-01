package com.example.locationlogger.Services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.locationlogger.R;
import com.example.locationlogger.UI.MainActivity;
import com.example.locationlogger.Utils.UserLocation;

public class GetLocationService extends Service {

    private UserLocation userLocation;
    private static final int NOTIF_ID = 1;
    private static final String NOTIF_CHANNEL_ID = "Channel_Id";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground();
        getUserLocation();
        return super.onStartCommand(intent, flags, startId);

    }

    private void getUserLocation() {
        if (userLocation==null) {
            userLocation = new UserLocation(getApplicationContext());
        }
            /*make user location uses*/
    }

    private void startForeground() {
        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        startForeground(NOTIF_ID, new NotificationCompat.Builder(this,
                NOTIF_CHANNEL_ID) // don't forget create a notification channel first
                .setOngoing(true)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Location is accessed in background")
                .setContentIntent(pendingIntent)
                .build());
    }
}
