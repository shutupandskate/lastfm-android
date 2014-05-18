package com.example.Lastfm.activities;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
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

    String userName;
    Integer limit, page;

    private static final String[] PROJECTION = {"_id", "trackTime", "trackName", "trackArtistName", "albumImageUrl"};
    private static final int LOADER_ID = 1;
    private static final Uri CONTENT_URI = Uri.parse("content://com.example.Lastfm.provider.Provider/tracks");

    private SimpleCursorAdapter mAdapter;
    private static LruCache<String, Bitmap> cache;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        this.userName = "mortidovs";
        this.limit = 3;
        this.page = 1; // latest tracks

        getRecentTracksTask = new GetRecentTracksTask(userName, limit, page, this);
        getRecentTracksTask.execute();

        Button moreTracks = (Button) findViewById(R.id.butMoreTracks);
        moreTracks.setOnClickListener(this);

        /* memory cache */
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024); // all available memory. stored in Kb
        final int cacheSize = maxMemory / 8;

        cache = new LruCache<String, Bitmap>(cacheSize);
    }

    public void onClick(View view) {
        Intent intent;

        switch(view.getId()) {
            case R.id.butMoreTracks:
                intent = new Intent(this, UserRecentTracksListActivity.class);
                intent.putExtra("userName", this.userName);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /* memory cache methods */

    public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if ((getBitmapFromMemCache(key) == null) && !(bitmap == null)) {
            cache.put(key, bitmap);
        }
    }

    public static Bitmap getBitmapFromMemCache(String key) {
        return cache.get(key);
    }

    /* Loader methods */

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cl = new CursorLoader(UserProfileActivity.this, CONTENT_URI, PROJECTION, null, null, null);
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
