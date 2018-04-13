package com.example.alejandro.findmyplace;

import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.alejandro.findmyplace.sql.FeedEntry;
import com.example.alejandro.findmyplace.sql.SqlController;
import com.google.android.gms.maps.model.LatLng;

public class AddPlaceActivity extends AppCompatActivity {

    //Views
    Button category;
    EditText title;
    EditText description;
    ImageView photo;
    Button addPlace;

    SqlController sqlController;

    //Place Data
    Place newPlace;

    //Values
    LatLng myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        sqlController = new SqlController(this);
        newPlace = new Place();
        //Set newPlace values

        category = findViewById(R.id.btnCategory);
        title = findViewById(R.id.inputTitle);
        description = findViewById(R.id.inputDescription);
        photo = findViewById(R.id.imagePhoto);
        addPlace = findViewById(R.id.btnSave);

        myLocation = new LatLng(getIntent().getDoubleExtra("LATITUDE", 0), getIntent().getDoubleExtra("LONGITUDE", 0));


        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCategory();
            }
        });

        addPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textTitle = title.getText().toString();
                String textDescription = description.getText().toString();

                if ( !textTitle.isEmpty() && !textDescription.isEmpty() ) {
                    newPlace.setLocation(myLocation);
                    newPlace.setTitle(textTitle);
                    newPlace.setDescription(textDescription);

                    sqlController.saveData(FeedEntry.TABLE_NAME,newPlace);

                    Toast.makeText(AddPlaceActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddPlaceActivity.this, "¡Por favor, llena todos los campos!", Toast.LENGTH_SHORT).show();
                }

            }
        });


        newPlace.setLocation(myLocation);

        Toast.makeText(this, "Location: "+myLocation.latitude+","+myLocation.longitude, Toast.LENGTH_SHORT).show();

    }

    public void selectCategory() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.category_menu,null);

        //Views
        final ImageView restaurant = view.findViewById(R.id.ic_restaurant);
        ImageView hotel = view.findViewById(R.id.ic_hotel);
        ImageView bar = view.findViewById(R.id.ic_bar);
        ImageView gas = view.findViewById(R.id.ic_gas);
        ImageView cafe = view.findViewById(R.id.ic_cafe);
        ImageView flight = view.findViewById(R.id.ic_flight);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert
                .setView(view)
                .setCancelable(true)
                .show();

        restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restaurant.setBackgroundColor(Color.BLUE);
                
            }
        });




    }
}
