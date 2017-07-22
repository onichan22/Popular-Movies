package com.example.fioni.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetails extends AppCompatActivity {
    TextView mDisplayTitle;
    TextView mDisplayRelease;
    TextView mDisplayRating;
    TextView mDisplayOvervw;
    ImageView mDisplayImage;

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
        MovieDB thisMovie = movieIntent.getParcelableExtra("thisMovie");
        String posterPath = "http://image.tmdb.org/t/p/w342" + thisMovie.getPosterPath();
        Picasso.with(MovieDetails.this).load(posterPath).into(this.mDisplayImage);

        mDisplayTitle.setText(thisMovie.getOriTitle());
        mDisplayRelease.setText(thisMovie.getmReleaseDate());
        mDisplayRating.setText(thisMovie.getRating());
        mDisplayOvervw.setText(thisMovie.getOverview());

    }

}
