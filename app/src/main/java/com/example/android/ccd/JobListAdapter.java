package com.example.android.ccd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
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
public class JobListAdapter extends ArrayAdapter<Integer>
{
    private Context context;
    private LayoutInflater inflater;

    public JobListAdapter(Context context)
    {
        super(context,R.layout.job_list_images);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v==null) v= inflater.inflate(R.layout.job_list_images,null);
        Job.fromId(context, (ImageButton) v.findViewById(R.id.img),getItem(position));

        return v;
    }

}
class Job {
    int id;
    Bitmap bmp;
    Job(int id,Bitmap bmp) {
        this.id = id;
        this.bmp = bmp;
    }

    static File getRootFile(int id) {
        File r = new File(Environment.getExternalStorageState());
        File dir = new File(r,"ConnectingDots");
        if(!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir, id+".jpg");
     }

    static Job getInstance(Context context, int id) {
        File f = getRootFile(id);
        if(f.exists()) {
            Bitmap bmp = BitmapFactory.decodeFile(String.valueOf(f));
            Job job = new Job(id,bmp);
            return job;
        }
        return  null;
    }

    static Job fromId(final Context context,final ImageButton iv, final int id) {
        Job o = getInstance(context, id);
        if(o!=null)
            return o;

        final Job job = new Job(id,null);
        Picasso.with(context).load(Main2Activity.BASE_URL+ "/skills/"+id+".jpg").into(iv, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                Bitmap bmp = ((BitmapDrawable)iv.getDrawable()).getBitmap();
                job.setBMP(bmp);
                job.saveImage(context);
            }

            @Override
            public void onError() {

            }
        });
        return job;
    }

    void setBMP(Bitmap bmp) {
        this.bmp = bmp;
    }

    void saveImage(Context context) {
        File file = getRootFile(id);
        OutputStream fout = null;
        try {
            fout = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG,85,fout);
            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

