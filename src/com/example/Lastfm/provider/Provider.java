package com.example.Lastfm.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import com.example.Lastfm.dbHelpers.UserTracksDbHelper;

import static com.example.Lastfm.provider.Contract.*;

/**
 * Created by ShutUpAndSkate on 13.04.14.
 */
public class Provider extends ContentProvider {

    static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static UserTracksDbHelper userTracksDbHelper;
    static SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        userTracksDbHelper = new UserTracksDbHelper(getContext());

        uriMatcher.addURI(AUTHORITY, UserTable.TABLE_NAME, 1);
        uriMatcher.addURI(AUTHORITY, TrackTable.TABLE_NAME, 2);

        return true; // what is it for?
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        db = userTracksDbHelper.getReadableDatabase();
        Cursor c;

        switch (uriMatcher.match(uri)) {
            case 2:
                c = db.query(
                        TrackTable.TABLE_NAME,
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
        switch (uriMatcher.match(uri)) {
            case 2:
                userTracksDbHelper.getWritableDatabase().insert(TrackTable.TABLE_NAME,
                        null, values);

                Log.d("log", getContext().toString());

                getContext()
                        .getContentResolver()
                        .notifyChange(uri.parse(Contract.AUTHORITY_URI), null);
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
