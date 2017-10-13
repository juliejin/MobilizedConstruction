package com.mobilizedconstruction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.mobilizedconstruction.R;

public class CreatedReportDisplayActivity extends AppCompatActivity {
    IdentityManager identityManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_report_display);

        identityManager = IdentityManager.getDefaultIdentityManager();
        identityManager.getCachedUserID();

        AmazonDynamoDBClient client =
                new AmazonDynamoDBClient(IdentityManager.getDefaultIdentityManager()
                        .getCredentialsProvider(), new ClientConfiguration());
        ScanRequest scanRequest = new ScanRequest()
                .withTableName("mobilizedconstructio-mobilehub-516637937-Report");
        //ScanResult result = client.scan(scanRequest);
    }
}
