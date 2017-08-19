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
package com.example.fioni.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.fioni.popularmovies.MovieDB;
import com.example.fioni.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * {@link MovieAdapter}
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private ArrayList <MovieDB> mMovieData;
    private String mPosterPath;

    private final MovieAdapterOnClickHandler mClickHandler;


    public interface MovieAdapterOnClickHandler {
        void onClick(MovieDB aMovie);
    }

    /**
     *
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public MovieAdapter(MovieAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }


    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        //public final TextView mMovieTextView;
        public final ImageView mMovieImageView;

        public MovieAdapterViewHolder(View view) {
            super(view);
            //mMovieTextView = (TextView) view.findViewById(R.id.tv_movie_data);
            mMovieImageView = (ImageView) view.findViewById(R.id.tv_movie_data);

            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            MovieDB aMovie = mMovieData.get(adapterPosition);
            mClickHandler.onClick(aMovie);
        }
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieAdapterViewHolder, int position) {
        mPosterPath = mMovieData.get(position).getPosterPath();
        //movieAdapterViewHolder.mMovieImageView.setText(mPosterPath);
        String posterPath = "http://image.tmdb.org/t/p/w185" + mPosterPath;
        Picasso.with(movieAdapterViewHolder.mMovieImageView
                .getContext()).load(posterPath).into(movieAdapterViewHolder.mMovieImageView);
    }

    @Override
    public int getItemCount() {
        if (null == mMovieData) return 0;
        return mMovieData.size();
    }

    public void setMovieData(ArrayList<MovieDB> movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }
}