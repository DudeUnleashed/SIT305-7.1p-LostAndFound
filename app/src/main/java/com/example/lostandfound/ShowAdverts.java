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

        //initializing all the ui elements used in this activity
        textLostAndFoundTitle = findViewById(R.id.textViewListTitle);
        spinnerLostItems = findViewById(R.id.spinnerLostItem);

        textItemName = findViewById(R.id.textItemName);
        textItemDescription = findViewById(R.id.textItemDescription);
        textItemContact = findViewById(R.id.textItemContact);
        textItemLocation = findViewById(R.id.textItemLocation);
        textItemDate = findViewById(R.id.textItemDate);
        textItemType = findViewById(R.id.textItemType);

        buttonRemoveAdvert = findViewById(R.id.buttonRemoveAdvert);

        //initialises the database helper for this class, and creates a list of all items
        databaseHelper = new DatabaseHelper(this);
        lostItems = databaseHelper.getAllItems();

        //creates a list of all the descriptions for all the items to be shown in the spinner, rather than the id
        lostDescriptions = new ArrayList<>();
        for(LostObjectData data : lostItems){
            lostDescriptions.add(data.getDescription());
        }

        //standard arrayadapter for the spinner used in this project
        lostItemAD = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, lostDescriptions);
        spinnerLostItems.setAdapter(lostItemAD);

        //takes the selected items information and populates the fields in the ui with said info
        spinnerLostItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //gets the specific item based on the position
                LostObjectData selectedItem = lostItems.get(spinnerLostItems.getSelectedItemPosition());

                //populates the text fields with the selected items information
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
                //gets the specific item based on the position and uses findItemId to get said id
                String description = spinnerLostItems.getSelectedItem().toString();
                int itemId = databaseHelper.findItemId(description);

                //deletes the item from the database with the retrieved id
                databaseHelper.deleteItem(itemId);

                //clears the list of lostItems and recreates it
                lostItems.clear();
                lostItems = databaseHelper.getAllItems();

                //clears lostDescriptions and recreates it
                lostDescriptions.clear();
                for(LostObjectData data : lostItems){
                    lostDescriptions.add(data.getDescription());
                }

                //updates the spinner
                lostItemAD.notifyDataSetChanged();
                spinnerLostItems.setAdapter(lostItemAD);

                //make a toast indicating an item was deleted
                Toast.makeText(ShowAdverts.this, "Item Deleted", Toast.LENGTH_SHORT).show();
            }
        });

    }
}