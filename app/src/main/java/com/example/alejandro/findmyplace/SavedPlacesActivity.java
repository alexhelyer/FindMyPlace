package com.example.alejandro.findmyplace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedPlacesActivity extends AppCompatActivity {

    @BindView(R.id.saved_search_view)
    EditText searchEditText;
    @BindView(R.id.saved_category_recycler)
    RecyclerView categoryRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_places);
        ButterKnife.bind(this);
    }
}
