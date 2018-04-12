package com.example.alejandro.findmyplace;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.alejandro.findmyplace.sql.FeedEntry;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

/**
 * Created by alejandro on 11/04/18.
 */

public class Place {

    int category;
    LatLng location;
    String title;
    String description;
    String address;
    String imageUrl;

    public Place() {
    }

    public Place(int category, LatLng location, String title, String description, String address, String imageUrl) {
        this.category = category;
        this.location = location;
        this.title = title;
        this.description = description;
        this.address = address;
        this.imageUrl = imageUrl;
    }


    public Place(Cursor cursor) {
        category = cursor.getInt(cursor.getColumnIndex(FeedEntry.COLUMN_CATEGORY));
        location = new LatLng(cursor.getDouble(cursor.getColumnIndex(FeedEntry.COLUMN_LATITUDE)), cursor.getDouble(cursor.getColumnIndex(FeedEntry.COLUMN_LONGITUDE)));
        title = cursor.getString(cursor.getColumnIndex(FeedEntry.COLUMN_TITLE));
        description = cursor.getString(cursor.getColumnIndex(FeedEntry.COLUMN_DESCRIPTION));
        address = cursor.getString(cursor.getColumnIndex(FeedEntry.COLUMN_ADDRESS));
        imageUrl = cursor.getString(cursor.getColumnIndex(FeedEntry.COLUMN_IMAGEURL));
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getDistance(LatLng fromLocation){
        return (int) SphericalUtil.computeDistanceBetween(fromLocation,location);
    }


    public ContentValues toContentValues(){
        ContentValues contentValues = new ContentValues();

        contentValues.put(FeedEntry.COLUMN_CATEGORY, category);
        contentValues.put(FeedEntry.COLUMN_LATITUDE, location.latitude);
        contentValues.put(FeedEntry.COLUMN_LONGITUDE, location.longitude);
        contentValues.put(FeedEntry.COLUMN_TITLE, title);
        contentValues.put(FeedEntry.COLUMN_DESCRIPTION, description);
        contentValues.put(FeedEntry.COLUMN_ADDRESS, address);
        contentValues.put(FeedEntry.COLUMN_IMAGEURL, imageUrl);

        return contentValues;
    }

}
