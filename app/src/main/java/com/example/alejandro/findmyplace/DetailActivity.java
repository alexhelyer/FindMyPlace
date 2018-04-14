package com.example.alejandro.findmyplace;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.alejandro.findmyplace.saved_places.PlaceAdapter;
import com.example.alejandro.findmyplace.sql.FeedEntry;
import com.example.alejandro.findmyplace.sql.SqlController;
import com.mikhaellopez.circularimageview.CircularImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.detail_image)
    ImageView image;
    @BindView(R.id.detail_category_image)
    CircularImageView categoryImage;
    @BindView(R.id.detail_title)
    TextView titleText;
    @BindView(R.id.detail_description)
    TextView descriptionText;

    private SqlController sqlController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        sqlController = new SqlController(this);

        int id = getIntent().getExtras().getInt(getString(R.string.id_key));
        String[] columns = {FeedEntry.COLUMN_TITLE,FeedEntry.COLUMN_CATEGORY,FeedEntry.COLUMN_LATITUDE,FeedEntry.COLUMN_LONGITUDE,
                            FeedEntry.COLUMN_DESCRIPTION,FeedEntry.COLUMN_IMAGE_URI,FeedEntry._ID};
        Cursor cursor = sqlController.readData(FeedEntry.TABLE_NAME,columns,FeedEntry._ID +"="  + id,null,
                null,null,null,null);
        Place currentPlace = new Place(cursor);

        setUiFields(currentPlace);
    }

    private void setUiFields(Place currentPlace) {
        Glide.with(this)
                .load("")
                .error(PlaceAdapter.getCategoryImage(currentPlace.getCategory()))
                .into(categoryImage);
        Glide.with(this)
                .load(Uri.parse(currentPlace.getImageUri()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(image);
        titleText.setText(currentPlace.getTitle());
        descriptionText.setText(currentPlace.getDescription());
    }

}
