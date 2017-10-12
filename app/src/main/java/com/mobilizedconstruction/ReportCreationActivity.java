package com.mobilizedconstruction;

import android.app.Activity;
import android.content.Intent;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;

import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.auth.core.IdentityProvider;
import com.mobilizedconstruction.R;

public class ReportCreationActivity extends Activity {

    private IdentityManager identityManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_creation);

        identityManager = IdentityManager.getDefaultIdentityManager();

        if(identityManager!=null) {
            IdentityProvider identityProvider = identityManager.getCurrentIdentityProvider();
            TextView userName = (TextView) findViewById(R.id.DisPlayedUserName);
            userName.setText(identityManager.getCurrentIdentityProvider().getDisplayName());
        }
        final Button button = (Button) findViewById(R.id.CreateNewReportButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                navigateToPage();
            }

        });
        final Button createdReportButton = (Button) findViewById(R.id.CreatedReportButton);
        createdReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToReportCreatedPage();
            }
        });
    }

    void navigateToPage(){
        this.startActivity(new Intent(this, ReportInfoUpload.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    void navigateToReportCreatedPage(){
        this.startActivity(new Intent(this, CreatedReportDisplayActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

}
