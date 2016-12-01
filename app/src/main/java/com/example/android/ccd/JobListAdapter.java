package com.example.android.ccd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by shifona on 28/11/16.
 */
public class JobListAdapter extends RecyclerView.Adapter<ImageViewHolder> implements JobCallback {
    private static final String TAG = JobListAdapter.class.getName();
    private Update_Profile_Employee activity;
    private ArrayList<Integer> jobs;
    private ArrayList<Boolean> jobsSelected;

    public JobListAdapter(Update_Profile_Employee activity) {
        this.activity = activity;
        jobs = new ArrayList<>();
        jobsSelected = new ArrayList<>();
    }

    public void addJobId(int id, boolean isselected) {
        jobs.add(id);
        jobsSelected.add(isselected);

    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_list_images, parent, false);
        return new ImageViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.setPosition(position);
        Job.fromId(activity, holder.getImageView(), jobs.get(position));
        holder.setSelected(jobsSelected.get(position));
        //v.setTag(getItem(position));

    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    public Context getContext() {
        return activity;
    }


    public Boolean getJobsSelected(int pos) {
        return jobsSelected.get(pos);
    }

    public void setJobSelected(int pos, boolean val) {
        jobsSelected.set(pos, val);
        notifyDataSetChanged();
    }

    public String getCSV() {
        String data = ".";
        for (int i=0;i<jobs.size();i++) {
            if(jobsSelected.get(i))
                data+=jobs.get(i)+".";
        }
        return data;
    }

    public void setCSV(String data,int maxjobs) {
        jobsSelected = new ArrayList<>();
        jobs = new ArrayList<>();
        for (int i=0;i<maxjobs;i++) {
            jobs.add(i);
            jobsSelected.add(false);

        }
        String[] datas = data.substring(1,data.length()-1).split("\\.");
        for (int i=0;i<datas.length;i++) {
            Log.e(TAG,"JOB Enabled  > "+i);
            jobsSelected.set(Integer.parseInt(datas[i]), true);
        }
        notifyDataSetChanged();
        for (int i=0;i<jobsSelected.size();i++) {
            Log.e(TAG,"JOB pos "+i+" > "+jobsSelected.get(i));
        }
    }
}

interface JobCallback{
    void setJobSelected(int pos,boolean val);
}
class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private ImageView iv;
    private JobListAdapter adapter;
    private int position;
    public ImageViewHolder(View itemView, JobListAdapter jobListAdapter) {
        super(itemView);
        iv = (ImageView) itemView.findViewById(R.id.img);
        iv.setOnClickListener(this);
        adapter = jobListAdapter;

    }

    @Override
    public void onClick(View view) {
        adapter.setJobSelected(position,!adapter.getJobsSelected(position));
    }

    public ImageView getImageView() {
        return iv;
    }

    public void setSelected(boolean istrue) {
        if(istrue)
            iv.setBackgroundColor(adapter.getContext().getResources().getColor(android.R.color.holo_blue_light));
        else
            iv.setBackgroundColor(adapter.getContext().getResources().getColor(android.R.color.white));

    }

    public void setPosition(int position) {
        this.position = position;
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

    static Job fromId(final Context context, final ImageView iv, final int id) {
//        Job o = getInstance(context, id);
//        if (o != null)
//            return o;

        final Job job = new Job(id, null);
        final String url = Upload_Image.BASE_URL + "/skills/" + id + ".jpg";
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

