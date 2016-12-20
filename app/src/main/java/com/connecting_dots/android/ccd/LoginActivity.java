package com.connecting_dots.android.ccd;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements com.nineoldandroids.animation.Animator.AnimatorListener {
    private static final String TAG = LoginActivity.class.getName();


    private final String UPLOAD_URL= Upload_Image.BASE_URL +"/login.php";
    private TextView tv_wait;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tv_wait = (TextView) findViewById(R.id.pleasewait);
        attemptLogin();

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
            Intent i = new Intent(LoginActivity.this, employer_homepage.class);
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
        String imei = (MyApplication.getInstance(LoginActivity.this.getApplicationContext())).getID();
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
                    String profes = jsonObject.getString("profes");

                    MyApplication.saveToSP(LoginActivity.this, type,name,phone,profes);
                    //Use this to get Username
                    //((MyApplication)getApplication()).getUsername();

                    gotoActivity(type);
                    return;
                } else if(success!=null && success.getInt(0)==1){
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    return;
                } else {
                    Toast.makeText(getApplicationContext(),"Please Check Internet Connectivity",Toast.LENGTH_LONG).show();
                    tv_wait.setText("Please Connect to Internet!");
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                    if(sp.contains(MyApplication.PREF_LOGGED_IN) && !sp.getString(MyApplication.PREF_LOGGED_IN,"").isEmpty()) {
                        gotoActivity(sp.getString(MyApplication.PREF_LOGGED_IN,""));
                        return;
                    }

                    LoginActivity.this.findViewById(R.id.login_progress).setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(LoginActivity.this,"Poor Internet Connection!",Toast.LENGTH_LONG).show();
            }
        }


    }
}



