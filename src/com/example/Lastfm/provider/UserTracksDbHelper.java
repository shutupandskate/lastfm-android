package com.example.Lastfm.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.Lastfm.provider.Contract;

/**
 * Created by ShutUpAndSkate on 13.04.14.
 */

public class UserTracksDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "UserTracks.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ", ";

    private static final String SQL_CREATE_USER_TABLE =
            "CREATE TABLE " + Contract.UserTable.TABLE_NAME + " ( " +
                    Contract.UserTable.ID  + " INT PRIMARY KEY, " +
                    Contract.UserTable.USER_NAME + TEXT_TYPE +
                    " );";

    private static final String SQL_CREATE_USER_INFO_TABLE =
            "CREATE TABLE " + Contract.UserInfoTable.TABLE_NAME + " ( " +
                    Contract.UserInfoTable.ID + " INT PRIMARY KEY, " +
                    Contract.UserInfoTable.USER_ID + TEXT_TYPE + COMMA_SEP +
                    Contract.UserInfoTable.USER_NAME + TEXT_TYPE + COMMA_SEP +
                    Contract.UserInfoTable.REAL_NAME + TEXT_TYPE + COMMA_SEP +
                    Contract.UserInfoTable.USER_PIC_URL + TEXT_TYPE + COMMA_SEP +
                    Contract.UserInfoTable.PLAY_COUNT + TEXT_TYPE + COMMA_SEP +
                    Contract.UserInfoTable.REG_TIME + TEXT_TYPE +
                    " );";

    private static final String SQL_CREATE_TRACK_TABLE =
            "CREATE TABLE " + Contract.TrackTable.TABLE_NAME + " ( " +
                    Contract.TrackTable.ID + " INT PRIMARY KEY, " +
                    Contract.TrackTable.USER_ID + TEXT_TYPE + COMMA_SEP +
                    Contract.TrackTable.TRACK_NAME + TEXT_TYPE + COMMA_SEP +
                    Contract.TrackTable.TRACK_TIME + TEXT_TYPE + COMMA_SEP +
                    Contract.TrackTable.TRACK_ARTIST + TEXT_TYPE + COMMA_SEP +
                    Contract.TrackTable.TRACK_IMG_URL + TEXT_TYPE +
                    " );";

    private static final String SQL_DELETE_USER_TABLE =
            "DROP TABLE IF EXISTS " + Contract.UserTable.TABLE_NAME + ";";

    private static final String SQL_DELETE_TRACK_TABLE =
            "DROP TABLE IF EXISTS " + Contract.TrackTable.TABLE_NAME + ";";

    private static final String SQL_DELETE_USER_INFO_TABLE =
            "DROP TABLE IF EXISTS " + Contract.UserInfoTable.TABLE_NAME + ";";

    public UserTracksDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USER_TABLE);
        db.execSQL(SQL_CREATE_TRACK_TABLE);
        db.execSQL(SQL_CREATE_USER_INFO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_USER_TABLE);
        db.execSQL(SQL_DELETE_TRACK_TABLE);
        db.execSQL(SQL_DELETE_USER_INFO_TABLE);
        onCreate(db);
    }
}