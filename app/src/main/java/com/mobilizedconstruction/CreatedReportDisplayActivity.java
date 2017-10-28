package com.mobilizedconstruction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Button;
import android.widget.TableRow.LayoutParams;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.mobilizedconstruction.R;
import com.mobilizedconstruction.model.Report;
import com.mobilizedconstruction.model.ReportDO;
import com.mobilizedconstruction.model.Image;
import com.mobilizedconstruction.model.ReportImageDO;

import static android.widget.TableLayout.*;

public class CreatedReportDisplayActivity extends AppCompatActivity {
    IdentityManager identityManager;
    private static final String LOG_TAG = CreatedReportDisplayActivity.class.getSimpleName();
    private Vector<Report> createdReport;
    final Context context = this;
    LinearLayout linearLayout;
    Vector<Button> tableButtons;
    Boolean scanStopped = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_report_display);
        linearLayout = (LinearLayout)findViewById(R.id.ll);
        createdReport = new Vector<Report>();
        tableButtons = new Vector<Button>();
        scanTable();
    }

    private void scanTable(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    identityManager = IdentityManager.getDefaultIdentityManager();
                    String userID = identityManager.getCachedUserID();
                    AmazonDynamoDBClient client =
                            new AmazonDynamoDBClient(IdentityManager.getDefaultIdentityManager()
                                    .getCredentialsProvider(), new ClientConfiguration());
                    //ScanRequest scanRequest = new ScanRequest()
                      //      .withTableName("mobilizedconstructio-mobilehub-516637937-Report");
                    Map<String, String> attributeNames = new HashMap<String, String >();
                    attributeNames.put("#userID", "User ID");
                    Map<String, AttributeValue> expressionAttributeValues =
                            new HashMap<String, AttributeValue>();
                    expressionAttributeValues.put(":userID", new AttributeValue().withS(userID));
                    ScanRequest scanRequest = new ScanRequest()
                            .withTableName("mobilizedconstructio-mobilehub-516637937-Report")
                            .withExpressionAttributeNames(attributeNames)
                            .withExpressionAttributeValues(expressionAttributeValues)
                            .withFilterExpression("#userID = :userID");
                    ScanResult result = client.scan(scanRequest);

                    for (Map<String, AttributeValue> item : result.getItems()) {
                        String comment = "";
                        if (item.get("Comment") != null) {
                            comment = item.get("Comment").getS();
                        }
                        Integer image_count = Integer.valueOf(item.get("Image Count").getN());
                        Integer report_ID = Integer.valueOf(item.get("Report ID").getN());
                        Integer severity = Integer.valueOf(item.get("Severity").getN());
                        String date_created = item.get("Date Created").getS();
                        Integer road_direction = Integer.valueOf(item.get("Road Direction").getN());
                        ReportDO reportdo = new ReportDO(report_ID, comment, date_created, image_count,
                                road_direction, severity, userID);
                        Report report = new Report(reportdo);
                        createdReport.add(report);
                    }
                } catch (final AmazonClientException ex) {
                    Log.e(LOG_TAG, "Failed fetching reports : " + ex.getMessage(), ex);
                }
                scanStopped = true;
            }
        }).start();

        while(!scanStopped)
        {

        }
        if (createdReport.size() == 0) {
           // display no report
        }
        else
        {
            for (int i = 0; i < createdReport.size(); i++)
            {
                addNewButton(i);
            }
        }
    }

    protected void addNewButton(final int row_index){
        Button reportButton = new Button(this);
        reportButton.setText("Report created at " + createdReport.elementAt(row_index).reportDO.getDateCreated());
        reportButton.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        reportButton.setId(row_index + 1);
        reportButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                toPreview(row_index);
            }
        });
        linearLayout.addView(reportButton);
    }

    protected void toPreview(int index){
        Intent intent = new Intent(this, PreviewReportActivity.class);
        intent.putExtra("showButtons", false);
        Report report = new Report(createdReport.elementAt(index).reportDO);
        for (int i = 0; i < report.reportDO.getImageCount(); i++)
        {
            ReportImageDO imaged = new ReportImageDO(report.reportDO.getReportID(), index, 0.0,0.0);
            Image image = new Image(imaged);
            image.fetchFromDB(report.reportDO.getReportID(), i);
            Bitmap imagebit = image.getImageBitmap();
            while(imagebit==null){
                imagebit = image.getImageBitmap();
            }
            OutputStream os;
            String filename = image.GetReportImage().getImageURL();
            filename = filename.replace("uploads/","");
            try {
                File imageFile = new File(getFilesDir(), filename);
                imageFile.createNewFile();
                image.SetImageFile(imageFile);
                image.SetFilePath(imageFile.getPath());
                os = new FileOutputStream(imageFile);
                imagebit.compress(Bitmap.CompressFormat.JPEG, 100, os);
                os.flush();
                os.close();
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
            }
            report.insertImage(image);
        }
        intent.putExtra("new_report", report);
        startActivity(intent);
    }
}
