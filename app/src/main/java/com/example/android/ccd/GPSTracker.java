package com.example.android.ccd;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GPSTracker extends Service {
    private final static String TAG = GPSTracker.class.getName();
    private LocationManager manager;
    private long INTERVAL = 10000;
    private float DISTANCE = 10f;

    public static String PREF_LOCATION_LAT = "Latitude";
    public static String PREF_LOCATION_LONG = "Longitude";
    public static String PREF_ZIP = "zip";



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
            checkZip((float)location.getLatitude()+","+(float)location.getLongitude());
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

    private void checkZip(final String longNlat) {
        new AsyncTask<Void,Void,Integer>(){

            @Override
            protected Integer doInBackground(Void... voids) {
                String url =
                        "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + longNlat
                                + "&key=" + Credentials.getGeocodingApi();
                String jsonresult = new RequestHandler().sendPostRequest(url,null).trim();
                Log.e(TAG, "CheckZip url : "+url);
                //Log.e(TAG, "CheckZip result : "+jsonresult);
                Integer found = null;
                try {
                    JSONObject jsonObject = new JSONObject(jsonresult);
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.e(TAG,"GOT RESULTS");

                    for (int i=0;found==null && i<results.length();i++) {
                        JSONObject result = results.getJSONObject(i);
                        Log.e(TAG,"Finding address_components");

                        if(!result.has("address_components"))
                            continue;;
                        JSONArray address = result.getJSONArray("address_components");
                        Log.e(TAG,"Found address_components");

                        for (int j=0;found==null && j<address.length();j++) {
                            JSONObject comp = address.getJSONObject(j);
                            Log.e(TAG,"Finding types");
                            if(!comp.has("types"))
                                continue;;
                            JSONArray type  = comp.getJSONArray("types");
                            Log.e(TAG,"Found types");
                            if(type.length()>0 && type.getString(0).equals("postal_code")) {
                                Log.e(TAG,"POSTAL");
                                int zipcode = -1;
                                try{
                                    zipcode = Integer.parseInt(comp.getString("long_name"));
                                }catch (Exception e) {

                                }
                                try{if(zipcode!=-1)
                                    zipcode = Integer.parseInt(comp.getString("short_name"));
                                }catch (Exception e) {

                                }
                                if(zipcode != -1) {
                                    found = zipcode;

                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e(TAG, "Final ZIP CODE : "+found);

                if(found!=null) {
                    if(((MyApplication)getApplication()).getZIP()==found)
                        return found;

                    //Pushing ZIP to server
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("IMEI", ((MyApplication) getApplication()).getID());
                    map.put("zip", "" + found);

                    new RequestHandler().sendPostRequest(Main2Activity.BASE_URL + "/updatelocation.php", map);
                }
                return  found;
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                if(integer == null)
                    return;
                SharedPreferences.Editor pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                pref.putFloat(PREF_ZIP,integer);
                Log.e(TAG,"SP "+PREF_ZIP+" > "+integer);


            }
        }.execute();


    }
}
