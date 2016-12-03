package com.example.android.ccd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.android.ccd.Upload_Image.BASE_URL;
import static com.example.android.ccd.Upload_Image.goingforimageupdate;

public class Employee_HOmePage extends AppCompatActivity {

    public static final String PIC_URL = Upload_Image.PIC_URL;
    public static final String URL_SUGGESTION = BASE_URL+"/job_sugg.php";
    private static final String TAG = Employee_HOmePage.class.getSimpleName();
    private String R_IMAGE;
    public static final String UPDATE_PIC_URL= Upload_Image.UPDATE_PIC_URL_EMPLOYEE;
    public static final String LOAD_WORKPIC_URL= Upload_Image.BASE_URL+"/loadworkImage.php" ;
    public static final String CLEAR_WORKPIC_URL= Upload_Image.BASE_URL+"/clearworkImage.php" ;
    public static final String UPDATE_WORKPIC_URL= Upload_Image.BASE_URL+"/updateworkImage.php" ;

    private boolean justShow;
    private  int person_id = -1;
    private String imei,contact,name;
    private int load_image;
    private ImageView tempIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee__home_page);
        justShow = false;
        Intent intent = getIntent();
        justShow = intent.getBooleanExtra("JUSTSHOW",false);
        refreshText();
        String joblist = "";
        ListView lv = (ListView) findViewById(R.id.list_jobs);
        if(justShow) {
            findViewById(R.id.update_profile_employee_button).setVisibility(View.GONE);
            findViewById(R.id.Update_dp).setVisibility(View.GONE);
            person_id = intent.getIntExtra("person_id",-1);
            joblist = intent.getStringExtra("jobs");
            findViewById(R.id.contact).setVisibility(View.VISIBLE);
            ArrayAdapter adapter = new ArrayAdapter<Integer>(this,0){
                @NonNull
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if(convertView == null)
                        convertView = new ImageView(parent.getContext());
                    Job.fromId(getContext(), (ImageView) convertView, getItem(position));

                    return convertView;
                }
            };
            String[] datas = joblist.substring(1,joblist.length()-1).split("\\.");
            for (int i=0;i<datas.length;i++) {
                Log.e(TAG,"JOB List  > "+i);
                adapter.add(Integer.parseInt(datas[i]));
            }


            lv.setAdapter(adapter);
            lv.setVisibility(View.VISIBLE);
            if(person_id==-1)
            {
                finish();return;
            }

        } else  {
            findViewById(R.id.list_jobs).setVisibility(View.GONE);
        }





        imei = ((MyApplication) getApplication()).getID();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.edit().putString("img", "").commit();

        ImageButton upload_dp = (ImageButton) findViewById(R.id.Update_dp);
        upload_dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Employee_HOmePage.this, Upload_Image.class);
                load_image = 101;
                startActivityForResult(i, 101);

            }
        });

        View update_button = (View) findViewById(R.id.update_profile_employee_button);
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Employee_HOmePage.this, Update_Profile_Employee.class);
                startActivity(i);
                finish();
            }
        });
        loadDP(false);

        final ImageView img[] = new ImageView[3];
        img[0]=(ImageView) findViewById(R.id.Image_One);
        for(int i=0;i<3;i++) {
            final int it2= i;
            img[i].setTag(false);
            img[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getTag() != null && v.getTag() == true) {
                        ImageView iv = new ImageView(Employee_HOmePage.this);
                        new MaterialDialog.Builder(Employee_HOmePage.this)
                                .title("Title")
                                .customView(iv, true)
                                .positiveText("Ok")
                                .neutralText("Delete")
                                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        replaceWork(it2, false, img[it2]);
                                    }
                                }).show();
                    } else {
                        replaceWork(it2,true, img[it2]);
                    }
                }
            });
            loadWork(i, img[i],true);
            }
        refreshSuggestion();
    }

    private void replaceWork(final int workid, boolean notclear, final ImageView imageView) {

        if(notclear==false)
        {
            new AsyncTask<Void,Void,String>()
            {
                RequestHandler rh = new RequestHandler();
                @Override
                protected String doInBackground(Void... params) {
                    HashMap<String,String> data = new HashMap<String, String>();
                    data.put("index",workid+"");
                    data.put("imei",imei);
                    String result = rh.sendPostRequest(CLEAR_WORKPIC_URL,data);
                    return result;
                }

                protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                    Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                    if(result.contains("Success")) {
                        final String url = LOAD_WORKPIC_URL + "?imei=" + imei + "&index=" + (workid + 1);

                        //clear workid for given imei in database
                        loadWork(workid, imageView, false);
                    }
                }
            }.execute();


        } else {
                Intent i = new Intent(Employee_HOmePage.this,Upload_Image.class);
            startActivityForResult(i, 102);
            load_image=200+workid;
            tempIV = imageView;
            //open ImagePick activity and upload picked imaged
            // on work id
            // on success
            // call loadWork("url",imageView,false)



        }
    }
    private String getWorkImageUrl(int index) {
        if(!justShow)
            return  LOAD_WORKPIC_URL + "?imei=" + imei + "&index=" + (index + 1);

        return  LOAD_WORKPIC_URL + "?id=" + person_id + "&index=" + (index + 1);

    }
    private void loadWork(int workid, final ImageView image,boolean cache){
        Glide.with(this)
                .load(getWorkImageUrl(workid))
                .override(100, 100)
                .centerCrop()
                .skipMemoryCache(!cache)
                .error(android.R.drawable.stat_notify_error)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        image.setTag(false);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        image.setTag(true);
                        return true;
                    }
                })
                .into(image);

    }

    private void refreshText() {
        Intent intent = getIntent();

        name = ((MyApplication) getApplication()).getUsername();
        contact = ((MyApplication) getApplication()).getPhoneNo();
        if(justShow) {
            name = intent.getStringExtra("name");
            contact = intent.getStringExtra("contact");

        }
        TextView textView = (TextView) findViewById(R.id.employee_name);
        textView.setText(name);
        ((TextView)findViewById(R.id.contact)).setText(contact);
    }

    private  void loadDP(boolean skipcache) {
        //.skipMemoryCache();
        final ImageView img = (ImageView) findViewById(R.id.profile_image);
        String url;
        if(justShow)
            url = PIC_URL + "?id="+person_id;
        else
            url = PIC_URL + "?IMEI=" + imei;
        Log.e(TAG, "Attempt Load Img " + url + " on " + img);
        RequestCreator t = Picasso.with(this).load(url).error(R.drawable.pic);
        if(skipcache)
            t=t.skipMemoryCache();
        t.placeholder(android.R.drawable.progress_horizontal).transform(new CircleTransform()).into(img);
        //final ImageView iv = new ImageView(this);


        t = Picasso.with(this).load(url);
        t.transform(new BlurTransformation(this)).into((ImageView) findViewById(R.id.img_back));

    }
    private void refreshSuggestion() {
        final ImageButton sugg = (ImageButton) findViewById(R.id.job_sug);
        new AsyncTask<Void,Void,String>(){

            @Override
            protected String doInBackground(Void... voids) {
                Log.e(TAG,"Suggesting DIB ");
                RequestHandler rh = new RequestHandler();
                String jobs = ((MyApplication)getApplication()).getProfession();
                Log.e(TAG,"JOBS \t"+jobs);
                if(jobs.length()<2)
                    jobs="-1";
                else
                    jobs = jobs.substring(1,jobs.length()-1);

                String jobs_[] = jobs.split("\\.");
                JSONArray param = null;
                ArrayList<Integer> list = new ArrayList<>();
                for (String _job:jobs_) try{
                    list.add(Integer.parseInt(_job));
                }catch (Exception e) {
                    Log.e(TAG,"ERROR PARSE INT "+_job+" > "+e);
                }
                param = new JSONArray(list);
                String result = rh.sendGetRequest(URL_SUGGESTION+"?jobs="+param.toString());
                Log.e(TAG,"SUG R\t"+result);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try{

                    Log.e(TAG,"Suggesting PE ");
                    final int id = Integer.parseInt(s.trim());

                    Log.e(TAG,"Suggested Job : "+id);
                    if(id>=0) {
                        findViewById(R.id.job_sug_card).setVisibility(View.VISIBLE);
                        Job.fromId(Employee_HOmePage.this, sugg, id);
                        sugg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                new MaterialDialog.Builder(Employee_HOmePage.this)
                                        .title("Job Selection")
                                        .content("Can you also do '"+id+"' as a Job?")
                                        .positiveText("Yes")
                                        .negativeText("No")
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                Toast.makeText(Employee_HOmePage.this,"Picked "+id,Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    refreshSuggestion();
                                            }
                                        }).show();

                            }
                        });
                        sugg.setVisibility(View.VISIBLE);
                    }
                }catch (Exception e) {

                }
            }
        }.execute();
    }


    @Override
    protected void onResume() {
        super.onResume();
        refreshText();
        if(goingforimageupdate) {
            goingforimageupdate = false;
            if(load_image==101) {
                final ImageView img = (ImageView) findViewById(R.id.profile_image);
                updateImage(((MyApplication) getApplication()).getID(), img);
            } else {
                final String img = PreferenceManager.getDefaultSharedPreferences(Employee_HOmePage.this).getString("img","");
                final int workid = load_image-200;


                //upload and call loadWork("url",imageView,false)

                new AsyncTask<Void,Void,String>()
                {
                    RequestHandler rh = new RequestHandler();
                    @Override
                    protected String doInBackground(Void... params) {
                        HashMap<String,String> data = new HashMap<String, String>();
                        data.put("index",workid+"");
                        data.put("imei",imei);
                        data.put("img",img);
                        String result = rh.sendPostRequest(UPDATE_WORKPIC_URL,data);

                        return result;
                    }

                    protected void onPostExecute(String result) {
                        super.onPostExecute(result);
                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                        if(result.contains("Success")) {
                            //clear workid for given imei in database
                            loadWork(workid, tempIV, false);
                        }
                    }
                }.execute();
            }

        }
    }

    private void updateImage(final String imei, final ImageView img)
    {
        R_IMAGE = PreferenceManager.getDefaultSharedPreferences(Employee_HOmePage.this).getString("img","");
        new AsyncTask<Void,Void,String>() {
            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                HashMap<String,String> data = new HashMap<String,String>();
                data.put("imei",imei);
                data.put("img", R_IMAGE);
                // Uploading DP
                String result=rh.sendPostRequest(UPDATE_PIC_URL, data);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                // Showing Dp on Homepage
                String url = PIC_URL + "?IMEI=" + imei;
                Log.e(TAG, "Attempt Load Img " + url + " on " + img);
                loadDP(true);
            }
        }.execute();


    }

}
