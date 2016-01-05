package com.codechris.friends;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.media.Image;
import android.net.Uri;
import android.nfc.Tag;
import android.os.CancellationSignal;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Switch;

import java.sql.Struct;
import java.util.regex.Matcher;

/**
 * Created by Christopher on 12/17/2015.
 */
public class FriendsProvider extends ContentProvider {
    private FriendsDatabase mOpenHelper;

    private static String Tag = FriendsProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int FRIENDS = 100;
    private static final int FRIENDS_ID =101;

    private static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FriendsContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, "friends", FRIENDS);
        matcher.addURI(authority, "friends/*", FRIENDS_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper =new FriendsDatabase(getContext());
        return true;
    }

    private void deleteDatabase(){
        mOpenHelper.close();
        FriendsDatabase.deleteDatabase(getContext());
        mOpenHelper =new FriendsDatabase(getContext());
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case FRIENDS:
                return FriendsContract.Friends.CONTENT_TYPE;
            case FRIENDS_ID:
                return FriendsContract.Friends.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown Uri:" + uri);
        }
        return null;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, CancellationSignal cancellationSignal) {
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(FriendsDatabase.Tables.FRIENDS);

        switch (match){
            case FRIENDS:
                //do nothing
                break;
            case FRIENDS_ID:
                String id = FriendsContract.Friends.getFriendId(uri);
                queryBuilder.appendWhere(BaseColumns._ID + "=" +id);
                break;
            default:
                throw new IllegalArgumentException("unknown Uri: " +uri);
        }
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        return null;
//        return super.query(uri, projection, selection, selectionArgs, sortOrder, cancellationSignal);
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.v(Tag, "insert(uri=" + uri + ", values=" + values.toString());
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match){
            case FRIENDS:
                long recordId = db.insertOrThrow(FriendsDatabase.Tables.FRIENDS, null, values);
                return FriendsContract.Friends.buildFriendUri(String.valueOf(recordId));
            default:
                throw new IllegalArgumentException("unknown Uri: " +uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.v(Tag, "update(uri=" + uri + ", values=" + values.toString());
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        String selectionCriteria + selection;
        switch (match){
            case FRIENDS:
                //do nothing
                break;
            case FRIENDS_ID:
                String id = FriendsContract.Friends.getFriendId(uri);
                selectionCriteria = BaseColumns._ID+ "="+ id
                        +(!TextUtils.isEmpty(selection)) ? "And (" + selection + ")" :"";
                break;


            default:
                throw new IllegalArgumentException("unknown Uri" + uri)
        }
        int updateCount = db.update(FriendsDatabase.Tables.FRIENDS, values, selectionCriteria, selectionArgs);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.v(Tag, "delete(uri=" + uri);

        if(uri.equals(FriendsContract.BASE_CONTENT_URI)){
            deleteDatabase();
            return 0;


        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        String selectionCriteria + selection;
        switch (match) {
            case FRIENDS_ID:
                String id = FriendsContract.Friends.getFriendId(uri);
                String selectionCriteria = BaseColumns._ID+ "="+ id
                        +(!TextUtils.isEmpty(selection)) ? "And (" + selection + ")" :"";
                return db.delete(FriendsDatabase.Tables.FRIENDS, selectionCriteria, selectionArgs)
                break;

            default:
                throw new IllegalArgumentException("unknown Uri" + uri)
        }
        }
    }
}


