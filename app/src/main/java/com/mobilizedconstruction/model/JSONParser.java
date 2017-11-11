package com.mobilizedconstruction.model;

/**
 * Created by Ling Jin on 08/11/2017.
 */
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class JSONParser {
    private static final String LOG_TAG = JSONParser.class.getSimpleName();

    public JSONParser()
    {
        super();
    }

    public ArrayList<DeptTable> parseDepartment(JSONObject object)
    {
        ArrayList<DeptTable> arrayList=new ArrayList<DeptTable>();
        try
        {
            JSONArray jsonArray=object.getJSONArray("Value");
            JSONObject jsonObj=null;
            for(int i=0;i<jsonArray.length();i++) {
                jsonObj=jsonArray.getJSONObject(i);
                arrayList.add(new DeptTable(jsonObj.getInt("no"), jsonObj.getString("name"))); }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d(LOG_TAG, e.getMessage());
        }
        return arrayList;
    }

    public boolean parseUserAuth(JSONObject object)
    {     boolean userAtuh=false;
        try {
            userAtuh= object.getBoolean("Value");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d(LOG_TAG, e.getMessage());
        }

        return userAtuh;
    }

    public UserDetailsTable parseUserDetails(JSONObject object)
    {
        UserDetailsTable userDetail=new UserDetailsTable();

        try {
            JSONObject jsonObj=object.getJSONArray("Value").getJSONObject(0);

            userDetail.setFirstName(jsonObj.getString("firstName"));
            userDetail.setLastName(jsonObj.getString("lastName"));

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d(LOG_TAG, e.getMessage());
        }

        return userDetail;

    }
}
