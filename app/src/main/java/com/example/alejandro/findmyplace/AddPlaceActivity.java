package com.example.alejandro.findmyplace;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.alejandro.findmyplace.sql.FeedEntry;
import com.example.alejandro.findmyplace.sql.SqlController;
import com.google.android.gms.maps.model.LatLng;

public class AddPlaceActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 100;
    //Views
    ImageView category;
    EditText title;
    EditText description;
    ImageView photo;
    Button addPlace;

    SqlController sqlController;

    //Place Data
    Place newPlace;

    //Values
    LatLng myLocation;
    int idCategory;
    int categoryValue;
    String datosPhoto;

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
        idCategory = -1;
        datosPhoto = null;

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCategory();
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               checkPermissionForPicture();
            }
        });

        addPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textTitle = title.getText().toString();
                String textDescription = description.getText().toString();

                if ( !textTitle.isEmpty() && !textDescription.isEmpty() && idCategory!=-1 && datosPhoto!=null ) {
                    newPlace.setLocation(myLocation);
                    newPlace.setTitle(textTitle);
                    newPlace.setDescription(textDescription);
                    newPlace.setImageUri(datosPhoto);
                    newPlace.setCategory(idCategory);

                    sqlController.saveData(FeedEntry.TABLE_NAME,newPlace);

                    Toast.makeText(AddPlaceActivity.this, "Saved", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent();
                    intent.putExtra("resultLat", newPlace.getLocation().latitude);
                    intent.putExtra("resultLon", newPlace.getLocation().longitude);
                    intent.putExtra("resultTitle", newPlace.getTitle());
                    setResult(101, intent);

                    finish();
                } else {
                    Toast.makeText(AddPlaceActivity.this, "¡Por favor, llena todos los campos!", Toast.LENGTH_SHORT).show();
                }

            }
        });


        newPlace.setLocation(myLocation);

        Toast.makeText(this, "Location: "+myLocation.latitude+","+myLocation.longitude, Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case 100:
                    Uri selectedImage = data.getData();
                    Glide.with(this)
                            .load(selectedImage)
                            .asBitmap()
                            .fitCenter()
                            .into(new BitmapImageViewTarget(photo));
                    datosPhoto = selectedImage.toString();
                    break;
            }
        }
    }

    public void selectCategory() {


        categoryValue = -1;
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.category_menu,null);

        //Views
        final ImageView restaurant = view.findViewById(R.id.ic_restaurant);
        final ImageView hotel = view.findViewById(R.id.ic_hotel);
        final ImageView bar = view.findViewById(R.id.ic_bar);
        final ImageView gas = view.findViewById(R.id.ic_gas);
        final ImageView cafe = view.findViewById(R.id.ic_cafe);
        final ImageView flight = view.findViewById(R.id.ic_flight);

        switch (idCategory) {
            case 0:
                restaurant.setBackgroundColor(Color.parseColor("#AAAAAA"));
                break;
            case 1:
                hotel.setBackgroundColor(Color.parseColor("#AAAAAA"));
                break;
            case 2:
                bar.setBackgroundColor(Color.parseColor("#AAAAAA"));
                break;
            case 3:
                gas.setBackgroundColor(Color.parseColor("#AAAAAA"));
                break;
            case 4:
                cafe.setBackgroundColor(Color.parseColor("#AAAAAA"));
                break;
            case 5:
                flight.setBackgroundColor(Color.parseColor("#AAAAAA"));
                break;
        }

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert
                .setView(view)
                .setCancelable(true)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        idCategory = categoryValue;

                        switch (idCategory) {
                            case 0:
                                category.setImageResource(R.drawable.restaurant);
                                break;
                            case 1:
                                category.setImageResource(R.drawable.hotel);
                                break;
                            case 2:
                                category.setImageResource(R.drawable.bar);
                                break;
                            case 3:
                                category.setImageResource(R.drawable.gas);
                                break;
                            case 4:
                                category.setImageResource(R.drawable.cafe);
                                break;
                            case 5:
                                category.setImageResource(R.drawable.airport);
                                break;
                        }
                    }
                })
                .show();

        restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restaurant.setBackgroundColor(Color.parseColor("#FFFFFF"));
                hotel.setBackgroundColor(Color.parseColor("#FFFFFF"));
                bar.setBackgroundColor(Color.parseColor("#FFFFFF"));
                gas.setBackgroundColor(Color.parseColor("#FFFFFF"));
                cafe.setBackgroundColor(Color.parseColor("#FFFFFF"));
                flight.setBackgroundColor(Color.parseColor("#FFFFFF"));

                restaurant.setBackgroundColor(Color.parseColor("#AAAAAA"));
                categoryValue = 0;
            }
        });
        hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restaurant.setBackgroundColor(Color.parseColor("#FFFFFF"));
                hotel.setBackgroundColor(Color.parseColor("#FFFFFF"));
                bar.setBackgroundColor(Color.parseColor("#FFFFFF"));
                gas.setBackgroundColor(Color.parseColor("#FFFFFF"));
                cafe.setBackgroundColor(Color.parseColor("#FFFFFF"));
                flight.setBackgroundColor(Color.parseColor("#FFFFFF"));

                hotel.setBackgroundColor(Color.parseColor("#AAAAAA"));
                categoryValue = 1;
            }
        });
        bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restaurant.setBackgroundColor(Color.parseColor("#FFFFFF"));
                hotel.setBackgroundColor(Color.parseColor("#FFFFFF"));
                bar.setBackgroundColor(Color.parseColor("#FFFFFF"));
                gas.setBackgroundColor(Color.parseColor("#FFFFFF"));
                cafe.setBackgroundColor(Color.parseColor("#FFFFFF"));
                flight.setBackgroundColor(Color.parseColor("#FFFFFF"));

                bar.setBackgroundColor(Color.parseColor("#AAAAAA"));
                categoryValue = 2;
            }
        });
        gas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restaurant.setBackgroundColor(Color.parseColor("#FFFFFF"));
                hotel.setBackgroundColor(Color.parseColor("#FFFFFF"));
                bar.setBackgroundColor(Color.parseColor("#FFFFFF"));
                gas.setBackgroundColor(Color.parseColor("#FFFFFF"));
                cafe.setBackgroundColor(Color.parseColor("#FFFFFF"));
                flight.setBackgroundColor(Color.parseColor("#FFFFFF"));

                gas.setBackgroundColor(Color.parseColor("#AAAAAA"));
                categoryValue = 3;
            }
        });
        cafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restaurant.setBackgroundColor(Color.parseColor("#FFFFFF"));
                hotel.setBackgroundColor(Color.parseColor("#FFFFFF"));
                bar.setBackgroundColor(Color.parseColor("#FFFFFF"));
                gas.setBackgroundColor(Color.parseColor("#FFFFFF"));
                cafe.setBackgroundColor(Color.parseColor("#FFFFFF"));
                flight.setBackgroundColor(Color.parseColor("#FFFFFF"));

                cafe.setBackgroundColor(Color.parseColor("#AAAAAA"));
                categoryValue = 4;
            }
        });
        flight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restaurant.setBackgroundColor(Color.parseColor("#FFFFFF"));
                hotel.setBackgroundColor(Color.parseColor("#FFFFFF"));
                bar.setBackgroundColor(Color.parseColor("#FFFFFF"));
                gas.setBackgroundColor(Color.parseColor("#FFFFFF"));
                cafe.setBackgroundColor(Color.parseColor("#FFFFFF"));
                flight.setBackgroundColor(Color.parseColor("#FFFFFF"));

                flight.setBackgroundColor(Color.parseColor("#AAAAAA"));
                categoryValue = 5;
            }
        });


    }

    private void checkPermissionForPicture(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);

        } else {
            getPictureFromGallery();
        }
    }

    private void getPictureFromGallery(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getPictureFromGallery();

                } else {
                    Toast.makeText(this, getString(R.string.grant_external), Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }
}
