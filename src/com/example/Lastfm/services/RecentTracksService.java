package com.example.Lastfm.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import com.example.Lastfm.helpers.CalendarHelper;
import com.example.Lastfm.helpers.QueryURLHelper;
import com.example.Lastfm.provider.Contract;
import com.example.Lastfm.provider.Provider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static android.provider.SyncStateContract.Helpers.get;
import static android.provider.SyncStateContract.Helpers.insert;
import static java.security.AccessController.getContext;

/**
 * Created by ShutUpAndSkate on 13.04.14.
 */
public class RecentTracksService extends IntentService {

    public RecentTracksService() {
        super("RecentTracksService"); // what is it for?
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String userName = intent.getStringExtra("userName");
        Integer limit = intent.getIntExtra("limit", 50); // default number is 50
        Integer page = intent.getIntExtra("page", 1);

        HttpURLConnection connection = null;
        StringBuilder sb = new StringBuilder();

        try {
            Map<String, String> queryParams = new HashMap<String, String>();
            queryParams.put("method", "getrecenttracks");
            queryParams.put("user", userName);
            queryParams.put("limit", limit.toString());
            queryParams.put("page", page.toString());

            URL url = new QueryURLHelper(queryParams).getURLFromQuery();

            connection = (HttpURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ( (line = br.readLine()) != null ) sb.append(line + "\n");
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }

        JSONObject jsonData, track;
        JSONArray tracks;
        ContentValues[] recentTracks = new ContentValues[limit];

        Provider pr = new Provider();

        try {
            jsonData = new JSONObject(sb.toString());
            tracks = (JSONArray) ((JSONObject) jsonData.get("recenttracks")).get("track");

            for(Integer i=0; i<limit; i++) {
                ContentValues m = new ContentValues();
                track = (JSONObject) tracks.get(i);

                if(track.has("date")) {
                    String uts = (String) ((JSONObject) track.get("date")).get("uts");
                    m.put("trackTime", CalendarHelper.prettifyTrackDate(Long.parseLong(uts)));
                } else if(track.has("@attr") && ((JSONObject)track.get("@attr")).has("nowplaying")
                        && ((JSONObject)track.get("@attr")).get("nowplaying").toString().equals("true") ) {
                    m.put("trackTime", "Now");
                } else {
                    m.put("trackTime", "In future"); // happens when user has timezone problems
                }

                m.put("trackName", (String) track.get("name"));
                m.put("trackArtistName", (String) ((JSONObject) track.get("artist")).get("#text"));
                m.put("albumImageUrl", (String) ((JSONObject)(((JSONArray) track.get("image")))
                        .get(1)).get("#text")); // "1" stands for img of medium size

                recentTracks[i] = m;

                pr.insert(Uri.parse("content://com.example.Lastfm.provider.Provider/tracks"), m);

            }

        } catch (JSONException e) {
            e.printStackTrace();
            //return null;
        }



        Cursor c = getContentResolver().query(
                Uri.parse("content://com.example.Lastfm.provider.Provider/tracks"),
                new String[] {
                        Contract.TrackTable.TRACK_TIME
                },
                null,
                null,
                null
        );

        Log.d("cursor", "cursor " + c);

    }
}
