package com.example.fioni.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by fioni on 7/29/2017.
 */

public class FavoriteMoviesContract {

    public static final String AUTHORITY =
            "com.example.fioni.popularmovies.provider";

    public static final Uri BASE_CONTENT_URI =
            Uri.parse("content://" + AUTHORITY);

    public static final String PATH_FAVORITES = "favorites";

    public static final class FavoriteMovies implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        public static final String TABLE_NAME = "favorites";

        public static final String COL_M_ID = "movieId";
        public static final String COL_M_TITLE = "movieTitle";
        public static final String COL_M_RELEASE = "releaseDate";
        public static final String COL_M_RATING = "rating";
        public static final String COL_M_POSTER = "poster";
        //public static final String COL_M_DURATION = "duration";
        public static final String COL_M_SYNOPSIS = "synopsis";

    }
}
