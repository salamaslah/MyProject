package com.example.moviesapp.MoviesTypes;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Movie implements Serializable {
    String id;
    String name;
    String desc;
    String img;
    String category;
    public Movie(){

    }
    public Movie(String name, String desc, String img,String category) {
        this.name = name;
        this.desc = desc;
        this.img = img;
        this.category=category;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
