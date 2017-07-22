package com.example.fioni.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.fioni.popularmovies.utilities.MoviesDBJsonUtils;
import com.example.fioni.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMsg;

    private TextView mSearchResultsTextView;

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mErrorMsg = (TextView) findViewById(R.id.tv_error_message_display);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_images);

        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns(), GridLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        if (isOnline()) {
            makeMovieSearchQuery("");
        }
        else{
            mErrorMsg.setVisibility(View.VISIBLE);
        }

    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the poster
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }


    private void makeMovieSearchQuery(String q) {

        URL movieSearchUrl = NetworkUtils.buildUrl(q);

        new QueryTask().execute(movieSearchUrl);
    }

    @Override
    public void onClick(MovieDB movieSelected) {
        Context context = this;
        //Toast.makeText(context, movieSelected, Toast.LENGTH_SHORT).show();

        Class destinationClass = MovieDetails.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        //intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, movieSelected);
        intentToStartDetailActivity.putExtra("thisMovie",movieSelected);
        startActivity(intentToStartDetailActivity);
    }


    public class QueryTask extends AsyncTask<URL, Void, ArrayList<MovieDB>> {

        @Override
        protected ArrayList<MovieDB> doInBackground(URL... params) {
            URL searchUrl = params[0];
            String movieSearchResults = null;
            try {
                movieSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);

                ArrayList <MovieDB> movies = MoviesDBJsonUtils.getMovieDetails(MainActivity.this, movieSearchResults);

                return movies;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(ArrayList<MovieDB> movieDataResults) {
            if (movieDataResults != null) {
                mMovieAdapter.setMovieData(movieDataResults);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.search_popular){
            makeMovieSearchQuery("popular");
            return true;
        }
        if (itemThatWasClickedId == R.id.search_top_rated) {
            makeMovieSearchQuery("top_rated");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
