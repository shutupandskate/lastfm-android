package com.example.Lastfm.activities;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import com.example.Lastfm.R;
import com.example.Lastfm.helpers.CalendarHelper;
import com.example.Lastfm.lists.RecentTracksListAdapter;
import com.example.Lastfm.services.RecentTracksService;
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

import static java.security.AccessController.getContext;

public class UserProfileActivity extends Activity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    /**
     * Called when the activity is first created.
     */

    GetRecentTracksTask getRecentTracksTask;
    //GetUserInfoTask getUserInfoTask;

    String userName;
    Integer limit, page;

    private static final String[] PROJECTION = {"_id", "trackTime", "trackName", "trackArtistName", "albumImageUrl"};
    private static final int LOADER_ID = 1;
    private static final Uri CONTENT_URI = Uri.parse("content://com.example.Lastfm.provider.Provider/tracks");

    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;
    private SimpleCursorAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        this.userName = "mortidovs";
        this.limit = 3;
        this.page = 1; // latest tracks

        /*Intent getRecentTracksIntent = new Intent(this, RecentTracksService.class);
        startService(getRecentTracksIntent
                .putExtra("userName", userName)
                .putExtra("limit", limit)
                .putExtra("page", page)
        );*/



/*
        String[] dataColumns = {"trackTime", "trackName",
                "trackArtistName"//, "albumImageUrl"
                };
        int[] viewIDs = {R.id.lvRecentTracksTime, R.id.lvRecentTracksName,
                R.id.lvRecentTracksArtist//, R.id.lvRecentTracksImage
                };

        mAdapter = new SimpleCursorAdapter(this, R.layout.recent_tracks_list_item, null, dataColumns, viewIDs, 0);

        ListView lv = (ListView) findViewById(R.id.lvRecentTracks);
        lv.setAdapter(mAdapter);

        mCallbacks = this;
        LoaderManager lm = getLoaderManager();
        lm.initLoader(LOADER_ID, null, mCallbacks);

*/

        getRecentTracksTask = new GetRecentTracksTask(userName, limit, page, this);
        getRecentTracksTask.execute();

//        getUserInfoTask = new GetUserInfoTask(userName);
//        getUserInfoTask.execute();

        Button moreTracks = (Button) findViewById(R.id.butMoreTracks);
        moreTracks.setOnClickListener(this);
    }

    public void onClick(View view) {
        Intent intent;

        switch(view.getId()) {
            case R.id.butMoreTracks:
//                Intent getRecentTracksIntent = new Intent(this, RecentTracksService.class);
//                startService(getRecentTracksIntent
//                        .putExtra("userName", userName)
//                        .putExtra("limit", 10)
//                        .putExtra("page", page)
//                );
//
//                String[] dataColumns = {"trackTime", "trackName",
//                        "trackArtistName"//, "albumImageUrl"
//                };
//                int[] viewIDs = {R.id.lvRecentTracksTime, R.id.lvRecentTracksName,
//                        R.id.lvRecentTracksArtist//, R.id.lvRecentTracksImage
//                };
//
//                mAdapter = new SimpleCursorAdapter(this, R.layout.recent_tracks_list_item, null, dataColumns, viewIDs, 0);
//
//                ListView lv = (ListView) findViewById(R.id.lvRecentTracks);
//                lv.setAdapter(mAdapter);
//
//                mCallbacks = this;
//                LoaderManager lm = getLoaderManager();
//                lm.initLoader(LOADER_ID, null, mCallbacks);



                intent = new Intent(this, UserRecentTracksListActivity.class);
                intent.putExtra("userName", this.userName);
                intent.putExtra("tracksPerLoading", this.limit);
                startActivity(intent);


                break;
            default:
                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cl = new CursorLoader(UserProfileActivity.this, CONTENT_URI, PROJECTION, null, null, null);
        Log.d("log", cl.toString());
        return cl;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_ID:
                mAdapter.swapCursor(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
