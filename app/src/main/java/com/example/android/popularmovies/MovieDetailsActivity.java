package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.Database.MovieContentProvider;
import com.example.android.popularmovies.Database.MovieContract;
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
    MovieManager manager = new MovieManager(null, this, this);

    ArrayList<Trailer> displayedTrailers;
    ArrayList<Review> displayedReviews;


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
        mPosterImageView.setClickable(true);
        mPosterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues v = new ContentValues();
                v.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, mMovie.getId());
                v.put(MovieContract.MovieEntry.COLUMN_TITLE, mMovie.getTitle());
                v.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, mMovie.getOverview());
                v.put(MovieContract.MovieEntry.COLUMN_RELEASE_YEAR, mMovie.getReleaseDate());
                Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, v);
                if (uri != null) {
                    Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
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
        this.displayedReviews = reviews;
    }

    @Override
    public void trailersFetchted(int movieId, ArrayList<Trailer> trailers) {
        this.displayedTrailers = trailers;
    }
}
