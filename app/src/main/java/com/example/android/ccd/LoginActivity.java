package com.example.android.ccd;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements com.nineoldandroids.animation.Animator.AnimatorListener {
    private static final String TAG = LoginActivity.class.getName();

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    private final String UPLOAD_URL=Main2Activity.BASE_URL +"/login.php";
    private TextView tv_wait;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tv_wait = (TextView) findViewById(R.id.pleasewait);
        attemptLogin();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if(sp.contains(MyApplication.PREF_LOGGED_IN) && !sp.getString(MyApplication.PREF_LOGGED_IN,"").isEmpty()) {
            gotoActivity(sp.getString(MyApplication.PREF_LOGGED_IN,""));
            return;
        }

        animatePW();
    }

    void animatePW() {
        YoYo.with(Techniques.Wobble)
                .duration(1000)
                .interpolate(new AccelerateDecelerateInterpolator())
                .withListener(this)
                .playOn(tv_wait);
    }

    private void gotoActivity(String type) {
        Log.e(TAG,"Activity Open : "+type);
        if(type.equals(MyApplication.LOG_TYPE_EMPLOYER)) {
            Intent i = new Intent(LoginActivity.this, fragment_employer.class);
            startActivity(i);
            finish();
        }
        else
        {
            Intent i = new Intent(LoginActivity.this, Employee_HOmePage.class);
            startActivity(i);
            finish();
        }
    }

    public void attemptLogin() {
        Log.e("TAG","Attempt Login");
        String imei = ((MyApplication)(getApplication())).getID();
        new UserLoginTask(imei).execute((Void) null);
    }

    @Override
    public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {

    }

    @Override
    public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
        animatePW();
    }

    @Override
    public void onAnimationCancel(com.nineoldandroids.animation.Animator animation) {

    }

    @Override
    public void onAnimationRepeat(com.nineoldandroids.animation.Animator animation) {

    }

    public class UserLoginTask extends AsyncTask<Void, Void, JSONArray> {

        private final String imei;
        RequestHandler rh = new RequestHandler();

        UserLoginTask(String imei) {
            this.imei = imei;
        }

        @Override
        protected JSONArray doInBackground(Void... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("IMEI", imei);

            JSONArray result = null;
            try {
                result = new JSONArray(rh.sendPostRequest(UPLOAD_URL,data).trim());
                Log.e(TAG,"LoginCode >  : "+result.getInt(0));
                return result;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "doInBackground: "+result);

            return result;

        }

        @Override
        protected void onPostExecute(final JSONArray success) {
            try {

                if (success!=null && success.getInt(0)==0){
                    JSONObject jsonObject = success.getJSONObject(1);
                    String name = jsonObject.getString("username");
                    String type = jsonObject.getString("type");
                    String phone = jsonObject.getString("phone");

                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(MyApplication.PREF_LOGGED_IN,type);
                    editor.putString(MyApplication.PREF_USERNAME,name);
                    editor.putString(MyApplication.PREF_PHONE,phone);
                    editor.commit();

                    //Use this to get Username
                    //((MyApplication)getApplication()).getUsername();

                    gotoActivity(type);
                } else if(success!=null && success.getInt(0)==1){
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),"Please Check Internet Connectivity",Toast.LENGTH_LONG).show();
                    tv_wait.setText("Please Connect to Internet!");
                    LoginActivity.this.findViewById(R.id.login_progress).setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
}



