package com.example.Lastfm.lists;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.Lastfm.R;
import com.example.Lastfm.helpers.RecentTracksViewHolder;
import com.example.Lastfm.tasks.ImageLoaderTask;

import java.util.Map;

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
        String imageUrl = "";
        RecentTracksViewHolder holder;
        try {
            if(convertView == null) {
                convertView = Inflater.from(context).inflate(R.layout.recent_tracks_list_item, parent, false);
                holder = new RecentTracksViewHolder();
                holder.trackName = ((TextView) convertView.findViewById(R.id.lvRecentTracksName));
                holder.trackArtistName = ((TextView) convertView.findViewById(R.id.lvRecentTracksArtist));
                holder.trackTime = ((TextView) convertView.findViewById(R.id.lvRecentTracksTime));
                holder.image = ((ImageView) convertView.findViewById(R.id.lvRecentTracksImage));

                convertView.setTag(holder);
            } else {
                holder = (RecentTracksViewHolder) convertView.getTag();
            }

            holder.trackName
                    .setText(data[position].get("trackName").toString());
            holder.trackArtistName
                    .setText(data[position].get("trackArtistName").toString());
            holder.trackTime
                    .setText(data[position].get("trackTime").toString());

            imageUrl = data[position].get("albumImageUrl").toString();


            if(imageUrl.isEmpty()) {
                holder.image.setImageResource(R.drawable.ic_default_album);
            } else {
                try {
                    Bitmap bm = (new ImageLoaderTask(imageUrl)).execute().get();
                    holder.image.setImageBitmap(bm);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
