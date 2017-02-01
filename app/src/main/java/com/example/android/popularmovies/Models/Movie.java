package com.example.android.popularmovies.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by makle on 29/01/2017.
 */

public class Movie implements Parcelable {
    String posterPath;
    String overview;
    String releaseDate;
    int id;
    String title;
    double voteAvg;

    public Movie() {
        this.posterPath = posterPath;
    }

    private Movie(Parcel in) {
        posterPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        id = in.readInt();
        title = in.readString();
        voteAvg = in.readDouble();
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setVoteAvg(double voteAvg) {
        this.voteAvg = voteAvg;
    }

    public double getVoteAvg() {
        return voteAvg;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }


    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(posterPath);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeDouble(voteAvg);
    }
}
