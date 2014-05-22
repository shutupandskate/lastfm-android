package com.example.Lastfm.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.LruCache;
import android.view.View;
import android.widget.Button;
import com.example.Lastfm.R;
import com.example.Lastfm.tasks.GetNewReleasesTask;
import com.example.Lastfm.tasks.GetRecommendedArtistsTask;

/**
 * Created by Lena on 21.05.14.
 */
public class MainPageActivity extends Activity  implements View.OnClickListener {
    private static LruCache<String, Bitmap> cache;

    GetRecommendedArtistsTask getRecommendedArtistsTask;
    GetNewReleasesTask getNewReleasesTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_page);
        Button moreArtists = (Button) findViewById(R.id.moreArtists);
        moreArtists.setOnClickListener(this);

        /* memory cache */
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024); // all available memory. stored in Kb
        final int cacheSize = maxMemory / 8;

        cache = new LruCache<String, Bitmap>(cacheSize);
        getRecommendedArtistsTask = new GetRecommendedArtistsTask(4, 1, this);
        getRecommendedArtistsTask.execute();
        getNewReleasesTask = new GetNewReleasesTask(2, 1, "ShutUpAndSkate", this);
        getNewReleasesTask.execute();

    }

    public void onClick(View view) {
        Intent intent;

        switch(view.getId()) {
            case R.id.moreArtists:
                intent = new Intent(this, UserProfileActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
