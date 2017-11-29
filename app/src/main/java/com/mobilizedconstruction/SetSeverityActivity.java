package com.mobilizedconstruction;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.mobilizedconstruction.model.Report;

import java.util.Vector;

public class SetSeverityActivity extends AppCompatActivity {
    protected int severity = -1;
    protected int direction = -1;
    boolean allow_edit = false;
    Report report;
    DynamoDBMapper mapper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_severity);
        Intent intent = getIntent();
        report = (Report)intent.getSerializableExtra("new_report");
        allow_edit = intent.getBooleanExtra("allow_edit", false);
        final Button severityButton1 = (Button) findViewById(R.id.severity1);
        final Button severityButton2 = (Button) findViewById(R.id.severity2);
        final Button severityButton3 = (Button) findViewById(R.id.severity3);
        final Button severityButton4 = (Button) findViewById(R.id.severity4);
        final Button submitButton = (Button) findViewById(R.id.submitSeverityButton);
        final Vector<Button> severityButtons = new Vector<Button>();
        final Vector<Integer> resids = new Vector<Integer>();
        final Vector<Integer> resids_selected = new Vector<Integer>();
        severityButtons.add(severityButton1);
        severityButtons.add(severityButton2);
        severityButtons.add(severityButton3);
        severityButtons.add(severityButton4);
        if(report.reportDO.getRoadHazard() == 0)
        {
            resids.add(R.mipmap.paved1);
            resids.add(R.mipmap.paved2);
            resids.add(R.mipmap.paved3);
            resids.add(R.mipmap.complete);
            resids_selected.add(R.mipmap.paved1_selected);
            resids_selected.add(R.mipmap.paved2_selected);
            resids_selected.add(R.mipmap.paved3_selected);
            resids_selected.add(R.mipmap.complete_selected);
        }
        else if(report.reportDO.getRoadHazard() == 1)
        {
            resids.add(R.mipmap.gravel1);
            resids.add(R.mipmap.gravel2);
            resids.add(R.mipmap.gravel3);
            resids.add(R.mipmap.complete);
            resids_selected.add(R.mipmap.gravel1_selected);
            resids_selected.add(R.mipmap.gravel2_selected);
            resids_selected.add(R.mipmap.gravel3_selected);
            resids_selected.add(R.mipmap.complete_selected);
        }
        else if(report.reportDO.getRoadHazard() == 2)
        {
            resids.add(R.mipmap.flooding1);
            resids.add(R.mipmap.flooding2);
            resids.add(R.mipmap.flooding3);
            resids.add(R.mipmap.complete);
            resids_selected.add(R.mipmap.flooding1_selected);
            resids_selected.add(R.mipmap.flooding2_selected);
            resids_selected.add(R.mipmap.flooding3_selected);
            resids_selected.add(R.mipmap.complete_selected);
        }
        else if(report.reportDO.getRoadHazard() == 3)
        {
            resids.add(R.mipmap.drainage1);
            resids.add(R.mipmap.drainage2);
            resids.add(R.mipmap.drainage3);
            resids.add(R.mipmap.complete);
            resids_selected.add(R.mipmap.drainage1_selected);
            resids_selected.add(R.mipmap.drainage2_selected);
            resids_selected.add(R.mipmap.drainage3_selected);
            resids_selected.add(R.mipmap.complete_selected);
        }
        else if(report.reportDO.getRoadHazard() == 4)
        {
            resids.add(R.mipmap.debris1);
            resids.add(R.mipmap.debris2);
            resids.add(R.mipmap.debris3);
            resids.add(R.mipmap.complete);
            resids_selected.add(R.mipmap.debris1_selected);
            resids_selected.add(R.mipmap.debris2_selected);
            resids_selected.add(R.mipmap.debris3_selected);
            resids_selected.add(R.mipmap.complete_selected);
        }
            severity = report.reportDO.getSeverity();
            for (int i = 0; i < 4; i++)
            {
                if (i == severity)
                {
                    severityButtons.elementAt(i).setBackgroundResource(resids_selected.elementAt(i));
                }
                else
                {
                    severityButtons.elementAt(i).setBackgroundResource(resids.elementAt(i));
                }
            }
        severityButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                severity = 0;
                for (int i = 0; i < 4; i++)
                {
                    if (i == severity)
                    {
                        severityButtons.elementAt(i).setBackgroundResource(resids_selected.elementAt(i));
                    }
                    else
                    {
                        severityButtons.elementAt(i).setBackgroundResource(resids.elementAt(i));
                    }
                }
            }
        });
        severityButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                severity = 1;
                for (int i = 0; i < 4; i++)
                {
                    if (i == severity)
                    {
                        severityButtons.elementAt(i).setBackgroundResource(resids_selected.elementAt(i));
                    }
                    else
                    {
                        severityButtons.elementAt(i).setBackgroundResource(resids.elementAt(i));
                    }
                }
            }
        });
        severityButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                severity = 2;
                for (int i = 0; i < 4; i++)
                {
                    if (i == severity)
                    {
                        severityButtons.elementAt(i).setBackgroundResource(resids_selected.elementAt(i));
                    }
                    else
                    {
                        severityButtons.elementAt(i).setBackgroundResource(resids.elementAt(i));
                    }
                }
            }
        });
        severityButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                severity = 3;
                for (int i = 0; i < 4; i++)
                {
                    if (i == severity)
                    {
                        severityButtons.elementAt(i).setBackgroundResource(resids_selected.elementAt(i));
                    }
                    else
                    {
                        severityButtons.elementAt(i).setBackgroundResource(resids.elementAt(i));
                    }
                }
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (severity != -1)
                {
                    report.reportDO.setSeverity(severity);
                    navigateToNextPage();
                }
            }
        });
    }

    void navigateToNextPage(){
        if (allow_edit)
        {
            Intent intent = new Intent(this, SetRoadDirectionActivity.class);
            intent.putExtra("new_report", report);
            intent.putExtra("allow_edit", true);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(this, SetRoadDirectionActivity.class);
            intent.putExtra("new_report", report);
            startActivity(intent);
        }
    }
}
