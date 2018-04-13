package com.example.alejandro.findmyplace.saved_places;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.alejandro.findmyplace.Place;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Soriano on 11/04/18.
 */

public class PlacesApiHandler {

    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/directions/json?";
    public static final String ORIGIN = "origin=";
    public static final String DESTINATION = "&destination=";
    public static final String MODE = "&mode=driving";
    public static final String API_KEY = "&key=AIzaSyA7m5YQp_OQXvZ7DzylErwubKq7BhIVUcs";


    public static String getDirectionsUrl(LatLng originLocation,LatLng destinationLocation){
        return BASE_URL + ORIGIN + originLocation.latitude + "," + originLocation.longitude
                + DESTINATION + destinationLocation.latitude + "," + destinationLocation.longitude
                + MODE + API_KEY;
    }

    public static List<List<HashMap<String,String>>> parseResponse(JSONObject response){
        List<List<HashMap<String, String>>> routes = new ArrayList<>();
        JSONArray routesArray;
        JSONArray legsArray;
        JSONArray stepsArray;

        try {

            routesArray = response.getJSONArray("routes");

            // Gets legs array
            for(int i=0;i<routesArray.length();i++){
                legsArray = ( (JSONObject)routesArray.get(i)).getJSONArray("legs");
                List path = new ArrayList<HashMap<String, String>>();

                // Gets steps array
                for(int j=0;j<legsArray.length();j++){
                    stepsArray = ( (JSONObject)legsArray.get(j)).getJSONArray("steps");

                    // Gets polyline string and decodes it
                    for(int k=0;k<stepsArray.length();k++){
                        String polyline = "";
                        polyline = (String)((JSONObject)((JSONObject)stepsArray.get(k)).get("polyline")).get("points");
                        List<LatLng> pointsList = decodePolyline(polyline);

                         //Saving latitude and longitude values into a hashmap
                        for(int l=0;l<pointsList.size();l++){
                            HashMap<String, String> hashmap = new HashMap<>();
                            hashmap.put("lat", Double.toString((pointsList.get(l)).latitude) );
                            hashmap.put("lng", Double.toString((pointsList.get(l)).longitude) );
                            path.add(hashmap);
                        }
                    }
                    routes.add(path);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
        }
        return routes;
    }

    private List<LatLng> decodePolyline(String polylineString) {

        List<LatLng> polyline = new ArrayList<>();
        int index = 0;;
        int polylineLenght = polylineString.length();
        int lat = 0;
        int lng = 0;

        while (index < polylineLenght) {
            int b, shift = 0, result = 0;
            do {
                b = polylineString.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = polylineString.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            polyline.add(p);
        }

        return polyline;
    }
}
