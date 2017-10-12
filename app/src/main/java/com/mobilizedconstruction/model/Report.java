package com.mobilizedconstruction.model;

/**
 * Created by julie on 9/28/2017.
 */

import android.util.Log;

import com.amazonaws.AmazonClientException;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.mobilizedconstruction.ReportInfoUpload;
import com.mobilizedconstruction.Application;
import com.amazonaws.ClientConfiguration;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Report implements Serializable{
    private static final String LOG_TAG = ReportInfoUpload.class.getSimpleName();

    public Report(DynamoDBMapper mapper){
       /* AmazonDynamoDBClient dynamoDBClient =
                new AmazonDynamoDBClient(IdentityManager.getDefaultIdentityManager()
                        .getCredentialsProvider(), new ClientConfiguration());
        mapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(Application.awsConfiguration)
                .build();*/
       //this.mapper = mapper;
    }

    public void insertReportData() throws AmazonClientException {
        Log.d(LOG_TAG, "Inserting Sample data.");
        final ReportDO firstItem = new ReportDO();

        firstItem.setReportID(1);
        firstItem.setImageCount(0);
        firstItem.setReportID(1);
        firstItem.setRoadDirection(1);
        firstItem.setUserID(IdentityManager.getDefaultIdentityManager().getCachedUserID());
        firstItem.setSeverity(1);
        Set<Integer> date =  new HashSet<Integer>();
        date.add(2017);
        date.add(10);
        date.add(4);
        firstItem.setDateCreated(date);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                   // mapper.save(firstItem);
                } catch (final AmazonClientException ex) {
                    Log.e(LOG_TAG, "Failed saving item : " + ex.getMessage(), ex);
                }
            }
        }).start();

    }
}
