package com.mobilizedconstruction;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.HorizontalScrollView;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.mobilizedconstruction.R;
import com.mobilizedconstruction.model.Report;
import com.mobilizedconstruction.model.ReportDO;

import org.w3c.dom.Text;

import java.io.File;
import java.io.ObjectOutputStream;

public class PreviewReportActivity extends AppCompatActivity {
    private static final String LOG_TAG = RoadFeaturesActivity.class.getSimpleName();
    Report report;
    DynamoDBMapper mapper;
    Boolean showButtons = true;
    Context context = this;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_report);
        Intent intent = getIntent();
        showButtons = getIntent().getBooleanExtra("showButtons", true);
        report = (Report)intent.getSerializableExtra("new_report");
        final Button publishButton = (Button)findViewById(R.id.PublishButton);
        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateReport();
            }
        });
        final Button saveButton = (Button)findViewById(R.id.SaveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveLocally();
            }
        });
        if(showButtons == false)
        {
            publishButton.setVisibility(View.INVISIBLE);
            saveButton.setVisibility(View.INVISIBLE);
        }
        final TextView commentTextView = (TextView) findViewById(R.id.CommentTextView);
        commentTextView.setText(report.reportDO.getComment());
        final TextView featuresTextView = (TextView) findViewById(R.id.FeaturesTextView);
        String features = "Severity: " + report.reportDO.getSeverity() +'\n';
        String roadDirection = "";
        if (report.reportDO.getRoadDirection() == 0)
            roadDirection = "Left";
        else if (report.reportDO.getRoadDirection() == 1)
            roadDirection = "Right";
        else if (report.reportDO.getRoadDirection() == 2)
            roadDirection = "Both";
        features = features + "Road Direction: " + roadDirection;
        featuresTextView.setText(features);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.preview_LL);
        HorizontalScrollView scrollView = (HorizontalScrollView) findViewById(R.id.preview_SV);
        for (int i = 0; i < report.reportImages.size(); i++)
        {
            ImageView imageView = new ImageView(this);
            int height = linearLayout.getHeight();
            imageView.setLayoutParams(new TableRow.LayoutParams(240, 240));
            Bitmap myBitmap = BitmapFactory
                    .decodeFile(report.reportImages.elementAt(i).getFilePath());
            myBitmap = Bitmap.createScaledBitmap(myBitmap, 240,240, true);
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            myBitmap = Bitmap.createBitmap(myBitmap , 0, 0, myBitmap .getWidth(), myBitmap .getHeight(), matrix, true);
            imageView.setImageBitmap(myBitmap);
            linearLayout.addView(imageView);
        }
    }

    public void UpdateReport(){
        AmazonDynamoDBClient dynamoDBClient =
                new AmazonDynamoDBClient(IdentityManager.getDefaultIdentityManager()
                        .getCredentialsProvider(), new ClientConfiguration());
        mapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(Application.awsConfiguration)
                .build();

        for (int i = 0; i < report.reportImages.size(); i++)
        {
            report.reportImages.elementAt(i).uploadToS3(context);
        }
        final Intent intent = new Intent(this, ReportCreationActivity.class);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    mapper.save(report.reportDO);
                    File dir = getFilesDir();
                    File file = new File(dir, report.reportDO.getUserID()+"_"+report.reportDO.getReportID());
                    file.delete();
                    Log.d(LOG_TAG, "Successfully updated");
                    startActivity(intent);
                } catch (final AmazonClientException ex) {
                    Log.e(LOG_TAG, "Failed updateing item : " + ex.getMessage(), ex);
                }
            }
        }).start();

    }


    public void saveLocally(){
        String FILENAME = report.reportDO.getUserID()+"_"+report.reportDO.getReportID();
        ObjectOutputStream fos;
        try {
            fos = new ObjectOutputStream(openFileOutput(FILENAME, MODE_PRIVATE));
            fos.writeObject(report);
            fos.close();
            startActivity(new Intent(this, ReportCreationActivity.class));
        }catch(Exception ex){
            Log.e(LOG_TAG, "failed writing item : " + ex.getMessage(), ex);
        }

    }

}

