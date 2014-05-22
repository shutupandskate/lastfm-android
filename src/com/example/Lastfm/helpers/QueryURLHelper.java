package com.example.Lastfm.helpers;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by ShutUpAndSkate on 12.04.14.
 */
public class QueryURLHelper {

    URL url;
    private static final String API_KEY = "4ee413fa8853b3c7d4d06fa6d9809e45";
    //private static final String API_KEY = "4ee413fa8853b3c7d4d45"; // lame key
    private static final String FORMAT = "json";

    public QueryURLHelper(Map<String, String> map) throws MalformedURLException {

        String query = "http://ws.audioscrobbler.com/2.0/?";

        Log.d("log", map.get("method"));

        if(map.get("method").equals("getrecenttracks")) {
            query += "method=user.getrecenttracks";
            query += "&user="+ map.get("user");
            query += "&api_key=" + API_KEY;
            query += "&format=" + FORMAT;
            query += "&limit=" + map.get("limit");
            if(!map.get("page").isEmpty()) query += "&page=" + map.get("page");
        } else if(map.get("method").equals("getInfo")) {
            Log.d("log", "sdfsdf");
            query += "method=user.getinfo";
            query += "&user=" + map.get("user");
            query += "&api_key=" + API_KEY;
            query += "&format=" + FORMAT;
        }

        url = new URL(query);
    }

    public URL getURLFromQuery() {
        return url;
    }
}
