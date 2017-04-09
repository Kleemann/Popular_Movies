package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.Database.MovieContract;
import com.example.android.popularmovies.Models.Movie;
import com.example.android.popularmovies.Models.Review;
import com.example.android.popularmovies.Models.Trailer;
import com.example.android.popularmovies.Util.MoviesAdapter;
import com.example.android.popularmovies.Util.ReviewAdapter;
import com.example.android.popularmovies.Util.TrailerAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieDetailsActivity extends AppCompatActivity implements MovieManager.MovieManagerDetailsListener, LoaderManager.LoaderCallbacks<Boolean> {

    Movie mMovie;
    ImageView mPosterImageView;
    TextView mTitleTextView;
    TextView mYeatTextView;
    TextView mOverviewTextView;
    TextView mAvgVote;
    Button mTrailerButton;
    Button mReviewButton;
    Button mFavoriteButton;
    MovieManager manager = new MovieManager(null, this, this);
    private ArrayList<Trailer> mTrailers;
    private ArrayList<Review> mReviews;
    private boolean isFavorite = false;

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
        mTrailerButton = (Button) findViewById(R.id.trailerButton);
        mReviewButton = (Button) findViewById(R.id.reviewButton);
        mFavoriteButton = (Button) findViewById(R.id.favoriteButton);
        getSupportLoaderManager().initLoader(0, null, this);
        mPosterImageView.setClickable(true);

        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markAsFavoritePressed();
            }
        });

        mTrailerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowAlertDialogWithTrailers();
            }
        });
        mReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowAlertDialogWithReviews();
            }
        });

        mTrailerButton.setVisibility(View.INVISIBLE);
        mReviewButton.setVisibility(View.INVISIBLE);

        configure();
        getMovieDetails();
    }

    private void markAsFavoritePressed() {
        if (isFavorite == false) {
            ContentValues v = new ContentValues();
            v.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, mMovie.getId());
            v.put(MovieContract.MovieEntry.COLUMN_TITLE, mMovie.getTitle());
            v.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, mMovie.getOverview());
            v.put(MovieContract.MovieEntry.COLUMN_RELEASE_YEAR, mMovie.getReleaseDate());
            v.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, mMovie.getPosterPath());
            v.put(MovieContract.MovieEntry.COLUMN_AVG_VOTE, mMovie.getVoteAvg());
            Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, v);
            if (uri != null) {
                Toast.makeText(getBaseContext(), R.string.marked_as_favorite_toast_text, Toast.LENGTH_SHORT).show();
                mFavoriteButton.setText(R.string.remove_favorite_button_text);
                isFavorite = true;
            }
        } else {
            String stringId = Integer.toString(mMovie.getId());
            Uri uri = MovieContract.MovieEntry.CONTENT_URI;
            uri = uri.buildUpon().appendPath(stringId).build();
            int deleted = 0;
            deleted = getContentResolver().delete(uri, null, null);
            if (deleted > 0) {
                Toast.makeText(getBaseContext(), R.string.deleted_from_favorites_toast_text, Toast.LENGTH_SHORT).show();
                mFavoriteButton.setText(R.string.mark_favorite_button_text);
                isFavorite = false;
            }
        }
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
    public void reviewsFetched(int movieId, ArrayList<Review> reviews) {
        mReviews = reviews;
        mReviewButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void trailersFetched(int movieId, ArrayList<Trailer> trailers) {
        mTrailers = trailers;
        mTrailerButton.setVisibility(View.VISIBLE);
    }

    public void ShowAlertDialogWithTrailers() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        TrailerAdapter adapter = new TrailerAdapter(this, mTrailers);
        dialogBuilder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Trailer t = mTrailers.get(i);
                manager.watchTrailer(t);
            }
        });
        dialogBuilder.setNegativeButton(R.string.general_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialogBuilder.setTitle(R.string.trailers_button_text);
        dialogBuilder.setCancelable(false);

        AlertDialog alertDialogObject = dialogBuilder.create();
        //Show the dialog
        alertDialogObject.show();
    }

    public void ShowAlertDialogWithReviews() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        ReviewAdapter adapter = new ReviewAdapter(this, mReviews);
        dialogBuilder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialogBuilder.setNegativeButton(R.string.general_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialogBuilder.setTitle(R.string.reviews_button_text);
        dialogBuilder.setCancelable(false);

        AlertDialog alertDialogObject = dialogBuilder.create();
        //Show the dialog
        alertDialogObject.show();
    }

    @Override
    public Loader<Boolean> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Boolean>(this.getBaseContext()) {


            @Override
            protected void onStartLoading() {
                forceLoad();
            }

            @Override
            public Boolean loadInBackground() {
                try {
                    String stringId = Integer.toString(mMovie.getId());
                    Uri uri = MovieContract.MovieEntry.CONTENT_URI;
                    uri = uri.buildUpon().appendPath(stringId).build();

                    Cursor c = getContext().getContentResolver().query(uri, null, null, null, MovieContract.MovieEntry.COLUMN_MOVIE_ID);
                    if (c.getCount() > 0) {
                        return true;
                    }
                    return false;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(Boolean data) {
                super.deliverResult(data);
            }
        };

    }

    @Override
    public void onLoadFinished(Loader<Boolean> loader, Boolean b) {
       isFavorite = b;
        if (isFavorite == true) {
            mFavoriteButton.setText(R.string.remove_favorite_button_text);
        } else {
            mFavoriteButton.setText(R.string.mark_favorite_button_text);
        }
    }

    @Override
    public void onLoaderReset(Loader<Boolean> loader) {

    }
}
