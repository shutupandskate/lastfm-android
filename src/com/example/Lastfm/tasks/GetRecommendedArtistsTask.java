package com.example.Lastfm.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.GridView;
import android.widget.ListView;
import com.example.Lastfm.R;
import com.example.Lastfm.helpers.CalendarHelper;
import com.example.Lastfm.helpers.QueryURLHelper;
import com.example.Lastfm.lists.RecentTracksListAdapter;
import com.example.Lastfm.lists.RecommendedArtistsAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lena on 21.05.14.
 */
public class GetRecommendedArtistsTask extends AsyncTask<Map[], Void, Map[]>{

    private static Integer LIMIT, PAGE;
    private Activity activity;
    private URL url;


    public GetRecommendedArtistsTask(Integer lim, Integer page, Activity activity) {
        this.LIMIT = lim;
        this.PAGE = page;
        this.activity = activity;
    }

    @Override
    protected Map[] doInBackground(Map[]... params) {
        HttpURLConnection connection = null;
        StringBuilder sb = new StringBuilder();

        try {
            Map<String, String> queryParams = new HashMap<String, String>();
            queryParams.put("method", "getrecommendedartists");
            queryParams.put("limit", LIMIT.toString());
            queryParams.put("page", PAGE.toString());

            url = new QueryURLHelper(queryParams).getURLFromQuery();

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

        JSONObject jsonData, artist;
        JSONArray artistsJson;
        Map[] artists = new Map[LIMIT];

        try {
            jsonData = new JSONObject(sb.toString());
            artistsJson = (JSONArray) ((JSONObject) jsonData.get("recommendations")).get("artist");

            for(Integer i=0; i<LIMIT; i++) {
                Map<String, String> m = new HashMap<String, String>();
                artist = (JSONObject) artistsJson.get(i);

                m.put("artistName", (String) artist.get("name"));
                m.put("image", (String) ((JSONObject)(((JSONArray) artist.get("image")))
                        .get(4)).get("#text"));
                JSONArray similars =  (JSONArray)((JSONObject) artist.get("context")).get("artist");
                artist = (JSONObject) similars.get(0);
                m.put("same1", (String) artist.get("name"));
                artist = (JSONObject) similars.get(1);
                m.put("same2", (String) artist.get("name"));




                artists[i] = m;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return artists;
    }

    @Override
    protected void onPostExecute(Map[] data) {
        super.onPostExecute(data);
        GridView artists = (GridView) activity.findViewById(R.id.lvRecommendedMusic);

        RecommendedArtistsAdapter adapter = new RecommendedArtistsAdapter(activity, data);
        artists.setAdapter(adapter);
        artists.setNumColumns(2);
        artists.setHorizontalSpacing(5);
        artists.setVerticalSpacing(5);

        artists.setAdapter(adapter);

        //UserRecentTracksListActivity.loadingFlag = false;
    }
}
