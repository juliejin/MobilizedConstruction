package com.mobilizedconstruction.model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;

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
import com.mobilizedconstruction.Application;
import com.mobilizedconstruction.ImageUploadActivity;
import com.mobilizedconstruction.R;
import com.mobilizedconstruction.ReportCreationActivity;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static android.provider.Settings.Global.getString;

/**
 * Created by apple on 24/10/2017.
 */

public class Image implements Serializable{
    private String FilePath;
    private Integer index;
    private File imageFile;
    private ReportImageDO reportImage;
    transient private UserFileManager userFileManager;
    public static final String S3_PREFIX_UPLOADS = "uploads/";
    //transient private final CountDownLatch userFileManagerCreatingLatch = new CountDownLatch(1);

    public Image(ReportImageDO image){
        this.reportImage = image;
        this.index = image.getIndex();
        this.FilePath = image.getImageURL();

    }


    /**
     * Permission Request Code (Must be < 256).
     */
    private static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 93;

    public Image(){}

    public void SetReportImageDO(ReportImageDO image){
        this.reportImage = image;
    }

    public Image(String filePath, File imageFile, Integer index, Double lng, Double lat, Integer reportID){
        this.index = index;
        this.FilePath = filePath;
        this.imageFile = imageFile;
        this.reportImage = new ReportImageDO(reportID, index, lng, lat);

    }
    public void SetImageUrl(String URL){
        this.reportImage.setImageURL(URL);
    }

    public void SetRoadHazard(Integer roadHazard){
        this.reportImage.setRoadHazard(roadHazard);
    }

    public String getFilePath(){
        return FilePath;
    }

    public ReportImageDO GetReportImage(){
        return reportImage;
    }

    //
    public void uploadToS3(Context context) {
        final CountDownLatch userFileManagerCreatingLatch = new CountDownLatch(1);
        new UserFileManager.Builder()
                .withContext(context)
                .withIdentityManager(IdentityManager.getDefaultIdentityManager())
                .withAWSConfiguration(new AWSConfiguration(context))
                .withS3ObjectDirPrefix(S3_PREFIX_UPLOADS)
                .withLocalBasePath(context.getFilesDir().getAbsolutePath())
                .build(new UserFileManager.BuilderResultHandler() {
                    @Override
                    public void onComplete(UserFileManager userFileManager) {
                        Image.this.userFileManager = userFileManager;
                        userFileManagerCreatingLatch.countDown();
                    }
                });
        AmazonDynamoDBClient dynamoDBClient =
                new AmazonDynamoDBClient(IdentityManager.getDefaultIdentityManager()
                        .getCredentialsProvider(), new ClientConfiguration());
        final DynamoDBMapper mapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(Application.awsConfiguration)
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    userFileManagerCreatingLatch.await();
                } catch (final InterruptedException ex) {
                    // This thread should never be interrupted.
                    throw new RuntimeException(ex);
                }
                SetImageUrl(FilePath);
                mapper.save(reportImage);
                /*userFileManager.uploadContent(imageFile, FilePath, new ContentProgressListener() {
                    @Override
                    public void onSuccess(final ContentItem contentItem) {
                        //URL _finalUrl = userFileManager.generatePresignedUrl(reportImage.getReportID().toString()+reportImage.getIndex());
                        SetImageUrl(FilePath);
                        mapper.save(reportImage);
                    }

                    @Override
                    public void onProgressUpdate(final String fileName, final boolean isWaiting,
                                                 final long bytesCurrent, final long bytesTotal) {

                    }

                    @Override
                    public void onError(final String fileName, final Exception ex) {

                    }
                });*/
            }
        }).start();
    }

    private void uploadToDB(){
        AmazonDynamoDBClient dynamoDBClient =
                new AmazonDynamoDBClient(IdentityManager.getDefaultIdentityManager()
                        .getCredentialsProvider(), new ClientConfiguration());
        final DynamoDBMapper mapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(Application.awsConfiguration)
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mapper.save(reportImage);
                } catch (final AmazonClientException ex) {

                }
            }
        }).start();
    }

    public ReportImageDO fetchFromDB(final Integer reportID,final int index, Context context) {
        final CountDownLatch userFileManagerCreatingLatch = new CountDownLatch(1);
        new UserFileManager.Builder()
                .withContext(context)
                .withIdentityManager(IdentityManager.getDefaultIdentityManager())
                .withAWSConfiguration(new AWSConfiguration(context))
                .withS3ObjectDirPrefix(S3_PREFIX_UPLOADS)
                .withLocalBasePath(context.getFilesDir().getAbsolutePath())
                .build(new UserFileManager.BuilderResultHandler() {
                    @Override
                    public void onComplete(UserFileManager userFileManager) {
                        Image.this.userFileManager = userFileManager;
                        userFileManagerCreatingLatch.countDown();
                    }
                });
        final ReportImageDO image = new ReportImageDO(reportID, index, 0.0,0.0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                    try {
                        userFileManagerCreatingLatch.await();
                    } catch (final InterruptedException ex) {
                        // This thread should never be interrupted.
                        throw new RuntimeException(ex);
                    }
                    AmazonDynamoDBClient client =
                            new AmazonDynamoDBClient(IdentityManager.getDefaultIdentityManager()
                                    .getCredentialsProvider(), new ClientConfiguration());
                    Map<String, String> attributeNames = new HashMap<String, String >();
                    attributeNames.put("#reportID", "Report ID");
                    Map<String, AttributeValue> expressionAttributeValues =
                            new HashMap<String, AttributeValue>();
                    String id = reportID.toString();
                    expressionAttributeValues.put(":reportID", new AttributeValue().withN(reportID.toString()));
                    ScanRequest scanRequest = new ScanRequest()
                            .withTableName("mobilizedconstructio-mobilehub-516637937-ReportImage");
                    ScanResult result = client.scan(scanRequest);
                    for (Map<String, AttributeValue> item : result.getItems()) {
                        if (item.get("ImageURL") != null) {
                            //userFileManager.getContent(url, 0, ContentDownloadPolicy.DOWNLOAD_IF_NOT_CACHED, true, null);
                            String url = item.get("ImageURL").getS();
                            userFileManager.getContent(url, 0, ContentDownloadPolicy.DOWNLOAD_IF_NOT_CACHED, true, null);
                            image.setImageURL(url);
                            imageFile = new File(url);
                        }
                        if(item.get("Longitude")!=null){
                            image.setLongitude(Double.parseDouble(item.get("longitude").getS()));
                        }
                        if(item.get("Latitude")!=null){
                            image.setLongitude(Double.parseDouble(item.get("latitude").getS()));
                        }
                    }


            }
        }).start();
        return image;
    }
}


