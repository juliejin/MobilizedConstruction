package com.mobilizedconstruction.model;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.mobilizedconstruction.ReportCreationActivity;
import com.mobilizedconstruction.SigninActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.Vector;

/**
 * Created by Michelle_Yu on 10/24/17.
 */

public class Report implements Serializable
{
    public Vector<Image> reportImages;
    public ReportDO reportDO;
    public String filePath;

    public Report(ReportDO reportdo) {
        reportImages = new Vector<Image>();
        reportDO = reportdo;
    }

    public void insertImage(Image image){
        reportImages.add(image);
    }

    public void setLocation(Double latitude, Double longitude){
        reportDO.setLatitude(latitude);
        reportDO.setLongitude(longitude);
    }


    public void UploadReportToServer(final Context context) {

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {
            }

            @Override
            protected void onPostExecute(String string1) {
                for (int i = 0; i < reportImages.size(); i++) {
                    reportImages.elementAt(i).setReportID(reportDO.getReportID());
                    reportImages.elementAt(i).ImageUploadToServer(context);
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                try{
                    HttpParams httpParameters = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
                    String link = "http://192.168.0.8:2020/report_put.php?username="+ SigninActivity.userName
                            +"&reportID="+reportDO.getReportID() + "&severity="+reportDO.getSeverity()
                            +"&RoadDirection="+reportDO.getRoadDirection()+"&DateModified="
                            +reportDO.getDateCreated()+"&comment="+reportDO.getComment()
                            +"&RoadHazard="+reportDO.getRoadHazard()+"&Longitude="+reportDO.getLongitude()
                            +"&Latitude="+reportDO.getLatitude()+"&ImageCount="+reportDO.getImageCount();
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
                    while ((line = in.readLine()) != null){
                        sb.append(line);
                        break;
                    }
                    in.close();
                    return sb.toString();
                }catch(Exception e){
                    return new String("Exception: " + e.getMessage());
                }
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();
    }
}
