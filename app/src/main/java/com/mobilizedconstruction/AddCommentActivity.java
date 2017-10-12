package com.mobilizedconstruction;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.mobilizedconstruction.R;
import com.mobilizedconstruction.model.ReportDO;

public class AddCommentActivity extends AppCompatActivity {
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

    }

    protected void navigateToNextPage(){
        this.startActivity(new Intent(this, RoadFeaturesActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}