package com.example.alejandro.findmyplace;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.alejandro.findmyplace.saved_places.Category;
import com.example.alejandro.findmyplace.saved_places.CategoryAdapter;
import com.example.alejandro.findmyplace.saved_places.PlaceAdapter;
import com.example.alejandro.findmyplace.saved_places.SortingManager;
import com.example.alejandro.findmyplace.sql.FeedEntry;
import com.example.alejandro.findmyplace.sql.SqlController;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedPlacesActivity extends AppCompatActivity implements CategoryAdapter.OnItemClickListener,
        PlaceAdapter.OnClickListener,TextWatcher, AdapterView.OnItemClickListener{

    private static final int DELETE = 0;

    @BindView(R.id.saved_search_view)
    EditText searchEditText;
    @BindView(R.id.saved_category_recycler)
    RecyclerView categoryRecycler;
    @BindView(R.id.saved_places)
    ListView placeListView;
    @BindView(R.id.saved_progress_bar)
    ProgressBar placesProgressBar;
    @BindView(R.id.list_empty_view)
    TextView emptyView;

    private PlaceAdapter placeAdapter;
    private List<Place> placeList;
    private List<Place> sortedPlaceList = new LinkedList<>();

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
        placeList = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        Double latitude = extras.getDouble(getString(R.string.latitude_key));
        Double longitude = extras.getDouble(getString(R.string.longitude_key));
        currentLocation = new LatLng(latitude,longitude);
        //currentLocation = new LatLng(19.90,-99.90);

        searchEditText.addTextChangedListener(this);

        categories = getCategoryList();
        categoryRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        categoryRecycler.setLayoutManager(layoutManager);
        categoryAdapter = new CategoryAdapter(categories,this);
        categoryRecycler.setAdapter(categoryAdapter);



        placeListView.setEmptyView(emptyView);
        placeListView.setOnItemClickListener(this);
        registerForContextMenu(placeListView);

        notifyDataSetChanged();
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    private void notifyDataSetChanged(){
        Cursor cursor = sqlController.selectColumnsFromDb(FeedEntry.TABLE_NAME,new String[]{FeedEntry._ID,
                FeedEntry.COLUMN_CATEGORY,FeedEntry.COLUMN_TITLE,FeedEntry.COLUMN_LATITUDE,FeedEntry.COLUMN_LONGITUDE,
                FeedEntry.COLUMN_IMAGE_URI});
        showPlacesList(cursor);
    }

    private void showPlacesList(Cursor cursor) {
        placeList.clear();
        if(cursor.getCount()>0) {
            getListFromCursor(cursor);
            getSortedList();
            placeAdapter = new PlaceAdapter(this,sortedPlaceList,this,currentLocation);
            placeListView.setAdapter(placeAdapter);
        } else {
            sortedPlaceList = new LinkedList<>();
            if(placeListView.getAdapter()!=null)
                placeAdapter.clear();
        }
        placesProgressBar.setVisibility(View.GONE);
    }


    private void getListFromCursor(Cursor cursor) {
        placeList.add(new Place(cursor,true));
        while (cursor.moveToNext()){
            placeList.add(new Place(cursor,true));
        }
    }

    private void getSortedList() {
        Place[] places = new Place[placeList.size()];
        places = placeList.toArray(places);
        SortingManager sortingManager = new SortingManager(places,Place.class,currentLocation);
        sortedPlaceList = sortingManager.mergeSort();
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    private List<Category> getCategoryList() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(R.drawable.all,getString(R.string.all)));
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
        if(position!=0){
        Cursor cursor = sqlController.readData(FeedEntry.TABLE_NAME,new String[]{FeedEntry._ID,
                        FeedEntry.COLUMN_CATEGORY,FeedEntry.COLUMN_TITLE,FeedEntry.COLUMN_LATITUDE,FeedEntry.COLUMN_LONGITUDE,
                        FeedEntry.COLUMN_IMAGE_URI},FeedEntry.COLUMN_CATEGORY + " = " + String.valueOf(position-1),null    ,
                null,null,null,null);
        searchEditText.setText("");
        showPlacesList(cursor);
        } else
            notifyDataSetChanged();

        placesProgressBar.setVisibility(View.GONE);
    }

    //Handles when clicking on place location button
    @Override
    public void OnClick(int position) {
        Place currentPlace = sortedPlaceList.get(position);
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.latitude_key),currentPlace.getLocation().latitude);
        intent.putExtra(getString(R.string.longitude_key),currentPlace.getLocation().longitude);
        //startActivity(intent);
        setResult(102, intent);
        finish();
    }


    //Opens the detail activity
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,DetailActivity.class);
        intent.putExtra(getString(R.string.id_key),sortedPlaceList.get(position).getId());
        startActivity(intent);
    }
    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE,DELETE,Menu.NONE,getString(R.string.delete));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo element = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Place currentPlace = sortedPlaceList.get(element.position);
        switch (item.getItemId()){
            case DELETE:
                sqlController.deleteDataWhereId(FeedEntry.TABLE_NAME,String.valueOf(currentPlace.getId()));
                sortedPlaceList.remove(currentPlace);
                placeAdapter.notifyDataSetChanged();
                break;

        }
        return super.onContextItemSelected(item);
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(!s.equals("")) {
            placesProgressBar.setVisibility(View.VISIBLE);
            Cursor cursor = sqlController.readData(FeedEntry.TABLE_NAME, new String[]{FeedEntry._ID,
                            FeedEntry.COLUMN_CATEGORY, FeedEntry.COLUMN_TITLE, FeedEntry.COLUMN_LATITUDE, FeedEntry.COLUMN_LONGITUDE,
                            FeedEntry.COLUMN_IMAGE_URI}, FeedEntry.COLUMN_TITLE + " LIKE '%" + s.toString().trim() + "%'", null,
                    null, null, null, null);
            showPlacesList(cursor);
            placesProgressBar.setVisibility(View.GONE);
        }else {
            Cursor cursor = sqlController.selectColumnsFromDb(FeedEntry.TABLE_NAME,new String[]{FeedEntry._ID,
                    FeedEntry.COLUMN_CATEGORY,FeedEntry.COLUMN_TITLE,FeedEntry.COLUMN_LATITUDE,FeedEntry.COLUMN_LONGITUDE,
                    FeedEntry.COLUMN_IMAGE_URI});
            showPlacesList(cursor);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {}

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

}
