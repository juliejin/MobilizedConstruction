package com.mobilizedconstruction;

import android.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.net.Uri;
import android.database.Cursor;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Environment;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobile.content.ContentItem;
import com.amazonaws.mobile.content.ContentProgressListener;
import com.amazonaws.mobile.content.UserFileManager;
import com.amazonaws.mobile.util.ImageSelectorUtils;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.mobilizedconstruction.R;
import com.mobilizedconstruction.model.Report;
import com.mobilizedconstruction.model.ReportDO;
import com.mobilizedconstruction.model.Image;
import com.mobilizedconstruction.model.ReportImageDO;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.Vector;
import java.util.Date;
import java.text.SimpleDateFormat;

public class ImageUploadActivity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMG = 1;
    static final int REQUEST_IMAGE_CAPTURE = 2;
    Report report;
    private String imgDecodableString;
    File imageFile;
    private ImageButton addImageButton;
    private ImageButton cameraButton;
    private int display_image = -1;
    private Vector<ImageButton> imageButtonVector;
    private HashSet<String> decodableSet;
    private HashMap<ImageButton, Bitmap> mapImagetoBitmap;
    Context context = this;
    private static final String LOG_TAG = ImageUploadActivity.class.getSimpleName();
    private UserFileManager userFileManager;
    public static final String S3_PREFIX_UPLOADS = "uploads/";
    private final CountDownLatch userFileManagerCreatingLatch = new CountDownLatch(1);

    /**
     * Permission Request Code (Must be < 256).
     */
    private static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 93;

    /**
     * Upload Request Code for uploads folder action
     **/
    private static final int UPLOAD_REQUEST_CODE = 112;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);
        imageButtonVector = new Vector<ImageButton>();
        decodableSet = new HashSet<String>();
        mapImagetoBitmap = new HashMap<ImageButton, Bitmap>();
        Intent intent = getIntent();
        report = (Report)intent.getSerializableExtra("new_report");
        final Button submitButton = (Button)findViewById(R.id.submitImageButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageButtonVector.size() > 0)
                {
                    navigateToNextPage();
                }
            }
        });
        addImageButton = (ImageButton)findViewById(R.id.addImageButton);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImage();
            }
        });
        cameraButton = (ImageButton)findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
    }

    protected void getImage(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    private void insertImage(String filePath, File imageFile, int index, Integer reportID){
        Image image = new Image(filePath, imageFile, index, reportID);
        report.insertImage(image);

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final ImageView imgView = (ImageView) findViewById(R.id.imagePreview);
        final LinearLayout imgLL = (LinearLayout) findViewById(R.id.imageLL);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                if (!decodableSet.contains(imgDecodableString))
                {
                    imageFile = new File(imgDecodableString);
                    cursor.close();
                    Image image = new Image(imgDecodableString, imageFile, imageButtonVector.size(), report.reportDO.getReportID());
                    report.insertImage(image);
                    ImageButton imageButton = new ImageButton(context);
                    Bitmap myBitmap = BitmapFactory
                            .decodeFile(imgDecodableString);
                    myBitmap = Bitmap.createScaledBitmap(myBitmap, 240, 240, true);
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    myBitmap = Bitmap.createBitmap(myBitmap , 0, 0, myBitmap .getWidth(), myBitmap .getHeight(), matrix, true);
                    imageButton.setImageBitmap(myBitmap);
                    imageButton.setLayoutParams(new TableRow.LayoutParams(240, 240));
                    mapImagetoBitmap.put(imageButton, myBitmap);
                    imgLL.addView(imageButton);
                    imageButtonVector.add(imageButton);
                    decodableSet.add(imgDecodableString);
                    // Set the Image in ImageView after decoding the String
                    imgView.setImageBitmap(myBitmap);
                }
                else
                {
                    Toast.makeText(this, "You have already added this file.",
                            Toast.LENGTH_LONG).show();
                }

            }
            else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap myBitmap = (Bitmap) extras.get("data");
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                imageFile = new File(imgDecodableString);
                Image image = new Image(imgDecodableString, imageFile, imageButtonVector.size(), report.reportDO.getReportID());
                report.insertImage(image);

                imgView.setImageBitmap(myBitmap);
                ImageButton imageButton = new ImageButton(context);
                myBitmap = Bitmap.createScaledBitmap(myBitmap, 240, 240, true);
                imageButton.setImageBitmap(myBitmap);
                imageButton.setLayoutParams(new TableRow.LayoutParams(240, 240));
                mapImagetoBitmap.put(imageButton, myBitmap);
                imgLL.addView(imageButton);
                imageButtonVector.add(imageButton);
                decodableSet.add(imgDecodableString);
                // Set the Image in ImageView after decoding the String
                imgView.setImageBitmap(myBitmap);
                //Image image = new Image(imgDecodableString, imageFile, imageButtonVector.size(), 0.0, 0.0, report.reportDO.getReportID());
                //report.insertImage(image);
            }
            else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            //Toast.makeText(this, "Something went wrong.", Toast.LENGTH_LONG)
              //      .show();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG)
                  .show();
        }
        for (int i = 0; i < imageButtonVector.size(); i++)
        {
            imageButtonVector.elementAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageButton imageButton = (ImageButton) view;
                    imgView.setImageBitmap(mapImagetoBitmap.get(imageButton));
                }
            });
        }
    }


    protected void navigateToNextPage(){
        //uploadImageToAWS();
        report.reportDO.setImageCount(imageButtonVector.size());
        Intent intent = new Intent(this, RoadFeaturesActivity.class);
        intent.putExtra("new_report", report);
        startActivity(intent);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void uploadImageToAWS() {
        final Context context = this.getApplicationContext();
        new UserFileManager.Builder()
                .withContext(context)
                .withIdentityManager(IdentityManager.getDefaultIdentityManager())
                .withAWSConfiguration(new AWSConfiguration(context))
                .withS3ObjectDirPrefix(S3_PREFIX_UPLOADS)
                .withLocalBasePath(context.getFilesDir().getAbsolutePath())
                .build(new UserFileManager.BuilderResultHandler() {
                    @Override
                    public void onComplete(UserFileManager userFileManager) {
                        ImageUploadActivity.this.userFileManager = userFileManager;
                        userFileManagerCreatingLatch.countDown();
                    }
                });


        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    userFileManagerCreatingLatch.await();
                } catch (final InterruptedException ex) {
                    // This thread should never be interrupted.
                    throw new RuntimeException(ex);
                }
                userFileManager.uploadContent(imageFile, imgDecodableString, new ContentProgressListener() {
                    @Override
                    public void onSuccess(final ContentItem contentItem) {

                        showUploadOk(R.string.user_files_demo_ok_message_upload_file,
                                imageFile.getName());
                        Intent intent = new Intent(context, AddCommentActivity.class);
                        intent.putExtra("new_report", report);
                        startActivity(intent);
                    }

                    @Override
                    public void onProgressUpdate(final String fileName, final boolean isWaiting,
                                                 final long bytesCurrent, final long bytesTotal) {

                    }

                    @Override
                    public void onError(final String fileName, final Exception ex) {

                        showError(R.string.user_files_browser_error_message_upload_file,
                                ex.getMessage());
                    }
                });
            }
        }).start();

    }


    private void showError(final int resId, Object... args) {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(getString(resId, (Object[]) args))
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    private void showUploadOk(final int resId, Object... args) {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_info)
                .setMessage(getString(resId, (Object[]) args))
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
        /*AsyncTask<String, String, String> _Task = new AsyncTask<String, String, String>() {

            @Override
            protected void onPreExecute() {


            }

            @Override
            protected String doInBackground(String... arg0)
            {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    AmazonS3Client s3Client1 = new AmazonS3Client(IdentityManager.getDefaultIdentityManager()
                            .getCredentialsProvider(), new ClientConfiguration());
                    AmazonS3 s3 = new AmazonS3Client(IdentityManager.getDefaultIdentityManager()
                            .getCredentialsProvider());
                    TransferUtility transferUtility = new TransferUtility(s3,context);
                    String existingBucketName = "mobilizedconstructio-userfiles-mobilehub-516637937";
                    String keyName = "test_image";
                    String filePath = imgDecodableString;
                    TransferObserver observer = transferUtility.upload(
                            existingBucketName,
                            keyName,
                            imageFile
                    );

                    observer.setTransferListener(new TransferListener(){

                        @Override
                        public void onStateChanged(int id, TransferState state) {
                            // do something
                        }

                        @Override
                        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                            int percentage = (int) (bytesCurrent/bytesTotal * 100);
                            //Display percentage transfered to user
                        }

                        @Override
                        public void onError(int id, Exception ex) {
                            // do something
                            Log.e(LOG_TAG, "Failed uploading image : " + ex.getMessage(), ex);
                        }

                    });


                    String _finalUrl = "https://" + existingBucketName + ".s3.amazonaws.com/" + keyName + ".png";
                    Intent intent = new Intent(context, AddCommentActivity.class);
                    intent.putExtra("new_report", report);
                    startActivity(intent);
                } catch (Exception e) {
                    // writing error to Log
                    e.printStackTrace();
                }
                return null;

            }
            @Override
            protected void onProgressUpdate(String... values) {
                // TODO Auto-generated method stub
                super.onProgressUpdate(values);
                System.out.println("Progress : "  + values);
            }
            @Override
            protected void onPostExecute(String result)
            {

            }
        };
        _Task.execute((String[]) null);


            }
        }).start();*/

}
