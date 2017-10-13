package com.mobilizedconstruction;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.mobilizedconstruction.model.Report;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.mobilizedconstruction.model.ReportDO;
import com.amazonaws.AmazonClientException;
import android.util.Log;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class ReportInfoUpload extends AppCompatActivity {
    private Report report;
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

        intent = new Intent(this, AddCommentActivity.class);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Set<Integer> date =  new HashSet<Integer>();
                    date.add(2017);
                    date.add(10);
                    date.add(4);
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
                    final ReportDO report = new ReportDO(id,"",date,0,0,0,user);
                    mapper.save(report);
                    intent.putExtra("new_report",report);
                    startActivity(intent);
                } catch (final AmazonClientException ex) {
                    Log.e(LOG_TAG, "Failed saving item : " + ex.getMessage(), ex);
                }
            }
        }).start();
    }


}