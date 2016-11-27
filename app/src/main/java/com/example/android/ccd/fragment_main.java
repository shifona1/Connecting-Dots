package com.example.android.ccd;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.ccd.R;

import java.util.HashMap;

public class fragment_main extends Fragment {


    private String R_NAME,R_PASS,R_ID,R_IMAGE;
    private  Boolean R_TYPE;
    private View rootView;
    public static final String UPLOAD_URL = Main2Activity.BASE_URL+"upload.php";
    public static final String UPLOAD_KEY = "image";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        this.rootView = rootView;
        Button mEmailSignInButton = (Button) rootView.findViewById(R.id.buttonLoadPicture);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), Main2Activity.class);

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
        Intent i = new Intent(getContext(), Main2Activity.class);
        i.putExtra("Name", ((EditText) rootView.findViewById(R.id.name)).getText().toString());
        i.putExtra("Password", ((EditText) rootView.findViewById(R.id.password)).getText().toString());
        i.putExtra("id", ((EditText) rootView.findViewById(R.id.email)).getText().toString());
        i.putExtra("ISEMPLOYER", ((RadioButton) rootView.findViewById(R.id.radio_employer)).isChecked());

        R_NAME = i.getStringExtra("Name");
        R_PASS=  i.getStringExtra("Password");
        R_ID=  i.getStringExtra("id");
        R_TYPE=i.getBooleanExtra("ISEMPLOYER", false);

        R_IMAGE = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("img","");

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
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                String Type=R_TYPE?"Employer":"Employee";
                HashMap<String,String> data = new HashMap<>();

                data.put("username", R_NAME);
                data.put("Email_Id",R_ID);
                data.put("Password",R_PASS);
                data.put("Type",Type);
                data.put(UPLOAD_KEY, R_IMAGE);

                String result = rh.sendPostRequest(UPLOAD_URL,data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute();
    }

}