package com.example.android.ccd;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class Update_Profile_Employer extends AppCompatActivity {

    String FETCH_URL= Upload_Image.BASE_URL+"/fetch.php";
    String UPLOAD_URL_EMPLOYER= Upload_Image.BASE_URL+"/update_employer.php";
    private String name,phone,profession;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__profile__employer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ((EditText)findViewById(R.id.name)).setText(((MyApplication) getApplication()).getUsername());
        ((EditText)findViewById(R.id.phone)).setText(((MyApplication) getApplication()).getPhoneNo());

        TextView typeView = (TextView) findViewById(R.id.type);
        typeView.setText(((MyApplication) getApplication()).getType());


        //fetchData();
        Button Save=(Button)findViewById(R.id.Save_Details);
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etname = ((EditText) findViewById(R.id.name));
                name=etname.getText().toString();
                EditText etphone=((EditText) findViewById(R.id.phone));
                phone=etphone.getText().toString();
                profession=((EditText)findViewById(R.id.job)).getText().toString();
                if(name.isEmpty()) {
                    etname.setError("Name is Required!");
                    return;
                }
                if(phone.isEmpty()) {
                    etphone.setError("Phone Number is Required!");
                    return;
                }


                uploadData();
                        //uploadImage(name,phone, profession);

            }
        });
//        findViewById(R.id.buttonSkills).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent in = new Intent(getBaseContext(), Upload_Image.class);
//                in.putExtra("isEditPage",true);
//                startActivity(in);
//            }
//        });
//        SharedPreferences sp = getApplicationContext().getSharedPreferences("sp", MODE_PRIVATE);
//        sp.edit().putString("img", "").commit();


    }





    private void uploadData(){
        class updateProfile extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Update_Profile_Employer.this, "Updating the profile ", "Please wait...",true,true);


            }



            @Override
            protected String doInBackground(Bitmap... params) {

                HashMap data=new HashMap();
                data.put("IMEI",((MyApplication)getApplication()).getID());
                data.put("name",name);
                data.put("phone",phone);
                data.put("job",profession);

                String result = rh.sendPostRequest(UPLOAD_URL_EMPLOYER,data);

                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                   loading.dismiss();

                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
        };
        updateProfile up=new updateProfile();
        up.execute();

    }

//    private void uploadImage(final String name,final String phone, final String profession){
//        class UploadImage extends AsyncTask<Bitmap,Void,String>{
//
//            ProgressDialog loading;
//            RequestHandler rh = new RequestHandler();
//            String img;
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
////                loading.dismiss();
//                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
//
//            }
//
//            @Override
//            protected String doInBackground(Bitmap... params) {
//             //   Bitmap bitmap = params[0];
//                //String uploadImage = getStringImage(bitmap);
//                HashMap<String,String> data = new HashMap<>();
//
//                //String img = PreferenceManager.getDefaultSharedPreferences(Update_Profile_Employer.this).getString("img","");
//
//                data.put("username", name);
//                data.put("IMEI",((MyApplication)getApplication()).getID());
//                //data.put("img",img);
//                data.put("phone",phone);
//                data.put("profession",profession);
//
//                String result = rh.sendPostRequest(UPLOAD_URL_EMPLOYER,data);
//                return result;
//            }
//        }
//
//        UploadImage ui = new UploadImage();
//        ui.execute();
//    }

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
