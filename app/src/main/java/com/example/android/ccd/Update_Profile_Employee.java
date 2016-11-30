package com.example.android.ccd;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.util.DialogUtils;

import java.util.HashMap;

import static com.example.android.ccd.fragment_main.UPLOAD_URL;

public class Update_Profile_Employee extends AppCompatActivity {

    private JobListAdapter adapt;
    private String UPLOAD_URL_EMPLOYEE=Main2Activity.BASE_URL+"/employee_homepage.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__profile__employee);
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

        ((EditText)findViewById(R.id.name)).setText(((MyApplication) getApplication()).getUsername());
        ((EditText)findViewById(R.id.phone)).setText(((MyApplication) getApplication()).getPhoneNo());


        TextView typeView = (TextView) findViewById(R.id.type);
        typeView.setText(((MyApplication) getApplication()).getType());

        adapt = new JobListAdapter(this);
        for (int i=0;i<5;i++)adapt.add(i);
        final ListView lv = new ListView(this);
        lv.setAdapter(adapt);
        findViewById(R.id.button_job_image_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(getApplicationContext())
                        .title("Pick Skills")
                        .customView(lv, true)
                        .show();
            }
        });


        String name=((EditText)findViewById(R.id.name)).getText().toString();
        String profession=((EditText)findViewById(R.id.job)).getText().toString();
        String phone=((EditText)findViewById(R.id.phone)).getText().toString();

        HashMap<String,String> data = new HashMap<>();
        data.put("username", name);
        data.put("Profession",profession);
        //data.put("Prof_Image",kkkkkkkk);
        RequestHandler rh = new RequestHandler();

        String result = rh.sendPostRequest(UPLOAD_URL_EMPLOYEE,data);



    }

}
