package com.example.android.ccd;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.android.ccd.Upload_Image.getBitmapFromString;

public class ProfessionListAdapter extends ArrayAdapter<Person> {

    private LayoutInflater inflater;

    public ProfessionListAdapter(Context context){
        super(context,R.layout.list_item);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v==null) v= inflater.inflate(R.layout.list_item,null);
        ((TextView)v.findViewById(R.id.phone)).setText(getItem(position).getPhone());
        ((TextView)v.findViewById(R.id.name)).setText(getItem(position).getName());
        ((ImageView)v.findViewById(R.id.img)).setImageBitmap(getItem(position).getImg());
        return v;
    }


    }


class Person {
    private String name, phone;
    private Bitmap img;

    public Person(String name, String phone,String img) {
        this.name = name;
        this.phone = phone;
        this.img = getBitmapFromString(img);
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
}