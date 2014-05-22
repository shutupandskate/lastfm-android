package com.example.Lastfm.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.example.Lastfm.helpers.QueryURLHelper;
import com.example.Lastfm.provider.Contract;
import com.example.Lastfm.provider.Provider;
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
 * Created by ShutUpAndSkate on 21.05.14.
 */
public class GetUserInfoService extends IntentService {

    public GetUserInfoService() {
        super("UserInfoService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String userName = intent.getStringExtra("userName");

       // Bundle bundle = intent.getExtras();

        HttpURLConnection connection = null;
        StringBuilder sb = new StringBuilder();

        Log.d("sdfsd", "sdfsdf" + userName);
        try {
            Map<String, String> queryParams = new HashMap<String, String>();
            queryParams.put("method", "getInfo");
            queryParams.put("user", userName);
            URL url = new QueryURLHelper(queryParams).getURLFromQuery();

            connection = (HttpURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ( (line = br.readLine()) != null ) sb.append(line + "\n");
            br.close();

            Log.d("log", url.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }

        JSONObject jsonData, user;

        String realName, userPicUrl;
        Integer playCount, regTime;

        ContentValues m = new ContentValues();

        Provider pr = new Provider();


        try {
            jsonData = new JSONObject(sb.toString());
            user = (JSONObject) jsonData.get("user");

            m.put("username", userName);
            if(user.has("realname")) {
                realName = (String) user.get("realname");
                m.put("realName", realName);
            }
            if(user.has("image")) {
                userPicUrl = (String) ((JSONObject)((JSONArray) user.get("image")).get(1)).get("#text"); // 1 is smallest size
                m.put("userPicUrl", userPicUrl);
            }
            if(user.has("playcount")) {
                playCount = Integer.valueOf((String) user.get("playcount"));
                m.put("playCount", playCount);
            }
            if(user.has("registered")) {
                regTime = Integer.valueOf((String) ((JSONObject)user.get("registered")).get("unixtime"));
                m.put("regTime", regTime);
            }

            Log.d("dsaf", m.toString());

            pr.insert(Uri.parse("content://com.example.Lastfm.provider.Provider/user_info"), m);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Cursor c = getContentResolver().query(
                Uri.parse("content://com.example.Lastfm.provider.Provider/user_info"),
                new String[] {
                        Contract.UserInfoTable.REG_TIME,
                },
                null,
                null,
                null
        );

        DatabaseUtils.dumpCursorToString(c);
    }
}
