package com.example.lostandfound;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    Button buttonNewAdvert, buttonShowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonNewAdvert = findViewById(R.id.buttonCreateNew);
        buttonShowItems = findViewById(R.id.buttonShowAdverts);


        buttonShowItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToAdvertList = new Intent(MainActivity.this, ShowAdverts.class);
                startActivity(intentToAdvertList);
                finish();
            }
        });

        buttonNewAdvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToNewAdvert = new Intent(MainActivity.this, MakeNewAdvert.class);
                startActivity(intentToNewAdvert);
                finish();
            }
        });
    }
}