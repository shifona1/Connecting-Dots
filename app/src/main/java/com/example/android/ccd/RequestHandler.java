package com.example.android.ccd;

/**
 * Created by shifona on 1/10/16.
 */
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Belal on 8/19/2015.
 */

public class RequestHandler {
    private static final String TAG = RequestHandler.class.getName();

    public String sendGetRequest(String uri) {
        try {
            Log.e(TAG,"Call to "+uri);
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String result;

            StringBuilder sb = new StringBuilder();

            while((result = bufferedReader.readLine())!=null){
                sb.append(result);
            }

            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public String sendPostRequest(String requestURL,
                                  HashMap<String, String> postDataParams) {

        URL url;
        String response = "";
        try {
            url = new URL(requestURL);
            Log.e(TAG, requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();
            Log.e(TAG,"RC : "+responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK)
            {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = null;
                StringBuffer sb = new StringBuffer();
                sb.append(br.readLine());
                while ((line = br.readLine()) != null) {
                    sb.append('\n');
                    sb.append(line);
                //    Log.e(TAG,"> "+line);
                }
                response = sb.toString();
            } else {
                response = "Error Registering";
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG,"Error "+e);
        }

        Log.e(TAG,"Response : "+response);
        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        if (params == null)
            params = new HashMap<>();

        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
