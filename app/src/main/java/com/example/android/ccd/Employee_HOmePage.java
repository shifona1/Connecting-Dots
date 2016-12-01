package com.example.android.ccd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class Employee_HOmePage extends AppCompatActivity {

    public static final String PIC_URL = Main2Activity.PIC_URL;
    private static final String TAG = Employee_HOmePage.class.getSimpleName();
    private String R_IMAGE;
    public static final String UPDATE_PIC_URL=Main2Activity.UPDATE_PIC_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee__home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        TextView textView = (TextView) findViewById(R.id.employee_name);
        textView.setText(((MyApplication) getApplication()).getUsername());
        final ImageView img = (ImageView) findViewById(R.id.profile_image);

        String imei = ((MyApplication) getApplication()).getID();
        String url = PIC_URL + "?IMEI=" + imei;
        Log.e(TAG, "Attempt Load Img " + url + " on " + img);
        Picasso.with(this).load(url).error(R.drawable.pic).placeholder(android.R.drawable.progress_horizontal).transform(new CircleTransform()).into(img);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.edit().putString("img", "").commit();

        Button upload_dp = (Button) findViewById(R.id.Update_dp);
        upload_dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Employee_HOmePage.this, Main2Activity.class);
                startActivity(i);
                finish();
            }
        });

        updateImage(imei,img);


        Button update_button = (Button) findViewById(R.id.update_profile_employee_button);
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Employee_HOmePage.this, Update_Profile_Employee.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void updateImage(String imei,ImageView img)
    {
        // Uploading DP
        R_IMAGE = PreferenceManager.getDefaultSharedPreferences(Employee_HOmePage.this).getString("img","");
        RequestHandler rh = new RequestHandler();
        HashMap<String,String> data = new HashMap<String,String>();
        data.put("imei",imei);
        data.put("img", R_IMAGE);
        String result=rh.sendPostRequest(UPDATE_PIC_URL, data);
        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
        // Showing Dp on Homepage
        String url = PIC_URL + "?IMEI=" + imei;
        Log.e(TAG, "Attempt Load Img " + url + " on " + img);
        Picasso.with(this).load(url).error(R.drawable.pic).placeholder(android.R.drawable.progress_horizontal).transform(new CircleTransform()).into(img);

    }

}
