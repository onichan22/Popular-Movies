package com.example.fioni.popularmovies;

/**
 * Created by fioni on 8/6/2017.
 */

public class AnObject {
    private String type;
    private String [] list;

    public AnObject() {
    }

    public AnObject(String type, String[] list) {
        this.type = type;
        this.list = list;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getList() {
        return list;
    }

    public void setList(String[] list) {
        this.list = list;
    }
}
