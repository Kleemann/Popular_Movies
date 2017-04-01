package com.example.android.popularmovies.Networking;

import com.example.android.popularmovies.Models.Review;
import com.example.android.popularmovies.Models.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by makle on 01/04/2017.
 */

public class ReviewParser {

    public static ArrayList<Review> parse(JSONObject json) {

        ArrayList<Review> parsedReviews = new ArrayList<>();
        try {
            JSONArray jsonArray = json.getJSONArray("results");
            for (int i=0; i < jsonArray.length(); i++) {
                Review r = new Review();
                JSONObject reviewJSON = jsonArray.getJSONObject(i);

                r.setAuthor(reviewJSON.getString("author"));
                r.setContent(reviewJSON.getString("content"));
                r.setId(reviewJSON.getString("id"));

                parsedReviews.add(r);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return parsedReviews;
    }
}
