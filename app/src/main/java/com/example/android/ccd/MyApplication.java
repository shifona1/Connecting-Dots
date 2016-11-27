package com.example.android.ccd;

import android.app.Application;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by scopeinfinity on 26/11/16.
 */

public class MyApplication extends Application {

    private final static String TAG = MyApplication.class.getName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG,"Application Started");
        Intent intent = new Intent(getApplicationContext(), GPSTracker.class);
        startService(intent);

    }


}
