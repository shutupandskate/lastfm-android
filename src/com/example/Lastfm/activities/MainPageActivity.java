package com.example.Lastfm.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import com.example.Lastfm.R;
import com.example.Lastfm.helpers.QueryURLHelper;
import com.example.Lastfm.lists.RecentTracksListAdapter;
import com.example.Lastfm.lists.RecommendedArtistsAdapter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lena on 21.05.14.
 */
public class MainPageActivity extends Activity {
    String token = "bdbc7e7a4ce582e6f03ffd3b1dc04fa5";
    Map[] data = new Map[3];
    GridView artists;
    RecommendedArtistsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_page);
        artists = (GridView) findViewById(R.id.lvRecommendedMusic);
        data[0] = new HashMap<String, String>();
        data[1] = new HashMap<String, String>();
        data[2] = new HashMap<String, String>();
        data[0].put("artistName", "Nightwish");
        data[0].put("same1", "Ava");
        data[0].put("same2", "unique!");
        data[1].put("artistName", "Nightwish2");
        data[1].put("same1", "Ava2");
        data[1].put("same2", "uniqu2e!");
        data[2].put("artistName", "Nightwish3");
        data[2].put("same1", "Ava3");
        data[2].put("same2", "unique3!");
        adapter = new RecommendedArtistsAdapter(this, data);
        artists.setAdapter(adapter);
        artists.setNumColumns(2);

        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("method", "getrecommendedartists");

        queryParams.put("limit", "10");


        try {
            URL url = new QueryURLHelper(queryParams).getURLFromQuery();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
