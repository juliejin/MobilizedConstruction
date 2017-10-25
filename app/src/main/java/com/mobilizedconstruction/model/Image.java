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
import com.amazonaws.mobile.content.ContentItem;
import com.amazonaws.mobile.content.ContentProgressListener;
import com.amazonaws.mobile.content.UserFileManager;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.mobilizedconstruction.Application;
import com.mobilizedconstruction.ImageUploadActivity;
import com.mobilizedconstruction.R;
import com.mobilizedconstruction.ReportCreationActivity;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
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
    private UserFileManager userFileManager;
    public static final String S3_PREFIX_UPLOADS = "uploads/";
    private final CountDownLatch userFileManagerCreatingLatch = new CountDownLatch(1);
    DynamoDBMapper mapper;

    /**
     * Permission Request Code (Must be < 256).
     */
    private static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 93;

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

    private void uploadToS3(Context context) {
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

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    userFileManagerCreatingLatch.await();
                } catch (final InterruptedException ex) {
                    // This thread should never be interrupted.
                    throw new RuntimeException(ex);
                }
                userFileManager.uploadContent(imageFile, FilePath, new ContentProgressListener() {
                    @Override
                    public void onSuccess(final ContentItem contentItem) {
                        URL _finalUrl = userFileManager.generatePresignedUrl(reportImage.getReportID().toString()+reportImage.getIndex());
                        SetImageUrl(_finalUrl.toString());

                    }

                    @Override
                    public void onProgressUpdate(final String fileName, final boolean isWaiting,
                                                 final long bytesCurrent, final long bytesTotal) {

                    }

                    @Override
                    public void onError(final String fileName, final Exception ex) {

                    }
                });
            }
        }).start();
    }

    private void uploadToDB(){
        AmazonDynamoDBClient dynamoDBClient =
                new AmazonDynamoDBClient(IdentityManager.getDefaultIdentityManager()
                        .getCredentialsProvider(), new ClientConfiguration());
        mapper = DynamoDBMapper.builder()
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




}

