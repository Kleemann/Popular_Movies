package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.Models.Movie;
import com.example.android.popularmovies.Models.Review;
import com.example.android.popularmovies.Models.Trailer;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.net.URL;
import java.util.ArrayList;

public class MovieDetailsActivity extends AppCompatActivity implements MovieManager.MovieManagerDetailsListener {

    Movie mMovie;
    ImageView mPosterImageView;
    TextView mTitleTextView;
    TextView mYeatTextView;
    TextView mOverviewTextView;
    TextView mAvgVote;
    MovieManager manager = new MovieManager(null, this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Intent i = getIntent();
        mMovie = i.getExtras().getParcelable("Movie");

        mPosterImageView = (ImageView) findViewById(R.id.iv_poster);
        mTitleTextView = (TextView) findViewById(R.id.title);
        mYeatTextView = (TextView) findViewById(R.id.year);
        mOverviewTextView = (TextView) findViewById(R.id.overview);
        mAvgVote = (TextView) findViewById(R.id.avgVote);
        configure();

       getMovieDetails();
    }

    private void getMovieDetails() {
        manager.getTrailersForMovie(mMovie.getId());
        manager.getReviewsForMovie(mMovie.getId());
    }

    private void configure() {
        Picasso.with(this).load(mMovie.getPosterPath()).into(mPosterImageView);
        mTitleTextView.setText(mMovie.getTitle());
        mYeatTextView.setText("("+mMovie.getReleaseDate()+")");
        mOverviewTextView.setText(mMovie.getOverview());
        String avg = String.valueOf(mMovie.getVoteAvg());
        mAvgVote.setText(String.valueOf(mMovie.getVoteAvg()));
    }

    @Override
    public void reviewsFetchted(int movieId, ArrayList<Review> reviews) {

    }

    @Override
    public void trailersFetchted(int movieId, ArrayList<Trailer> trailers) {

    }
}
