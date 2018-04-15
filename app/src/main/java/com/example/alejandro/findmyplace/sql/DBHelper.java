package com.example.alejandro.findmyplace.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.alejandro.findmyplace.Place;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by diego on 12/04/18.
 */

public class DBHelper extends SQLiteOpenHelper {

    private Context context;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "myplaces.db";
    private static final String CREATE_WORKER_ENTRIES = "CREATE TABLE " + FeedEntry.TABLE_NAME;
    public static final String COLUMNS = " (" + FeedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + FeedEntry.COLUMN_CATEGORY + " INTEGER NOT NULL,"
            + FeedEntry.COLUMN_LONGITUDE + " DOUBLE NOT NULL,"
            + FeedEntry.COLUMN_LATITUDE + " DOUBLE NOT NULL,"
            + FeedEntry.COLUMN_IMAGE_URI + " TEXT NOT NULL,"
            + FeedEntry.COLUMN_TITLE + " TEXT NOT NULL,"
            + FeedEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL)";

    private static final String DELETE_PENDING_ENTRIES = "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_WORKER_ENTRIES + COLUMNS);
        createRegisters(db);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(DELETE_PENDING_ENTRIES);
        onCreate(db);

    }

    private void createRegisters(SQLiteDatabase db){
        SqlController sqlController = new SqlController(context);

        Place place1 = new Place(0,new LatLng(19.4081757,-99.1735575),
                "Campeche 300","Na At Technologies",
                "https://i0.wp.com/tentulogo.com/wp-content/uploads/na-at.jpg");

        Place place2 = new Place(5,new LatLng(19.427638,-99.0940633),
                "Airport CDMX","Mexico City International Airport",
                "http://ciudadanosenred.com.mx/wp-content/uploads/2015/11/NOT93.jpg");

        Place place3 = new Place(4,new LatLng(19.4359462,-99.1565544),"Café Don Porfirio",
                "Wonderful sight of Bellas Artes","https://imagenescityexpress.scdn6.secure.raxcdn.com/sites/default/files/inline-images/9267662446_dec031ec50_b.jpg");

        db.insert(FeedEntry.TABLE_NAME,null,place1.toContentValues());
        db.insert(FeedEntry.TABLE_NAME,null,place2.toContentValues());
        db.insert(FeedEntry.TABLE_NAME,null,place3.toContentValues());
    }
}
