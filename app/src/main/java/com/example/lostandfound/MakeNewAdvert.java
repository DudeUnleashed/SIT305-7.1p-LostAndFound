package com.example.lostandfound;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lostandfound.room.LostObjectDao;
import com.example.lostandfound.room.LostObjectData;
import com.example.lostandfound.room.LostObjectDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MakeNewAdvert extends AppCompatActivity {

    Button buttonSaveAdvert;
    EditText editName, editPhone, editDescription, editDate, editLocation;
    CheckBox checkboxLost, checkboxFound;
    DatabaseHelper databaseHelper;

    String objectType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_new_advert);

        buttonSaveAdvert = findViewById(R.id.buttonSubmitAdvert);

        editName = findViewById(R.id.editTextName);
        editPhone = findViewById(R.id.editTextPhone);
        editDescription = findViewById(R.id.editTextDescription);
        editDate = findViewById(R.id.editTextDate);
        editLocation = findViewById(R.id.editTextPostalAddress);

        checkboxFound = findViewById(R.id.checkBoxFound);
        checkboxLost = findViewById(R.id.checkBoxLost);

        databaseHelper = new DatabaseHelper(this);


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
}