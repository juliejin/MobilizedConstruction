package com.mobilizedconstruction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
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

import com.mobilizedconstruction.R;
import com.mobilizedconstruction.model.ReportDO;

public class ImageUploadActivity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMG = 1;
    ReportDO report;
    private String imgDecodableString;
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
        Intent intent = new Intent(this, AddCommentActivity.class);
        intent.putExtra("new_report", report);
        startActivity(intent);
    }
}
