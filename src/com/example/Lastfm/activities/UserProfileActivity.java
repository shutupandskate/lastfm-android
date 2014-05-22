package com.example.Lastfm.activities;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.*;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.*;
import com.example.Lastfm.R;
import com.example.Lastfm.helpers.CalendarHelper;
import com.example.Lastfm.helpers.ImageHelper;
import com.example.Lastfm.lists.RecentTracksListAdapter;
import com.example.Lastfm.services.GetUserInfoService;
import com.example.Lastfm.services.RecentTracksService;
import com.example.Lastfm.tasks.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static java.security.AccessController.getContext;

public class UserProfileActivity extends Activity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    /**
     * Called when the activity is first created.
     */

    GetRecentTracksTask getRecentTracksTask;

    String userName;
    Integer limit, page;
    TextView userInfoRealName, userInfoPlaysSince;
    ImageView userInfoPic;

    private static final int LOADER_ID = 1;
    private final static Uri CONTENT_URI = Uri.parse("content://com.example.Lastfm.provider.Provider/user_info");
    private static final String[] PROJECTION = {"_id", "realName", "userPicUrl", "playCount", "regTime"};
    public static LruCache<String, Bitmap> imageCache;
    public static LruCache<String, Map<String, String>> trackCache; // id -- map with data
    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        /* memory cache */
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024); // all available memory. stored in Kb
        final int cacheSize = maxMemory / 8;
        imageCache = new LruCache<String, Bitmap>(cacheSize);

        this.userName = "mortidovs";
        this.limit = 3;
        this.page = 1; // latest tracks

        Button moreTracks = (Button) findViewById(R.id.butMoreTracks);
        moreTracks.setOnClickListener(this);

        getRecentTracksTask = new GetRecentTracksTask(userName, limit, page, this);
        AsyncTask<Map[],Void,Map[]> tr = getRecentTracksTask.execute();

        try {
            Map[] taskResult = tr.get();
            if(taskResult == null) Toast.makeText(this, "Unable to get tracks. No connection.", Toast.LENGTH_LONG).show();
        } catch (InterruptedException e) { e.printStackTrace(); }
        catch (ExecutionException e) { e.printStackTrace(); }


//        Handler handler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                Bundle reply = msg.getData();
//                // do whatever with the bundle here
//            }
//        };

        Intent getUserInfoIntent = new Intent(this, GetUserInfoService.class);
        startService(getUserInfoIntent
                .putExtra("userName", userName)
                //.putExtra("messenger", new Messenger(handler))
        );

        userInfoRealName = (TextView) findViewById(R.id.userInfoRealName);
        userInfoPlaysSince = (TextView) findViewById(R.id.userInfoPlaysSince);
        userInfoPic = (ImageView) findViewById(R.id.userInfoPic);

        mCallbacks = this;
        LoaderManager lm = getLoaderManager();
        lm.initLoader(LOADER_ID, null, mCallbacks);

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
            imageCache.put(key, bitmap);
        }
    }

    public static Bitmap getBitmapFromMemCache(String key) {
        return imageCache.get(key);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cl = new CursorLoader(UserProfileActivity.this, CONTENT_URI, PROJECTION, null, null, null);
        Log.d("log", cl.toString());
        return cl;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        String realName = data.getString(data.getColumnIndex("realName"));

        long regTime = Integer.valueOf(data.getString(data.getColumnIndex("regTime")));
        Date date = new Date(regTime*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = sdf.format(date);

        String playCount = data.getString(data.getColumnIndex("playCount"));

        String countSince = playCount + " plays since " + formattedDate;

        String userPicUrl = data.getString(data.getColumnIndex("userPicUrl"));
        Bitmap userPicBitmap = ImageHelper.loadBitmap(userPicUrl);

        Bitmap circlePic = ImageHelper.getRoundedCornerBitmap(userPicBitmap, 50);

        data.close();

        switch (loader.getId()) {
            case LOADER_ID:
                userInfoRealName.setText(realName);
                userInfoPlaysSince.setText(countSince);
                userInfoPic.setImageBitmap(circlePic);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
