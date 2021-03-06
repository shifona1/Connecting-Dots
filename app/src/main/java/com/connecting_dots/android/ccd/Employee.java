package com.connecting_dots.android.ccd;

import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by scopeinfinity on 30/11/16.
 */

public class Employee implements Serializable{
    private static final long serialVersionUID = 8541181351515415152L;
    private static final String TAG = Employee.class.getName();
    private String pos;
    private String name;
    private int ID;
    private String jobs;
    private String contact;


    Employee(String pos,String name,String contact,int ID,String jobs) {
        this.pos = pos;
        this.name = name;
        this.ID = ID;
        this.jobs = jobs;
        this.contact = contact;
    }

    public String getPos() {
        return pos;
    }

    public String getContact() {
        return contact;
    }

    public String getName() {
        return name;
    }


    public static String getEncoded(ArrayList<Employee> list) {
        String out = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bos);
            os.writeObject(list);
            os.flush();
            out =  Base64.encodeToString(bos.toByteArray(), Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"ERROR "+e);
        }
        Log.e(TAG,"Encoding List \n"+out);
        return out;
    }

    public static ArrayList<Employee> getDecoded(String input) {
        Log.e(TAG,"Decoding  List \n"+input);
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(Base64.decode(input,Base64.DEFAULT));
            ObjectInputStream ois = new ObjectInputStream(bis);
            return (ArrayList<Employee>) ois.readObject();

        } catch (IOException e) {
            e.printStackTrace();

            Log.e(TAG,"ERROR "+e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

            Log.e(TAG,"ERROR "+e);
        }
        return null;
    }

    public int getID() {
        return ID;
    }

    public String getJobs() {
        return jobs;
    }
}
