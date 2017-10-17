package com.mobilizedconstruction;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.mobilizedconstruction.R;
import com.mobilizedconstruction.model.ReportDO;

public class RoadFeaturesActivity extends AppCompatActivity {
    private static final String LOG_TAG = RoadFeaturesActivity.class.getSimpleName();
    protected int severity = -1;
    protected int direction = -1;
    ReportDO report;
    DynamoDBMapper mapper;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road_features);
        final Button severityButton1 = (Button) findViewById(R.id.severity1Button);
        final Button severityButton2 = (Button) findViewById(R.id.severity2Button);
        final Button severityButton3 = (Button) findViewById(R.id.severity3Button);
        final Button leftDirectionButton = (Button) findViewById(R.id.leftDirectionButton);
        final Button rightDirectionButton = (Button) findViewById(R.id.rightDirectionButton);
        final Button bothDirectionButton = (Button) findViewById(R.id.bothDirectionButton);
        final Button nextButton = (Button) findViewById(R.id.nextButton);
        severityButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                severityButton1.setBackgroundResource(R.drawable.round_button_clicked_folder);
                severityButton2.setBackgroundResource(R.drawable.round_button_folder);
                severityButton3.setBackgroundResource(R.drawable.round_button_folder);
                severity = 1;
            }
        });
        severityButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                severityButton1.setBackgroundResource(R.drawable.round_button_folder);
                severityButton2.setBackgroundResource(R.drawable.round_button_clicked_folder);
                severityButton3.setBackgroundResource(R.drawable.round_button_folder);
                severity = 2;
            }
        });
        severityButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                severityButton1.setBackgroundResource(R.drawable.round_button_folder);
                severityButton2.setBackgroundResource(R.drawable.round_button_folder);
                severityButton3.setBackgroundResource(R.drawable.round_button_clicked_folder);
                severity = 3;
            }
        });
        leftDirectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leftDirectionButton.setBackgroundResource(R.drawable.round_button_clicked_folder);
                rightDirectionButton.setBackgroundResource(R.drawable.round_button_folder);
                bothDirectionButton.setBackgroundResource(R.drawable.round_button_folder);
                direction = 0;
            }
        });
        rightDirectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leftDirectionButton.setBackgroundResource(R.drawable.round_button_folder);
                rightDirectionButton.setBackgroundResource(R.drawable.round_button_clicked_folder);
                bothDirectionButton.setBackgroundResource(R.drawable.round_button_folder);
                direction = 1;
            }
        });
        bothDirectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leftDirectionButton.setBackgroundResource(R.drawable.round_button_folder);
                rightDirectionButton.setBackgroundResource(R.drawable.round_button_folder);
                bothDirectionButton.setBackgroundResource(R.drawable.round_button_clicked_folder);
                direction = 2;
            }
        });

        Intent intent = getIntent();
        report = (ReportDO)intent.getSerializableExtra("new_report");
        nextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (severity != -1 && direction != -1)
                {
                    report.setRoadDirection(direction);
                    report.setSeverity(severity);
                    //UpdateReport();
                    navigateToPreview();
                }
                else
                {

                }
            }
        });
    }

    public void navigateToPreview(){
        Intent intent = new Intent(this, PreviewReportActivity.class);
        intent.putExtra("new_report", report);
        startActivity(intent);
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
                    Log.d(LOG_TAG, "Successfully updated");
                    startActivity(intent);
                } catch (final AmazonClientException ex) {
                    Log.e(LOG_TAG, "Failed updating item : " + ex.getMessage(), ex);
                }
            }
        }).start();

    }

}
