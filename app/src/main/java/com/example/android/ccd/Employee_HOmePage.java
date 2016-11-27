package com.example.android.ccd;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class Employee_HOmePage extends AppCompatActivity {

    public static final String PIC_URL = Main2Activity.BASE_URL+"getImage.php";
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

        ImageView img=(ImageView)findViewById(R.id.profile_image);
        RequestHandler rh=new RequestHandler();
        HashMap<String,String> data = new HashMap<>();
        data.put("IMEI","+++++++++++++++++++++++++++++");

        String url=rh.sendPostRequest(PIC_URL,data);
        Picasso.with(getApplicationContext()).load(url).into(img);

        Button update_button=(Button)findViewById(R.id.update_profile_employee_button);
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Employee_HOmePage.this, Update_Profile_Employee.class);
                startActivity(i);
                finish();
            }
        });

    }

}
