package com.mobilizedconstruction;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.mobilizedconstruction.R;
import com.mobilizedconstruction.model.ReportDO;

import java.io.File;
import java.io.ObjectOutputStream;

public class PreviewReportActivity extends AppCompatActivity {
    private static final String LOG_TAG = RoadFeaturesActivity.class.getSimpleName();
    ReportDO report;
    DynamoDBMapper mapper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_report);
        Intent intent = getIntent();
        report = (ReportDO)intent.getSerializableExtra("new_report");
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
    }

    public void UpdateReport(){
        AmazonDynamoDBClient dynamoDBClient =
                new AmazonDynamoDBClient(IdentityManager.getDefaultIdentityManager()
                        .getCredentialsProvider(), new ClientConfiguration());
        mapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(Application.awsConfiguration)
                .build();
        final Intent intent = new Intent(this, ReportCreationActivity.class);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mapper.save(report);
                    File dir = getFilesDir();
                    File file = new File(dir, report.getUserID()+"_"+report.getReportID());
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
        String FILENAME = report.getUserID()+"_"+report.getReportID();
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
