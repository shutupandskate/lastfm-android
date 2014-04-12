package com.example.Lastfm.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.ListView;
import com.example.Lastfm.R;
import com.example.Lastfm.tasks.GetRecentTracksTask;

/**
 * Created by ShutUpAndSkate on 12.04.14.
 */
public class UserRecentTracksListActivity extends Activity {

    GetRecentTracksTask getRecentTracksTask;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recent_tracks_list);

        activity  = this;
        ListView lv = (ListView) findViewById(R.id.lvRecentTracks);

        getRecentTracksTask = new GetRecentTracksTask("ShutUpAndSkate", "json", 12, this);
        getRecentTracksTask.execute();

        

        lv.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
                {
                    if(getRecentTracksTask.loadingFlag == false)
                    {
                        getRecentTracksTask.loadingFlag = true;
                        GetRecentTracksTask task = new GetRecentTracksTask("ShutUpAndSkate", "json", 12, activity);
                        task.execute();
                        task.loadingFlag = false;
                    }
                }
            }
        });
    }
}
