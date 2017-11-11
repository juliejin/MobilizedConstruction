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
        final Button potholes = (Button)findViewById(R.id.potholesButton);
        potholes.setBackgroundColor(Color.GRAY);
        buttons.add(potholes);
        final Button speedBumps = (Button)findViewById(R.id.speedBumpsButton);
        speedBumps.setBackgroundColor(Color.GRAY);
        buttons.add(speedBumps);
        final Button drainage = (Button)findViewById(R.id.drainageButton);
        drainage.setBackgroundColor(Color.GRAY);
        buttons.add(drainage);
        final Button roadDebris = (Button)findViewById(R.id.roadDebrisButton);
        roadDebris.setBackgroundColor(Color.GRAY);
        buttons.add(roadDebris);
        final Button weather = (Button)findViewById(R.id.inclementWeatherButton);
        weather.setBackgroundColor(Color.GRAY);
        buttons.add(weather);
        final Button accidents = (Button)findViewById(R.id.accidentsButton);
        accidents.setBackgroundColor(Color.GRAY);
        buttons.add(accidents);
        final Button streetSigns = (Button)findViewById(R.id.streetSignsButton);
        streetSigns.setBackgroundColor(Color.GRAY);
        buttons.add(streetSigns);
        final Button other = (Button)findViewById((R.id.otherHazardButton));
        other.setBackgroundColor(Color.GRAY);
        buttons.add(other);
        if (report.reportDO.getRoadHazard() != -1)
        {
            roadHazard = report.reportDO.getRoadHazard();
            buttons.elementAt(roadHazard).setBackgroundColor(Color.DKGRAY);
            previousChoice = report.reportDO.getRoadHazard();
        }
        potholes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roadHazard = 0;
                if (previousChoice != -1)
                {
                    buttons.elementAt(previousChoice).setBackgroundColor(Color.GRAY);
                }
                potholes.setBackgroundColor(Color.DKGRAY);
                previousChoice = 0;
            }
        });
        speedBumps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roadHazard = 1;
                if (previousChoice != -1)
                {
                    buttons.elementAt(previousChoice).setBackgroundColor(Color.GRAY);
                }
                speedBumps.setBackgroundColor(Color.DKGRAY);
                previousChoice = 1;
            }
        });
        drainage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roadHazard = 2;
                if (previousChoice != -1)
                {
                    buttons.elementAt(previousChoice).setBackgroundColor(Color.GRAY);
                }
                drainage.setBackgroundColor(Color.DKGRAY);
                previousChoice = 2;
            }
        });
        roadDebris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roadHazard = 3;
                if (previousChoice != -1)
                {
                    buttons.elementAt(previousChoice).setBackgroundColor(Color.GRAY);
                }
                roadDebris.setBackgroundColor(Color.DKGRAY);
                previousChoice = 3;
            }
        });
        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roadHazard = 4;
                if (previousChoice != -1)
                {
                    buttons.elementAt(previousChoice).setBackgroundColor(Color.GRAY);
                }
                weather.setBackgroundColor(Color.DKGRAY);
                previousChoice = 4;
            }
        });
        accidents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roadHazard = 5;
                if (previousChoice != -1)
                {
                    buttons.elementAt(previousChoice).setBackgroundColor(Color.GRAY);
                }
                accidents.setBackgroundColor(Color.DKGRAY);
                previousChoice = 5;
            }
        });
        streetSigns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roadHazard = 6;
                if (previousChoice != -1)
                {
                    buttons.elementAt(previousChoice).setBackgroundColor(Color.GRAY);
                }
                streetSigns.setBackgroundColor(Color.DKGRAY);
                previousChoice = 6;
            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roadHazard = 7;
                if (previousChoice != -1)
                {
                    buttons.elementAt(previousChoice).setBackgroundColor(Color.GRAY);
                }
                other.setBackgroundColor(Color.DKGRAY);
                previousChoice = 7;
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
