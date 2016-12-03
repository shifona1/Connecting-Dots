package com.example.android.ccd;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.android.ccd.GPSTracker.PREF_LOCATION_LAT;
import static com.example.android.ccd.Upload_Image.goingforimageupdate;

public class employer_homepage extends ActionBarActivity {
    private final String Log_Tag = employer_homepage.class.getSimpleName();

    private ProfessionListAdapter adapt;
    private String SEARCH_URL= Upload_Image.BASE_URL+"/search.php";


    public static final String PIC_URL = Upload_Image.PIC_URL;
    public static final String UPDATE_PIC_URL= Upload_Image.UPDATE_PIC_URL_EMPLOYER;
    private static final String TAG = Employee_HOmePage.class.getSimpleName();
    private static final String JobList_URL=Upload_Image.BASE_URL+"/jobList.php";

    private String R_NAME,R_PHONE,R_IMAGE,R_IMAGE_SMALL;
    private String imei,zip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(Log_Tag, "Forecast entry: ");
        setContentView(R.layout.fragment_employer);
        zip = ((MyApplication)getApplication()).getZIP();
        R_NAME = ((MyApplication)getApplication()).getUsername();
        R_PHONE = ((MyApplication)getApplication()).getPhoneNo();


        //  if (savedInstanceState == null) {
       //     getSupportFragmentManager().beginTransaction()
      //              .add(R.id.container, new SearchFragment())
      //              .commit();
      //  }


        Intent intent = getIntent();
        //final String data[] = new String[]{"A","Hello","Hi","Gagan","Gassss"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        AutoCompleteTextView sr = (AutoCompleteTextView)findViewById(R.id.searchView);
        sr.setAdapter(adapter);
        sr.setThreshold(0);
        sr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                updateList(i,adapt);
            }
        });
        new AsyncTask<Void,Void,ArrayList<String> >(){

            @Override
            protected ArrayList<String> doInBackground(Void... params) {
                RequestHandler rf = new RequestHandler();
                String data =rf.sendPostRequest(JobList_URL, new HashMap<String, String>());
                Log.e(TAG,"Fetching  "+JobList_URL);
                try {
                    JSONArray arr = new JSONArray(data);
                    ArrayList<String> a = new ArrayList<String>();
                    for(int i=0;i<arr.length();i++) {
                        Log.e(TAG,"> "+arr.getString(i));
                        a.add(arr.getString(i));
                    }
                    return a;
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(employer_homepage.this,"Please Connect to Internet!!",Toast.LENGTH_SHORT).show();
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<String> strings) {
                super.onPostExecute(strings);
                if(strings==null)
                    return;
                adapter.clear();
                adapter.addAll(strings);
                adapter.notifyDataSetChanged();
            }
        }.execute();
        Log.e(TAG,"Fetching Trying  "+JobList_URL);



        View update_button=(View)findViewById(R.id.update_profile_employee_button);
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(employer_homepage.this, Update_Profile_Employer.class);
                i.putExtra("isEmployer", true);
                startActivity(i);
                finish();
            }
        });

        refreshText();


        final ImageView img = (ImageView) findViewById(R.id.profile_image);
        imei = ((MyApplication) getApplication()).getID();
        loadDP(false);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("img","");
        editor.putString("img_small","");
        editor.commit();

        ImageButton upload_dp = (ImageButton) findViewById(R.id.Update_dp);
        upload_dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(employer_homepage.this, Upload_Image.class);
                startActivityForResult(i, 101);

            }
        });

        adapt = new ProfessionListAdapter(this);

//        Log.v("**************<<<<", ":::::::::::::::::::::::;;");
        ((ListView)findViewById(R.id.listView)).setAdapter(adapt);

        findViewById(R.id.findemployee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /********************* SAMPLEDATA ***********/
                ArrayList<Employee> list = new ArrayList<Employee>();
                SharedPreferences pm = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                if(!pm.contains(PREF_LOCATION_LAT)) {
                    Toast.makeText(getApplicationContext(),"GPS Not working!",Toast.LENGTH_LONG).show();
                    return;
                }

//                float minelat = pm.getFloat(PREF_LOCATION_LAT,0);
//                float minelong = pm.getFloat(PREF_LOCATION_LONG,0);
//                list.add(new Employee((minelat+","+minelong),"ME : "+R_NAME,R_PHONE));


                //Other Employees
                for(int i=0;i <  adapt.getCount();i++) {
                    Person person = adapt.getItem(i);
                    if(person.getZip().equals(zip))
                        list.add(new Employee((person.getLat()+","+person.getLon()),person.getName()
                            ,person.getPhone(),person.getID(),person.getProfession()));
                }

                Intent intent = new Intent(getApplicationContext(),FindEmployee.class);
                intent.putExtra("data",Employee.getEncoded(list));
                startActivity(intent);
            }
        });

    }


    private void refreshText() {
        Intent intent = getIntent();

        R_NAME = ((MyApplication) getApplication()).getUsername();
        R_PHONE = ((MyApplication) getApplication()).getPhoneNo();
        TextView textView = (TextView) findViewById(R.id.employer_name);
        textView.setText(R_NAME);
        ((TextView)findViewById(R.id.contact)).setText(R_PHONE);
    }

    private  void loadDP(boolean skipcache) {
        //.skipMemoryCache();
        final ImageView img = (ImageView) findViewById(R.id.profile_image);
        final String url = PIC_URL + "?IMEI=" + imei;
        Log.e(TAG, "Attempt Load Img " + url + " on " + img);
        Picasso.with(this).load(url).error(R.drawable.pic).placeholder(android.R.drawable.progress_horizontal).transform(new CircleTransform()).into(img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(employer_homepage.this, DisplayPic.class);
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
        if(skipcache)
            t=t.skipMemoryCache();
        t.transform(new BlurTransformation(this)).into((ImageView)findViewById(R.id.img_back));

    }


    @Override
    protected void onResume() {
        super.onResume();
        refreshText();
        if(goingforimageupdate) {
            goingforimageupdate = false;
            final ImageView img = (ImageView) findViewById(R.id.profile_image);
            updateImage(((MyApplication) getApplication()).getID(), img);

        }
    }


    /**
     * A placeholder fragment containing a simple view.
     */


    private void updateImage(final String imei, final ImageView img)
    {
        R_IMAGE = PreferenceManager.getDefaultSharedPreferences(employer_homepage.this).getString("img","");
        R_IMAGE_SMALL = PreferenceManager.getDefaultSharedPreferences(employer_homepage.this).getString("img_small","");
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







      private void updateList(int jobid, final ProfessionListAdapter adapt)
      {
          class UpdateList extends AsyncTask<String,Void,ArrayList<Person> > {

              ProgressDialog loading;
              RequestHandler rh = new RequestHandler();

              @Override
              protected void onPreExecute() {
                  super.onPreExecute();
                  loading = ProgressDialog.show(employer_homepage.this, "Searching","Please wait...",true,true);
              }

              @Override
              protected void onPostExecute(ArrayList<Person>  s) {
                  super.onPostExecute(s);
                  loading.dismiss();
                  if(s!=null) {
                      adapt.clear();
                      adapt.addAll(s);
                      adapt.notifyDataSetChanged();
                  } else {
                      Toast.makeText(employer_homepage.this, "Unable to Search", Toast.LENGTH_LONG).show();
                  }
               }

              @Override
              protected ArrayList<Person>  doInBackground(String... params) {

                  HashMap<String,String> data = new HashMap<>();

                  data.put("jobid", params[0]);
                  ArrayList<Person> list = new ArrayList<>();
                  String result = rh.sendPostRequest(SEARCH_URL,data);
                  try {
                      JSONArray arr = new JSONArray(result);
                      if(arr.getInt(0)==0) {
                          JSONArray data_ = arr.getJSONArray(1);
                          for (int i = 0; i < data_.length(); i++) {
                              JSONObject obj = data_.getJSONObject(i);
                              String name = obj.getString("name");
                              String phone = obj.getString("phone");
                              String profession = obj.getString("prof");
                              float lat = (float) obj.getDouble("lat");
                              float lon = (float) obj.getDouble("lon");
                              String zip =  obj.getString("zip");
                              int eid =  obj.getInt("id");
                              //Actually Small One
                              String dp = obj.getString("img");
                              Person p = new Person(eid,name, phone, dp,profession,lat,lon,zip);
                              list.add(p);
                              Log.e(TAG, "Added Employee : " + name);
                          }
                          return list;
                      }
                      Log.e(TAG,"Error in Search");
                  } catch (JSONException e) {
                      Log.e(TAG,e.toString());
                      e.printStackTrace();
                  }
                  return null;
              }
          }

          UpdateList ui = new UpdateList();
          ui.execute(""+jobid);
      }





}
