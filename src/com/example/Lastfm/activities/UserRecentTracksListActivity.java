package com.example.Lastfm.activities;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.*;
import com.example.Lastfm.R;
import com.example.Lastfm.helpers.CalendarHelper;
import com.example.Lastfm.services.RecentTracksService;
import com.example.Lastfm.tasks.ImageLoaderTask;


/**
 * Created by ShutUpAndSkate on 12.04.14.
 */
public class UserRecentTracksListActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    String userName;
    Integer limit, page;
    SimpleCursorAdapter mAdapter;
    private static final String[] PROJECTION = {"_id", "trackTime", "trackName", "trackArtistName", "albumImageUrl"};
    private static final int LOADER_ID = 1;
    private static final Uri CONTENT_URI = Uri.parse("content://com.example.Lastfm.provider.Provider/tracks");
    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;
    private static LruCache<String, Bitmap> mMemoryCache;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recent_tracks_list);

        /* memory cache */
//        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024); // all available memory. stored in Kb
//        final int cacheSize = maxMemory / 8;
//
//        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
//            @Override
//            protected int sizeOf(String key, Bitmap value) {
//                return value.getByteCount() / 1024;
//            }
//        };


        /* intent service */

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
                "trackArtistName", "albumImageUrl" };
        int[] viewIDs = {R.id.lvRecentTracksTime, R.id.lvRecentTracksName,
                R.id.lvRecentTracksArtist, R.id.lvRecentTracksImage };

        mAdapter = new SimpleCursorAdapter(this, R.layout.recent_tracks_list_item, null, dataColumns, viewIDs, 0);
        mAdapter.setViewBinder(new TracksViewBinder()); // overrides adapter view

        ListView lv = (ListView) findViewById(R.id.lvRecentTracks);
        lv.setAdapter(mAdapter);

        mCallbacks = this;
        LoaderManager lm = getLoaderManager();
        lm.initLoader(LOADER_ID, null, mCallbacks);
    }


//    public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
//        if (getBitmapFromMemCache(key) == null) {
//            Log.d("log", "add");
//            mMemoryCache.put(key, bitmap);
//        }
//    }
//
//    public static Bitmap getBitmapFromMemCache(String key) {
//        Log.d("key", key);
//        Log.d("log", "ffffff ");
//        Log.d("cache", mMemoryCache.toString());
//        return mMemoryCache.get(key);
//    }


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

    public class TracksViewBinder implements SimpleCursorAdapter.ViewBinder {
        @Override
        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
            switch (view.getId()) {
                case R.id.lvRecentTracksTime:
                    Long time = cursor.getLong(columnIndex);
                    TextView v = (TextView) view;
                    v.setText(CalendarHelper.prettifyTrackDate(time));
                    return true;
                case R.id.lvRecentTracksImage:
                    String url = cursor.getString(columnIndex);
                    ImageView iv = (ImageView) view;
                    try {
                        Bitmap bm = (new ImageLoaderTask(url)).execute().get();
                        iv.setImageBitmap(bm);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
            }
            return false;
        }
    }

}
