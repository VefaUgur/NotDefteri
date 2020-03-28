package com.example.colorsplit;

import android.graphics.Bitmap;

public class Image {
    private Bitmap bitmap;
    private String dominantC;
    private int distance;

    public Image(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap uri) {
        this.bitmap = uri;
    }

    public String getDominantC() {
        return dominantC;
    }

    public void setDominantC(String type) {
        this.dominantC = type;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
