package com.example.Lastfm.activities;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import com.example.Lastfm.R;
import com.example.Lastfm.services.RecentTracksService;
import com.example.Lastfm.tasks.GetRecentTracksTask;


/**
 * Created by ShutUpAndSkate on 12.04.14.
 */
public class UserRecentTracksListActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    Activity activity;
    GetRecentTracksTask getRecentTracksTask;
    String userName;
    Integer limit, page;
    public static Boolean loadingFlag;
    SimpleCursorAdapter mAdapter;
    private static final String[] PROJECTION = {"_id", "trackTime", "trackName", "trackArtistName", "albumImageUrl"};
    private static final int LOADER_ID = 1;
    private static final Uri CONTENT_URI = Uri.parse("content://com.example.Lastfm.provider.Provider/tracks");
    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recent_tracks_list);

        userName = getIntent().getStringExtra("userName");
        limit = 10;
        page = 1;

        Intent getRecentTracksIntent = new Intent(this, RecentTracksService.class);
        startService(getRecentTracksIntent
                .putExtra("userName", userName)
                .putExtra("limit", limit)
                .putExtra("page", page)
        );

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

//        final ListView lv = (ListView) findViewById(R.id.lvRecentTracks);
//        activity = this;
//        userName = getIntent().getStringExtra("userName");
//        limit = 50;
//        page = 1;
//
//        getRecentTracksTask = new GetRecentTracksTask(userName, limit, page, this);
//        getRecentTracksTask.execute();

//        loadingFlag = false;
       // page++;

//        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
//
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
//                {
//                    if(loadingFlag == false)
//                    {
//                        Log.d("log", "STARTED LOADING " + limit + "TRACKS FROM " + page + " PAGE");
//                        loadingFlag = true; // changed in onPostExecute
//                        GetRecentTracksTask task = new GetRecentTracksTask(userName, limit,
//                                page, activity);
//                        task.execute();
//                        page++;
//                    }
//                }
//            }
//        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cl = new CursorLoader(UserRecentTracksListActivity.this, CONTENT_URI, PROJECTION, null, null, null);
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
