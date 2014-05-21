package com.example.Lastfm.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.LruCache;
import com.example.Lastfm.R;
import com.example.Lastfm.tasks.GetRecommendedArtistsTask;

/**
 * Created by Lena on 21.05.14.
 */
public class MainPageActivity extends Activity {
    private static LruCache<String, Bitmap> cache;

    GetRecommendedArtistsTask getRecommendedArtistsTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_page);

        /* memory cache */
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024); // all available memory. stored in Kb
        final int cacheSize = maxMemory / 8;

        cache = new LruCache<String, Bitmap>(cacheSize);
        getRecommendedArtistsTask = new GetRecommendedArtistsTask(4, 1, this);
        getRecommendedArtistsTask.execute();

    }
}
