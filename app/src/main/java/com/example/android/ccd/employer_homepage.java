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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.android.ccd.GPSTracker.PREF_LOCATION_LAT;
import static com.example.android.ccd.Upload_Image.goingforimageupdate;

public class employer_homepage extends ActionBarActivity {
    private final String Log_Tag = employer_homepage.class.getSimpleName();
//    private ProfessionListAdapter adapt;
    private String UPLOAD_URL= Upload_Image.BASE_URL+"/search.php";
    public static final String PIC_URL = Upload_Image.PIC_URL;
    public static final String UPDATE_PIC_URL= Upload_Image.UPDATE_PIC_URL_EMPLOYER;
    private static final String TAG = Employee_HOmePage.class.getSimpleName();

    private String R_NAME,R_PHONE,R_IMAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(Log_Tag, "Forecast entry: ");
        setContentView(R.layout.fragment_employer);
        R_NAME = ((MyApplication)getApplication()).getUsername();
        R_PHONE = ((MyApplication)getApplication()).getPhoneNo();


        //  if (savedInstanceState == null) {
       //     getSupportFragmentManager().beginTransaction()
      //              .add(R.id.container, new SearchFragment())
      //              .commit();
      //  }


        Intent intent = getIntent();
        final String data[] = new String[]{"A","Hello","Hi","Gagan","Gassss"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        AutoCompleteTextView sr = (AutoCompleteTextView)findViewById(R.id.searchView);
        sr.setAdapter(adapter);
        sr.setThreshold(0);
        sr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(employer_homepage.this, ">> " +  data[position]  ,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(employer_homepage.this, "Not Seleceted"  ,Toast.LENGTH_SHORT).show();

            }
        });

        Button update_button=(Button)findViewById(R.id.update_profile_employer_button);
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(employer_homepage.this, Update_Profile_Employer.class);
                i.putExtra("isEmployer", true);
                startActivity(i);
                finish();
            }
        });


        TextView textView = (TextView) findViewById(R.id.employer_name);
        textView.setText(((MyApplication) getApplication()).getUsername());


        final ImageView img = (ImageView) findViewById(R.id.profile_image);
        String imei = ((MyApplication) getApplication()).getID();
        String url = PIC_URL + "?IMEI=" + imei;
        Log.e(TAG, "Attempt Load Img " + url + " on " + img);
        Picasso.with(this).load(url).error(R.drawable.pic).placeholder(android.R.drawable.progress_horizontal).transform(new CircleTransform()).into(img);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.edit().putString("img", "").commit();

        ImageButton upload_dp = (ImageButton) findViewById(R.id.Update_dp);
        upload_dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(employer_homepage.this, Upload_Image.class);
                startActivityForResult(i, 101);

            }
        });

//        adapt = new ProfessionListAdapter(this);
//        final SearchView searchView = (SearchView) findViewById(R.id.searchView);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                String search = query;
//
//                Log.v("**************<<<<", search);
//                updateList(query, adapt);
//                // Intent i = new Intent(employer_homepage.this, SearchFragment.class);
//                //startActivity(i);
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                String search = newText;
//
////                    TextView quantityTextView = (TextView) P.rootView.findViewById(
////                            R.id.search);
////                    quantityTextView.setText(search);
//
//                return true;
//            }
//        });

//        Log.v("**************<<<<", ":::::::::::::::::::::::;;");
//        ((ListView)findViewById(R.id.listView)).setAdapter(adapt);


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
                list.add(new Employee((25.5282709+","+84.8541815),"Shifona","9006742501",1,".1.2.4."));
                list.add(new Employee((25.5394474+","+84.8586603),"Abhishek","9005236215",2,".3."));
                list.add(new Employee((25.5583262+","+84.8683597),"Zeeshan","7855221456",3,".3.4."));




                Intent intent = new Intent(getApplicationContext(),FindEmployee.class);
                intent.putExtra("data",Employee.getEncoded(list));
                startActivity(intent);
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        if(goingforimageupdate) {
            goingforimageupdate = false;
            final ImageView img = (ImageView) findViewById(R.id.profile_image);
            updateImage(((MyApplication) getApplication()).getID(), img);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */


    private void updateImage(final String imei, final ImageView img)
    {
        R_IMAGE = PreferenceManager.getDefaultSharedPreferences(employer_homepage.this).getString("img","");
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
                Picasso.with(employer_homepage.this).load(url).skipMemoryCache().error(R.drawable.pic).placeholder(android.R.drawable.progress_horizontal).transform(new CircleTransform()).into(img);

            }
        }.execute();


    }







      private void updateList(String str, final ProfessionListAdapter adapt)
      {
          class UpdateList extends AsyncTask<String,Void,String> {

              ProgressDialog loading;
              RequestHandler rh = new RequestHandler();

              @Override
              protected void onPreExecute() {
                  super.onPreExecute();
                  loading = ProgressDialog.show(employer_homepage.this, "Searching","Please wait...",true,true);
              }

              @Override
              protected void onPostExecute(String s) {
                  super.onPostExecute(s);
                  loading.dismiss();
                  try {
                      JSONArray arr = new JSONArray(s);
                      adapt.clear();
                      for (int i=0;i<arr.length();i++) {
                          JSONObject  obj = arr.getJSONObject(i);
                          String name = obj.getString("name");
                          String phone = obj.getString("phone");
                           String prof = obj.getString("prof");
                              String dp = obj.getString("img");
                          Person p = new Person(name,phone,prof,dp);
                          adapt.add(p);
                      }
                  } catch (JSONException e) {
                      e.printStackTrace();
                  }
                  Toast.makeText(employer_homepage.this, s, Toast.LENGTH_LONG).show();
              }

              @Override
              protected String doInBackground(String... params) {

                  HashMap<String,String> data = new HashMap<>();

                  data.put("job", params[0]);

                  String result = rh.sendPostRequest(UPLOAD_URL,data);

                  return result;
              }
          }

          UpdateList ui = new UpdateList();
          ui.execute(str);
      }





}
