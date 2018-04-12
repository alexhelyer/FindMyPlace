package com.example.alejandro.findmyplace.saved_places;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.alejandro.findmyplace.Place;

import org.json.JSONObject;

/**
 * Created by Soriano on 11/04/18.
 */

public class PlacesApiHandler {

    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=";
    public static final String LOCATION = "&location=";
    public static final String RADIUS = "&radius=100";
    public static final String API_KEY = "&key=AIzaSyA7m5YQp_OQXvZ7DzylErwubKq7BhIVUcs";


    public static String getImageUrl(Place place,Context context){
        String requestingUrl = getRequestingUrl(place);

        VolleySingleton singleton = VolleySingleton.getInstance(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, requestingUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                return;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        singleton.addToRequestQueue(request);
        return request.toString();

    }

    private static String getRequestingUrl(Place place){
        String address = place.getAddress();
        double latitude = place.getLocation().latitude;
        double longitude = place.getLocation().longitude;

        return BASE_URL + address + LOCATION + String.valueOf(latitude) + "," + String.valueOf(longitude) + RADIUS + API_KEY;
    }

    private String parseResponse(JSONObject response){
        return "faskhldfj";
    }
}
