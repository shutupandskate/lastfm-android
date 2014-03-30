package com.example.Lastfm.lists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.Lastfm.R;
import java.util.Map;

/**
 * Created by ShutUpAndSkate on 30.03.14.
 */
public class RecentTracksListAdapter extends BaseAdapter{
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

        return convertView;
    }
}
