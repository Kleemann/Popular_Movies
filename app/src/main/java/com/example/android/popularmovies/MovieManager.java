package com.example.android.popularmovies;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.example.android.popularmovies.Database.MovieContract;
import com.example.android.popularmovies.Models.Movie;
import com.example.android.popularmovies.Models.Review;
import com.example.android.popularmovies.Models.Trailer;
import com.example.android.popularmovies.Networking.MovieParser;
import com.example.android.popularmovies.Networking.NetworkManager;
import com.example.android.popularmovies.Networking.ReviewParser;
import com.example.android.popularmovies.Networking.TrailerParser;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;



public class MovieManager implements LoaderManager.LoaderCallbacks<Cursor> {

    private String mSorting;
    private NetworkManager mFetcher;
    private final static String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private final static String API_PARAM = "api_key";
    private final static String MOVIE_PATH = "movies";
    private final static String TRAILER_PATH = "videos";
    private final static String REVIEW_PATH = "reviews";
    private final static String API_KEY = "3316f56d2fce80d099bd8b9497b4a544";
    private final MovieManagerListener mListener;
    private final MovieManagerDetailsListener mDetailsListener;
    private final Context mContext;



    public interface MovieManagerListener {
        void moviesFetches(ArrayList<Movie> movies);
    }
    public interface MovieManagerDetailsListener {
        void trailersFetchted(int movieId, ArrayList<Trailer> trailers);
        void reviewsFetchted(int movieId, ArrayList<Review> reviews);
    }

    public MovieManager(MovieManagerListener listener, MovieManagerDetailsListener detailsListener, Context context) {
        this.mListener = listener;
        this.mDetailsListener = detailsListener;
        this.mContext = context;
    }


    void getMovies(String sorting) {
        mSorting = sorting;
        ArrayList<Movie> movies = null;
        if (mSorting == MovieSorting.FAVORITES) {
            movies = getFavoriteMovies();
        } else {
            URL url = this.buildURL();
            JSONObject j = null;

            try {
                j = new MovieQueryTask().execute(url).get();
                movies = MovieParser.parse(j);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (mListener != null) {
            mListener.moviesFetches(movies);
        }
    }

    private ArrayList<Movie> getFavoriteMovies() {
        Cursor c = mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                MovieContract.MovieEntry.COLUMN_TITLE);
        ArrayList<Movie> movies = new ArrayList<Movie>();
        try {
            while (c.moveToNext()) {
                Movie m = new Movie();
                m.setTitle(c.getString(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE)));
                m.setId(c.getInt(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)));
                m.setOverview(c.getString(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW)));
                m.setReleaseDate(c.getString(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_YEAR)));

                movies.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            c.close();
        }

        return movies;
    }

    void getTrailersForMovie(int movieId) {

        ArrayList<String> paths = new ArrayList<String>();
        paths.add(TRAILER_PATH);
        URL url = this.buildURLWithPaths(String.valueOf(movieId), paths);
        JSONObject j = null;
        ArrayList<Trailer> trailers = null;
        try {
            j = new MovieQueryTask().execute(url).get();
            trailers = TrailerParser.parse(j);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, String.valueOf(j));
        if (mDetailsListener != null) {
            mDetailsListener.trailersFetchted(movieId, trailers);
        }
    }

    void getReviewsForMovie(int movieId) {
        ArrayList<String> paths = new ArrayList<String>();
        paths.add(REVIEW_PATH);
        URL url = this.buildURLWithPaths(String.valueOf(movieId), paths);
        ArrayList<Review> reviews = null;
        JSONObject j = null;
        try {
            j = new MovieQueryTask().execute(url).get();
            reviews = ReviewParser.parse(j);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, String.valueOf(j));
        if (mDetailsListener != null) {
            mDetailsListener.reviewsFetchted(movieId, reviews);
        }
    }

    private URL buildURL() {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(mSorting)
                .appendQueryParameter(API_PARAM, API_KEY).build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    private URL buildURLWithPaths(String movieId, ArrayList<String> paths) {
        Uri.Builder uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(API_PARAM, API_KEY)
                .appendPath(movieId);

        for (String path : paths) {
            uri.appendPath(path);
        }

        URL url = null;
        try {
            url = new URL(uri.build().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;

    }

    class MovieQueryTask extends AsyncTask<URL, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(URL... urls) {
            JSONObject json = null;
            try {
                json = NetworkManager.getResponseJSON(urls[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(mContext) {

            Cursor mMovieData = null;

            @Override
            protected void onStartLoading() {
                if (mMovieData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mMovieData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            MovieContract.MovieEntry.COLUMN_TITLE);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(Cursor data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.e("onLoadFinished", " ");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}


