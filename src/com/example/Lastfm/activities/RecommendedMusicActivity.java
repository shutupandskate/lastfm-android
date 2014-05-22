package com.example.Lastfm.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.LruCache;
import android.view.View;
import android.widget.Button;
import com.example.Lastfm.R;
import com.example.Lastfm.tasks.GetRecommendedArtistsTask;

/**
 * Created by Lena on 22.05.14.
 */
public class RecommendedMusicActivity extends Activity  implements View.OnClickListener {
    private static LruCache<String, Bitmap> cache;

    GetRecommendedArtistsTask getRecommendedArtistsTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recommended_music);

        Button moreArtists = (Button) findViewById(R.id.moreArtists);
        moreArtists.setOnClickListener(this);

        /* memory cache */
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024); // all available memory. stored in Kb
        final int cacheSize = maxMemory / 8;

        cache = new LruCache<String, Bitmap>(cacheSize);
        getRecommendedArtistsTask = new GetRecommendedArtistsTask(10, 1, this);
        getRecommendedArtistsTask.execute();

    }

    public void onClick(View view) {
        Intent intent;

        switch(view.getId()) {
            case R.id.toRecommendedMusic:
                intent = new Intent(this, MainPageActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}

