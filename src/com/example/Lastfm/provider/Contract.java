package com.example.Lastfm.provider;

/**
 * Created by ShutUpAndSkate on 13.04.14.
 */
public class Contract {
    public static final String AUTHORITY = "com.example.Lastfm.provider.Provider";
    public static final String AUTHORITY_URI = "content://" + AUTHORITY;

    public static class UserTable {
        public static final String TABLE_NAME = "users";
        public static final String ID = "_id"; // pk

        public static final String USER_NAME = "user_name";  // типа ShutUpAndSkate
    }

    public static abstract class UserInfoTable {
        public static final String TABLE_NAME = "user_info";
        public static final String ID = "_id";

        public static final String USER_ID = "user_id"; //fk в UserTable
        public static final String USER_NAME = "username";
        public static final String REAL_NAME = "realName";
        public static final String USER_PIC_URL = "userPicUrl";
        public static final String PLAY_COUNT = "playCount";
        public static final String REG_TIME = "regTime";
    }

    public static abstract class TrackTable {
        public static final String TABLE_NAME = "tracks";
        public static final String ID = "_id";
        public static final String USER_ID = "user_id"; // fk в UserTable

        public static final String TRACK_NAME = "trackName";
        public static final String TRACK_TIME = "trackTime";
        public static final String TRACK_ARTIST = "trackArtistName";
        public static final String TRACK_IMG_URL = "albumImageUrl";
    }

}
