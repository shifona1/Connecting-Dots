package com.example.android.ccd;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

public class GPSTracker extends Service {
    private final static String TAG = GPSTracker.class.getName();
    private LocationManager manager;
    private long INTERVAL = 1000;
    private float DISTANCE = 10f;

    public static String PREF_LOCATION_LAT = "Latitude";
    public static String PREF_LOCATION_LONG = "Longitude";

    private Listener[] listeners  = new Listener[] {
            new Listener(LocationManager.GPS_PROVIDER),
            new Listener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private void getGpsAccess() {
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, INTERVAL, DISTANCE, listeners[1]);
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, INTERVAL, DISTANCE, listeners[0]);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initilizeLocationManager();
        getGpsAccess();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "Destroying");
        if (manager != null) {
            for (int i = 0; i < listeners.length; i++) {
                try {
                    manager.removeUpdates(listeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "Failed", ex);
                }
            }
        }
    }

    private void initilizeLocationManager() {
        Log.e(TAG, "Init LM *********");
        if(manager == null)
            manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class Listener implements LocationListener {
        Location lastLocation;

        public Listener(String provider) {
            Log.e(TAG,"Constructor");
            lastLocation  = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG,"Changed : "+location);
            lastLocation.set(location);
            SharedPreferences.Editor pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
            pref.putFloat(PREF_LOCATION_LAT,(float)location.getLatitude());
            pref.putFloat(PREF_LOCATION_LONG,(float)location.getLongitude());
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            Log.e(TAG,"Status Changed");
        }

        @Override
        public void onProviderEnabled(String s) {
            Log.e(TAG,"Provider Enabled "+s);
        }

        @Override
        public void onProviderDisabled(String s) {
            Log.e(TAG,"Provider Disabled");
        }
    }
}
