package com.mobilizedconstruction;

/**
 * Created by Ling Jin on 10/11/2017.
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.TextView;

public class SigninActivity extends AsyncTask{
    private TextView statusField;
    private Context context;
    private int byGetOrPost = 0;
    public static String userName;

    //flag 0 means get and 1 means post.(By default it is get.)
    public SigninActivity(Context context,TextView statusField,int flag) {
        this.context = context;
        this.statusField = statusField;
        byGetOrPost = flag;

    }

    protected void onPreExecute(){
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        HttpParams httpParameters = new BasicHttpParams();
        //HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
        if(byGetOrPost == 0){
            try{
                String username = (String)objects[0];
                userName = (String)objects[0];
                String password = (String)objects[1];
                String link = "http://192.168.0.8:8080/user_get.php?username="+username+"&password="+password;
                //link = URLEncoder.encode(link,"UTF-8");
                URL url = new URL(link);
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setParams(httpParameters);
                request.setURI(new URI(link));
                HttpResponse response = client.execute(request);
                BufferedReader in = new BufferedReader(new
                        InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line="";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    break;
                }

                in.close();
                return sb.toString();
            } catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        } else{
            try{
                String username = (String)objects[0];
                String password = (String)objects[1];
                userName = username;
                String link = "http://192.168.0.8:4000/user_put.php?username="+username+"&password="+password;
                //link = URLEncoder.encode(link,"UTF-8");
                URL url = new URL(link);
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setParams(httpParameters);
                request.setURI(new URI(link));
                HttpResponse response = client.execute(request);
                BufferedReader in = new BufferedReader(new
                        InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line="";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    break;
                }

                in.close();
                return sb.toString();
            } catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }
    }

    @Override
    protected void onPostExecute(Object result){
        if(byGetOrPost == 0) {
            if (result.toString().equals("  "))
                this.statusField.setText("No username and password combination exists");
            else {
                this.statusField.setText(result.toString());
                if(!result.toString().contains("Exception:")) {
                    Intent intent = new Intent(context, ReportCreationActivity.class);
                    intent.putExtra("username", userName);
                    context.startActivity(intent);
                }
            }
        }else{
            if (result.toString().contains("name already exists ")){
                this.statusField.setText("username already exists");
            }else{
                this.statusField.setText(result.toString());
                if(!result.toString().contains("Exception:")) {
                    Intent intent = new Intent(context, ReportCreationActivity.class);
                    intent.putExtra("username", userName);
                    context.startActivity(intent);
                }
            }
        }
    }
}
