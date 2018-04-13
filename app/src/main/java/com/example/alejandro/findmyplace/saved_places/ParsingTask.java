package com.example.alejandro.findmyplace.saved_places;

import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by diego on 13/04/18.
 */

public class ParsingTask extends AsyncTask<JSONObject,Void,List<List<HashMap<String,String>>>> {

    private ParseHandler parseHandler;

    public ParsingTask(ParseHandler parseHandler) {
        this.parseHandler = parseHandler;
    }

    public interface ParseHandler {
        void handlePolyline(PolylineOptions polylineOptions);
    }

    @Override
    protected List<List<HashMap<String, String>>> doInBackground(JSONObject... jsonObjects) {
        return PlacesApiHandler.parseResponse(jsonObjects[0]);
    }

    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
        ArrayList<LatLng> points = new ArrayList<>();
        PolylineOptions lineOptions = new PolylineOptions();

        for(int i=0;i<lists.size();i++){
            List<HashMap<String, String>> path = lists.get(i);

            for(int j=0;j<path.size();j++){
                HashMap<String,String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }

            lineOptions.addAll(points);
            lineOptions.width(2);
            lineOptions.color(Color.BLUE);
        }

       parseHandler.handlePolyline(lineOptions);
    }
}

