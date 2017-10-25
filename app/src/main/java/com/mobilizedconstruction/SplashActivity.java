
package com.mobilizedconstruction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.amazonaws.mobile.auth.core.DefaultSignInResultHandler;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.auth.core.IdentityProvider;
import com.amazonaws.mobile.auth.core.StartupAuthErrorDetails;
import com.amazonaws.mobile.auth.core.StartupAuthResult;
import com.amazonaws.mobile.auth.core.StartupAuthResultHandler;
import com.amazonaws.mobile.auth.core.signin.AuthException;
import com.amazonaws.mobile.auth.ui.SignInActivity;

import java.lang.ref.WeakReference;

/**
 * Splash Activity is the start-up activity that appears until a delay is expired
 * or the user taps the screen.  When the splash activity starts, various app
 * initialization operations are performed.
 */
public class SplashActivity extends Activity implements StartupAuthResultHandler {

    private static final String LOG_TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final IdentityManager identityManager = IdentityManager.getDefaultIdentityManager();

        identityManager.doStartupAuth(this, this);
    }

    private void doMandatorySignIn(final IdentityManager identityManager) {
        final WeakReference<SplashActivity> self = new WeakReference<SplashActivity>(this);

        identityManager.setUpToAuthenticate(this, new DefaultSignInResultHandler() {

            @Override
            public void onSuccess(Activity activity, IdentityProvider identityProvider) {
                // User has signed in
                Log.e("NotError", "User signed in");
                Activity callingActivity = self.get();
                callingActivity.startActivity(new Intent(callingActivity, ReportCreationActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                callingActivity.finish();
            }

            @Override
            public boolean onCancel(Activity activity) {
                // This
                return false;
            }
        });
        SignInActivity.startSignInActivity(this, Application.sAuthUIConfiguration);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Touch event bypasses waiting for the splash timeout to expire.
        IdentityManager.getDefaultIdentityManager().expireSignInTimeout();
        return true;
    }

    @Override
    public void onComplete(StartupAuthResult authResult) {
        final IdentityManager identityManager = authResult.getIdentityManager();

        if (authResult.isUserSignedIn()) {
            final IdentityProvider provider = identityManager.getCurrentIdentityProvider();
            // If we were signed in previously with a provider indicate that to the user with a toast.
            Toast.makeText(SplashActivity.this, String.format("Signed in with %s",
                    provider.getDisplayName()), Toast.LENGTH_LONG).show();
        } else {
            // Either the user has never signed in with a provider before or refresh failed with a previously
            // signed in provider.

            // Optionally, you may want to check if refresh failed for the previously signed in provider.
            final StartupAuthErrorDetails errors = authResult.getErrorDetails();

            if (errors.didErrorOccurRefreshingProvider()) {
                final AuthException providerAuthException = errors.getProviderRefreshException();
                Log.w(LOG_TAG, String.format(
                        "Credentials for Previously signed-in provider %s could not be refreshed.",
                        providerAuthException.getProvider().getDisplayName()), providerAuthException);
            }

            doMandatorySignIn(identityManager);
            return;
        }

        this.startActivity(new Intent(this, ReportCreationActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        this.finish();
    }
}
