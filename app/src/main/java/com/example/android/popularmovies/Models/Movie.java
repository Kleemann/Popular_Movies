package com.example.android.popularmovies.Models;

/**
 * Created by makle on 29/01/2017.
 */

public class Movie {
    String posterPath;
    String overview;
    String releaseDate;
    int id;
    String title;
    double voteAvg;

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


}
