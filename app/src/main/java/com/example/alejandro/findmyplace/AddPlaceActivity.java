package com.example.alejandro.findmyplace;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class AddPlaceActivity extends AppCompatActivity {

    Button categoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        categoria = findViewById(R.id.btnCategory);
        categoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(AddPlaceActivity.this)
                        .setView(R.layout.category_menu)
                        .show();
            }
        });

        Double getLatitude = getIntent().getDoubleExtra("LATITUDE", 0);
        Double getLongitude = getIntent().getDoubleExtra("LONGITUDE", 0);

        Toast.makeText(this, "Location: "+getLatitude+","+getLongitude, Toast.LENGTH_SHORT).show();



        //Esto es un comentario de Alex





    }
}
