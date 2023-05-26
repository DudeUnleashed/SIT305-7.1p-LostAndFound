package com.example.lostandfound;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    Button buttonNewAdvert, buttonShowItems, buttonShowMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonNewAdvert = findViewById(R.id.buttonCreateNew);
        buttonShowItems = findViewById(R.id.buttonShowAdverts);
        buttonShowMap = findViewById(R.id.buttonShowOnMap);

        buttonShowItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //starts the advert list activity to show all listings
                Intent intentToAdvertList = new Intent(MainActivity.this, ShowAdverts.class);
                startActivity(intentToAdvertList);

            }
        });

        buttonNewAdvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //starts the new advert activity to create a new listing
                Intent intentToNewAdvert = new Intent(MainActivity.this, MakeNewAdvert.class);
                startActivity(intentToNewAdvert);

            }
        });

        buttonShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //starts a new activity that will show the map with all the lost items
                Intent intentToShowMap = new Intent(MainActivity.this, ShowMap.class);
                startActivity(intentToShowMap);

            }
        });
    }
}