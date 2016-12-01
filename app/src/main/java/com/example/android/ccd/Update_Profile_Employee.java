package com.example.android.ccd;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.HashMap;


public class Update_Profile_Employee extends AppCompatActivity  {


    private static final String TAG = Update_Profile_Employee.class.getName();
    private JobListAdapter adapt;
    private String UPLOAD_URL_EMPLOYEE= Upload_Image.BASE_URL+"/employee_homepage.php";

    private ListView lv;
    private MaterialDialog dialog;

    private String R_NAME;
    private String R_TYPE;
    private String R_JOBIDs;
    private int MAX_JOBS = 5;

    private ArrayList<Integer> jobs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__profile__employee);
        jobs = new ArrayList<>();
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

        /////////////////////////////////// ADDDED job in ADAPTED USING THISSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS ////////////////////////
        //FETCH MAX_JOBS
        R_JOBIDs = ".2.4.";

        findViewById(R.id.button_job_image_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapt.setCSV(R_JOBIDs,MAX_JOBS);
                new MaterialDialog.Builder(Update_Profile_Employee.this)
                        .limitIconToDefaultSize()
                        .title("Pick Skills")
                        .positiveText("Save")
                        .negativeText("Cancel")
                        .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {

                                return true;
                            }
                        })
                        .adapter(adapt,new LinearLayoutManager(getApplicationContext()))
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                R_JOBIDs = adapt.getCSV();

                            }
                        })

                        .show();

            }
        });


        EditText etname = ((EditText) findViewById(R.id.name));
        String name=etname.getText().toString();

        String profession=((EditText)findViewById(R.id.job)).getText().toString();
        EditText etphone=((EditText) findViewById(R.id.phone));
        String phone=etphone.getText().toString();

        if(name.isEmpty()) {
            etname.setError("Name is Required!");
            return;
        }
        if(phone.isEmpty()) {
            etname.setError("Phone Number is Required!");
            return;
        }

        HashMap<String,String> data = new HashMap<>();
        data.put("username", name);
        data.put("phone",phone);
        data.put("Profession",profession);
        //data.put("Prof_Image",kkkkkkkk);
        RequestHandler rh = new RequestHandler();

        //String result = rh.sendPostRequest(UPLOAD_URL_EMPLOYEE,data);


        /************* INITLIZE VARIABLE R_ANYTHING,, from server ******/


        /** then on click update button, send to server ****************/
        Log.e(TAG,"To Send Server");
        Log.e(TAG,"NAME : "+R_NAME);
        Log.e(TAG,"TYPE : "+R_TYPE);
        Log.e(TAG,"JID : "+R_JOBIDs);


    }


}
