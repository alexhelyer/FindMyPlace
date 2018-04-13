package com.example.alejandro.findmyplace;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.alejandro.findmyplace.saved_places.Category;
import com.example.alejandro.findmyplace.saved_places.CategoryAdapter;
import com.example.alejandro.findmyplace.saved_places.PlaceAdapter;
import com.example.alejandro.findmyplace.saved_places.SortingManager;
import com.example.alejandro.findmyplace.sql.DBHelper;
import com.example.alejandro.findmyplace.sql.FeedEntry;
import com.example.alejandro.findmyplace.sql.SqlController;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedPlacesActivity extends AppCompatActivity implements CategoryAdapter.OnItemClickListener,
        PlaceAdapter.OnClickListener,TextWatcher{

    @BindView(R.id.saved_search_view)
    EditText searchEditText;
    @BindView(R.id.saved_category_recycler)
    RecyclerView categoryRecycler;
    @BindView(R.id.saved_places)
    ListView placesList;
    @BindView(R.id.saved_progress_bar)
    ProgressBar placesProgressBar;

    private PlaceAdapter placeAdapter;
    private List<Place> placeList = new ArrayList<>();

    private CategoryAdapter categoryAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Category> categories;

    private LatLng currentLocation;
    private SqlController sqlController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_places);
        ButterKnife.bind(this);
        sqlController = new SqlController(this);

        Bundle extras = getIntent().getExtras();
        //Double latitude = extras.getDouble(getString(R.string.latitude_key));
       // Double longitude = extras.getDouble(getString(R.string.longitude_key));
        //currentLocation = new LatLng(latitude,longitude);
        currentLocation = new LatLng(19.90,-99.90);

        searchEditText.addTextChangedListener(this);

        categories = getCategoryList();
        categoryRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        categoryRecycler.setLayoutManager(layoutManager);
        categoryAdapter = new CategoryAdapter(categories,this);
        categoryRecycler.setAdapter(categoryAdapter);

        placeAdapter = new PlaceAdapter(this,placeList,this,currentLocation);
        placesList.setAdapter(placeAdapter);

        Cursor cursor = sqlController.selectColumnsFromDb(FeedEntry.TABLE_NAME,new String[]{FeedEntry._ID,
                FeedEntry.COLUMN_CATEGORY,FeedEntry.COLUMN_TITLE,FeedEntry.COLUMN_LATITUDE,FeedEntry.COLUMN_LONGITUDE,
                FeedEntry.COLUMN_IMAGEURL});
        showPlacesList(cursor);
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    private void showPlacesList(Cursor cursor) {
        getListFromCursor(cursor);
        sortPlacesList();
    }

    private void getListFromCursor(Cursor cursor) {
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            placeList.add(new Place(cursor,true));
        }
    }

    private void sortPlacesList() {
        Place[] placeArray = new Place[placeList.size()];
        placeArray = placeList.toArray(placeArray);

        SortingManager sortingManager = new SortingManager(placeArray,Place.class,currentLocation);
        placeList = sortingManager.mergeSort();
        placesProgressBar.setVisibility(View.GONE);
        placeAdapter.notifyDataSetChanged();
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    private List<Category> getCategoryList() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(R.drawable.restaurant,getString(R.string.restaurant)));
        categories.add(new Category(R.drawable.hotel,getString(R.string.hotel)));
        categories.add(new Category(R.drawable.bar,getString(R.string.bar)));
        categories.add(new Category(R.drawable.gas,getString(R.string.gas)));
        categories.add(new Category(R.drawable.cafe,getString(R.string.cafe)));
        categories.add(new Category(R.drawable.airport,getString(R.string.airport)));
        return categories;
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    //Handles category click
    @Override
    public void OnItemClick(Category category, int position) {
        placesProgressBar.setVisibility(View.VISIBLE);
        Cursor cursor = sqlController.readData(FeedEntry.TABLE_NAME,new String[]{FeedEntry._ID,
                        FeedEntry.COLUMN_CATEGORY,FeedEntry.COLUMN_TITLE,FeedEntry.COLUMN_LATITUDE,FeedEntry.COLUMN_LONGITUDE,
                        FeedEntry.COLUMN_IMAGEURL},FeedEntry.COLUMN_CATEGORY + " = " + String.valueOf(position),null    ,
                null,null,null,null);
        showPlacesList(cursor);
    }

    //Handles when clicking on place location button
    @Override
    public void OnClick(int position) {

    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        placesProgressBar.setVisibility(View.VISIBLE);
        Cursor cursor = sqlController.readData(FeedEntry.TABLE_NAME,new String[]{FeedEntry._ID,
                FeedEntry.COLUMN_CATEGORY,FeedEntry.COLUMN_TITLE,FeedEntry.COLUMN_LATITUDE,FeedEntry.COLUMN_LONGITUDE,
                FeedEntry.COLUMN_IMAGEURL},FeedEntry.COLUMN_TITLE + " LIKE '%" +s.toString().trim() + "%'",null,
                null,null,null,null);
        showPlacesList(cursor);
    }

    @Override
    public void afterTextChanged(Editable s) {}

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

}
