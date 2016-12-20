package com.connecting_dots.android.ccd;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import static com.connecting_dots.android.ccd.GPSTracker.PREF_LOCATION_LAT;
import static com.connecting_dots.android.ccd.GPSTracker.PREF_LOCATION_LONG;

public class FindEmployee extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = FindEmployee.class.getName();
    private GoogleMap mMap;
    private ArrayList<Employee> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_employee);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        list = Employee.getDecoded(data);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Log.e(TAG,"Employee List");

        String[] pos;
        float lat;
        float lon;
        ArrayList<LatLng> markers = new ArrayList<>();

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat,lon)));
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        lat = PreferenceManager.getDefaultSharedPreferences(FindEmployee.this).getFloat(PREF_LOCATION_LAT,0);;
        lon = PreferenceManager.getDefaultSharedPreferences(FindEmployee.this).getFloat(PREF_LOCATION_LONG,0);
        builder.include(new LatLng(lat,lon));


        for (int i=0;i<list.size();i++) {
            Employee emp = list.get(i);
            pos = emp.getPos().split(",");
            lat = Float.parseFloat(pos[0]);
            lon = Float.parseFloat(pos[1]);

            LatLng location = new LatLng(lat,lon);
            String name = emp.getName();
            String contact = emp.getContact();
            int id = emp.getID();
            MarkerOptions marker = new MarkerOptions().position(location).title(name).snippet(contact+"\n"+"ID :"+id);
            markers.add(marker.getPosition());
            mMap.addMarker(marker);

        }


        for (LatLng marker : markers) {
            builder.include(marker);
        }
        LatLngBounds bounds = builder.build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,0));
        mMap.setMyLocationEnabled(true);
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                //Toast.makeText(FindEmployee.this,"Contact : "+marker.getSnippet(),Toast.LENGTH_SHORT).show();
                int id = Integer.parseInt(marker.getSnippet().split(":")[1]);

                for (int i=0;i<list.size();i++) {
                    if(list.get(i).getID() == id) {
                        openEmployee(FindEmployee.this,
                                id,
                                list.get(i).getName(),
                                list.get(i).getJobs(),
                                list.get(i).getContact()
                                );


                    }
                }

            }
        });
    }

    public static void openEmployee(Context context, int empid,String name,String jobs,String contact) {
        Intent intent = new Intent(context,Employee_HOmePage.class);
        int id = empid;
        intent.putExtra("person_id",id);
        intent.putExtra("JUSTSHOW",true);

        intent.putExtra("name",name);
        intent.putExtra("jobs",jobs);
        intent.putExtra("contact",contact);
        context.startActivity(intent);
    }


}
