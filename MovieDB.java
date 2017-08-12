package com.example.fioni.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fioni on 7/6/2017.
 */

public class MovieDB implements Parcelable{

    private String mMovieId;
    private String mPosterPath;
    private String mOriTitle;
    private String mRating;
    private String mOverview;
    private String mReleaseDate;
    private boolean mFavorite;

    public MovieDB() {
    }

    public MovieDB(String mMovieId, String mPosterPath, String mOriTitle, String mRating, String mOverview,
                   String mReleaseDate) {
        this.mMovieId = mMovieId;
        this.mPosterPath = mPosterPath;
        this.mOriTitle = mOriTitle;
        this.mRating = mRating;
        this.mOverview = mOverview;

        this.mReleaseDate = mReleaseDate;
    }

    protected MovieDB(Parcel in) {
        mMovieId = in.readString();
        mPosterPath = in.readString();
        mOriTitle = in.readString();
        mRating = in.readString();
        mOverview = in.readString();
        mReleaseDate = in.readString();
        mFavorite = in.readByte() != 0;
    }

    public static final Creator<MovieDB> CREATOR = new Creator<MovieDB>() {
        @Override
        public MovieDB createFromParcel(Parcel in) {
            return new MovieDB(in);
        }

        @Override
        public MovieDB[] newArray(int i) {
            return new MovieDB[i];
        }
    };

    public void setMovieId(String mMovieId) {
        this.mMovieId = mMovieId;
    }

    public void setPosterPath(String mPosterPath) {
        this.mPosterPath = mPosterPath;
    }

    public void setOriTitle(String mOriTitle) {
        this.mOriTitle = mOriTitle;
    }

    public void setRating(String mRating) {
        this.mRating = mRating;
    }

    public void setOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;}

    public void setFavorite(Boolean mFavorite){ this.mFavorite = mFavorite; }

    public String getmReleaseDate() {
        return mReleaseDate;    }

    public String getMovieId() {
        return mMovieId;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public String getOriTitle() {
        return mOriTitle;
    }

    public String getRating() {
        return mRating;
    }

    public String getOverview() {
        return mOverview;
    }

    public boolean getFavorite() { return mFavorite; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mMovieId);
        dest.writeString(mPosterPath);
        dest.writeString(mOriTitle);
        dest.writeString(mRating);
        dest.writeString(mOverview);
        dest.writeString(mReleaseDate);
        dest.writeByte((byte)(mFavorite ? 1 : 0));
    }
}
