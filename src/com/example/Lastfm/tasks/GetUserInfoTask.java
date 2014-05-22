package com.example.Lastfm.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import com.example.Lastfm.helpers.QueryURLHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ShutUpAndSkate on 13.04.14.
 */
public class GetUserInfoTask extends AsyncTask<Map[], Void, Map[]> {

    private static String userName;

    public GetUserInfoTask(String userName) {
        this.userName = userName;
    }

    @Override
    protected Map[] doInBackground(Map[]... params) {
        //Log.d("log", userName);

        HttpURLConnection connection = null;
        StringBuilder sb = new StringBuilder();

        try {
            Map<String, String> queryParams = new HashMap<String, String>();
            queryParams.put("method", "getInfo");
            queryParams.put("user", userName);

            URL url = new QueryURLHelper(queryParams).getURLFromQuery();
            //Log.d("log", url.toString());

            connection = (HttpURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ( (line = br.readLine()) != null ) sb.append(line + "\n");

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }

        JSONObject jsonData;



        return null;
    }

    @Override
    protected void onPostExecute(Map[] data) {
        super.onPostExecute(data);
    }

}
