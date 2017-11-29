package com.mobilizedconstruction;

import android.content.Intent;
import android.graphics.Color;
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
import com.mobilizedconstruction.model.Report;

import java.util.Vector;

public class SetRoadDirectionActivity extends AppCompatActivity {
    private static final String LOG_TAG = SetRoadDirectionActivity.class.getSimpleName();
    protected int direction = -1;
    boolean allow_edit = false;
    Report report;
    DynamoDBMapper mapper;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_road_direction);
        Intent intent = getIntent();
        report = (Report)intent.getSerializableExtra("new_report");
        allow_edit = intent.getBooleanExtra("allow_edit", false);

        final Button leftDirectionButton = (Button) findViewById(R.id.leftDirectionButton);
        final Button rightDirectionButton = (Button) findViewById(R.id.rightDirectionButton);
        final Button bothDirectionButton = (Button) findViewById(R.id.bothDirectionButton);
        final Button nextButton = (Button) findViewById(R.id.nextButton);
        final Vector<Button> directionButtons = new Vector<Button>();
        directionButtons.add(leftDirectionButton);
        directionButtons.add(rightDirectionButton);
        directionButtons.add(bothDirectionButton);
        direction = report.reportDO.getRoadDirection();
        for (int i = 0; i < 3; i++)
        {
            if (i == direction)
            {
                directionButtons.elementAt(i).setBackgroundColor(Color.DKGRAY);
            }
            else
            {
                directionButtons.elementAt(i).setBackgroundColor(Color.GRAY);
            }
        }
        leftDirectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                direction = 0;
                for (int i = 0; i < 3; i++)
                {
                    if (i == direction)
                    {
                        directionButtons.elementAt(i).setBackgroundColor(Color.DKGRAY);
                    }
                    else
                    {
                        directionButtons.elementAt(i).setBackgroundColor(Color.GRAY);
                    }
                }
            }
        });
        rightDirectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                direction = 1;
                for (int i = 0; i < 3; i++)
                {
                    if (i == direction)
                    {
                        directionButtons.elementAt(i).setBackgroundColor(Color.DKGRAY);
                    }
                    else
                    {
                        directionButtons.elementAt(i).setBackgroundColor(Color.GRAY);
                    }
                }
            }
        });
        bothDirectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                direction = 2;
                for (int i = 0; i < 3; i++)
                {
                    if (i == direction)
                    {
                        directionButtons.elementAt(i).setBackgroundColor(Color.DKGRAY);
                    }
                    else
                    {
                        directionButtons.elementAt(i).setBackgroundColor(Color.GRAY);
                    }
                }
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (direction != -1)
                {
                    report.reportDO.setRoadDirection(direction);
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
