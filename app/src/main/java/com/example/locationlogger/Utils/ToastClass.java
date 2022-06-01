package com.example.locationlogger.Utils;

import android.content.Context;
import android.widget.Toast;

public class ToastClass {

    private String toastMessage;
    private Context context;

    private void showToast() {
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
    }

    public ToastClass(Context context, String toastMessage) {
        this.toastMessage = toastMessage;
        this.context = context;
        this.showToast();
    }
}
