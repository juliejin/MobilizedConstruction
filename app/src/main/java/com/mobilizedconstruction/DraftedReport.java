package com.mobilizedconstruction;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.mobilizedconstruction.R;
import com.mobilizedconstruction.model.ReportDO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Vector;

public class DraftedReport extends AppCompatActivity {
    private static final String LOG_TAG = DraftedReport.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drafted_report);
        TableLayout draftedReports = (TableLayout) findViewById(R.id.DraftList);

        final Vector<ReportDO> savedReports = new Vector<ReportDO>();
        try{
            File fileshandler = getFilesDir();
            File[] files =  fileshandler.listFiles();
            for(int i=0;i<files.length;i++) {
                ObjectInputStream ois = new ObjectInputStream(openFileInput(files[i].getName()));
                ReportDO new_report = (ReportDO) ois.readObject();
                savedReports.addElement(new_report);
                ois.close();
            }
        }catch (Exception ex){
            Log.e(LOG_TAG, "failed reading reports : " + ex.getMessage(), ex);
        }

        for(int i=0;i<savedReports.size();i++){
            final ReportDO report = savedReports.get(i);
            TableRow row = new TableRow(this);
            //row.setLayoutParams(new TableLayout.LayoutParams());
            Button reportButton = new Button(this);
            reportButton.setText("Report ID: " + report.getReportID().toString());
            reportButton.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT));
            final Intent intent = new Intent(this, PreviewReportActivity.class);
            reportButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent.putExtra("new_report",report);
                    startActivity(intent);
                }
            });
            row.addView(reportButton);
            draftedReports.addView(row,i);
        }
    }
}
