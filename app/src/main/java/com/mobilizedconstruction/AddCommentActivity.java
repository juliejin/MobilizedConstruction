package com.mobilizedconstruction;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mobilizedconstruction.R;
import com.mobilizedconstruction.model.ReportDO;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class AddCommentActivity extends AppCompatActivity {
    private static final String LOG_TAG = AddCommentActivity.class.getSimpleName();
    ReportDO report;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);
        final Button submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setText("Submit");
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToNextPage();
            }
        });
        Intent intent = getIntent();
        report = (ReportDO)intent.getSerializableExtra("new_report");
        ReportDO report_test;
        String FILENAME = "hello_file";
        ObjectInputStream fis;
        try{
            fis = new ObjectInputStream(openFileInput(FILENAME));
            report_test = (ReportDO) fis.readObject();
            fis.close();
            //submitButton.setText(report_test.getUserID());
        }catch (Exception ex){
            Log.e(LOG_TAG, "failed reading item : " + ex.getMessage(), ex);
        }
    }

    protected void navigateToNextPage(){
        Intent intent = new Intent(this, RoadFeaturesActivity.class);
        EditText comment = (EditText) findViewById(R.id.commitText);
        report.setComment(comment.getText().toString());
        intent.putExtra("new_report", report);
        startActivity(intent);
    }
}