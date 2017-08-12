package com.example.fioni.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.fioni.popularmovies.adapters.AnAdapter;
import com.example.fioni.popularmovies.data.FavoriteMoviesContract;
import com.example.fioni.popularmovies.utilities.MoviesDBJsonUtils;
import com.example.fioni.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.Boolean.FALSE;

public class MovieDetails extends AppCompatActivity implements AnAdapter.AnAdapterOnClickHandler {
    private TextView mDisplayTitle;
    private TextView mDisplayRelease;
    private TextView mDisplayRating;
    private TextView mDisplayOvervw;
    private ImageView mDisplayImage;
    private MovieDB mMovie;

    private AnAdapter mAnAdapter;
    private RecyclerView mRecyclerView;

    private AnAdapter mBnAdapter;
    private RecyclerView mBRecyclerView;

    private static final String TRAILER_TYPE = "trailer";
    private static final String REVIEW_TYPE = "review";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        mDisplayTitle = (TextView) findViewById(R.id.tv_movie_title);
        mDisplayRelease = (TextView) findViewById(R.id.tv_release);
        mDisplayRating = (TextView) findViewById(R.id.tv_rating);
        mDisplayOvervw = (TextView) findViewById(R.id.tv_overview);
        mDisplayImage = (ImageView) findViewById(R.id.tv_movie_poster);

        Intent movieIntent = getIntent();
        mMovie = movieIntent.getParcelableExtra("thisMovie");
        String posterPath = "http://image.tmdb.org/t/p/w342" + mMovie.getPosterPath();
        Picasso.with(MovieDetails.this).load(posterPath).into(this.mDisplayImage);

        mDisplayTitle.setText(mMovie.getOriTitle());
        mDisplayRelease.setText(mMovie.getmReleaseDate());
        mDisplayRating.setText(mMovie.getRating());
        mDisplayOvervw.setText(mMovie.getOverview());

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_trailer);
        LinearLayoutManager layoutManagerTrailer = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,FALSE);
        mRecyclerView.setLayoutManager(layoutManagerTrailer);
        mRecyclerView.setHasFixedSize(true);
        mAnAdapter = new AnAdapter(this);
        mRecyclerView.setAdapter(mAnAdapter);

        mBRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_reviews);
        LinearLayoutManager layoutManagerReview = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,FALSE);
        mBRecyclerView.setLayoutManager(layoutManagerReview);
        mBRecyclerView.setHasFixedSize(true);
        mBnAdapter = new AnAdapter(this);
        mBRecyclerView.setAdapter(mBnAdapter);

        makeSearchQuery(mMovie.getMovieId());

        final ToggleButton toggleFave = (ToggleButton) findViewById(R.id.addToFavorites);
        boolean isFavorite = mMovie.getFavorite();
        toggleFave.setChecked(isFavorite);

        toggleFave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    addMovieToFavorites();
                    mMovie.setFavorite(true);
                    Toast.makeText(getBaseContext(), "Added to Fave", Toast.LENGTH_LONG).show();
                } else {
                    mMovie.setFavorite(false);
                    toggleFave.setChecked(false);
                }
            }
        });

    }

    private void makeSearchQuery(String q) {

        URL trailerSearchUrl = NetworkUtils.buildTrailerUrl(q);
        new QueryTask().execute(trailerSearchUrl.toString(),TRAILER_TYPE);

        URL reviewSearchUrl = NetworkUtils.buildReviewUrl(q);
        //Toast.makeText(this, reviewSearchUrl.toString(), Toast.LENGTH_LONG).show();
        new QueryTask().execute(reviewSearchUrl.toString(),REVIEW_TYPE);

    }

    @Override
    public void onClick(String aString) {
        Toast.makeText(this, "video clicked", Toast.LENGTH_SHORT).show();
        watchTrailer(aString);
    }

    private void watchTrailer(String aString) {
        /*Intent youTubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + aString));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + aString));*/
        Intent watchIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + aString));
        Intent chooser = Intent.createChooser(watchIntent, "Play on");

        if (watchIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
    }

    public class QueryTask extends AsyncTask<String, Void, AnObject> {

        @Override
        protected AnObject doInBackground(String... params) {
            URL searchUrl = null;

            try {
                searchUrl = new URL(params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            String type = params[1];
            AnObject parmObj = new AnObject();
            parmObj.setType(type);
            String movieSearchResults = null;
            try {
                movieSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                if (type.equals(TRAILER_TYPE)) {
                    String[] videos = MoviesDBJsonUtils.getTrailerMoviesStringsFromJson(MovieDetails.this, movieSearchResults);
                    parmObj.setList(videos);
                }
                if (type.equals(REVIEW_TYPE)) {
                    String[] reviews = MoviesDBJsonUtils.getReviewsMovieStringsFromJson(MovieDetails.this, movieSearchResults);
                    parmObj.setList(reviews);
                }
                return parmObj;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(AnObject object) {
            if (object != null) {
                if(object.getType().equals(TRAILER_TYPE)){
                    mAnAdapter.setObjectData(object);
                }
                if(object.getType().equals(REVIEW_TYPE)){
                    mBnAdapter.setObjectData(object);
                }

            }
        }

    }


    private void addMovieToFavorites(){
        String title = mMovie.getOriTitle();
        String movieID = mMovie.getMovieId();
        String poster = mMovie.getPosterPath();
        String rating = mMovie.getRating();
        String synopsis = mMovie.getOverview();
        String release = mMovie.getmReleaseDate();

        ContentValues cv = new ContentValues();
        cv.put(FavoriteMoviesContract.FavoriteMovies.COL_M_TITLE, title);
        cv.put(FavoriteMoviesContract.FavoriteMovies.COL_M_ID, movieID);
        cv.put(FavoriteMoviesContract.FavoriteMovies.COL_M_POSTER, poster);
        cv.put(FavoriteMoviesContract.FavoriteMovies.COL_M_RATING, rating);
        cv.put(FavoriteMoviesContract.FavoriteMovies.COL_M_SYNOPSIS, synopsis);
        cv.put(FavoriteMoviesContract.FavoriteMovies.COL_M_RELEASE, release);

        getContentResolver().insert(FavoriteMoviesContract.FavoriteMovies.CONTENT_URI, cv);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(!mMovie.getFavorite()){
            Uri uri = FavoriteMoviesContract.FavoriteMovies.CONTENT_URI;
            uri = uri.buildUpon().appendPath(mMovie.getMovieId()).build();

            getContentResolver().delete(uri, null, null);

        }
    }
}
