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
    String imageUri;
    int id;

    public Place() {
    }

    public Place(int category, LatLng location, String title, String description, String imageUri) {
        this.category = category;
        this.location = location;
        this.title = title;
        this.description = description;
        this.imageUri = imageUri;
    }

    public Place(Cursor cursor) {
        category = cursor.getInt(cursor.getColumnIndex(FeedEntry.COLUMN_CATEGORY));
        location = new LatLng(cursor.getDouble(cursor.getColumnIndex(FeedEntry.COLUMN_LATITUDE)), cursor.getDouble(cursor.getColumnIndex(FeedEntry.COLUMN_LONGITUDE)));
        title = cursor.getString(cursor.getColumnIndex(FeedEntry.COLUMN_TITLE));
        description = cursor.getString(cursor.getColumnIndex(FeedEntry.COLUMN_DESCRIPTION));
        imageUri = cursor.getString(cursor.getColumnIndex(FeedEntry.COLUMN_IMAGE_URI));
        id = cursor.getInt(cursor.getColumnIndex(FeedEntry._ID));
    }

    public Place(Cursor cursor,boolean isPlaceAdapter) {
        category = cursor.getInt(cursor.getColumnIndex(FeedEntry.COLUMN_CATEGORY));
        location = new LatLng(cursor.getDouble(cursor.getColumnIndex(FeedEntry.COLUMN_LATITUDE)), cursor.getDouble(cursor.getColumnIndex(FeedEntry.COLUMN_LONGITUDE)));
        title = cursor.getString(cursor.getColumnIndex(FeedEntry.COLUMN_TITLE));
        imageUri = cursor.getString(cursor.getColumnIndex(FeedEntry.COLUMN_IMAGE_URI));
        id = cursor.getInt(cursor.getColumnIndex(FeedEntry._ID));
    }

    public int getId() {
        return id;
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

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
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
        contentValues.put(FeedEntry.COLUMN_IMAGE_URI, imageUri);

        return contentValues;
    }

}
