package com.example.android.ccd;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by shifona on 28/11/16.
 */
public class JobListAdapter extends ArrayAdapter<Integer> {
    private Update_Profile_Employee activity;
    private LayoutInflater inflater;

    public JobListAdapter(Update_Profile_Employee activity) {
        super(activity, R.layout.job_list_images);
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) v = inflater.inflate(R.layout.job_list_images, null);
        v.setOnClickListener( activity);
        Job.fromId(activity, (ImageButton) v.findViewById(R.id.img), getItem(position));
        v.setTag(getItem(position));

        return v;
    }

}

class Job {
    private static final String TAG = Job.class.getName();
    int id;
    Bitmap bmp;

    Job(int id, Bitmap bmp) {
        this.id = id;
        this.bmp = bmp;
    }

    static File getRootFile(int id) {
        File r = Environment.getExternalStorageDirectory();
        File dir = new File(r, "ConnectingDots");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir, id + ".jpg");
    }

    static Job getInstance(Context context, int id) {
        File f = getRootFile(id);
        Log.e(TAG,"Trying to load "+id+" Job from File : "+f);
        if (f.exists()) {
            Bitmap bmp = BitmapFactory.decodeFile(String.valueOf(f));
            Job job = new Job(id, bmp);

            Log.e(TAG,"Success");
            return job;
        }

        Log.e(TAG,"Failed");
        return null;
    }

    static Job fromId(final Context context, final ImageButton iv, final int id) {
//        Job o = getInstance(context, id);
//        if (o != null)
//            return o;

        final Job job = new Job(id, null);
        final String url = Main2Activity.BASE_URL + "/skills/" + id + ".jpg";
        Picasso.with(context).load(url).into(iv, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                Bitmap bmp = ((BitmapDrawable) iv.getDrawable()).getBitmap();
                job.setBMP(bmp);
                Log.e(TAG,"JOB Load Success : "+url);
          //      job.saveImage();

            }

            @Override
            public void onError() {

                Log.e(TAG,"JOB Load Failed : "+url);
            }
        });
        return job;
    }

    void setBMP(Bitmap bmp) {
        this.bmp = bmp;
    }

    void saveImage() {
        File file = getRootFile(id);
        Log.e(TAG,"Tring to save Job "+id+" at "+file);
        OutputStream fout = null;
        try {
            file.createNewFile();
            fout = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 85, fout);
            fout.close();
            Log.e(TAG,"Saved");return;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG,e.getMessage());
        }

        Log.e(TAG,"NotSaved");
    }


}

