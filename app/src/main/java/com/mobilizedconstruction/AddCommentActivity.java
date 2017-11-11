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
import com.mobilizedconstruction.model.Report;
import com.mobilizedconstruction.model.ReportDO;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class AddCommentActivity extends AppCompatActivity {
    private static final String LOG_TAG = AddCommentActivity.class.getSimpleName();
    Report report;
    boolean allow_edit = false;
    EditText comment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);
        Intent intent = getIntent();
        report = (Report)intent.getSerializableExtra("new_report");
        final Button submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setText("Submit");
        comment = (EditText) findViewById(R.id.commitText);
        comment.setText(report.reportDO.getComment());
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToNextPage();
            }
        });
    }

    protected void navigateToNextPage(){
        report.reportDO.setComment(comment.getText().toString());
        Intent intent = new Intent(this, PreviewReportActivity.class);
        intent.putExtra("new_report", report);
        startActivity(intent);
    }
}