package com.example.alejandro.findmyplace;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.alejandro.findmyplace.saved_places.ParsingTask;
import com.example.alejandro.findmyplace.saved_places.PlacesApiHandler;
import com.example.alejandro.findmyplace.saved_places.VolleySingleton;
import com.example.alejandro.findmyplace.sql.FeedEntry;
import com.example.alejandro.findmyplace.sql.SqlController;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.loopj.android.http.AsyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    private FloatingActionButton addPlaceButton;

    LocationManager locationManager;
    Localizacion localizacion;

    private Place place;

    //Constant - PERMISSION
    static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 43;
    static final int MY_REQUEST_CODE = 100;

    //Marker
    Marker marker;
    MarkerOptions myMarker;
    Polyline myRoutePolyline;


    private SqlController sqlController;

    //List MArkers
    List<Place> listMarkers;

    //Flags
    boolean isMapReady = false;

    //Cursor
    Cursor cursor;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        sqlController = new SqlController(this);


        addPlaceButton = findViewById(R.id.main_float_button);
        addPlaceButton.setOnClickListener(this);

        place = new Place();
        //place.setLocation(new LatLng(-34, 151));
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        localizacion = new Localizacion(place);

        FloatingActionButton btnLocation = findViewById(R.id.btnMyLocation);
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (place.getLocation() != null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.getLocation().latitude, place.getLocation().longitude), 17.5f));
                    marker.setPosition(place.getLocation());
                    //drawRoute(new LatLng(19.40,-99.16), new LatLng(19.35,-99.12));
                } else {
                    Toast.makeText(MapsActivity.this, "Obteniendo ubicacion...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == 0) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, localizacion);
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
        }



        //========================



        listMarkers = new ArrayList<>();


        //Toast.makeText(this, "mark:"+markPlace.getTitle(), Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, localizacion);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)==0) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, localizacion);
        }

        if (isMapReady) {
            printMarkers();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        locationManager.removeUpdates(localizacion);
        mMap.clear();
        myMarker = new MarkerOptions().position(new LatLng(-34,151)).icon(BitmapDescriptorFactory.fromResource(R.drawable.location));
        marker = mMap.addMarker(myMarker);
        localizacion.setMarker(marker);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.saved_places:
                Intent intent = new Intent(this,SavedPlacesActivity.class);
                intent.putExtra(getString(R.string.latitude_key), place.getLocation().latitude);
                intent.putExtra(getString(R.string.longitude_key), place.getLocation().longitude);
                startActivityForResult(intent, MY_REQUEST_CODE);
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main_float_button:

                if (place.getLocation()!=null) {
                    Intent intent = new Intent(this,AddPlaceActivity.class);
                    intent.putExtra("LATITUDE", place.getLocation().latitude);
                    intent.putExtra("LONGITUDE", place.getLocation().longitude);
                    startActivityForResult(intent, MY_REQUEST_CODE);
                } else {
                    Toast.makeText(this, "Aun no se ha detectado el GPS", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode== MY_REQUEST_CODE) {
            switch (resultCode) {
                case 101:
                    Toast.makeText(this, "Marker saved", Toast.LENGTH_SHORT).show();
                    break;
                case 102:
                    Double toLat = data.getDoubleExtra(getString(R.string.latitude_key),0);
                    Double toLng = data.getDoubleExtra(getString(R.string.longitude_key), 0);

                    Toast.makeText(this, "Result To Location:"+toLat+","+toLng, Toast.LENGTH_SHORT).show();
                    drawRoute(place.location, new LatLng(toLat,toLng));
                    break;
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-34, 151), 1.0f));


        myMarker = new MarkerOptions().position(new LatLng(-34,151)).icon(BitmapDescriptorFactory.fromResource(R.drawable.location));
        marker = mMap.addMarker(myMarker);
        localizacion.setMarker(marker);

        isMapReady = true;

        printMarkers();
    }

    public void printMarkers() {

        cursor = sqlController.selectColumnsFromDb(FeedEntry.TABLE_NAME,new String[]{FeedEntry._ID,
                FeedEntry.COLUMN_CATEGORY,FeedEntry.COLUMN_TITLE,FeedEntry.COLUMN_LATITUDE,FeedEntry.COLUMN_LONGITUDE,
                FeedEntry.COLUMN_IMAGE_URI});

        Place markPlace = new Place(cursor, true);
        mMap.addMarker(new MarkerOptions().position(markPlace.getLocation()).title(markPlace.getTitle()));
        while (cursor.moveToNext()) {
            markPlace = new Place(cursor, true);
            mMap.addMarker(new MarkerOptions().position(markPlace.getLocation()).title(markPlace.getTitle()));
        }

    }

    public void drawRoute(LatLng _from, LatLng _to) {
        LatLng from = _from; /*new LatLng(19.40,-99.16);*/
        LatLng to = _to; /*new LatLng(19.35,-99.12);*/
        String url = PlacesApiHandler.getDirectionsUrl(from,to);
        //String url = "http://maps.googleapis.com/maps/api/directions/json?origin=" + from.latitude+","+from.longitude + "&destination=" + to.latitude+","+to.longitude + "&sensor=false";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //new ParsingTask(MapsActivity.this).execute(new JSONObject[]{response});


                try {
                    JSONArray routesArray = response.getJSONArray("routes");
                    JSONObject route = routesArray.getJSONObject(0);
                    JSONObject poly = route.getJSONObject("overview_polyline");
                    String polyline = poly.getString("points");

                    List<LatLng> points = PlacesApiHandler.decodePolyline(polyline);

                    Log.w("Polyline:", points.get(0).latitude+","+points.get(0).longitude);

                    PolylineOptions myRoute = new PolylineOptions();
                    for (LatLng point:points) {
                        myRoute.add(point);
                    }
                    myRoutePolyline =  mMap.addPolyline(myRoute);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

}
