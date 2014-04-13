package com.example.Lastfm.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.net.HttpURLConnection;
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
        Log.d("log", userName);

        HttpURLConnection connection = null;
        StringBuilder sb = new StringBuilder();


        return null;
    }

    @Override
    protected void onPostExecute(Map[] data) {
        super.onPostExecute(data);
    }

}
