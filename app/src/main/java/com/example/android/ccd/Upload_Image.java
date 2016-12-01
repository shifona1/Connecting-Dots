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

public class Upload_Image extends AppCompatActivity implements View.OnClickListener {

    public static final String BASE_URL = "http://172.16.26.196/ccd";
    private static final String TAG = Upload_Image.class.getName();
    public static final String PIC_URL = Upload_Image.BASE_URL + "/getImage.php";
    public static final String UPDATE_PIC_URL = Upload_Image.BASE_URL + "/uploadDp.php";

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
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
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
        bitmap = null;
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            bitmap = null;
            Glide.with(this)
                    .load(data.getData())
                    .centerCrop()
                    .crossFade()
                    .placeholder(android.R.drawable.progress_horizontal)
                    .listener(new RequestListener<Uri, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            bitmap = ((GlideBitmapDrawable)resource.getCurrent()).getBitmap();
                            imageView.setImageBitmap(bitmap);
                            Log.e(TAG,"new short image : "+getStringImage(bitmap).length());
                            return true;
                        }
                    })
                    .into(450,450);

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