package com.example.alejandro.findmyplace.saved_places;

/**
 * Created by diego on 11/04/18.
 */

public class Category {
    private int image;
    private String title;

    public Category(int image, String title) {
        this.image = image;
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }
}
