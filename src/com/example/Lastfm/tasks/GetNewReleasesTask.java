package com.example.Lastfm.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.GridView;
import com.example.Lastfm.R;
import com.example.Lastfm.helpers.QueryURLHelper;
import com.example.Lastfm.lists.NewReleasesAdapter;
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
 * Created by Lena on 22.05.14.
 */
public class GetNewReleasesTask  extends AsyncTask<Map[], Void, Map[]> {

    private static Integer LIMIT, PAGE;
    private Activity activity;
    private URL url;
    private String username;


    public GetNewReleasesTask(Integer lim, Integer page, String username, Activity activity) {
        this.LIMIT = lim;
        this.PAGE = page;
        this.activity = activity;
        this.username = username;
    }

    @Override
    protected Map[] doInBackground(Map[]... params) {
        HttpURLConnection connection = null;
        StringBuilder sb = new StringBuilder();

        try {
            Map<String, String> queryParams = new HashMap<String, String>();
            queryParams.put("method", "user.getnewreleases");
            queryParams.put("limit", LIMIT.toString());
            queryParams.put("page", PAGE.toString());
            queryParams.put("user", username);

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

        JSONObject jsonData, album;
        JSONArray albumsjson;
        Map[] albums = new Map[LIMIT];

        try {
            jsonData = new JSONObject(sb.toString());
            albumsjson = (JSONArray) ((JSONObject) jsonData.get("albums")).get("album");

            for(Integer i=0; i<LIMIT; i++) {
                Map<String, String> m = new HashMap<String, String>();
                album = (JSONObject) albumsjson.get(i);

                m.put("album", (String) album.get("name"));
                m.put("image", (String) ((JSONObject)(((JSONArray) album.get("image")))
                        .get(3)).get("#text"));

                JSONObject artist = (JSONObject) album.get("artist");
                m.put("artist", (String) artist.get("name"));

                albums[i] = m;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return albums;
    }

    @Override
    protected void onPostExecute(Map[] data) {
        super.onPostExecute(data);
        GridView artists = (GridView) activity.findViewById(R.id.lvLatestReleases);

       NewReleasesAdapter adapter = new NewReleasesAdapter(activity, data);
        artists.setAdapter(adapter);
        artists.setNumColumns(2);
        artists.setHorizontalSpacing(5);
        artists.setVerticalSpacing(5);

        artists.setAdapter(adapter);

        //UserRecentTracksListActivity.loadingFlag = false;
    }
}
