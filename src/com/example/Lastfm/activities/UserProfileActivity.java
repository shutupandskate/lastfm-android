package com.example.Lastfm.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import com.example.Lastfm.R;
import com.example.Lastfm.helpers.CalendarHelper;
import com.example.Lastfm.lists.RecentTracksListAdapter;
import com.example.Lastfm.tasks.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserProfileActivity extends Activity {
    /**
     * Called when the activity is first created.
     */


    GetRecentTracksTask task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        task = new GetRecentTracksTask("ShutUpAndSkate", "json", 7, this);
        task.execute();
    }


}
