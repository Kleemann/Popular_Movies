package com.example.android.popularmovies.Networking;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkManager {

    private static OkHttpClient client = new OkHttpClient();

    public static JSONObject getResponseJSON(URL url) throws IOException, JSONException {
        Request request = new Request.Builder().url(url).build();
        Response responses = null;
        responses = client.newCall(request).execute();
        String jsonData = responses.body().string();
        JSONObject j = new JSONObject(jsonData);

        return j;
    }

}
