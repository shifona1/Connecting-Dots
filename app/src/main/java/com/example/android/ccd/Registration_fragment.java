package com.example.android.ccd;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.HashMap;

public class Registration_fragment extends Fragment {


    private static final String TAG = Registration_fragment.class.getName();
    private String R_IMAGE_SMALL;
    private String R_NAME,R_PASS,R_ID,R_IMAGE,R_PHONE;
    private  Boolean R_TYPE;
    private View rootView;
    public static final String UPLOAD_URL = Upload_Image.BASE_URL+"/Registration.php";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        this.rootView = rootView;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("img","");
        editor.putString("img_small","");
        editor.commit();

        Button pickPicture = (Button) rootView.findViewById(R.id.buttonLoadPicture);
        pickPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), Upload_Image.class);

                startActivity(i);
                //getActivity().finish();

            }

        });
        rootView.findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerNow();
            }
        });
        return rootView;

    }
    void registerNow() {
        EditText etname = ((EditText) rootView.findViewById(R.id.name));
        R_NAME = etname.getText().toString();
        EditText etphone=((EditText) rootView.findViewById(R.id.phone));
        R_PHONE = etphone.getText().toString();

        R_TYPE=((RadioButton) rootView.findViewById(R.id.radio_employer)).isChecked();
        R_IMAGE = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("img","");
        R_IMAGE_SMALL = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("img_small","");
        Log.e(TAG, "ImageLength : " + R_IMAGE.length());
        Toast.makeText(getContext(),"ImageLength : "+R_IMAGE.length(),Toast.LENGTH_SHORT).show();

        if(R_NAME.isEmpty()) {
            etname.setError("Name is Required!");
            return;
        }
        if(R_PHONE.isEmpty()) {
            etphone.setError("Phone Number is Required!");
            return;
        }



        doRegister();
    }



    private void doRegister(){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Registering", "Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
                if(s.contains("Success")) {
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    startActivity(i);
                    getActivity().finish();
                    MyApplication.getInstance(getActivity().getApplicationContext()).getMAXJOBS();
                }
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                String Type=R_TYPE?"Employer":"Employee";
                HashMap<String,String> data = new HashMap<>();

                data.put("username", R_NAME);
                data.put("IMEI",MyApplication.getInstance().getID());
                data.put("Type",Type);
                data.put("phone",R_PHONE);
                data.put("image", R_IMAGE);
                data.put("image_small", R_IMAGE_SMALL);

                String result = rh.sendPostRequest(UPLOAD_URL,data);
                Log.e(TAG,"DIB : "+result);
                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute();
    }

}