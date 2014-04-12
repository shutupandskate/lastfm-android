package com.example.Lastfm.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;
import com.example.Lastfm.R;
import com.example.Lastfm.tasks.GetRecentTracksTask;


/**
 * Created by ShutUpAndSkate on 12.04.14.
 */
public class UserRecentTracksListActivity extends Activity {

    Activity activity;
    GetRecentTracksTask getRecentTracksTask;
    String userName;
    Integer limit, page;
    public static Boolean loadingFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recent_tracks_list);

        final ListView lv = (ListView) findViewById(R.id.lvRecentTracks);
        activity = this;
        userName = getIntent().getStringExtra("userName");
        limit = 50;
        page = 1;

        getRecentTracksTask = new GetRecentTracksTask(userName, limit, page, this);
        getRecentTracksTask.execute();

        loadingFlag = false;
        page++;

        lv.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
                {
                    if(loadingFlag == false)
                    {
                        Log.d("log", "STARTED LOADING " + limit + "TRACKS FROM " + page + " PAGE");
                        loadingFlag = true; // changed in onPostExecute
                        GetRecentTracksTask task = new GetRecentTracksTask(userName, limit,
                                page, activity);
                        task.execute();
                        page++;
                    }
                }
            }
        });
    }
}
