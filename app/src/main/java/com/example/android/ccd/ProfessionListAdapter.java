package com.example.android.ccd;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import static com.example.android.ccd.Upload_Image.getBitmapFromString;

public class ProfessionListAdapter extends ArrayAdapter<Person> {

    private LayoutInflater inflater;
    private Context context;

    public ProfessionListAdapter(Context context){
        super(context,R.layout.list_item);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v==null) v= inflater.inflate(R.layout.list_item,null);
        ((TextView)v.findViewById(R.id.phone)).setText(getItem(position).getPhone());
        ((TextView)v.findViewById(R.id.name)).setText(getItem(position).getName());


        ((ImageView)v.findViewById(R.id.img)).setImageBitmap(getItem(position).getImg());
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Person person = getItem(position);
                FindEmployee.openEmployee(context,
                        person.getID(),
                        person.getName(),
                        person.getProfession(),
                        person.getPhone());
            }
        });
        return v;
    }


    }


class Person {
    private String name, phone,profession,zip;
    private int ID;
    //Can be small or big depending upon situation
    private Bitmap img;
    private float lat,lon;


    public Person(int eid, String name, String phone, String dp, String profession, float lat, float lon, String zip) {
        this.name = name;
        this.phone = phone;
        this.ID = eid;
        Bitmap tempdp = getBitmapFromString(dp);
        if(tempdp!=null)
            this.img = new CircleTransform().transform(tempdp);
        this.profession = profession;
        this.lat = lat;
        this.lon = lon;
        this.zip = zip;

    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public Bitmap getImg() {
        return img;
    }

    public int getID() {
        return ID;
    }

    public String getProfession() {
        return profession;
    }

    public float getLat() {
        return lat;
    }

    public float getLon() {
        return lon;
    }

    public String getZip() {
        return zip;
    }
}