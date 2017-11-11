package com.mobilizedconstruction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;

import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.mobilizedconstruction.api.RestAPI;
import com.mobilizedconstruction.model.DeptTable;
import com.mobilizedconstruction.model.JSONParser;

public class DeptActivity extends AppCompatActivity {
    ArrayAdapter<String> adapter;
    ListView listv;
    Context context;
    ArrayList<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dept);
        // Show the Up button in the action bar.
        setupActionBar();
        data = new ArrayList<String>();
        listv = (ListView) findViewById(R.id.lv_dept);
        context = this;

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, data);
        listv.setAdapter(adapter);
        Toast.makeText(this,"Loading Please Wait..",Toast.LENGTH_SHORT).show();
        new AsyncLoadDeptDetails().execute();

    }

    protected class AsyncLoadDeptDetails extends
            AsyncTask<Void, JSONObject, ArrayList<DeptTable>> {
        ArrayList<DeptTable> deptTable = null;

        @Override
        protected ArrayList<DeptTable> doInBackground(Void... params) {
            // TODO Auto-generated method stub

            RestAPI api = new RestAPI();
            try {

                JSONObject jsonObj = api.GetDepartmentDetails();

                JSONParser parser = new JSONParser();

                deptTable = parser.parseDepartment(jsonObj);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                Log.d("AsyncLoadDeptDetails", e.getMessage());

            }

            return deptTable;
        }

        @Override
        protected void onPostExecute(ArrayList<DeptTable> result) {
            // TODO Auto-generated method stub

            for (int i = 0; i < result.size(); i++) {
                data.add(result.get(i).getNo() + " " + result.get(i).getName());
            }
            adapter.notifyDataSetChanged();
            Toast.makeText(context,"Loading Completed",Toast.LENGTH_SHORT).show();
        }
    }
    /** * Set up the {@link android.app.ActionBar}, if the API is available. */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB) private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.dept, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
