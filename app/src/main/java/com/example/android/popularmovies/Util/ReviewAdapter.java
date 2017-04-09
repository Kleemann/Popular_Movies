package com.example.android.popularmovies.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.popularmovies.Models.Review;
import com.example.android.popularmovies.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by makle on 09/04/2017.
 */

public class ReviewAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<Review> mReviews;

    public ReviewAdapter(Context context, ArrayList<Review> reviews) {
        mContext = context;
        mReviews = reviews;
    }

    @Override
    public int getCount() {
        if (mReviews == null) {return 0;}
        return mReviews.size();
    }

    @Override
    public Object getItem(int i) {
        return mReviews.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Review review = mReviews.get(i);
        if (view == null) {
            final LayoutInflater l = LayoutInflater.from(mContext);
            view = l.inflate(R.layout.review_list_item, viewGroup, false);
        }
        final TextView authorTextView = (TextView) view.findViewById(R.id.authorName);
        authorTextView.setText(review.getAuthor());
        final TextView contentTextView = (TextView) view.findViewById(R.id.contentText);
        contentTextView.setText(review.getContent());
        return view;
    }
}
