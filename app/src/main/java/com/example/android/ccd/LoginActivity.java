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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements com.nineoldandroids.animation.Animator.AnimatorListener {
    private static final String TAG = LoginActivity.class.getName();


    private final String UPLOAD_URL=Main2Activity.BASE_URL +"/login.php";
    private TextView tv_wait;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        attemptLogin();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if(sp.contains(MyApplication.PREF_LOGGED_IN) && !sp.getString(MyApplication.PREF_LOGGED_IN,"").isEmpty()) {
            gotoActivity(sp.getString(MyApplication.PREF_LOGGED_IN,""));
        }
        tv_wait = (TextView) findViewById(R.id.pleasewait);

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

    public class UserLoginTask extends AsyncTask<Void, Void, String> {

        private final String imei;
        RequestHandler rh = new RequestHandler();

        UserLoginTask(String imei) {
            this.imei = imei;
        }

        @Override
        protected String doInBackground(Void... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("IMEI",imei);
                String result = rh.sendPostRequest(UPLOAD_URL,data).trim();
                Log.e(TAG, "doInBackground: "+result);

                return result;

        }

        @Override
        protected void onPostExecute(final String success) {
            if (success.equals(MyApplication.LOG_TYPE_EMPLOYEE)||success.equals(MyApplication.LOG_TYPE_EMPLOYER)) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                sp.edit().putString(MyApplication.PREF_LOGGED_IN,success).commit();

                gotoActivity(success);
            } else if(success.equals("Invalid")){
                Intent i = new Intent(LoginActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(getApplicationContext(),"Please Check Internet Connectivity",Toast.LENGTH_LONG).show();
                tv_wait.setText("Please Connect to Internet!");
                LoginActivity.this.findViewById(R.id.login_progress).setVisibility(View.GONE);
            }
        }


    }
}



