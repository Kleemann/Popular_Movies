package com.example.android.popularmovies.Networking;

import com.example.android.popularmovies.Models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieParser {


    public static ArrayList<Movie> parse(JSONObject json) {

        ArrayList<Movie> parsedMovies = new ArrayList<>();
        try {
            JSONArray jsonArray = json.getJSONArray("results");
            for (int i=0; i < jsonArray.length(); i++) {
                Movie m = new Movie();
                JSONObject movieJSON = jsonArray.getJSONObject(i);

                m.setId(movieJSON.getInt("id"));
                m.setOverview(movieJSON.getString("overview"));
                m.setReleaseDate(movieJSON.getString("release_date"));
                m.setTitle(movieJSON.getString("original_title"));
                m.setVoteAvg(movieJSON.getDouble("vote_average"));
                String path = movieJSON.getString("poster_path");
                m.setPosterPath("http://image.tmdb.org/t/p/w185/"+path);

                parsedMovies.add(m);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return parsedMovies;
    }
}
