package com.example.Lastfm.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
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

public class UserProfileActivity extends Activity implements View.OnClickListener {
    /**
     * Called when the activity is first created.
     */


    GetRecentTracksTask getRecentTracksTask;
    Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        getRecentTracksTask = new GetRecentTracksTask("ShutUpAndSkate", "json", 3, this);
        getRecentTracksTask.execute();

        activity  = this;

        Button moreTracks = (Button) findViewById(R.id.butMoreTracks);
        moreTracks.setOnClickListener(this);
    }

    public void onClick(View view) {
        Intent intent;

        switch(view.getId()) {
            case R.id.butMoreTracks:
                intent = new Intent(this, UserRecentTracksListActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

}
