package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.Models.Movie;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie mMovie;
    ImageView mPosterImageView;
    TextView mTitleTextView;
    TextView mYeatTextView;
    TextView mOverviewTextView;
    TextView mAvgVote;

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
    }

    private void configure() {
        Picasso.with(this).load(mMovie.getPosterPath()).into(mPosterImageView);
        mTitleTextView.setText(mMovie.getTitle());
        mYeatTextView.setText("("+mMovie.getReleaseDate()+")");
        mOverviewTextView.setText(mMovie.getOverview());
        String avg = String.valueOf(mMovie.getVoteAvg());
        mAvgVote.setText(String.valueOf(mMovie.getVoteAvg()));
    }
}
