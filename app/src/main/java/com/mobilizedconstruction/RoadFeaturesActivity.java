package com.mobilizedconstruction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mobilizedconstruction.R;

public class RoadFeaturesActivity extends AppCompatActivity {
    protected int severity = -1;
    protected int direction = -1;
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
        nextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (severity != -1 && direction != -1)
                {

                }
                else
                {

                }
            }
        });
    }
}
