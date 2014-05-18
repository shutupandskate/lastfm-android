package com.example.Lastfm.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;
import com.example.Lastfm.R;
import com.example.Lastfm.tasks.ImageLoaderTask;

import java.util.concurrent.ExecutionException;

import static com.example.Lastfm.activities.UserProfileActivity.getBitmapFromMemCache;


/**
 * Created by ShutUpAndSkate on 18.05.14.
 */
public class ImageHelper {

    public static Bitmap loadBitmap(String url) {
        Bitmap bitmap = getBitmapFromMemCache(url);

        if (bitmap != null) {
            return bitmap;
        } else {

            ImageLoaderTask task = new ImageLoaderTask(url);
            try {
                Bitmap bm = task.execute().get();
                return bm;
            } catch (Exception e) {
                //e.printStackTrace();
                return null;
            }
        }
    }
}
