package com.example.android.ccd;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by scopeinfinity on 26/11/16.
 */

public class MyApplication extends Application {

    private final static String TAG = MyApplication.class.getName();

    public static String PREF_LOGGED_IN =   "LOGGEDIN";
    public static String LOG_TYPE_EMPLOYER =   "Employer";
    public static String LOG_TYPE_EMPLOYEE =   "Employee";

    private TelephonyManager telephonyManager;
    private String IMEI;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG,"Application Started");
        telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        IMEI = telephonyManager.getDeviceId();
        if(IMEI == null || IMEI.isEmpty()) {
           Toast.makeText(getApplicationContext(),"Sorry! Application Not Supported : E101",Toast.LENGTH_SHORT).show();
            System.runFinalizersOnExit(true);
            System.exit(0);
            return;
        }
        Toast.makeText(this,IMEI,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), GPSTracker.class);
        startService(intent);

    }

    public String getID() {
        return IMEI;
    }


}
