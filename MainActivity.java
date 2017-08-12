package com.example.fioni.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fioni.popularmovies.adapters.MovieAdapter;
import com.example.fioni.popularmovies.data.FavoriteMoviesContract;
import com.example.fioni.popularmovies.utilities.MoviesDBJsonUtils;
import com.example.fioni.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMsg;

    private static final int TASK_LOADER_ID = 0;

    private static final String LIFECYCLE_SORT_KEY = "sort_key";
    private static final String SORT_ITEM_POPULAR = "popular";
    private static final String SORT_ITEM_TOP_RATED = "top_rated";
    private static final String SORT_ITEM_FAVORITES = "favorites";

    private ArrayList<MovieDB> mFavemovies = new ArrayList<MovieDB>();

    private ArrayList<String> mMovieId = new ArrayList<>();
    private String mSelectedMenu;

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
            if (savedInstanceState != null) {
                if (savedInstanceState.containsKey(LIFECYCLE_SORT_KEY)) {
                    String allPreviousLifecycleCallbacks = savedInstanceState
                            .getString(LIFECYCLE_SORT_KEY);
                    mSelectedMenu = allPreviousLifecycleCallbacks;
                    makeMovieSearchQuery(allPreviousLifecycleCallbacks);
                }
            } else {
                makeMovieSearchQuery(SORT_ITEM_POPULAR);
                //getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);
            }
        } else {
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

        if (q == SORT_ITEM_TOP_RATED || q == SORT_ITEM_POPULAR) {
            URL movieSearchUrl = NetworkUtils.buildUrl(q);
            new QueryTask().execute(movieSearchUrl);
        } else {
            //new QueryFavorite().execute();
            boolean isEmpty = mFavemovies.isEmpty();
            if (isEmpty) {
                Toast.makeText(this, "It's empty", Toast.LENGTH_SHORT).show();
            }
            mMovieAdapter.setMovieData(mFavemovies);
        }
    }

    @Override
    public void onClick(MovieDB movieSelected) {
        Context context = this;
        //Toast.makeText(context, movieSelected, Toast.LENGTH_SHORT).show();

        //Toast.makeText(context, " size of cursor and mMovieId: " + mMovieId.size(), Toast.LENGTH_SHORT).show();
        String thisMovieId = movieSelected.getMovieId();

        for (int i = 0; i < mMovieId.size(); i++) {
            String aFaveId = mMovieId.get(i);
            //Toast.makeText(this, "this movide id" + thisMovieId + " afave id " + aFaveId, Toast.LENGTH_LONG).show();
            if (thisMovieId.equals(aFaveId)) {
                //intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, movieSelected);
                movieSelected.setFavorite(true);
                //Toast.makeText(this, movieSelected.getOriTitle() + movieSelected.getFavorite(), Toast.LENGTH_SHORT).show();
            }
        }

        Class destinationClass = MovieDetails.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra("thisMovie", movieSelected);
        startActivity(intentToStartDetailActivity);

        mFavemovies.clear();
        mMovieId.clear();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            Cursor mCursor = null;
            @Override
            protected void onStartLoading() {
                if (mCursor != null) {
                    deliverResult(mCursor);

              } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                mFavemovies.clear();
                mMovieId.clear();

                try {
                    mCursor = getContentResolver().query(FavoriteMoviesContract.FavoriteMovies.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);
                    return mCursor;

                } catch (Exception e) {
                    Log.e("Main Activity:", "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor movieDataResults) {
        if (movieDataResults != null) {
            //Toast.makeText(getBaseContext(), "onPostLoadFinished", Toast.LENGTH_LONG).show();
            //ArrayList <MovieDB> movies = new ArrayList<MovieDB>();
            mFavemovies.clear();
            mMovieId.clear();
            try {
                while (movieDataResults.moveToNext()) {
                    //Toast.makeText(getBaseContext(), movieDataResults.getString(1), Toast.LENGTH_LONG).show();
                    MovieDB aFaveMovie = new MovieDB(movieDataResults.getString(2),
                            movieDataResults.getString(5),
                            movieDataResults.getString(1),
                            movieDataResults.getString(4),
                            movieDataResults.getString(6),
                            movieDataResults.getString(3));
                    mFavemovies.add(aFaveMovie);
                    mMovieId.add(aFaveMovie.getMovieId().toString());

                }
            } finally {
                movieDataResults.close();
                //Toast.makeText(getBaseContext(), "onPostLoadFinished:"+ mMovieId.size(), Toast.LENGTH_LONG).show();
            }
            //mMovieAdapter.setMovieData(mFavemovies);
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFavemovies.clear();
        mMovieId.clear();
    }

    public class QueryTask extends AsyncTask<URL, Void, ArrayList<MovieDB>> {

        @Override
        protected ArrayList<MovieDB> doInBackground(URL... params) {
            URL searchUrl = params[0];
            String movieSearchResults = null;
            try {
                movieSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);

                ArrayList<MovieDB> movies = MoviesDBJsonUtils.getMovieDetails(MainActivity.this, movieSearchResults);

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
        if (itemThatWasClickedId == R.id.search_popular) {
            makeMovieSearchQuery(SORT_ITEM_POPULAR);
            mSelectedMenu = SORT_ITEM_POPULAR;
            return true;
        }
        if (itemThatWasClickedId == R.id.search_top_rated) {
            makeMovieSearchQuery(SORT_ITEM_TOP_RATED);
            mSelectedMenu = SORT_ITEM_TOP_RATED;
            return true;
        }
        if (itemThatWasClickedId == R.id.display_favorites) {
            mSelectedMenu = SORT_ITEM_FAVORITES;
            makeMovieSearchQuery(SORT_ITEM_FAVORITES);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(LIFECYCLE_SORT_KEY, mSelectedMenu);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        outState.putString(LIFECYCLE_SORT_KEY, mSelectedMenu);
        super.onRestoreInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFavemovies.clear();
        mMovieId.clear();
        getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mFavemovies.clear();
        mMovieId.clear();
        //getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);
        getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
        //mMovieAdapter.setMovieData(mFavemovies);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFavemovies.clear();
        mMovieId.clear();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mFavemovies.clear();
        mMovieId.clear();
    }
}
