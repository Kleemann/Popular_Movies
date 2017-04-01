package com.example.android.popularmovies.Networking;

import com.example.android.popularmovies.Models.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by makle on 01/04/2017.
 */

public class TrailerParser {
    public static ArrayList<Trailer> parse(JSONObject json) {

        ArrayList<Trailer> parsedTrailers = new ArrayList<>();
        try {
            JSONArray jsonArray = json.getJSONArray("results");
            for (int i=0; i < jsonArray.length(); i++) {
                Trailer t = new Trailer();
                JSONObject trailerJSON = jsonArray.getJSONObject(i);

                t.setKey(trailerJSON.getString("type"));
                t.setName(trailerJSON.getString("name"));
                t.setSite(trailerJSON.getString("site"));
                t.setType(trailerJSON.getString("type"));

                parsedTrailers.add(t);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return parsedTrailers;
    }
}
