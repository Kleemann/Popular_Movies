package com.example.android.popularmovies;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.popularmovies.Models.Movie;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieManager.MovieManagerListener {

    MovieManager manager = new MovieManager(this);
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = (GridView)findViewById(R.id.gridview);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie m = (Movie) gridView.getAdapter().getItem(i);
                Intent startMovieDetails = new Intent(view.getContext(), MovieDetailsActivity.class);
                startMovieDetails.putExtra("Movie", m);
                startActivity(startMovieDetails);
            }
        });

        manager.getMovies(MovieSorting.TOP_RATED);
    }

    @Override
    public void moviesFetches(ArrayList<Movie> movies) {
        final MoviesAdapter adapter = new MoviesAdapter(this, movies);
        gridView.setAdapter(adapter);
    }
}
