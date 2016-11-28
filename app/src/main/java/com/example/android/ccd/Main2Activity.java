package com.example.android.ccd;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.util.HashMap;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    public static final String BASE_URL = "http://172.16.26.196/ccd";
    private static final String TAG = Main2Activity.class.getName();

    private int PICK_IMAGE_REQUEST = 1;

    private Button buttonChoose;
    private Button buttonUpload;
    private Button buttonView;

    private ImageView imageView;

    private Bitmap bitmap;

    private Uri filePath;

    private String R_NAME,R_PASS,R_ID;
    private  Boolean R_TYPE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        buttonView = (Button) findViewById(R.id.buttonViewImage);

        //if(intent.hasExtra("isEditPage")) {
            buttonView.setVisibility(View.VISIBLE);
            buttonUpload.setVisibility(View.GONE);
            buttonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String str =  getStringImage(bitmap);
                    SharedPreferences sp = getApplicationContext().getSharedPreferences("sp", MODE_PRIVATE);
                    Log.e(TAG,"ImagePusingInSP : "+str.length());
                    sp.edit().putString("img",str).commit();
                    finish();

                }
            });
//        }
//        else {
//            R_NAME = intent.getStringExtra("Name");
//            R_PASS=  intent.getStringExtra("Password");
//            R_ID=  intent.getStringExtra("id");
//            R_TYPE=intent.getBooleanExtra("ISEMPLOYER", false);
//
//
//        }
        imageView = (ImageView) findViewById(R.id.imageView);

        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        if(bmp == null)
            return "";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();

        Log.e(TAG,"OriginalImageLength : "+imageBytes.length);
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Log.e(TAG,"EncodeImageLength : "+encodedImage.length());
        return encodedImage;
    }
    public static Bitmap getBitmapFromString(String str){
        byte[] data =  Base64.decode(str, Base64.DEFAULT);
        ByteArrayInputStream baos = new ByteArrayInputStream(data);
        Bitmap bitmap = BitmapFactory.decodeStream(baos);
        return bitmap;
    }


    @Override
    public void onClick(View v) {
       // if (v == buttonChoose) {
            showFileChooser();
//        }
//        if(v == buttonUpload){
//            uploadImage();
//        }
    }


}