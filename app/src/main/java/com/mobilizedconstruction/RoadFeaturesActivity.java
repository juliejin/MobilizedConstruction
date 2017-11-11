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
import com.mobilizedconstruction.model.Report;
import com.mobilizedconstruction.model.ReportDO;

import java.util.Vector;

public class RoadFeaturesActivity extends AppCompatActivity {
    private static final String LOG_TAG = RoadFeaturesActivity.class.getSimpleName();
    protected int severity = -1;
    protected int direction = -1;
    boolean allow_edit = false;
    Report report;
    DynamoDBMapper mapper;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road_features);
        Intent intent = getIntent();
        report = (Report)intent.getSerializableExtra("new_report");
        allow_edit = intent.getBooleanExtra("allow_edit", false);
        final Button severityButton1 = (Button) findViewById(R.id.severity1Button);
        final Button severityButton2 = (Button) findViewById(R.id.severity2Button);
        final Button severityButton3 = (Button) findViewById(R.id.severity3Button);
        final Button leftDirectionButton = (Button) findViewById(R.id.leftDirectionButton);
        final Button rightDirectionButton = (Button) findViewById(R.id.rightDirectionButton);
        final Button bothDirectionButton = (Button) findViewById(R.id.bothDirectionButton);
        final Button nextButton = (Button) findViewById(R.id.nextButton);
        Vector<Button> severityButtons = new Vector<Button>();
        Vector<Button> directionButtons = new Vector<Button>();
        severityButtons.add(severityButton1);
        severityButtons.add(severityButton2);
        severityButtons.add(severityButton3);
        directionButtons.add(leftDirectionButton);
        directionButtons.add(rightDirectionButton);
        directionButtons.add(bothDirectionButton);
        if(report.reportDO.getSeverity() != -1)
        {
            severity = report.reportDO.getSeverity();
            for (int i = 1; i < 4; i++)
            {
                if (i == severity)
                {
                    severityButtons.elementAt(i - 1).setBackgroundResource(R.drawable.round_button_clicked_folder);
                }
                else
                {
                    severityButtons.elementAt(i - 1).setBackgroundResource(R.drawable.round_button_folder);
                }
            }
        }
        if(report.reportDO.getRoadDirection() != -1)
        {
            direction = report.reportDO.getRoadDirection();
            for (int i = 0; i < 3; i++)
            {
                if (i == direction)
                {
                    directionButtons.elementAt(i).setBackgroundResource(R.drawable.round_button_clicked_folder);
                }
                else
                {
                    directionButtons.elementAt(i).setBackgroundResource(R.drawable.round_button_folder);
                }
            }
        }
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
        nextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (severity != -1 && direction != -1)
                {
                    report.reportDO.setRoadDirection(direction);
                    report.reportDO.setSeverity(severity);
                    //UpdateReport();
                    navigateToPreview();
                }
            }
        });
    }

    public void navigateToPreview(){
        if (allow_edit)
        {
            Intent intent = new Intent(this, PreviewReportActivity.class);
            intent.putExtra("new_report", report);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(this, AddCommentActivity.class);
            intent.putExtra("new_report", report);
            startActivity(intent);
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
