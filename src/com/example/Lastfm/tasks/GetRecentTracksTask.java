package com.example.Lastfm.tasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;
import com.example.Lastfm.R;
import com.example.Lastfm.activities.UserProfileActivity;
import com.example.Lastfm.helpers.CalendarHelper;
import com.example.Lastfm.lists.RecentTracksListAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ShutUpAndSkate on 12.04.14.
 */
public class GetRecentTracksTask extends AsyncTask<Map[], Void, Map[]> {

    private static String USER;
    private static String FORMAT;
    private static Integer LIMIT;
    private final static String API_KEY = "4ee413fa8853b3c7d4d06fa6d9809e45";
    private Activity activity;

    public static Boolean loadingFlag = false;

    public GetRecentTracksTask(String user, String format, Integer lim, Activity activity) {
        this.USER = user;
        this.FORMAT = format;
        this.LIMIT = lim;
        this.activity = activity;
    }

    @Override
    protected Map[] doInBackground(Map[]... params) {
        HttpURLConnection connection = null;
        StringBuilder sb = new StringBuilder();

        try {
            URL url = new URL("http://ws.audioscrobbler.com/2.0/?" +
                    "method=user.getrecenttracks" +
                    "&user="+ USER + "&api_key=" + API_KEY +
                    "&format=" + FORMAT + "&limit=" + LIMIT);

            connection = (HttpURLConnection)url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ( (line = br.readLine()) != null ) sb.append(line + "\n");
            br.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }

        JSONObject jsonData, track;
        JSONArray tracks;
        Map[] recentTracks = new Map[LIMIT];

        try {
            jsonData = new JSONObject(sb.toString());
            tracks = (JSONArray) ((JSONObject) jsonData.get("recenttracks")).get("track");

            for(Integer i=0; i<LIMIT; i++) {
                Map<String, String> m = new HashMap<String, String>();
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
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recentTracks;
    }

    @Override
    protected void onPostExecute(Map[] data) {
        super.onPostExecute(data);


        ListView lv = (ListView) activity.findViewById(R.id.lvRecentTracks);
        RecentTracksListAdapter adapter = new RecentTracksListAdapter(activity, data);

        lv.setAdapter(adapter);

    }

}