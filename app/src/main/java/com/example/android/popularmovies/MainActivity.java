package com.example.android.popularmovies;

import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.popularmovies.Models.Movie;
import com.example.android.popularmovies.Models.Trailer;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieManager.MovieManagerListener {

    MovieManager manager = new MovieManager(this, null, this);
    GridView gridView;
    private String mCurrentSorting;

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

        getMovies(MovieSorting.TOP_RATED);
    }

    private void getMovies(String sorting) {
        if (sorting.equals(mCurrentSorting) ) {return;}
        mCurrentSorting = sorting;
        manager.getMovies(mCurrentSorting);
    }

    @Override
    public void moviesFetches(ArrayList<Movie> movies) {
        final MoviesAdapter adapter = new MoviesAdapter(this, movies);
        gridView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_sort, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selected = item.getItemId();
        if (selected == R.id.menuSortPopular) {getMovies(MovieSorting.POPULAR);}
        else if (selected == R.id.menuSortTop){getMovies(MovieSorting.TOP_RATED);}
        else if (selected == R.id.menuFavorites) {
            getMovies(MovieSorting.FAVORITES);
        }
        return true;
    }
}
