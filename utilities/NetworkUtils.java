package com.example.fioni.popularmovies.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by fioni on 7/5/2017.
 */

public class NetworkUtils {
    final static String GITHUB_BASE_URL =
            "https://api.themoviedb.org/3/movie";

    final static String PARAM_KEY = "api_key";
    final static String api_key = "-------";


    final static String PARAM_SORT = "sort";
    final static String sortBy = "popular";

    final static String PATH_VIDEO = "videos";
    final static String PATH_REVIEW = "reviews";

    public static URL buildUrl(String q) {
        if(q == ""){
            q = sortBy;
        }
        Uri builtUri = Uri.parse(GITHUB_BASE_URL).buildUpon()
                .appendPath(q)
                .appendQueryParameter(PARAM_KEY, api_key)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildTrailerUrl(String id) {
        Uri builtUri = Uri.parse(GITHUB_BASE_URL).buildUpon()
                .appendPath(id)
                .appendPath(PATH_VIDEO)
                .appendQueryParameter(PARAM_KEY, api_key)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildReviewUrl(String id) {
        Uri builtUri = Uri.parse(GITHUB_BASE_URL).buildUpon()
                .appendPath(id)
                .appendPath(PATH_REVIEW)
                .appendQueryParameter(PARAM_KEY, api_key)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
