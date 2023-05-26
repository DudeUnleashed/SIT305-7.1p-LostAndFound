package com.example.lostandfound;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.example.lostandfound.room.LostObjectData;

import android.location.Location;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;


public class MakeNewAdvert extends AppCompatActivity {

    Button buttonSaveAdvert, buttonSetCurrentLocation;
    EditText editName, editPhone, editDescription, editDate, editLocation;
    CheckBox checkboxLost, checkboxFound;
    DatabaseHelper databaseHelper;
    String objectType;
    FusedLocationProviderClient fusedLocationClient;
    int REQUEST_LOCATION_PERMISSION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_new_advert);

        buttonSaveAdvert = findViewById(R.id.buttonSubmitAdvert);
        buttonSetCurrentLocation = findViewById(R.id.buttonAddCurrentLocation);

        editName = findViewById(R.id.editTextName);
        editPhone = findViewById(R.id.editTextPhone);
        editDescription = findViewById(R.id.editTextDescription);
        editDate = findViewById(R.id.editTextDate);
        editLocation = findViewById(R.id.editTextPostalAddress);

        checkboxFound = findViewById(R.id.checkBoxFound);
        checkboxLost = findViewById(R.id.checkBoxLost);

        databaseHelper = new DatabaseHelper(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //initializes places api with the apikey
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyC7eM_5i_6RkfforZwMHzOp5G9m67_2dXo");
        }

        //creates the autocomplete fragment used for getting location
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        //set the fields that are retrieved when autocompleting to name, id and lat/lng
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        //listens for when an item is selected
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {

            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                //takes the latlng of the selected option and strips it
                LatLng autoLocation = place.getLatLng();
                String autoLocationString = autoLocation.toString();
                String autoLocationStripped1 = autoLocationString.replaceFirst("lat/lng: \\(", "");
                String autoLocationStripped2 = autoLocationStripped1.substring(0, autoLocationStripped1.length()-1);
                //fills the location text box with the lat,long
                editLocation.setText(autoLocationStripped2);
            }
        });


        buttonSetCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });

        buttonSaveAdvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //checks if lost or found box is ticked, not none, and not both
                if (checkboxFound.isChecked() && checkboxLost.isChecked()){
                    Toast.makeText(getApplicationContext(), "Please check only one post type", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!checkboxLost.isChecked() && !checkboxFound.isChecked()){
                    Toast.makeText(getApplicationContext(), "Please select a post type", Toast.LENGTH_SHORT).show();
                    return;
                }

                //sets the object type to the correct one based on what checkbox is checked
                if (checkboxFound.isChecked()){
                    objectType = "found";
                } else objectType = "lost";

                //checks if fields are empty, if they are, send appropriate toast
                if (editName.getText().toString().matches("")){
                    Toast.makeText(getApplicationContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (editPhone.getText().toString().matches("")){
                    Toast.makeText(getApplicationContext(), "Please enter some contact information", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (editDescription.getText().toString().matches("")){
                    Toast.makeText(getApplicationContext(), "Please enter a description of the item", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (editDate.getText().toString().matches("")){
                    Toast.makeText(getApplicationContext(), "Please enter the date you " + objectType + " it", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (editLocation.getText().toString().matches("")){
                    Toast.makeText(getApplicationContext(), "Please enter the location of the item", Toast.LENGTH_SHORT).show();
                    return;
                }

                //create new instance of data using the input fields
                LostObjectData lostData = new LostObjectData(0, editName.getText().toString(), editPhone.getText().toString(), editDescription.getText().toString(),
                        editDate.getText().toString(), editLocation.getText().toString(), objectType);

                //insert item into the database
                databaseHelper.insertItem(lostData);

                //make toast indicating that it was successful and an item was added
                Toast.makeText(getApplicationContext(), "Item added successfully", Toast.LENGTH_SHORT).show();

                //clear input fields
                editName.setText("");
                editPhone.setText("");
                editDescription.setText("");
                editDate.setText("");
                editLocation.setText("");
            }
        });
    }
    public void getCurrentLocation(){
        //checks for permissions have been granted, if not, request the permissions
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            //gets the most recent location data on the device
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null){
                        //creates a new LatLng object that stores the location
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                        //Strips the latLng result into latitude,longitude for easier retrieval later on
                        String latLngString = latLng.toString();
                        String latLngStripped1 = latLngString.replaceFirst("lat/lng: \\(", "");
                        String latLngStripped2 = latLngStripped1.substring(0, latLngStripped1.length()-1);

                        //sets the text box to the latitude and longitude
                        editLocation.setText(latLngStripped2);
                    }
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }
}