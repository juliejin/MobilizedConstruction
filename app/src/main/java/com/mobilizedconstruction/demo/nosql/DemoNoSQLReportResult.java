package com.mobilizedconstruction.demo.nosql;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.mobilizedconstruction.model.ReportDO;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.mobilizedconstruction.Application;

import java.util.Set;

public class DemoNoSQLReportResult implements DemoNoSQLResult {
    private static final int KEY_TEXT_COLOR = 0xFF333333;
    private final ReportDO result;
    private DynamoDBMapper mapper;

    DemoNoSQLReportResult(final ReportDO result) {
        this.result = result;
        AmazonDynamoDBClient dynamoDBClient =
                new AmazonDynamoDBClient(IdentityManager.getDefaultIdentityManager()
                        .getCredentialsProvider(), new ClientConfiguration());
        this.mapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(Application.awsConfiguration)
                .build();
    }

    @Override
    public void updateItem() {
        final int originalValue = result.getImageCount();
        result.setImageCount(DemoSampleDataGenerator.getRandomSampleNumber().intValue());
        try {
            mapper.save(result);
        } catch (final AmazonClientException ex) {
            // Restore original data if save fails, and re-throw.
            result.setImageCount( originalValue);
            throw ex;
        }
    }

    @Override
    public void deleteItem() {
        mapper.delete(result);
    }

    private void setKeyTextViewStyle(final TextView textView) {
        textView.setTextColor(KEY_TEXT_COLOR);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(dp(5), dp(2), dp(5), 0);
        textView.setLayoutParams(layoutParams);
    }

    /**
     * @param dp number of design pixels.
     * @return number of pixels corresponding to the desired design pixels.
     */
    private int dp(int dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    private void setValueTextViewStyle(final TextView textView) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(dp(15), 0, dp(15), dp(2));
        textView.setLayoutParams(layoutParams);
    }

    private void setKeyAndValueTextViewStyles(final TextView keyTextView, final TextView valueTextView) {
        setKeyTextViewStyle(keyTextView);
        setValueTextViewStyle(valueTextView);
    }

    private static String bytesToHexString(byte[] bytes) {
        final StringBuilder builder = new StringBuilder();
        builder.append(String.format("%02X", bytes[0]));
        for (int index = 1; index < bytes.length; index++) {
            builder.append(String.format(" %02X", bytes[index]));
        }
        return builder.toString();
    }

    private static String byteSetsToHexStrings(Set<byte[]> bytesSet) {
        final StringBuilder builder = new StringBuilder();
        int index = 0;
        for (byte[] bytes : bytesSet) {
            builder.append(String.format("%d: ", ++index));
            builder.append(bytesToHexString(bytes));
            if (index < bytesSet.size()) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    @Override
    public View getView(final Context context, final View convertView, int position) {
        final LinearLayout layout;
        final TextView resultNumberTextView;
        final TextView reportIDKeyTextView;
        final TextView reportIDValueTextView;
        final TextView imageCountKeyTextView;
        final TextView imageCountValueTextView;
        final TextView roadDirectionKeyTextView;
        final TextView roadDirectionValueTextView;
        final TextView severityKeyTextView;
        final TextView severityValueTextView;
        final TextView userIDKeyTextView;
        final TextView userIDValueTextView;
        if (convertView == null) {
            layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            resultNumberTextView = new TextView(context);
            resultNumberTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            layout.addView(resultNumberTextView);


            reportIDKeyTextView = new TextView(context);
            reportIDValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(reportIDKeyTextView, reportIDValueTextView);
            layout.addView(reportIDKeyTextView);
            layout.addView(reportIDValueTextView);

            imageCountKeyTextView = new TextView(context);
            imageCountValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(imageCountKeyTextView, imageCountValueTextView);
            layout.addView(imageCountKeyTextView);
            layout.addView(imageCountValueTextView);

            roadDirectionKeyTextView = new TextView(context);
            roadDirectionValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(roadDirectionKeyTextView, roadDirectionValueTextView);
            layout.addView(roadDirectionKeyTextView);
            layout.addView(roadDirectionValueTextView);

            severityKeyTextView = new TextView(context);
            severityValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(severityKeyTextView, severityValueTextView);
            layout.addView(severityKeyTextView);
            layout.addView(severityValueTextView);

            userIDKeyTextView = new TextView(context);
            userIDValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(userIDKeyTextView, userIDValueTextView);
            layout.addView(userIDKeyTextView);
            layout.addView(userIDValueTextView);
        } else {
            layout = (LinearLayout) convertView;
            resultNumberTextView = (TextView) layout.getChildAt(0);

            reportIDKeyTextView = (TextView) layout.getChildAt(1);
            reportIDValueTextView = (TextView) layout.getChildAt(2);

            imageCountKeyTextView = (TextView) layout.getChildAt(3);
            imageCountValueTextView = (TextView) layout.getChildAt(4);

            roadDirectionKeyTextView = (TextView) layout.getChildAt(5);
            roadDirectionValueTextView = (TextView) layout.getChildAt(6);

            severityKeyTextView = (TextView) layout.getChildAt(7);
            severityValueTextView = (TextView) layout.getChildAt(8);

            userIDKeyTextView = (TextView) layout.getChildAt(9);
            userIDValueTextView = (TextView) layout.getChildAt(10);
        }

        resultNumberTextView.setText(String.format("#%d", +position + 1));
        reportIDKeyTextView.setText("Report ID");
        reportIDValueTextView.setText("" + result.getReportID().longValue());
        imageCountKeyTextView.setText("Image Count");
        imageCountValueTextView.setText("" + result.getImageCount().longValue());
        roadDirectionKeyTextView.setText("Road Direction");
        roadDirectionValueTextView.setText("" + result.getRoadDirection().longValue());
        severityKeyTextView.setText("Severity");
        severityValueTextView.setText("" + result.getSeverity().longValue());
        userIDKeyTextView.setText("User ID");
        userIDValueTextView.setText(result.getUserID());
        return layout;
    }
}
