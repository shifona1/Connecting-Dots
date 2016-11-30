package com.example.android.ccd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.example.android.ccd.fragment_main.UPLOAD_URL;

public class Update_Profile_Employer extends AppCompatActivity {

    String FETCH_URL=Main2Activity.BASE_URL+"/fetch.php";
    String UPLOAD_URL_EMPLOYER=Main2Activity.BASE_URL+"/update_employer.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__profile__employer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fetchData();
        Button Save=(Button)findViewById(R.id.Save_Details);
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=((EditText)findViewById(R.id.name)).getText().toString();
                String phone="+++++++++++++++++";//((EditText)findViewById(R.id.name)).getText().toString();
                String profession=((EditText)findViewById(R.id.job)).getText().toString();
                        uploadImage(name,phone, profession);

            }
        });
        findViewById(R.id.buttonSkills).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getBaseContext(), Main2Activity.class);
                in.putExtra("isEditPage",true);
                startActivity(in);
            }
        });
        SharedPreferences sp = getApplicationContext().getSharedPreferences("sp", MODE_PRIVATE);
        sp.edit().putString("img", "").commit();


    }





    private void fetchData(){
        class FetchData extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }



            @Override
            protected String doInBackground(Bitmap... params) {

                HashMap data=new HashMap();
                data.put("IMEI",((MyApplication)getApplication()).getID());

                String result = rh.sendPostRequest(FETCH_URL,data);

                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                loading.dismiss();
                try {
                    JSONObject obj = new JSONObject(s);
                    String name = obj.getString("name");
                    ((EditText)findViewById(R.id.name)).setText(name);
                    String password = obj.getString("pass");
                    ((EditText)findViewById(R.id.name)).setText(password);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
        }
        FetchData fetch=new FetchData();
        fetch.execute();

    }

    private void uploadImage(final String name,final String phone, final String profession){
        class UploadImage extends AsyncTask<Bitmap,Void,String>{

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();
            String img;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(Bitmap... params) {
             //   Bitmap bitmap = params[0];
                //String uploadImage = getStringImage(bitmap);
                HashMap<String,String> data = new HashMap<>();

                //String img = PreferenceManager.getDefaultSharedPreferences(Update_Profile_Employer.this).getString("img","");

                data.put("username", name);
                data.put("IMEI",((MyApplication)getApplication()).getID());
                //data.put("img",img);
                data.put("phone",phone);
                data.put("profession",profession);

                String result = rh.sendPostRequest(UPLOAD_URL_EMPLOYER,data);
                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute();
    }

//    @Override
//    public void onClick(View v) {
//        if (v == buttonChoose) {
//            showFileChooser();
//        }
//        if(v == buttonUpload){
//            uploadImage();
//        }
//    }

}
