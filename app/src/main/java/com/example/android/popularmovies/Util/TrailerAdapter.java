package com.example.android.popularmovies.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.popularmovies.Models.Trailer;
import com.example.android.popularmovies.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by makle on 09/04/2017.
 */

public class TrailerAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<Trailer> mTrailers;

    public TrailerAdapter(Context context, ArrayList<Trailer> trailers) {
        mContext = context;
        mTrailers = trailers;
    }

    @Override
    public int getCount() {
        if (mTrailers == null) {return 0;}
        return mTrailers.size();
    }

    @Override
    public Object getItem(int i) {
        return mTrailers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Trailer trailer = mTrailers.get(i);
        if (view == null) {
            final LayoutInflater l = LayoutInflater.from(mContext);
            view = l.inflate(R.layout.trailer_list_item, viewGroup, false);
        }
        final TextView nameTextView = (TextView) view.findViewById(R.id.trailerName);
        nameTextView.setText(trailer.getName());
        return view;
    }


}
