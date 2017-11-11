package com.mobilizedconstruction;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.mobilizedconstruction.R;
import com.mobilizedconstruction.model.Report;

import java.io.File;
import java.io.ObjectOutputStream;
import java.util.Vector;

public class PreviewReportActivity extends AppCompatActivity {
    private static final String LOG_TAG = RoadFeaturesActivity.class.getSimpleName();
    Report report;
    DynamoDBMapper mapper;
    Boolean showButtons = true;
    Context context = this;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_report);
        Intent intent = getIntent();
        showButtons = getIntent().getBooleanExtra("showButtons", true);
        report = (Report)intent.getSerializableExtra("new_report");
        final Button publishButton = (Button)findViewById(R.id.PublishButton);
        final Button editCategoryButton = (Button)findViewById(R.id.EditCategoryButton);
        final Button editFeaturesButton = (Button)findViewById(R.id.EditFeaturesButton);
        final Button editCommentButton = (Button)findViewById(R.id.EditCommentButton);

        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    //we are connected to a network
                    connected = true;
                }
                else
                    connected = false;
                if (connected) {
                    UpdateReport();
                }
                else
                {
                    saveLocally();
                }
            }
        });

        editCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SetRoadHazardActivity.class);
                intent.putExtra("new_report", report);
                intent.putExtra("allow_edit", true);
                startActivity(intent);
            }
        });

        editFeaturesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RoadFeaturesActivity.class);
                intent.putExtra("new_report", report);
                intent.putExtra("allow_edit", true);
                startActivity(intent);
            }
        });

        editCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddCommentActivity.class);
                intent.putExtra("new_report", report);
                startActivity(intent);
            }
        });

        if(showButtons == false)
        {
            publishButton.setVisibility(View.INVISIBLE);
            editCategoryButton.setVisibility(View.INVISIBLE);
            editFeaturesButton.setVisibility(View.INVISIBLE);
            editCommentButton.setVisibility(View.INVISIBLE);
        }
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.preview_LL);
        for (int i = 0; i < report.reportImages.size(); i++)
        {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new TableRow.LayoutParams(240, 240));
            Bitmap myBitmap = BitmapFactory
                    .decodeFile(report.reportImages.elementAt(i).getFilePath());
            myBitmap = Bitmap.createScaledBitmap(myBitmap, 240,240, true);
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            myBitmap = Bitmap.createBitmap(myBitmap , 0, 0, myBitmap .getWidth(), myBitmap .getHeight(), matrix, true);
            imageView.setImageBitmap(myBitmap);
            linearLayout.addView(imageView);
        }
        final TextView commentTextView = (TextView) findViewById(R.id.CommentTextView);
        commentTextView.setText(report.reportDO.getComment());
        final TextView locationTextView = (TextView) findViewById(R.id.LocationTextView);
        final TextView categoryTextView = (TextView) findViewById(R.id.CategoryTextView);
        final TextView featuresTextView = (TextView) findViewById(R.id.FeaturesTextView);
        String longitude = "N/A";
        String latitude = "N/A";
        String hazard = "N/A";
        Vector<String> hazards = new Vector<String>();
        hazards.add("Potholes");
        hazards.add("Speed Bumps");
        hazards.add("Drainage");
        hazards.add("Road Debris");
        hazards.add("Inclement Weather");
        hazards.add("Accidents");
        hazards.add("Street Signs");
        hazards.add("Other");
        hazard = hazards.elementAt(report.reportDO.getRoadHazard());
        if (report.reportDO.getLatitude() != -1)
        {
            latitude = report.reportDO.getLatitude().toString();
        }
        if (report.reportDO.getLongitude() != -1)
        {
            longitude = report.reportDO.getLongitude().toString();
        }
        String location = "Longitude: " + longitude + '\n';
        location = location + "Latitude: " + latitude + '\n';
        locationTextView.setText(location);
        String road_hazard = "Road Hazard: " + hazard + '\n';
        categoryTextView.setText(road_hazard);
        String features = "Severity: " + report.reportDO.getSeverity() +'\n';
        String roadDirection = "";
        if (report.reportDO.getRoadDirection() == 0)
            roadDirection = "Left";
        else if (report.reportDO.getRoadDirection() == 1)
            roadDirection = "Right";
        else if (report.reportDO.getRoadDirection() == 2)
            roadDirection = "Both";
        features = features + "Road Direction: " + roadDirection + '\n';
        featuresTextView.setText(features);
    }

    public void UpdateReport(){
        AmazonDynamoDBClient dynamoDBClient =
                new AmazonDynamoDBClient(IdentityManager.getDefaultIdentityManager()
                        .getCredentialsProvider(), new ClientConfiguration());
        mapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(Application.awsConfiguration)
                .build();
        final Intent intent = new Intent(this, ReportCreationActivity.class);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ScanRequest scanRequest = new ScanRequest()
                            .withTableName("mobilizedconstructio-mobilehub-516637937-Report");
                    AmazonDynamoDBClient dynamoDBClient =
                            new AmazonDynamoDBClient(IdentityManager.getDefaultIdentityManager()
                                    .getCredentialsProvider(), new ClientConfiguration());
                    ScanResult result = dynamoDBClient.scan(scanRequest);
                    Integer id = result.getCount() + 1;
                    report.reportDO.setReportID(id);
                    mapper.save(report.reportDO);
                    if(report.filePath!=null) {
                        File dir = getFilesDir();
                        File file = new File(report.filePath);
                        file.delete();
                    }
                    for (int i = 0; i < report.reportImages.size(); i++)
                    {
                        report.reportImages.elementAt(i).setReportID(id);
                        report.reportImages.elementAt(i).uploadToS3(context);
                        //report.reportImages.elementAt(i).fetchFromDB(report.reportImages.elementAt(i).GetReportImage().getReportID(),report.reportImages.elementAt(i).GetReportImage().getIndex(),context);
                    }
                    Log.d(LOG_TAG, "Successfully updated");
                    startActivity(intent);
                } catch (final AmazonClientException ex) {
                    Log.e(LOG_TAG, "Failed updateing item : " + ex.getMessage(), ex);
                }
            }
        }).start();
    }


    public void saveLocally(){
        File fileshandler = getFilesDir();
        File[] files =  fileshandler.listFiles();
        String FILENAME = report.reportDO.getUserID()+"_"+report.reportDO.getReportID()+files.length+".txt";
        ObjectOutputStream fos;
        try {
            fos = new ObjectOutputStream(openFileOutput(FILENAME, MODE_PRIVATE));
            fos.writeObject(report);
            fos.close();
            startActivity(new Intent(this, ReportCreationActivity.class));
        }catch(Exception ex){
            Toast.makeText(this, ex.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

    }

}

