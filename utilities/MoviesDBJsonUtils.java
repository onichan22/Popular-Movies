/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.fioni.popularmovies.utilities;
import android.content.Context;

import com.example.fioni.popularmovies.MovieDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public final class MoviesDBJsonUtils {

    /**
     *
     * @param moviesJsonStr JSON response from server
     *
     * @return Array of Strings describing movies
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static String[] getSimpleMoviesStringsFromJson(Context context, String moviesJsonStr)
            throws JSONException {

        final String MDB_LIST = "results";
        final String MDB_LIST_LENGTH = "total_results";
        final String MDB_ID = "id";
        final String MDB_PATH = "poster_path";

        String[] parsedMovieData = null;
        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray moviesArray = moviesJson.getJSONArray(MDB_LIST);
        parsedMovieData = new String[moviesArray.length()];

        for (int i = 0; i < moviesArray.length(); i++) {
            String movieId;
            String posterPath;

            JSONObject aMovie = moviesArray.getJSONObject(i);
            movieId = aMovie.getString(MDB_ID);
            posterPath = aMovie.getString(MDB_PATH);

            parsedMovieData[i] = posterPath;
        }

        return parsedMovieData;
    }

    public static String[] getTrailerMoviesStringsFromJson(Context context, String moviesJsonStr)
            throws JSONException {

        final String MDB_LIST = "results";
        final String MDB_VIDEO_ID = "key";

        String[] parsedMovieData = null;
        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray trailerArray = moviesJson.getJSONArray(MDB_LIST);
        parsedMovieData = new String[3];
    //trailerArray.length();`

        for (int i = 0; i < trailerArray.length(); i++) {
            //String movieId;
            /*String trailerPath;

            JSONObject aMovie = trailerArray.getJSONObject(i);
            //movieId = aMovie.getString(MDB_ID);
            trailerPath = aMovie.getString(MDB_VIDEO_ID);
            parsedMovieData[i] = trailerPath;*/

            if(i < 3){
                String trailerPath;

                JSONObject aMovie = trailerArray.getJSONObject(i);
                //movieId = aMovie.getString(MDB_ID);
                trailerPath = aMovie.getString(MDB_VIDEO_ID);
                parsedMovieData[i] = trailerPath;

            }else{
                i = (trailerArray.length()+1);
                return parsedMovieData;
            }

        }

        return parsedMovieData;
    }

    public static String[] getReviewsMovieStringsFromJson(Context context, String moviesJsonStr)
            throws JSONException {

        final String MDB_LIST = "results";
        final String MDB_REVIEWS = "content";

        String[] parsedMovieData = null;
        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray ReviewArray = moviesJson.getJSONArray(MDB_LIST);
        parsedMovieData = new String[3];

        for (int i = 0; i < ReviewArray.length(); i++) {
            //String movieId;
            /*String trailerPath;

            JSONObject aMovie = trailerArray.getJSONObject(i);
            //movieId = aMovie.getString(MDB_ID);
            trailerPath = aMovie.getString(MDB_VIDEO_ID);
            parsedMovieData[i] = trailerPath;*/

            if(i < 3){
                String trailerPath;

                JSONObject aMovie = ReviewArray.getJSONObject(i);
                //movieId = aMovie.getString(MDB_ID);
                trailerPath = aMovie.getString(MDB_REVIEWS);
                parsedMovieData[i] = trailerPath;

            }else{
                i = (ReviewArray.length()+1);
                return parsedMovieData;
            }

        }

        return parsedMovieData;
    }

    public static ArrayList<MovieDB> getMovieDetails(Context context, String moviesJsonStr)
        throws JSONException{

        final String MDB_LIST = "results";

        final String MDB_ID = "id";
        final String MDB_PATH = "poster_path";
        final String MDB_OVERVW = "overview";
        final String MDB_VOTE = "vote_average";
        final String MDB_TITLE = "original_title";
        final String MDB_DATE = "release_date";

        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray moviesArray = moviesJson.getJSONArray(MDB_LIST);
        ArrayList<MovieDB> aMovieObjList = new ArrayList<MovieDB>();

        for (int i = 0; i < moviesArray.length(); i++) {

            JSONObject aMovie = moviesArray.getJSONObject(i);

            aMovieObjList.add(new MovieDB(aMovie.getString(MDB_ID),aMovie.getString(MDB_PATH)
                    ,aMovie.getString(MDB_TITLE),aMovie.getString(MDB_VOTE),aMovie.getString(MDB_OVERVW)
                    ,aMovie.getString(MDB_DATE)));

        }

        return aMovieObjList;
    }

}