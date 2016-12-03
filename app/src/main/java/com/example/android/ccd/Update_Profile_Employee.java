package com.example.android.ccd;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.nearby.messages.internal.Update;
import com.squareup.picasso.Picasso;

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



        ((EditText)findViewById(R.id.name)).setText(MyApplication.getInstance().getUsername());
        ((EditText)findViewById(R.id.phone)).setText(MyApplication.getInstance().getPhoneNo());


        TextView typeView = (TextView) findViewById(R.id.type);
        typeView.setText(MyApplication.getInstance().getType());

        adapt = new JobListAdapter(this);

        /////////////////////////////////// ADDDED job in ADAPTED USING THISSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS ////////////////////////
        //FETCH MAX_JOBS
        R_JOBIDs = MyApplication.getInstance().getProfession();

        findViewById(R.id.button_job_image_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapt.setCSV(R_JOBIDs, MAX_JOBS);
                new MaterialDialog.Builder(Update_Profile_Employee.this)
                        .limitIconToDefaultSize()
                        .title("Pick Skills")
                        .positiveText("Save")
                        .iconRes(R.drawable.profession)
                        .negativeText("Cancel")
                        .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {

                                return true;
                            }
                        })
                        .adapter(adapt, new LinearLayoutManager(getApplicationContext()))
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                R_JOBIDs = adapt.getCSV();

                            }
                        })

                        .show();

            }
        });

        Button Save =(Button) findViewById(R.id.Save_Details);
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etname = ((EditText) findViewById(R.id.name));
                String name = etname.getText().toString();
                EditText etphone = ((EditText) findViewById(R.id.phone));
                String phone = etphone.getText().toString();


                if (name.isEmpty()) {
                    etname.setError("Name is Required!");
                    return;
                }
                if (phone.isEmpty()) {
                    etphone.setError("Phone Number is Required!");
                    return;
                }
                UpdateProfile(name, phone, R_JOBIDs);
            }
        });





        /************* INITLIZE VARIABLE R_ANYTHING,, from server ******/


        /** then on click update button, send to server ****************/
        Log.e(TAG,"To Send Server");
        Log.e(TAG,"NAME : "+R_NAME);
        Log.e(TAG,"TYPE : "+R_TYPE);
        Log.e(TAG,"JID : "+R_JOBIDs);


    }

    private void UpdateProfile(final String name,final String phone, final String profession)
    {
        new AsyncTask<Void,Void,String>() {
            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                HashMap<String,String> data = new HashMap<String,String>();
                data.put("imei", MyApplication.getInstance().getID());
                data.put("username", name);
                data.put("phone",phone);
                data.put("Profession",profession);

                String result = rh.sendPostRequest(UPLOAD_URL_EMPLOYEE,data);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                if(result.contains("Success")) {
                    MyApplication.saveToSP(Update_Profile_Employee.this, null,name,phone,profession);
                    finish();
                }

            }
        }.execute();
    }


}
