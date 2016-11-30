package com.example.android.ccd;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.util.DialogUtils;

import java.util.HashMap;

import static com.example.android.ccd.fragment_main.UPLOAD_URL;


public class Update_Profile_Employee extends AppCompatActivity  implements View.OnClickListener, AdapterView.OnItemClickListener {


    private static final String TAG = Update_Profile_Employee.class.getName();
    private JobListAdapter adapt;
    private String UPLOAD_URL_EMPLOYEE=Main2Activity.BASE_URL+"/employee_homepage.php";

    private ListView lv;
    private MaterialDialog dialog;

    private String R_NAME;
    private String R_TYPE;
    private int R_ID;



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

        for (int i = 0; i < 5; i++) adapt.add(i);
        lv = new ListView(this);
        lv.setAdapter(adapt);
        lv.setOnItemClickListener(this);

        findViewById(R.id.button_job_image_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new MaterialDialog.Builder(Update_Profile_Employee.this)
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


        /************* INITLIZE VARIABLE R_ANYTHING,, from server ******/


        /** then on click update button, send to server ****************/
        Log.e(TAG,"To Send Server");
        Log.e(TAG,"NAME : "+R_NAME);
        Log.e(TAG,"TYPE : "+R_TYPE);
        Log.e(TAG,"JID : "+R_ID);








    }


    @Override
    public void onClick(View view) {

        R_ID = (int) view.getTag();
        Log.e(TAG,"NEW ID : "+R_ID);
        dialog.dismiss();

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        R_ID = (int) view.getTag();
        Log.e(TAG,"> NEW ID : "+R_ID);
        dialog.dismiss();
    }
}
