package com.example.Lastfm.helpers;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ShutUpAndSkate on 15.04.14.
 */
public class JSONHelper {

    public static String checkForServerErrors(JSONObject object) {
        try {
            Object error = object.get("error");
            Object message = object.get("message");

            if(error!=null && message!=null) {
                // выводить message
                Log.d("log", message.toString());
            }
            return message.toString();
        } catch (JSONException e) {
            return null;
        }
    }

}
