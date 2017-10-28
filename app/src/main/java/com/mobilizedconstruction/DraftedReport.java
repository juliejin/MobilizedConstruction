package com.mobilizedconstruction;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.mobilizedconstruction.R;
import com.mobilizedconstruction.model.Report;
import com.mobilizedconstruction.model.ReportDO;
import android.widget.TableRow.LayoutParams;
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
        LinearLayout draftedReports = (LinearLayout) findViewById(R.id.ll_draft);

        final Vector<Report> savedReports = new Vector<Report>();
        try{
            File fileshandler = getFilesDir();
            File[] files =  fileshandler.listFiles();
            for(int i=0;i<files.length;i++) {
                files[i].getParentFile().mkdirs();
                ObjectInputStream ois = new ObjectInputStream(openFileInput(files[i].getName()));
                Report new_report = (Report) ois.readObject();
                new_report.filePath = files[i].getPath();
                savedReports.addElement(new_report);
                ois.close();
            }
        }catch (Exception ex){
            Log.e(LOG_TAG, "failed reading reports : " + ex.getMessage(), ex);
        }

        for(int i=0;i<savedReports.size();i++){
            final Report report = savedReports.get(i);
            //row.setLayoutParams(new TableLayout.LayoutParams());
            Button reportButton = new Button(this);
            reportButton.setText("Date Created: " + report.reportDO.getDateCreated());
            reportButton.setLayoutParams(new LayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)));
            final Intent intent = new Intent(this, PreviewReportActivity.class);
            reportButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    intent.putExtra("new_report",report);
                    startActivity(intent);
                }
            });
            draftedReports.addView(reportButton);
        }
    }
}
