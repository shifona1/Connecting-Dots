package com.example.android.ccd;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class Upload_Image extends AppCompatActivity implements View.OnClickListener {

    public static final String BASE_URL = "https://connectingdots1.000webhostapp.com";
    private static final String TAG = Upload_Image.class.getName();
    public static final String PIC_URL = Upload_Image.BASE_URL + "/getImage.php";
    public static final String UPDATE_PIC_URL_EMPLOYEE = Upload_Image.BASE_URL + "/uploadDpEmployee.php";
    public static final String UPDATE_PIC_URL_EMPLOYER = Upload_Image.BASE_URL + "/uploadDpEmployer.php";

    private int PICK_IMAGE_REQUEST = 1;
    public static boolean goingforimageupdate = false;

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
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    Log.e(TAG, "ImagePusingInSP : " + str.length());
                    sp.edit().putString("img",str).commit();
                    setResult(RESULT_OK);
                    Log.e(TAG,"RESULT OK");
                    goingforimageupdate = true;
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
        bitmap = null;
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            bitmap = null;
            byte[] dat = null;
            try {
                dat = MyApplication.getBytes( getContentResolver().openInputStream(data.getData()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Glide.with(this)
                    .load(dat)
                    .centerCrop()
                    .crossFade()
                    .placeholder(android.R.drawable.progress_horizontal)
                    .listener(new RequestListener<byte[], GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, byte[] model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, byte[] model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            bitmap = ((GlideBitmapDrawable)resource.getCurrent()).getBitmap();
                            imageView.setImageBitmap(bitmap);

                            Log.e(TAG,"new short image : "+getStringImage(bitmap).length());
                            return true;
                        }

                        })
                    .into(450,450);

            Glide.with(Upload_Image.this)
                    .load(dat)
                    .listener(new RequestListener<byte[], GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, byte[] model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, byte[] model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            sp.edit().putString("img_small",getStringImage(((GlideBitmapDrawable)resource.getCurrent()).getBitmap())).commit();
                            return true;
                        }
                    })
                    .into(100,100);

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
        if(str==null)
            return null;
        try {
            byte[] data = Base64.decode(str, Base64.DEFAULT);
            ByteArrayInputStream baos = new ByteArrayInputStream(data);
            Bitmap bitmap = BitmapFactory.decodeStream(baos);
            return bitmap;
        }catch (Exception e) {
            return null;
        }
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