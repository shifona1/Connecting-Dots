package com.example.android.ccd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;

import static com.example.android.ccd.Upload_Image.goingforimageupdate;

public class Employee_HOmePage extends AppCompatActivity {

    public static final String PIC_URL = Upload_Image.PIC_URL;
    private static final String TAG = Employee_HOmePage.class.getSimpleName();
    private String R_IMAGE;
    public static final String UPDATE_PIC_URL= Upload_Image.UPDATE_PIC_URL_EMPLOYEE;
    private boolean justShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee__home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        justShow = false;
        Intent intent = getIntent();
        int person_id = -1;
        justShow = intent.getBooleanExtra("JUSTSHOW",false);
        String name = ((MyApplication) getApplication()).getUsername();
        String joblist = "";
        String contact = "";
        ListView lv = (ListView) findViewById(R.id.list_jobs);
        if(justShow) {
            findViewById(R.id.update_profile_employee_button).setVisibility(View.GONE);
            findViewById(R.id.Update_dp).setVisibility(View.GONE);
            person_id = intent.getIntExtra("person_id",-1);
            name = intent.getStringExtra("name");
            joblist = intent.getStringExtra("jobs");
            contact = intent.getStringExtra("contact");
            findViewById(R.id.contact).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.contact)).setText(contact);
            ArrayAdapter adapter = new ArrayAdapter<Integer>(this,0){
                @NonNull
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if(convertView == null)
                        convertView = new ImageView(parent.getContext());
                    Job.fromId(getContext(), (ImageView) convertView, getItem(position));

                    return convertView;
                }
            };
            String[] datas = joblist.substring(1,joblist.length()-1).split("\\.");
            for (int i=0;i<datas.length;i++) {
                Log.e(TAG,"JOB List  > "+i);
                adapter.add(Integer.parseInt(datas[i]));
            }


            lv.setAdapter(adapter);
            lv.setVisibility(View.VISIBLE);
            if(person_id==-1)
            {
                finish();return;
            }

        } else  {
            findViewById(R.id.list_jobs).setVisibility(View.GONE);
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        TextView textView = (TextView) findViewById(R.id.employee_name);
        textView.setText(name);
        final ImageView img = (ImageView) findViewById(R.id.profile_image);

        String imei = ((MyApplication) getApplication()).getID();
        String url;
        if(justShow)
            url = PIC_URL + "?id="+person_id;
        else
            url = PIC_URL + "?IMEI=" + imei;
        Log.e(TAG, "Attempt Load Img " + url + " on " + img);
        Picasso.with(this).load(url).error(R.drawable.pic).placeholder(android.R.drawable.progress_horizontal).transform(new CircleTransform()).into(img);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.edit().putString("img", "").commit();

        Button upload_dp = (Button) findViewById(R.id.Update_dp);
        upload_dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Employee_HOmePage.this, Upload_Image.class);
                startActivityForResult(i,101);

            }
        });




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

    @Override
    protected void onResume() {
        super.onResume();
        if(goingforimageupdate) {
            goingforimageupdate = false;
            final ImageView img = (ImageView) findViewById(R.id.profile_image);
            updateImage(((MyApplication) getApplication()).getID(), img);

        }
    }

    private void updateImage(final String imei, final ImageView img)
    {
        R_IMAGE = PreferenceManager.getDefaultSharedPreferences(Employee_HOmePage.this).getString("img","");
        new AsyncTask<Void,Void,String>() {
            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                HashMap<String,String> data = new HashMap<String,String>();
                data.put("imei",imei);
                data.put("img", R_IMAGE);
                // Uploading DP
                String result=rh.sendPostRequest(UPDATE_PIC_URL, data);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                // Showing Dp on Homepage
                String url = PIC_URL + "?IMEI=" + imei;
                Log.e(TAG, "Attempt Load Img " + url + " on " + img);
                Picasso.with(Employee_HOmePage.this).load(url).skipMemoryCache().error(R.drawable.pic).placeholder(android.R.drawable.progress_horizontal).transform(new CircleTransform()).into(img);

            }
        }.execute();


    }

}
