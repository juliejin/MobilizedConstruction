package com.mobilizedconstruction.api;

import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;
import android.util.Pair;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Ling Jin on 09/11/2017.
 */



public class JsonParser {

    public void parseJSON() {
        //http post
        // new Thread(new Runnable() {
        //  @Override
        //   public void run() {

        AsyncTask<String, String, String> _Task = new AsyncTask<String, String, String>() {

            @Override
            protected void onPreExecute() {


            }

            @Override
            protected String doInBackground(String... arg0) {

                        String result = "";
                        String x = "";
                        InputStream is = null;
                        try {

                            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                            HttpClient httpclient = new DefaultHttpClient();
                            HttpPost httppost = new HttpPost("http://10.0.2.2:8080/user.php");
                            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                            HttpResponse response = httpclient.execute(httppost);
                            HttpEntity entity = response.getEntity();
                            is = entity.getContent();
                        } catch (Exception e) {
                            Log.e("log_tag", "Error in http connection " + e.toString());
                        }
                        //convert response to string
                        try {

                            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                            StringBuilder sb = new StringBuilder();
                            String line = null;
                            while ((line = reader.readLine()) != null) {
                                sb.append(line + "\n");
                            }
                            is.close();

                            result = sb.toString();

                        } catch (Exception e) {
                            Log.e("log_tag", "Error converting result " + e.toString());
                        }


                return "";
            }

        };
        _Task.execute((String[]) null);


    }

}