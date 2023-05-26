package com.example.lostandfound;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.example.lostandfound.room.LostObjectData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class ShowMap extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    DatabaseHelper databaseHelper;
    List<LostObjectData> lostItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);
        //creates a support map fragment that will show the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //initialises database helper for this class and creates a list of all items
        databaseHelper = new DatabaseHelper(this);
        lostItems = databaseHelper.getAllItems();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //seed an initial marker for deakin uni, also moves camera to a fixed location
        LatLng deakinUni = new LatLng(-38.1439, 144.3603);
        mMap.addMarker(new MarkerOptions().position(deakinUni).title("Deakin University"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(deakinUni, 15));

        //for each value in the list
        for(LostObjectData data : lostItems){
            //get string location
            String locationString = data.getLocation();
            //split the string which is stored as latitude,longitude at the comma
            String[] locationConverted = locationString.split(",");
            //store each part as separate variables
            double latitude = Double.parseDouble(locationConverted[0]);
            double longitude = Double.parseDouble(locationConverted[1]);
            //create new latLng object with the correct lat/lng
            LatLng item = new LatLng(latitude, longitude);
            //add a marker for said lat/lng with the title being the items description
            mMap.addMarker(new MarkerOptions().position(item).title(data.getDescription()));
        }
    }
}