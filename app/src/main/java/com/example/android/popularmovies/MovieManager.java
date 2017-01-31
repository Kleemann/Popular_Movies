package com.example.android.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.popularmovies.Models.Movie;
import com.example.android.popularmovies.Networking.MovieParser;
import com.example.android.popularmovies.Networking.NetworkManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;



public class MovieManager {

    private String mSorting;
    private NetworkManager mFetcher;
    private final static String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private final static String API_PARAM = "api_key";
    private final MovieManagerListener mListener;

    public interface MovieManagerListener {
        void moviesFetches(ArrayList<Movie> movies);
    }

    public MovieManager(MovieManagerListener listener) {
        this.mListener = listener;
    }


    void getMovies(String sorting) {
        mSorting = sorting;
        URL url = this.buildURL();
        JSONObject j = null;
        ArrayList<Movie> movies = null;
        try {
            j = new MovieQueryTask().execute(url).get();
            movies = MovieParser.parse(j);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG, String.valueOf(j));
        if (mListener != null) {
            mListener.moviesFetches(movies);
        }
    }

    private URL buildURL() {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(mSorting)
                .appendQueryParameter(API_PARAM, "3316f56d2fce80d099bd8b9497b4a544").build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
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
}
