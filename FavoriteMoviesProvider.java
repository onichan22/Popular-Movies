package com.example.fioni.popularmovies;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.fioni.popularmovies.data.FavoriteMoviesContract;
import com.example.fioni.popularmovies.data.FavoriteMoviesDbHelper;

import static com.example.fioni.popularmovies.data.FavoriteMoviesContract.FavoriteMovies.TABLE_NAME;

/**
 * Created by fioni on 7/30/2017.
 */

public class FavoriteMoviesProvider extends ContentProvider {

    private FavoriteMoviesDbHelper mDbHelper;
    public static final int FAVE_MOVIES = 100;
    public static final int A_FAVE_MOVIE = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(FavoriteMoviesContract.AUTHORITY, FavoriteMoviesContract.PATH_FAVORITES, FAVE_MOVIES);
        uriMatcher.addURI(FavoriteMoviesContract.AUTHORITY, FavoriteMoviesContract.PATH_FAVORITES + "/#", A_FAVE_MOVIE);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {

        Context context = getContext();
        mDbHelper = new FavoriteMoviesDbHelper(context);
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase db = mDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;


        switch (match) {

            case FAVE_MOVIES:
                retCursor =  db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }


    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        long id = db.insert(TABLE_NAME, null, values);
        switch (match) {
            case FAVE_MOVIES:
            if (id > 0) {
                returnUri = ContentUris.withAppendedId(FavoriteMoviesContract.FavoriteMovies.CONTENT_URI, id);
            } else {
                throw new android.database.SQLException("Failed to insert row into " + uri);
            }
            break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        int moviesDeleted;

        switch (match) {

            case A_FAVE_MOVIE:
                String id = uri.getPathSegments().get(1);

                moviesDeleted = db.delete(TABLE_NAME, "movieId=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (moviesDeleted != 0) {

            getContext().getContentResolver().notifyChange(uri, null);
        }

        return moviesDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
