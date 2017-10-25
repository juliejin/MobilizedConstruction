package com.mobilizedconstruction;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.mobilizedconstruction.model.Image;
import com.mobilizedconstruction.model.Report;
import com.mobilizedconstruction.model.ReportDO;
import com.amazonaws.AmazonClientException;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import static android.content.Context.*;


public class ReportInfoUpload extends AppCompatActivity {
    private static final String LOG_TAG = ReportInfoUpload.class.getSimpleName();
    DynamoDBMapper mapper;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_info_upload);
        AmazonDynamoDBClient dynamoDBClient =
                new AmazonDynamoDBClient(IdentityManager.getDefaultIdentityManager()
                        .getCredentialsProvider(), new ClientConfiguration());
        mapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(Application.awsConfiguration)
                .build();
        intent = new Intent(this, ImageUploadActivity.class);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    AmazonDynamoDBClient client =
                            new AmazonDynamoDBClient(IdentityManager.getDefaultIdentityManager()
                                    .getCredentialsProvider(), new ClientConfiguration());
                    ScanRequest scanRequest = new ScanRequest()
                            .withTableName("mobilizedconstructio-mobilehub-516637937-Report");
                    ScanResult result = client.scan(scanRequest);
                    Integer id = result.getCount()+1;
                    String user = " ";
                    IdentityManager identityManager = IdentityManager.getDefaultIdentityManager();
                    if(identityManager!=null) {
                        user = identityManager.getCachedUserID();
                    }
                    Date currentDate = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    final ReportDO reportDO = new ReportDO(id,"",formatter.format(currentDate).toString(),0,0,0,user);
                    Report report = new Report(reportDO);
                    //mapper.save(report);
                    intent.putExtra("new_report",report);
                    startActivity(intent);
                } catch (final AmazonClientException ex) {
                    Log.e(LOG_TAG, "Failed saving item : " + ex.getMessage(), ex);
                }
            }
        }).start();
    }


}