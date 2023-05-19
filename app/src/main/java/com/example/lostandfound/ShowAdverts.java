package com.example.lostandfound;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.ClipData;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lostandfound.room.LostObjectDao;
import com.example.lostandfound.room.LostObjectData;
import com.example.lostandfound.room.LostObjectDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShowAdverts extends AppCompatActivity {

    TextView textLostAndFoundTitle;
    Spinner spinnerLostItems;
    DatabaseHelper databaseHelper;
    TextView textItemName, textItemDescription, textItemContact, textItemLocation, textItemDate, textItemType;
    Button buttonRemoveAdvert;
    ArrayAdapter lostItemAD;
    List<LostObjectData> lostItems;
    List<String> lostDescriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_adverts);

        textLostAndFoundTitle = findViewById(R.id.textViewListTitle);
        spinnerLostItems = findViewById(R.id.spinnerLostItem);

        textItemName = findViewById(R.id.textItemName);
        textItemDescription = findViewById(R.id.textItemDescription);
        textItemContact = findViewById(R.id.textItemContact);
        textItemLocation = findViewById(R.id.textItemLocation);
        textItemDate = findViewById(R.id.textItemDate);
        textItemType = findViewById(R.id.textItemType);

        buttonRemoveAdvert = findViewById(R.id.buttonRemoveAdvert);

        databaseHelper = new DatabaseHelper(this);
        lostItems = databaseHelper.getAllItems();

        lostDescriptions = new ArrayList<>();
        for(LostObjectData data : lostItems){
            lostDescriptions.add(data.getDescription());
        }

        lostItemAD = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, lostDescriptions);
        spinnerLostItems.setAdapter(lostItemAD);

        spinnerLostItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LostObjectData selectedItem = lostItems.get(spinnerLostItems.getSelectedItemPosition());
                textItemName.setText(selectedItem.getName());
                textItemDescription.setText(selectedItem.getDescription());
                textItemContact.setText(selectedItem.getPhone());
                textItemLocation.setText(selectedItem.getLocation());
                textItemDate.setText(selectedItem.getDate());
                textItemType.setText(selectedItem.getType());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonRemoveAdvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = spinnerLostItems.getSelectedItem().toString();
                int itemId = databaseHelper.findItemId(description);
                databaseHelper.deleteItem(itemId);
                lostItems.clear();
                lostItems = databaseHelper.getAllItems();
                lostDescriptions.clear();
                for(LostObjectData data : lostItems){
                    lostDescriptions.add(data.getDescription());
                }
                lostItemAD.notifyDataSetChanged();
                spinnerLostItems.setAdapter(lostItemAD);
                Toast.makeText(ShowAdverts.this, "Item Deleted", Toast.LENGTH_SHORT).show();
            }
        });

    }
}