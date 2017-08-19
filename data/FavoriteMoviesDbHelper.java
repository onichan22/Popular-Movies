package com.example.fioni.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.fioni.popularmovies.data.FavoriteMoviesContract.FavoriteMovies;

/**
 * Created by fioni on 7/29/2017.
 */

public class FavoriteMoviesDbHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = '1';

    public FavoriteMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_FAVORITE_MOVIE_TABLE = "CREATE TABLE " +
                FavoriteMovies.TABLE_NAME + "(" +
                FavoriteMovies._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavoriteMovies.COL_M_TITLE + " TEXT NOT NULL, " +
                FavoriteMovies.COL_M_ID + " TEXT NOT NULL, " +
                FavoriteMovies.COL_M_RELEASE + " TEXT NOT NULL, " +
                FavoriteMovies.COL_M_RATING + " TEXT NOT NULL, " +
                FavoriteMovies.COL_M_POSTER + " TEXT NOT NULL, " +
                //FavoriteMovies.COL_M_DURATION + " TEXT NOT NULL, " +
                FavoriteMovies.COL_M_SYNOPSIS + " TEXT NOT NULL " +
                ");";

        db.execSQL(SQL_CREATE_FAVORITE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXIST "+ FavoriteMovies.TABLE_NAME);
        onCreate(db);
    }
}
