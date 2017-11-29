package com.mobilizedconstruction;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mobilizedconstruction.model.Report;

import java.util.Vector;

public class SetRoadHazardActivity extends AppCompatActivity {
    Report report;
    int roadHazard = -1;
    Vector<Button> buttons;
    int previousChoice = -1;
    boolean allow_edit = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_road_hazard);
        Intent intent = getIntent();
        report = (Report)intent.getSerializableExtra("new_report");
        allow_edit = getIntent().getBooleanExtra("allow_edit", false);
        buttons = new Vector<Button>();
        final Button pavedPotholes = (Button)findViewById(R.id.pavedPotholeButton);
        pavedPotholes.setBackgroundColor(Color.GRAY);
        buttons.add(pavedPotholes);
        final Button gravelPothole = (Button)findViewById(R.id.gravelPotholeButton);
        gravelPothole.setBackgroundColor(Color.GRAY);
        buttons.add(gravelPothole);
        final Button flooding = (Button)findViewById(R.id.floodingButton);
        flooding.setBackgroundColor(Color.GRAY);
        buttons.add(flooding);
        final Button drainage = (Button)findViewById(R.id.drainageButton);
        drainage.setBackgroundColor(Color.GRAY);
        buttons.add(drainage);
        final Button debris = (Button)findViewById(R.id.debrisButton);
        debris.setBackgroundColor(Color.GRAY);
        buttons.add(debris);
        final Button other = (Button)findViewById(R.id.otherButton);
        other.setBackgroundColor(Color.GRAY);
        buttons.add(other);
        if (report.reportDO.getRoadHazard() != -1)
        {
            roadHazard = report.reportDO.getRoadHazard();
            buttons.elementAt(roadHazard).setBackgroundColor(Color.DKGRAY);
            previousChoice = report.reportDO.getRoadHazard();
        }
        pavedPotholes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roadHazard = 0;
                if (previousChoice != -1)
                {
                    buttons.elementAt(previousChoice).setBackgroundColor(Color.GRAY);
                }
                pavedPotholes.setBackgroundColor(Color.DKGRAY);
                previousChoice = 0;
            }
        });
        gravelPothole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roadHazard = 1;
                if (previousChoice != -1)
                {
                    buttons.elementAt(previousChoice).setBackgroundColor(Color.GRAY);
                }
                gravelPothole.setBackgroundColor(Color.DKGRAY);
                previousChoice = 1;
            }
        });
        flooding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roadHazard = 2;
                if (previousChoice != -1)
                {
                    buttons.elementAt(previousChoice).setBackgroundColor(Color.GRAY);
                }
                flooding.setBackgroundColor(Color.DKGRAY);
                previousChoice = 2;
            }
        });
        drainage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roadHazard = 3;
                if (previousChoice != -1)
                {
                    buttons.elementAt(previousChoice).setBackgroundColor(Color.GRAY);
                }
                drainage.setBackgroundColor(Color.DKGRAY);
                previousChoice = 3;
            }
        });
        debris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roadHazard = 4;
                if (previousChoice != -1)
                {
                    buttons.elementAt(previousChoice).setBackgroundColor(Color.GRAY);
                }
                debris.setBackgroundColor(Color.DKGRAY);
                previousChoice = 4;
            }
        });
       other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roadHazard = 5;
                if (previousChoice != -1)
                {
                    buttons.elementAt(previousChoice).setBackgroundColor(Color.GRAY);
                }
                other.setBackgroundColor(Color.DKGRAY);
                previousChoice = 5;
            }
        });
        final Button submit = (Button)findViewById(R.id.submitHazardButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(previousChoice != -1) {
                    navigateToNextPage();
                }
            }
        });
    }

    private void navigateToNextPage(){
        report.reportDO.setRoadHazard(previousChoice);
        report.reportDO.setSeverity(-1);
        report.reportDO.setRoadDirection(-1);
        if (allow_edit)
        {
            Intent intent = new Intent(this, PreviewReportActivity.class);
            intent.putExtra("new_report", report);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(this, ImageUploadActivity.class);
            intent.putExtra("new_report", report);
            startActivity(intent);
        }
    }
}
