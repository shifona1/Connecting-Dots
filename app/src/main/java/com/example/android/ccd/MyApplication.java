package com.example.android.ccd;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.multidex.MultiDexApplication;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.example.android.ccd.GPSTracker.PREF_ZIP;

/**
 * Created by scopeinfinity on 26/11/16.
 */

public class MyApplication extends MultiDexApplication {

    private final static String TAG = MyApplication.class.getName();

    public static String PREF_LOGGED_IN =   "LOGGEDIN";
    public static String LOG_TYPE_EMPLOYER =   "Employer";
    public static String LOG_TYPE_EMPLOYEE =   "Employee";
    public static String PREF_PHONE="123456789";

    public static String PREF_USERNAME =   "username";


    private TelephonyManager telephonyManager;
    private String IMEI;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG,"Application Started");
        telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        IMEI = md5(telephonyManager.getDeviceId());
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

    public String getZIP() {
        SharedPreferences pm = PreferenceManager.getDefaultSharedPreferences(this);
        if(pm.contains(PREF_ZIP)) {
            return pm.getString(PREF_LOGGED_IN,"");
        }

        return "";
    }

    private String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    public String getUsername() {
        return PreferenceManager.getDefaultSharedPreferences(this).getString(PREF_USERNAME,"unnamed");
    }

    public String getType() {
        return PreferenceManager.getDefaultSharedPreferences(this).getString(PREF_LOGGED_IN,"unnamed");
    }

    public String getPhoneNo()
    {
        return PreferenceManager.getDefaultSharedPreferences(this).getString(PREF_PHONE,"unnamed");
    }


}
