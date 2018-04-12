package com.example.alejandro.findmyplace;

import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by alejandro on 11/04/18.
 */

public class Localizacion implements LocationListener {

    Place place;

    public Localizacion(Place place) {
        this.place = place;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("Location",location.getLatitude()+":"+location.getLongitude());
        place.setLocation(new LatLng(location.getLatitude(), location.getLongitude()));
        //new Geocoder();
        //Geocoder geocoder = new Geocoder(this,location.);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


}
