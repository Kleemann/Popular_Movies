package com.example.android.popularmovies.Models;

/**
 * Created by makle on 01/04/2017.
 */

public class Review {

    String id;
    String author;
    String content;

    public Review() {
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
