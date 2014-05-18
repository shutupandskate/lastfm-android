package com.example.Lastfm.tasks;

/**
 * Created by ShutUpAndSkate on 01.04.14.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.Lastfm.activities.UserProfileActivity.addBitmapToMemoryCache;


/**
 * Created by ShutUpAndSkate on 01.04.14.
 */

public class ImageLoaderTask extends AsyncTask<String, Void, Bitmap> {

    public static String url;

    public ImageLoaderTask(String imageUrl) {
        this.url = imageUrl;
    }

    public static Bitmap getBitmapFromUrl(String source) {
        try {
            URL url = new URL(source);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);

            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap imageBitmap = getBitmapFromUrl(url);
        addBitmapToMemoryCache(url, imageBitmap);
        return imageBitmap;
    }
}

