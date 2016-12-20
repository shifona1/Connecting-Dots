package com.connecting_dots.android.ccd;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.connecting_dots.android.ccd.GPSTracker.PREF_ZIP;

/**
 * Created by scopeinfinity on 26/11/16.
 */

public class MyApplication {
    private static MyApplication instance;


    public static MyApplication getInstance(Context context) {
        if(instance == null)
            instance = new MyApplication(context);
        return instance;
    }

    public static MyApplication getInstance(Activity activity) {
        if(instance == null)
            instance = new MyApplication(activity.getApplicationContext());
        return instance;
    }

    public static MyApplication getInstance(Fragment fragment) {
        if(instance == null)
            instance = new MyApplication(fragment.getActivity().getApplicationContext());
        return instance;
    }

    public static MyApplication getInstance() {
        return instance;
    }

    private final static String TAG = MyApplication.class.getName();
    private String URL_MAXJOBS= Upload_Image.BASE_URL+"/getcountJobs.php";


    private Context context;
    public static String PREF_LOGGED_IN     =   "LOGGEDIN";
    public static String LOG_TYPE_EMPLOYER  =   "Employer";
    public static String LOG_TYPE_EMPLOYEE  =   "Employee";
    public static String PREF_PROFESSION    =   "Profession";
    public static String PREF_MAX_JOBS    =   "MAXJOBS";
    public static String PREF_PHONE         =   "phone";
    public static String PREF_IMEI         =   "IMEI";

    public static String PREF_USERNAME =   "username";


    private TelephonyManager telephonyManager;
    private String IMEI;

    public MyApplication(Context context) {
        this.context = context;
        Log.e(TAG,"Application Started");
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        SharedPreferences pm = PreferenceManager.getDefaultSharedPreferences(context);
        if(pm.contains(PREF_IMEI))
            IMEI = pm.getString(PREF_IMEI,null);
        if(IMEI==null)
        {
            IMEI = md5(telephonyManager.getDeviceId());
            pm.edit().putString(PREF_IMEI,IMEI).commit();
        }
        if(IMEI == null || IMEI.isEmpty()) {
           Toast.makeText(context,"Sorry! Application Not Supported : E101",Toast.LENGTH_SHORT).show();
            System.runFinalizersOnExit(true);
            System.exit(0);
            return;
        }
        Intent intent = new Intent(context, GPSTracker.class);
        context.startService(intent);
        getMAXJOBS();

    }

    public String getID() {
        return IMEI;
    }

    public String getZIP() {
        SharedPreferences pm = PreferenceManager.getDefaultSharedPreferences(context);
        if(pm.contains(PREF_ZIP)) {
            return pm.getString(PREF_ZIP,"");
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
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_USERNAME,"unnamed");
    }

    public String getType() {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_LOGGED_IN,"unnamed");
    }

    public String getPhoneNo()
    {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_PHONE,"unnamed");
    }



    public String getProfession() {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_PROFESSION,".");
    }

    public static void saveToSP(Context context,String type,String name,String phone, String profession) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
            putStringSP(editor,MyApplication.PREF_LOGGED_IN, type);
            putStringSP(editor,MyApplication.PREF_USERNAME,name);
            putStringSP(editor,MyApplication.PREF_PHONE,phone);
            putStringSP(editor,MyApplication.PREF_PROFESSION,profession);
        editor.commit();

    }

    public static void putStringSP( SharedPreferences.Editor editor,String key,String val) {
        if(val!=null) {
            Log.e(TAG,key+" > "+val);
            editor.putString(key,val);
        }
    }


    public static byte[] getBytes(InputStream inputStream)  {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        try {
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteBuffer.toByteArray();
    }

    public int getMAXJOBS() {
        int jobs = PreferenceManager.getDefaultSharedPreferences(context).getInt(PREF_MAX_JOBS,0);
        new AsyncTask<Void,String,String>()
        {
            RequestHandler rh = new RequestHandler();
            @Override
            protected String doInBackground(Void... params) {

                return rh.sendGetRequest(URL_MAXJOBS);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    if(s!=null) {
                        Integer c = Integer.parseInt(s);
                        Log.e(TAG, "NEW MAX JOBS : " + c);
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt(MyApplication.PREF_MAX_JOBS,c);
                        editor.commit();

                    }
                }catch (Exception e) {

                }
            }
        }.execute();

        return jobs;
    }
}
