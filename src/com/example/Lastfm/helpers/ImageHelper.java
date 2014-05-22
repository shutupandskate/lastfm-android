package com.example.Lastfm.helpers;

import android.content.Context;
import android.graphics.*;
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
                e.printStackTrace();
                return null;
            }
        }
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
