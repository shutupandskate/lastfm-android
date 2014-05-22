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
import com.example.Lastfm.tasks.ImageLoaderTask;

import java.util.Map;

/**
 * Created by Lena on 21.05.14.
 */
public class RecommendedArtistsAdapter extends BaseAdapter {
    Context context;
    LayoutInflater Inflater;
    Map[] data;

    public RecommendedArtistsAdapter(Context context, Map[] data) {
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
        RecommendedArtistsViewHolder holder;
        try {
            if(convertView == null) {
                convertView = Inflater.from(context).inflate(R.layout.recommended_music_item, parent, false);
                holder = new RecommendedArtistsViewHolder();
                holder.artistName = ((TextView) convertView.findViewById(R.id.recommendedMusic_artistname));
                holder.same = ((TextView) convertView.findViewById(R.id.recommendedMusic_same));
                holder.image = ((ImageView) convertView.findViewById(R.id.recommendedMusic_logo));

                convertView.setTag(holder);
            } else {
                holder = (RecommendedArtistsViewHolder) convertView.getTag();
            }

            holder.artistName
                    .setText(data[position].get("artistName").toString());
            holder.same
                    .setText("Similar to " + data[position].get("same1").toString()
                    + " and " + data[position].get("same2").toString());


            imageUrl = data[position].get("image").toString();


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
