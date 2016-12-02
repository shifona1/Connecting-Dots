package com.example.android.ccd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;

public class Home extends Activity {

    private static final String TAG = Home.class.getName();
    private Intent nextIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        nextIntent = new Intent(this,ScreenSlidePagerActivity.class);
        boolean networkGood = false;
        if(isNetworkAvailable())
            networkGood= true;
        else
        {
            Log.e(TAG,"FadeIn HomeInternet");

            findViewById(R.id.home_internet).setVisibility(View.INVISIBLE);
            YoYo.with(Techniques.FadeIn)
                    .duration(500)
                    //.delay(1500)
                    .withListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                            findViewById(R.id.home_internet).setVisibility(View.VISIBLE);

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            Log.e(TAG,"Fade");

                            findViewById(R.id.home_next).setVisibility(View.INVISIBLE);
                            YoYo.with(Techniques.FadeIn)
                                    .duration(900)
                                    //.delay(500)
                                    .withListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animation) {
                                            findViewById(R.id.home_next).setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animation) {

                                        }
                                    })
                                    .playOn(findViewById(R.id.home_next));
                            findViewById(R.id.home_next).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    moveNext();
                                }
                            });

                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    })
                    .playOn(findViewById(R.id.home_internet));
        }
        YoYo.with(Techniques.FadeInUp)
                .duration(1000)
                .playOn(findViewById(R.id.home_text));
        if(networkGood) {
            Log.e(TAG,"NetworkGood : FadeOut HomeInternet");

            YoYo.with(Techniques.FadeOutDown)
                    .duration(500)
                    .delay(3000)
                    .withListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            moveNext();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    })
                    .playOn(findViewById(R.id.home_text));
        }


    }

    static boolean resumeSecondTry= false;
    @Override
    protected void onResume() {
        super.onResume();
        if(resumeSecondTry)
            testGPSData();
        resumeSecondTry = true;
    }

    private boolean testGPSData() {
        LocationManager manager = (LocationManager) getSystemService( getApplicationContext().LOCATION_SERVICE );
        boolean isGPSEnabled = manager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        Log.e(TAG,"GPS Enabled : "+isGPSEnabled);
        if(!isGPSEnabled) {
            new MaterialDialog.Builder(this)
                    .title("GPS not Enabled!")
                    .content("Please Turn on GPS for full utilization")
                    .positiveText("Ok")
                    .negativeText("Dismiss")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Intent gpsSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            gpsSettings.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(gpsSettings);
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            startActivity(nextIntent);
                            finish();
                        }
                    })
                    .show();

            return false;
        }
        return true;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean status = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        Log.e(TAG,"Network Status : "+status);
        return status;
    }

    private void moveNext() {
        if(testGPSData()) {
            startActivity(nextIntent);
            finish();
        }
    }
}
