package com.example.Lastfm.lists;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.Lastfm.R;
import com.example.Lastfm.helpers.ImageLoaderTask;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by ShutUpAndSkate on 30.03.14.
 */
public class RecentTracksListAdapter extends BaseAdapter {
    Context context;
    LayoutInflater Inflater;
    Map[] data;

    public RecentTracksListAdapter(Context context, Map[] data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null )
            convertView = Inflater.from(context).inflate(R.layout.recent_tracks_list_item, parent, false);

        ((TextView) convertView.findViewById(R.id.lvRecentTracksName))
                .setText(data[position].get("trackName").toString());
        ((TextView) convertView.findViewById(R.id.lvRecentTracksArtist))
                .setText(data[position].get("trackArtistName").toString());
        ((TextView) convertView.findViewById(R.id.lvRecentTracksTime))
                .setText(data[position].get("trackTime").toString());

        String imageUrl = data[position].get("albumImageUrl").toString();

        ImageView image = ((ImageView) convertView.findViewById(R.id.lvRecentTracksImage));
        if(imageUrl.isEmpty()) {
            image.setImageResource(R.drawable.ic_default_album);
        } else {
            try {
                Bitmap bm = (new ImageLoaderTask(imageUrl)).execute().get();
                image.setImageBitmap(bm);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return convertView;
    }
}
