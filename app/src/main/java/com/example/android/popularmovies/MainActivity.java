package com.example.android.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.Loader;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.popularmovies.Database.MovieContract;
import com.example.android.popularmovies.Models.Movie;
import com.example.android.popularmovies.Util.MovieSorting;
import com.example.android.popularmovies.Util.MoviesAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieManager.MovieManagerListener, LoaderManager.LoaderCallbacks<Cursor> {

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

    @Override
    protected void onResume() {
        super.onResume();
        if (mCurrentSorting == MovieSorting.FAVORITES) {
            getSupportLoaderManager().restartLoader(0, null, this);
        }
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
            mCurrentSorting = MovieSorting.FAVORITES;
            getSupportLoaderManager().initLoader(0, null, this);
        }
        return true;
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this.getBaseContext()) {

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
                    return getContext().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
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
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        ArrayList<Movie> movies = new ArrayList<Movie>();
        try {
            while (c.moveToNext()) {
                Movie m = new Movie();
                m.setTitle(c.getString(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE)));
                m.setId(c.getInt(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)));
                m.setOverview(c.getString(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW)));
                m.setReleaseDate(c.getString(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_YEAR)));
                m.setPosterPath(c.getString(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH)));
                m.setVoteAvg(c.getDouble(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_AVG_VOTE)));
                movies.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            c.close();
        }
        final MoviesAdapter adapter = new MoviesAdapter(this, movies);
        gridView.setAdapter(adapter);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.e("onLoaderReset", " ");
    }
}
