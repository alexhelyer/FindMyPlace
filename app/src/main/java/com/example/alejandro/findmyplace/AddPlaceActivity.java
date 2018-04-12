package com.example.alejandro.findmyplace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class AddPlaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        Double getLatitude = getIntent().getDoubleExtra("LATITUDE", 0);
        Double getLongitude = getIntent().getDoubleExtra("LONGITUDE", 0);

        Toast.makeText(this, "Location: "+getLatitude+","+getLongitude, Toast.LENGTH_SHORT).show();



        //Esto es un comentario de Alex





    }
}
