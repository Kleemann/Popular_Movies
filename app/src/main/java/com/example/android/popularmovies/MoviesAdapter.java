package com.example.android.popularmovies;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.Models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MoviesAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<Movie> mMovies;

    public MoviesAdapter(Context context, ArrayList<Movie> movies) {
        this.mContext = context;
        this.mMovies = movies;
    }

    @Override
    public int getCount() {
        if (mMovies == null) {return 0;}
        return mMovies.size();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return mMovies.get(i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Movie movie = mMovies.get(position);
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.movie_item, parent, false);
        }
        final ImageView posterImageView = (ImageView) convertView.findViewById(R.id.image_move_poster);
        Picasso.with(convertView.getContext()).load(movie.getPosterPath()).into(posterImageView);
        return convertView;
    }
}
