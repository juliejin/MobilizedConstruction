package com.mobilizedconstruction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Button;
import android.view.View;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.mobile.auth.core.DefaultSignInResultHandler;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.auth.core.IdentityProvider;
import com.amazonaws.mobile.auth.ui.SignInActivity;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.mobilizedconstruction.model.Report;
import com.mobilizedconstruction.model.ReportDO;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//main page
public class ReportCreationActivity extends Activity {

    private IdentityManager identityManager;
    private static final String LOG_TAG = ReportInfoUpload.class.getSimpleName();
    DynamoDBMapper mapper;
    Intent intent;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_creation);

        //get user ID
        identityManager = IdentityManager.getDefaultIdentityManager();
        CognitoUserPool userPool = new CognitoUserPool(this, identityManager.getConfiguration());
        final CognitoUser user = userPool.getUser(identityManager.getCachedUserID());
        //TextView userName = (TextView) findViewById(R.id.DisPlayedUserName);
        // userName.setText(identityManager.getCachedUserID());


        final Button button = (Button) findViewById(R.id.CreateNewReportButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                navigateToPage();
            }

        });

        // navigate to published report page
        final Button createdReportButton = (Button) findViewById(R.id.CreatedReportButton);
        createdReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToReportCreatedPage();
            }
        });

        //navigate to sign in page
        final Button signout = (Button) findViewById((R.id.SignOut));
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToSignIn();
            }
        });

        //navigate to locally saved page
        final Button drafted = (Button) findViewById(R.id.DraftedReportButton);
        drafted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToDraftedPage();
            }
        });

    }

    void navigateToPage() {
        AmazonDynamoDBClient dynamoDBClient =
                new AmazonDynamoDBClient(IdentityManager.getDefaultIdentityManager()
                        .getCredentialsProvider(), new ClientConfiguration());
        mapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(Application.awsConfiguration)
                .build();
        intent = new Intent(this, SetRoadHazardActivity.class);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    /*AmazonDynamoDBClient client =
                            new AmazonDynamoDBClient(IdentityManager.getDefaultIdentityManager()
                                    .getCredentialsProvider(), new ClientConfiguration());
                    ScanRequest scanRequest = new ScanRequest()
                            .withTableName("mobilizedconstructio-mobilehub-516637937-Report");
                    ScanResult result = client.scan(scanRequest);*/
                    //Integer id = result.getCount() + 1;
                    Integer id = -1;
                    try{
                        File fileshandler = getFilesDir();
                        File[] files =  fileshandler.listFiles();
                        id = -1 * (files.length + 1);
                    }catch (Exception ex){
                        Log.e(LOG_TAG, "failed reading reports : " + ex.getMessage(), ex);
                    }
                    String user = " ";
                    IdentityManager identityManager = IdentityManager.getDefaultIdentityManager();
                    if (identityManager != null) {
                        user = identityManager.getCachedUserID();
                    }
                    LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                    Location utilLocation = null;
                    List<String> providers = manager.getProviders(false);
                    Double latitude = -1.0;
                    Double longitude = -1.0;
                    for (String provider : providers) {

                        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        utilLocation = manager.getLastKnownLocation(provider);
                        if(utilLocation != null)
                        {
                            latitude = utilLocation.getLatitude();
                            longitude = utilLocation.getLongitude();
                        }
                    }
                    Date currentDate = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    final ReportDO reportDO = new ReportDO(id,"",formatter.format(currentDate).toString(),0,latitude, longitude, -1,-1,-1,user);
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

    void navigateToReportCreatedPage(){
        this.startActivity(new Intent(this, CreatedReportDisplayActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    void navigateToDraftedPage(){
        this.startActivity(new Intent(this, DraftedReport.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }


    void navigateToSignIn(){
        identityManager.signOut();
        identityManager.setUpToAuthenticate(this, new DefaultSignInResultHandler() {

            @Override
            public void onSuccess(Activity activity, IdentityProvider identityProvider) {
                // User has signed in
                Log.e("Success", "User signed in");
                activity.finish();
            }

            @Override
            public boolean onCancel(Activity activity) {
                return false;
            }
        });
        SignInActivity.startSignInActivity(this, Application.sAuthUIConfiguration);
    }


}
