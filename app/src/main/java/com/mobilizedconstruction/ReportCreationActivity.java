package com.mobilizedconstruction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amazonaws.mobile.auth.core.DefaultSignInResultHandler;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.auth.core.IdentityProvider;
import com.amazonaws.mobile.auth.ui.SignInActivity;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;

public class ReportCreationActivity extends Activity {

    private IdentityManager identityManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_creation);

        identityManager = IdentityManager.getDefaultIdentityManager();
        CognitoUserPool userPool = new CognitoUserPool(this, identityManager.getConfiguration());
        final CognitoUser user = userPool.getUser(identityManager.getCachedUserID());
        TextView userName = (TextView) findViewById(R.id.DisPlayedUserName);
        userName.setText(identityManager.getCachedUserID());


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

        final Button signout = (Button) findViewById((R.id.SignOut));
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToSignIn();
            }
        });

        final Button drafted = (Button) findViewById(R.id.DraftedReportButton);
        drafted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToDraftedPage();
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
