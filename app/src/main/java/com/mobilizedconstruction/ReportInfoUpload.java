package com.mobilizedconstruction;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.retry.RetryPolicy;
import com.mobilizedconstruction.model.Report;
import com.mobilizedconstruction.Application;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.mobilizedconstruction.model.ReportDO;

import java.util.Arrays;


public class ReportInfoUpload extends AppCompatActivity {
    private Report report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_info_upload);
        AmazonDynamoDBClient dynamoDBClient =
                new AmazonDynamoDBClient(IdentityManager.getDefaultIdentityManager()
                        .getCredentialsProvider(), new ClientConfiguration());
        DynamoDBMapper mapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(Application.awsConfiguration)
                .build();
        ReportDO report = new ReportDO();
        Intent intent = new Intent(this, AddCommentActivity.class);
       // intent.putExtra("report", report);
        this.startActivity(intent);
       // navigateToComment();
        //report.insertReportData();
    }

    void navigateToComment(){
        Intent intent = new Intent(this, AddCommentActivity.class);
        intent.putExtra("report", report);
        this.startActivity(intent);

    }

}
