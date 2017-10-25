package com.mobilizedconstruction;

import android.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.net.Uri;
import android.database.Cursor;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.widget.Toast;
import android.graphics.Bitmap;

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
import com.mobilizedconstruction.demo.UserFilesDemoFragment;
import com.mobilizedconstruction.model.Image;
import com.mobilizedconstruction.model.ReportDO;

import java.io.File;
import java.net.URI;
import java.util.concurrent.CountDownLatch;

public class ImageUploadActivity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMG = 1;
    ReportDO report;
    private String imgDecodableString;
    File imageFile;
    Context context = this;
    private static final String LOG_TAG = ImageUploadActivity.class.getSimpleName();
    private UserFileManager userFileManager;

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
        Intent intent = getIntent();
        report = (ReportDO)intent.getSerializableExtra("new_report");
        final Button submitButton = (Button)findViewById(R.id.submitImageButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToNextPage();
            }
        });
        final ImageButton imageButton = (ImageButton)findViewById(R.id.addImageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImage();
            }
        });

    }

    protected void getImage(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                imageFile = new File(imgDecodableString);
                cursor.close();
                ImageView imgView = (ImageView) findViewById(R.id.imagePreview);
                // Set the Image in ImageView after decoding the String
                Bitmap myBitmap = BitmapFactory
                        .decodeFile(imgDecodableString);
                imgView.setImageBitmap(myBitmap);
                //bitmapIntoImageView(imageView, bitmap, MainActivity.this
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }


    protected void navigateToNextPage(){

        Intent intent = new Intent(context, AddCommentActivity.class);
        intent.putExtra("new_report", report);
        startActivity(intent);
    }

    private void insertImage(String filePath, File imageFile, int index, Double lng, Double lat, Integer reportID){
        Image image = new Image(filePath, imageFile, index, lng, lat, reportID);
        report.insertImage(image);

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