package com.connecting_dots.android.ccd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

import static com.connecting_dots.android.ccd.Upload_Image.BASE_URL;
import static com.connecting_dots.android.ccd.Upload_Image.goingforimageupdate;

public class Employee_HOmePage extends AppCompatActivity {

    public static final String PIC_URL = Upload_Image.PIC_URL;
    public static final String URL_SUGGESTION = BASE_URL+"/job_sugg.php";
    private static final String TAG = Employee_HOmePage.class.getSimpleName();
    private String R_IMAGE,R_IMAGE_SMALL;
    public static final String UPDATE_PIC_URL= Upload_Image.UPDATE_PIC_URL_EMPLOYEE;
    public static final String LOAD_WORKPIC_URL= Upload_Image.BASE_URL+"/loadworkImage.php" ;
    public static final String CLEAR_WORKPIC_URL= Upload_Image.BASE_URL+"/clearworkImage.php" ;
    public static final String UPDATE_WORKPIC_URL= Upload_Image.BASE_URL+"/updateworkImage.php" ;

    private boolean justShow;
    private  int person_id = -1;
    private String imei,contact,name,joblist;
    private int load_image;
    private ImageView tempIV;
    private RecyclerView.Adapter<MyViewHolder> radapter;
    private boolean worksbool[];
    private final ImageView img[] = new ImageView[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_employee__home_page);
        justShow = false;
        Intent intent = getIntent();
        justShow = intent.getBooleanExtra("JUSTSHOW",false);
        refreshText();
        joblist = MyApplication.getInstance(Employee_HOmePage.this.getApplicationContext()).getProfession();
        if(justShow) {
            findViewById(R.id.emp_help).setVisibility(View.GONE);
            findViewById(R.id.update_profile_employee_button).setVisibility(View.GONE);
            findViewById(R.id.Update_dp).setVisibility(View.GONE);
            person_id = intent.getIntExtra("person_id",-1);
            if(person_id==-1)
            {
                finish();return;
            }
            joblist = intent.getStringExtra("jobs");
            findViewById(R.id.contact).setVisibility(View.VISIBLE);


        } else  {
         //   findViewById(R.id.card_jobs).setVisibility(View.GONE);
        }




        updateJobs();





        imei = MyApplication.getInstance(Employee_HOmePage.this.getApplicationContext()).getID();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("img","");
        editor.putString("img_small","");
        editor.commit();

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
            }
        });
        loadDP(false);

        img[0]=(ImageView) findViewById(R.id.Image_One);
        img[1]=(ImageView) findViewById(R.id.Image_Two);
        img[2]=(ImageView) findViewById(R.id.Image_Three);
        worksbool = new boolean[3];
        for(int i=0;i<3;i++) {
            final int it2= i;
            if(justShow) {

                //***********************************/
                //If iamge is blank hide it
                ///
                img[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Show FULL SCREEN
                        Intent intent1 = new Intent(Employee_HOmePage.this, DisplayPic.class);
                        intent1.putExtra("url", getWorkImageUrl(it2));
                        startActivity(intent1);
                    }
                });
            }
            else
            img[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Problem HERE
                    if (worksbool[it2]) {
                        ImageView iv = new ImageView(Employee_HOmePage.this);
                        new MaterialDialog.Builder(Employee_HOmePage.this)
                                .title("Remove Work")
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
        refreshSuggestion(this,Employee_HOmePage.this);
    }

    private void updateJobs() {
        if(!justShow)
            joblist = MyApplication.getInstance(Employee_HOmePage.this.getApplicationContext()).getProfession();


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list_jobs);


        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        String[] datas;
        if(joblist.length()<2)
            datas = new String[0];
        else datas = joblist.substring(1,joblist.length()-1).split("\\.");
        final ArrayList<Integer> jobs = new ArrayList<>();
        for (int i=0;i<datas.length;i++) {
            Log.e(TAG,"JOB List  > "+i);
            jobs.add(Integer.parseInt(datas[i]));
        }
        findViewById(R.id.list_jobs).setVisibility(View.VISIBLE);
        findViewById(R.id.no_job).setVisibility(View.VISIBLE);
        if(datas.length==0) {
            findViewById(R.id.list_jobs).setVisibility(View.GONE);
        }
        else
            findViewById(R.id.no_job).setVisibility(View.GONE);

        radapter = new RecyclerView.Adapter<MyViewHolder> (){
            @Override
            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                ImageView v = new ImageView(Employee_HOmePage.this);
                return new MyViewHolder(v);
            }

            @Override
            public void onBindViewHolder(MyViewHolder holder, int position) {
                Job.fromId(Employee_HOmePage.this, holder.v, jobs.get(position));

            }

            @Override
            public int getItemCount() {
                return jobs.size();
            }
        };



        recyclerView.setAdapter(radapter);

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
                    data.put("index",(1+workid)+"");
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
    private void loadWork(final int workid, final ImageView image, boolean cache){
        final String url = getWorkImageUrl(workid);
        Log.e(TAG,"ATTEMPT > "+url);
        Picasso.with(this).load(url)
                .error(android.R.drawable.ic_menu_add)
                .skipMemoryCache()
                .placeholder(android.R.drawable.progress_horizontal)
                .into(image, new Callback() {
                    @Override
                    public void onSuccess() {
                        worksbool[workid] = true;
                        if(justShow)
                        {
                            findViewById(R.id.text_work_images).setVisibility(View.GONE);
                            image.setVisibility(View.VISIBLE);
                        }




                    }

                    @Override
                    public void onError() {
                        worksbool[workid] = false;
                        if(justShow) {
                            image.setVisibility(View.GONE);

                            findViewById(R.id.text_work_images).setVisibility(View.VISIBLE);
                            for (int i=0;i<3;i++)
                                if(img[i].getVisibility()==View.VISIBLE) {
                                    findViewById(R.id.text_work_images).setVisibility(View.GONE);
                                }

                        }

                    }
                });

    }

    private void refreshText() {
        Intent intent = getIntent();

        name = MyApplication.getInstance(Employee_HOmePage.this.getApplicationContext()).getUsername();
        contact = MyApplication.getInstance(Employee_HOmePage.this.getApplicationContext()).getPhoneNo();
        if(justShow) {
            name = intent.getStringExtra("name");
            contact = intent.getStringExtra("contact");

        }
        TextView textView = (TextView) findViewById(R.id.employee_name);
        textView.setText(name);
        ((TextView)findViewById(R.id.contact)).setText(contact);
    }
    static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView v;
        public MyViewHolder(ImageView v) {
            super(v);
            this.v = v;
        }
    }

    private  void loadDP(boolean skipcache) {
        //.skipMemoryCache();
        final ImageView img = (ImageView) findViewById(R.id.profile_image);
        final String url;
        if(justShow)
            url = PIC_URL + "?id="+person_id;
        else
            url = PIC_URL + "?IMEI=" + imei;

        Log.e(TAG, "Attempt Load Img " + url + " on " + img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Employee_HOmePage.this, DisplayPic.class);
                intent.putExtra("url",url);
                startActivity(intent);
            }
        });
        RequestCreator t = Picasso.with(this).load(url).error(R.drawable.pic);
        if(skipcache)
            t=t.skipMemoryCache();
        t.placeholder(android.R.drawable.progress_horizontal).transform(new CircleTransform()).into(img);
        //final ImageView iv = new ImageView(this);


        t = Picasso.with(this).load(url);
        t.transform(new BlurTransformation(this)).into((ImageView) findViewById(R.id.img_back));
        if(skipcache)
            t=t.skipMemoryCache();
        t.transform(new BlurTransformation(this)).into((ImageView)findViewById(R.id.img_back));

    }

    //if employeehomepage == null just update data
    public static void refreshSuggestion(final Activity employeehomepage, final Context context) {
         new AsyncTask<Void,Void,String>(){

            @Override
            protected String doInBackground(Void... voids) {
                Log.e(TAG,"Suggesting DIB ");
                RequestHandler rh = new RequestHandler();
                String jobs = MyApplication.getInstance(context.getApplicationContext()).getProfession();
                Log.e(TAG,"JOBS \t"+jobs);
                if(jobs.length()<2)
                    jobs="-1";
                else
                    jobs = jobs.substring(1,jobs.length()-1);

                String jobs_[] = jobs.split("\\.");
                JSONArray param = null;
                ArrayList<Integer> list = new ArrayList<>();
                Log.e(TAG,"_job "+jobs);
                for (String _job:jobs_) try{
                    list.add(Integer.parseInt(_job));
                }catch (Exception e) {
                    Log.e(TAG,"ERROR PARSE INT "+_job+" > "+e);
                }
                param = new JSONArray(list);
                String url = URL_SUGGESTION+"?jobs="+param.toString();

                if(employeehomepage == null)
                    url+="&newjobs="+param.toString();

                String result = rh.sendGetRequest(url);
                Log.e(TAG,"SUG R\t"+result);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try{
                    if(employeehomepage == null) {
                        Log.e(TAG,"UPDATE TO ML "+s);
                        return;
                    }

                    Log.e(TAG,"Suggesting PE ");
                    final int id = Integer.parseInt(s.trim());

                    Log.e(TAG,"Suggested Job : "+id);
                    if(id>=0) {
                        ImageButton sugg = (ImageButton) employeehomepage.findViewById(R.id.job_sug);
                        employeehomepage.findViewById(R.id.job_sug_card).setVisibility(View.VISIBLE);
                        Job.fromId(employeehomepage, sugg, id);
                        sugg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                new MaterialDialog.Builder(employeehomepage)
                                        .title("Job Selection")
                                        .content("Can you also do this as a Job?")
                                        .positiveText("Yes")
                                        .negativeText("No")
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                Intent i = new Intent(employeehomepage, Update_Profile_Employee.class);
                                                employeehomepage.startActivity(i);
                                            }
                                        })
                                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    refreshSuggestion(employeehomepage,context);
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
        updateJobs();
        if(goingforimageupdate) {
            goingforimageupdate = false;
            if(load_image==101) {
                final ImageView img = (ImageView) findViewById(R.id.profile_image);
                updateImage(MyApplication.getInstance(Employee_HOmePage.this.getApplicationContext()).getID(), img);
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
                        data.put("index",(1+workid)+"");
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
        R_IMAGE_SMALL = PreferenceManager.getDefaultSharedPreferences(Employee_HOmePage.this).getString("img_small","");
        new AsyncTask<Void,Void,String>() {
            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                HashMap<String,String> data = new HashMap<String,String>();
                data.put("imei",imei);
                data.put("img", R_IMAGE);
                data.put("img_small", R_IMAGE_SMALL);
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

    public void openhelp(View v) {
        Intent intent
                 = new Intent(this,ScreenSlidePagerActivity.class);
        intent.putExtra("JUSTSHOW",true);
        startActivity(intent);
    }

}
