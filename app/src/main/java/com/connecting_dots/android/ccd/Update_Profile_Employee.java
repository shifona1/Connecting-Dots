package com.connecting_dots.android.ccd;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.HashMap;


public class Update_Profile_Employee extends AppCompatActivity  {


    private static final String TAG = Update_Profile_Employee.class.getName();
    private JobListAdapter adapt;
    private String UPLOAD_URL_EMPLOYEE= Upload_Image.BASE_URL+"/employee_homepage.php";
    private String SUBMIT_REQUEST_URL= Upload_Image.BASE_URL+"/submit_request.php";
    private ListView lv;
    private MaterialDialog dialog;

    private String R_NAME;
    private String R_TYPE;
    private String R_JOBIDs;
    private int MAX_JOBS;

    private ArrayList<Integer> jobs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__profile__employee);
        MAX_JOBS = MyApplication.getInstance(getApplicationContext()).getMAXJOBS();
        jobs = new ArrayList<>();



        ((EditText)findViewById(R.id.name)).setText(MyApplication.getInstance(Update_Profile_Employee.this.getApplicationContext()).getUsername());
        ((EditText)findViewById(R.id.phone)).setText(MyApplication.getInstance(Update_Profile_Employee.this.getApplicationContext()).getPhoneNo());


        TextView typeView = (TextView) findViewById(R.id.type);
        typeView.setText(MyApplication.getInstance(Update_Profile_Employee.this.getApplicationContext()).getType());

        adapt = new JobListAdapter(this);

        /////////////////////////////////// ADDDED job in ADAPTED USING THISSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS ////////////////////////
        //FETCH MAX_JOBS
        R_JOBIDs = MyApplication.getInstance(Update_Profile_Employee.this.getApplicationContext()).getProfession();

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

        Button Others = (Button) findViewById(R.id.custom_job_others_text);
        Others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(Update_Profile_Employee.this);
                editText.setHint("Your Profession");
                new MaterialDialog.Builder(Update_Profile_Employee.this)
                        .title("Please type to submit the Request ? ")
                        .customView(editText, true)
                        .positiveText("Submit")
                        .negativeText("Cancel")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                final String msg = editText.getText().toString();
                                new AsyncTask<Void,String,String>()
                                {
                                    RequestHandler rh = new RequestHandler();
                                    @Override
                                    protected String doInBackground(Void... params) {
                                          HashMap<String,String> data = new HashMap<String, String>();
                                          data.put("imei",(MyApplication.getInstance(Update_Profile_Employee.this.getApplicationContext())).getID());
                                          data.put("request",msg);

                                        return rh.sendPostRequest(SUBMIT_REQUEST_URL,data);
                                    }

                                    @Override
                                    protected void onPostExecute(String s) {
                                        super.onPostExecute(s);
                                        if(s==null) {
                                            Toast.makeText(Update_Profile_Employee.this,"Cannot Connect to Internet!",Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        Toast.makeText(Update_Profile_Employee.this,s,Toast.LENGTH_SHORT).show();

                                        if(s.contains("Success")) {
                                            Toast.makeText(Update_Profile_Employee.this,"Please wait until the Admin Approves",Toast.LENGTH_SHORT).show();
                                        }
                                        Log.e(TAG,s);
                                    }
                                }.execute();
                            }
                        }).show();
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
                data.put("imei", MyApplication.getInstance(Update_Profile_Employee.this.getApplicationContext()).getID());
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
                    Employee_HOmePage.refreshSuggestion(null);
                    finish();
                }

            }
        }.execute();
    }


}
