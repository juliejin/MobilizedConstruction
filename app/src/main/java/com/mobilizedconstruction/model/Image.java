package com.mobilizedconstruction.model;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.mobilizedconstruction.demo.UserFilesBrowserFragment;
import com.mobilizedconstruction.demo.UserFilesDemoFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static android.content.Context.MODE_PRIVATE;
import static android.provider.Settings.Global.getString;
import static android.provider.Telephony.Mms.Part.FILENAME;

/**
 * Created by apple on 24/10/2017.
 */

public class Image implements Serializable{
    private String FilePath;
    private Integer index;
    private File imageFile;
    private ReportImageDO reportImage;
    transient private Bitmap imageBitmap;
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

    public void SetImageFile(File file){this.imageFile = file;}

    public void SetFilePath(String path){this.FilePath = path;}

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
                String key = S3_PREFIX_UPLOADS+imageFile.getName();
                SetImageUrl(key);
                mapper.save(reportImage);
                try {
                    AmazonS3 s3Client = new AmazonS3Client(IdentityManager.getDefaultIdentityManager()
                            .getCredentialsProvider());
                    PutObjectRequest obj = new PutObjectRequest("mobilizedconstructio-userfiles-mobilehub-516637937", key, imageFile);
                    s3Client.putObject(obj);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
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

    public void fetchFromDB(final Integer reportID,final int index) {
        new Thread(new Runnable() {
            @Override
            public void run() {
<<<<<<< HEAD
=======
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
>>>>>>> 794a7151f10becea5a112321204224ab3a9f4850
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
                            String url = item.get("ImageURL").getS();
                            AmazonS3 s3Client = new AmazonS3Client(IdentityManager.getDefaultIdentityManager()
                                    .getCredentialsProvider());
                            S3Object object = s3Client.getObject(
                                    new GetObjectRequest("mobilizedconstructio-userfiles-mobilehub-516637937", url));
                            InputStream objectData = object.getObjectContent();
                            try {
                                reportImage.setImageURL(url);
                                imageBitmap = BitmapFactory.decodeStream(objectData);
                            }catch (Exception e){
                                System.out.println(e.getMessage());
                            }
                        }
                        if(item.get("Longitude")!=null){
                            reportImage.setLongitude(Double.parseDouble(item.get("Longitude").getN()));
                        }
                        if(item.get("Latitude")!=null){
                            reportImage.setLongitude(Double.parseDouble(item.get("Latitude").getN()));
                        }
                    }

<<<<<<< HEAD
=======

>>>>>>> 794a7151f10becea5a112321204224ab3a9f4850
            }
        }).start();

    }

    public Bitmap getImageBitmap(){
        return imageBitmap;
    }

}


