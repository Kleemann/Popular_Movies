package com.example.android.popularmovies.Models;

/**
 * Created by makle on 01/04/2017.
 */

public class Trailer {

    String type;
    String name;
    String site;
    String key;


    public Trailer() {
    }

    public String getKey() {
        return key;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
