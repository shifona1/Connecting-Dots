package com.connecting_dots.android.ccd;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.connecting_dots.android.ccd.Upload_Image.getBitmapFromString;

public class ViewProfile extends AppCompatActivity {
    String FETCH_URL= Upload_Image.BASE_URL+"fetch2.php";
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        email = "krish@123";//getIntent().getStringExtra("email");
        ((TextView)findViewById(R.id.email)).setText(email);
        fetchData();

    }



    private void fetchData(){
        class FetchData extends AsyncTask<String,Void,String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }



            @Override
            protected String doInBackground(String... params) {

                HashMap data=new HashMap();
                data.put("Email_id",email);

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
                    ((EditText)findViewById(R.id.job)).setText(obj.getString("job"));
                    Bitmap bmp = getBitmapFromString(obj.getString("img"));
                    if(bmp!=null)
                            ((ImageView) findViewById(R.id.vp_img)).setImageBitmap(bmp);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
        }
        FetchData fetch=new FetchData();
        fetch.execute();

    }


}
