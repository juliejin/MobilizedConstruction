package com.mobilizedconstruction.model;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobile.content.ContentDownloadPolicy;
import com.amazonaws.mobile.content.ContentItem;
import com.amazonaws.mobile.content.ContentProgressListener;
import com.amazonaws.mobile.content.UserFileManager;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.mobilizedconstruction.Application;
import com.mobilizedconstruction.ImageUploadActivity;
import com.mobilizedconstruction.R;
import com.mobilizedconstruction.ReportCreationActivity;
import com.mobilizedconstruction.SigninActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.net.ssl.HttpsURLConnection;

import static android.content.Context.MODE_PRIVATE;
import static android.provider.Settings.Global.getString;
import static android.provider.Telephony.Mms.Part.FILENAME;

/**
 * Created by Ling Jin on 24/10/2017.
 */

public class Image implements Serializable{
    private String FilePath;
    private Integer index;
    private File imageFile;
    private ReportImageDO reportImage;
    transient private Bitmap imageBitmap;
    transient private UserFileManager userFileManager;
    public static final String S3_PREFIX_UPLOADS = "uploads/";
    ProgressDialog progressDialog ;
    boolean check = true;
    //transient private final CountDownLatch userFileManagerCreatingLatch = new CountDownLatch(1);

    public Image(ReportImageDO image){
        this.reportImage = image;
        this.index = image.getIndex();
        this.FilePath = image.getImageURL();
    }

    public void setReportID(Integer id){
        reportImage.setReportID(id);
    }



    /**
     * Permission Request Code (Must be < 256).
     */
    private static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 93;

    public Image(){}

    public void SetReportImageDO(ReportImageDO image){
        this.reportImage = image;
    }

    public Image(String filePath, File imageFile, Integer index, Integer reportID){
        this.index = index;
        this.FilePath = filePath;
        this.imageFile = imageFile;
        this.reportImage = new ReportImageDO(reportID, index);
    }
    public void SetImageUrl(String URL){
        this.reportImage.setImageURL(URL);
    }

    public void SetImageFile(File file){this.imageFile = file;}

    public void SetFilePath(String path){this.FilePath = path;}



    public String getFilePath(){
        return FilePath;
    }

    public ReportImageDO GetReportImage(){
        return reportImage;
    }

    public Bitmap fetchFromDB(final Integer reportID,final int index) {
        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {
            Bitmap bmp;
            @Override
            protected void onPreExecute() {
            }

            @Override
            protected void onPostExecute(String string) {
                byte[] imageByteArray = Base64.decode(string, Base64.DEFAULT);
                bmp = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            }

            @Override
            protected String doInBackground(Void... params) {
                try {
                    HttpParams httpParameters = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(httpParameters, 15000);
                    String link = "http://192.168.0.8:5050/image_get.php?ReportID=" + reportImage.getReportID();
                    HttpClient client = new DefaultHttpClient();
                    HttpGet request = new HttpGet();
                    request.setParams(httpParameters);
                    request.setURI(new URI(link));
                    HttpResponse response = client.execute(request);

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(response.getEntity().getContent()));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";
                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                        JSONObject json_data = new JSONObject(line.trim());
                        Integer report_ID = json_data.getInt("Report ID");
                        Integer index = json_data.getInt("Image Index");
                        String url = json_data.getString("ImageURL");
                        reportImage.setImageURL(url);
                        reportImage.setIndex(index);
                        reportImage.setReportID(report_ID);
                        break;
                    }
                    return line;
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();
        return AsyncTaskUploadClassOBJ.bmp;
    }

    public Bitmap getImageBitmap(){
        while (imageBitmap == null)
        {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return imageBitmap;
    }

    public void ImageUploadToServer(final Context context){

        ByteArrayOutputStream byteArrayOutputStreamObject ;

        byteArrayOutputStreamObject = new ByteArrayOutputStream();

        imageBitmap = BitmapFactory
                .decodeFile(FilePath);

        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);

        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {
                progressDialog = ProgressDialog.show(context,"Image is Uploading","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {
                // Dismiss the progress dialog after done uploading.
                progressDialog.dismiss();

                // Printing uploading success message coming from server on android app.
                Toast.makeText(context,string1,Toast.LENGTH_LONG).show();
                final Intent intent = new Intent(context, ReportCreationActivity.class);
                context.startActivity(intent);

            }

            @Override
            protected String doInBackground(Void... params) {
                try{
                    HttpParams httpParameters = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(httpParameters, 15000);
                   /* String link = "http://192.168.0.8:2017/image_upload.php?image_name="+imageFile.getName()
                            +"&ReportID="+reportImage.getReportID()
                            +"&image_index="+reportImage.getIndex() +"&image_path="+ConvertImage ;
                    link= link.trim();
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
                    return sb.toString();*/
                    ImageProcessClass imageProcessClass = new ImageProcessClass();
                    HashMap<String,Object> HashMapParams = new HashMap<String,Object>();
                    HashMapParams.put("image_name", imageFile.getName());
                    HashMapParams.put("image_data", ConvertImage);
                    HashMapParams.put("image_index", reportImage.getIndex());
                    HashMapParams.put("ReportID", reportImage.getReportID());
                    String UploadPath = "http://192.168.0.8:2017/image_upload.php";
                    String FinalData = imageProcessClass.ImageHttpRequest(UploadPath, HashMapParams);

                    return FinalData;
                }catch(Exception e){
                    return new String("Exception: " + e.getMessage());
                }
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();

        AsyncTaskUploadClassOBJ.execute();
    }

    public class ImageProcessClass{

        public String ImageHttpRequest(String requestURL,HashMap<String,Object> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {

                URL url;
                HttpURLConnection httpURLConnectionObject ;
                OutputStream OutPutStream;
                BufferedWriter bufferedWriterObject ;
                BufferedReader bufferedReaderObject ;
                int RC ;

                url = new URL(requestURL);

                httpURLConnectionObject = (HttpURLConnection) url.openConnection();

                httpURLConnectionObject.setReadTimeout(19000);

                httpURLConnectionObject.setConnectTimeout(19000);

                httpURLConnectionObject.setRequestMethod("POST");

                httpURLConnectionObject.setDoInput(true);

                httpURLConnectionObject.setDoOutput(true);

                OutPutStream = httpURLConnectionObject.getOutputStream();

                bufferedWriterObject = new BufferedWriter(

                        new OutputStreamWriter(OutPutStream, "UTF-8"));

                bufferedWriterObject.write(bufferedWriterDataFN(PData));

                bufferedWriterObject.flush();

                bufferedWriterObject.close();

                OutPutStream.close();

                RC = httpURLConnectionObject.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReaderObject.readLine()) != null){

                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, Object> HashMapParams) throws UnsupportedEncodingException {

            StringBuilder stringBuilderObject;

            stringBuilderObject = new StringBuilder();

            for (Map.Entry<String, Object> KEY : HashMapParams.entrySet()) {

                if (check)

                    check = false;
                else
                    stringBuilderObject.append("&");

                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilderObject.append("=");

                stringBuilderObject.append(URLEncoder.encode(KEY.getValue().toString(), "UTF-8"));
            }

            return stringBuilderObject.toString();
        }

    }

}