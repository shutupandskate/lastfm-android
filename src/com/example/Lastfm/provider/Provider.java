package com.example.Lastfm.provider;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import static com.example.Lastfm.provider.Contract.*;

/**
 * Created by ShutUpAndSkate on 13.04.14.
 */
public class Provider extends ContentProvider {

    static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static UserTracksDbHelper userTracksDbHelper;
    static SQLiteDatabase db;
    //static Context context;

    static final int USERS = 1;
    static final int TRACKS = 2;

    static final int USER_INFO = 3;


    @Override
    public boolean onCreate() {
        userTracksDbHelper = new UserTracksDbHelper(getContext());

        uriMatcher.addURI(AUTHORITY, UserTable.TABLE_NAME, USERS);
        uriMatcher.addURI(AUTHORITY, TrackTable.TABLE_NAME, TRACKS);
        uriMatcher.addURI(AUTHORITY, UserInfoTable.TABLE_NAME, USER_INFO);

        //context = getContext();

        return true; // what is it for?
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        db = userTracksDbHelper.getReadableDatabase();
        Cursor c;

        switch (uriMatcher.match(uri)) {
            case TRACKS:
                c = db.query(
                        TrackTable.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        TrackTable.TRACK_TIME + " DESC"
                );
                return c;
            case USER_INFO:
                c = db.query(
                        UserInfoTable.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                return c;
            default:
                return null;
        }
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d("uri", String.valueOf(uri));
        Log.d("switch", String.valueOf(uriMatcher.match(uri)));
        switch (uriMatcher.match(uri)) {
            case TRACKS:
                userTracksDbHelper.getWritableDatabase().insert(TrackTable.TABLE_NAME,
                        null, values);
            case USER_INFO:
                Log.d("log", "insert");
                userTracksDbHelper.getWritableDatabase().insert(UserInfoTable.TABLE_NAME,
                        null, values);

            default:
                return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
