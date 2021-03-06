package com.codechris.friends;

import android.net.Uri;
import android.provider.BaseColumns;



/**
 * Created by Christopher on 12/17/2015.
 */
public class FriendsContract{
interface FriendsColumns {
    String FRIENDS_ID = "_id";
    String FRIENDS_NAME = "friends_name";
    String FRIENDS_EMAIL = "friends_email";
    String FRIENDS_PHONE = "friends_phone";
}

    public static final String CONTENT_AUTHORITY = "com.codechris.friends.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String PATHS_FRIENDS = "friends";
    public static final Uri URI_TABLE = Uri.parse(BASE_CONTENT_URI.toString() + "/" + PATHS_FRIENDS);

    public static final String[] TOP_LEVELS_PATHS = {
      PATHS_FRIENDS
    };

    public static class Friends implements FriendsColumns, BaseColumns{
        public static final Uri CONTENT_URI=
                BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATHS_FRIENDS).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." +CONTENT_AUTHORITY+ ".friends";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." +CONTENT_AUTHORITY+ ".friends";

        public static Uri buildFriendUri(String friendId){
            return CONTENT_URI.buildUpon().appendEncodedPath(friendId).build();
        }
        public static String getFriendId(Uri uri){
            return uri.getPathSegments().get(1);
        }
    }

}

