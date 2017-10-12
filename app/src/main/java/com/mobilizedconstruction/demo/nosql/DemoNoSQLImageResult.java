package com.mobilizedconstruction.demo.nosql;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.mobilizedconstruction.model.ImageDO;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.mobilizedconstruction.Application;

import java.util.Set;

public class DemoNoSQLImageResult implements DemoNoSQLResult {
    private static final int KEY_TEXT_COLOR = 0xFF333333;
    private final ImageDO result;
    private DynamoDBMapper mapper;

    DemoNoSQLImageResult(final ImageDO result) {
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
        final double originalValue = result.getReportID();
        result.setReportID(DemoSampleDataGenerator.getRandomSampleNumber());
        try {
            mapper.save(result);
        } catch (final AmazonClientException ex) {
            // Restore original data if save fails, and re-throw.
            result.setReportID(originalValue);
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
        final TextView userIdKeyTextView;
        final TextView userIdValueTextView;
        final TextView imageIndexKeyTextView;
        final TextView imageIndexValueTextView;
        final TextView reportIDKeyTextView;
        final TextView reportIDValueTextView;
        final TextView commentKeyTextView;
        final TextView commentValueTextView;
        final TextView imageFileKeyTextView;
        final TextView imageFileValueTextView;
        final TextView latitudeKeyTextView;
        final TextView latitudeValueTextView;
        final TextView longitudeKeyTextView;
        final TextView longitudeValueTextView;
        if (convertView == null) {
            layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            resultNumberTextView = new TextView(context);
            resultNumberTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            layout.addView(resultNumberTextView);


            userIdKeyTextView = new TextView(context);
            userIdValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(userIdKeyTextView, userIdValueTextView);
            layout.addView(userIdKeyTextView);
            layout.addView(userIdValueTextView);

            imageIndexKeyTextView = new TextView(context);
            imageIndexValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(imageIndexKeyTextView, imageIndexValueTextView);
            layout.addView(imageIndexKeyTextView);
            layout.addView(imageIndexValueTextView);

            reportIDKeyTextView = new TextView(context);
            reportIDValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(reportIDKeyTextView, reportIDValueTextView);
            layout.addView(reportIDKeyTextView);
            layout.addView(reportIDValueTextView);

            commentKeyTextView = new TextView(context);
            commentValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(commentKeyTextView, commentValueTextView);
            layout.addView(commentKeyTextView);
            layout.addView(commentValueTextView);

            imageFileKeyTextView = new TextView(context);
            imageFileValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(imageFileKeyTextView, imageFileValueTextView);
            imageFileValueTextView.setTypeface(Typeface.MONOSPACE);
            layout.addView(imageFileKeyTextView);
            layout.addView(imageFileValueTextView);

            latitudeKeyTextView = new TextView(context);
            latitudeValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(latitudeKeyTextView, latitudeValueTextView);
            layout.addView(latitudeKeyTextView);
            layout.addView(latitudeValueTextView);

            longitudeKeyTextView = new TextView(context);
            longitudeValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(longitudeKeyTextView, longitudeValueTextView);
            layout.addView(longitudeKeyTextView);
            layout.addView(longitudeValueTextView);
        } else {
            layout = (LinearLayout) convertView;
            resultNumberTextView = (TextView) layout.getChildAt(0);

            userIdKeyTextView = (TextView) layout.getChildAt(1);
            userIdValueTextView = (TextView) layout.getChildAt(2);

            imageIndexKeyTextView = (TextView) layout.getChildAt(3);
            imageIndexValueTextView = (TextView) layout.getChildAt(4);

            reportIDKeyTextView = (TextView) layout.getChildAt(5);
            reportIDValueTextView = (TextView) layout.getChildAt(6);

            commentKeyTextView = (TextView) layout.getChildAt(7);
            commentValueTextView = (TextView) layout.getChildAt(8);

            imageFileKeyTextView = (TextView) layout.getChildAt(9);
            imageFileValueTextView = (TextView) layout.getChildAt(10);

            latitudeKeyTextView = (TextView) layout.getChildAt(11);
            latitudeValueTextView = (TextView) layout.getChildAt(12);

            longitudeKeyTextView = (TextView) layout.getChildAt(13);
            longitudeValueTextView = (TextView) layout.getChildAt(14);
        }

        resultNumberTextView.setText(String.format("#%d", +position + 1));
        userIdKeyTextView.setText("userId");
        userIdValueTextView.setText(result.getUserId());
        imageIndexKeyTextView.setText("image index");
        imageIndexValueTextView.setText("" + result.getImageIndex().longValue());
        reportIDKeyTextView.setText("Report ID");
        reportIDValueTextView.setText("" + result.getReportID().longValue());
        commentKeyTextView.setText("comment");
        commentValueTextView.setText(result.getComment());
        imageFileKeyTextView.setText("image file");
        imageFileValueTextView.setText(bytesToHexString(result.getImageFile()));
        latitudeKeyTextView.setText("latitude");
        latitudeValueTextView.setText("" + result.getLatitude().longValue());
        longitudeKeyTextView.setText("longitude");
        longitudeValueTextView.setText("" + result.getLongitude().longValue());
        return layout;
    }
}
