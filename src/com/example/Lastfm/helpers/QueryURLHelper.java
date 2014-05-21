package com.example.Lastfm.helpers;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Created by ShutUpAndSkate on 12.04.14.
 */
public class QueryURLHelper {

    URL url;
  //  private static final String API_KEY = "4ee413fa8853b3c7d4d06fa6d9809e45";
    private static final String API_KEY = "145051ba210cb5029251cd638f906183";
    private static final String SECRET_KEY = "29cf0a931aeddcc4f2e42f5a6ad1c68b";
    private static final String SESSION = "a992c1fe8450f07c3b53e513248ba023";

    private static final String FORMAT = "json";

    public QueryURLHelper(Map<String, String> map) throws MalformedURLException {

        String query = "http://ws.audioscrobbler.com/2.0/?";

        if(map.get("method").equals ("getrecenttracks")) {
            query += "method=user.getrecenttracks";
            query += "&user="+ map.get("user");
            query += "&api_key=" + API_KEY;
            query += "&format=" + FORMAT;
            query += "&limit=" + map.get("limit");
            if(!map.get("page").isEmpty()) query += "&page=" + map.get("page");
        }

        if(map.get("method").equals ("getrecommendedartists")) {
            query += "method=user.getRecommendedArtists";

            query += "&api_key=" + API_KEY;
            query += "&format=" + FORMAT;
            query += "&limit=" + map.get("limit");
            query += "&sk=" + SESSION;
        //    if(!map.get("page").isEmpty()) query += "&page=" + map.get("page");
            StringBuilder signature = new StringBuilder();
            signature.append("api_key");
            signature.append(API_KEY);
            signature.append("limit");
            signature.append(map.get("limit"));
            signature.append("methoduser.getRecommendedArtists");
            signature.append("sk");
            signature.append(SESSION);
            signature.append(SECRET_KEY);
            query += "&api_sig=";
            query += getMD5(signature.toString());

        }


        url = new URL(query);
    }

    public URL getURLFromQuery() {
        return url;
    }

    public String getMD5(String string) {
        StringBuilder hexString = new StringBuilder();
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hash = new byte[0];
        try {
            hash = md.digest(string.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < hash.length; i++) {
            if ((0xff & hash[i]) < 0x10) {
                hexString.append("0"
                        + Integer.toHexString((0xFF & hash[i])));
            } else {
                hexString.append(Integer.toHexString(0xFF & hash[i]));
            }
        }
        return hexString.toString();

    }

}
