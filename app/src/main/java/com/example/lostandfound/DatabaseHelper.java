package com.example.lostandfound;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.lostandfound.room.LostObjectData;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    //database filename and version
    public static final String DATABASE = "database.db";
    public static final int VERSION = 1;


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE, null, VERSION);
    }

    //Creates database table with all the entry columns and their data type
    @Override
    public void onCreate(SQLiteDatabase db) {

        //creates a table with all the needed values
        db.execSQL("CREATE TABLE lostObjects (id INTEGER PRIMARY KEY, type TEXT, name TEXT, phone TEXT, description TEXT, date TEXT, location TEXT)");

    }

    //If database needs to upgrade, drop it and recreate
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //deletes the table if it exists
        db.execSQL("DROP TABLE IF EXISTS lostObjects");
        //creates a new one
        onCreate(db);

    }

    //Get all items from the database in a list of LostObjectData type
    public List<LostObjectData> getAllItems() {
        List<LostObjectData> lostItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        //SQL query that finds all entries in lostObjects
        String selectQuery = "SELECT * FROM lostObjects";
        Cursor cursor = db.rawQuery(selectQuery, null);

        //scans the result of the query
        if (cursor.moveToNext()) {
            do {
                //initialises index values for each column
                int idIndex = cursor.getColumnIndex("id");
                int nameIndex = cursor.getColumnIndex("name");
                int phoneIndex = cursor.getColumnIndex("phone");
                int descriptionIndex = cursor.getColumnIndex("description");
                int dateIndex = cursor.getColumnIndex("date");
                int locationIndex = cursor.getColumnIndex("location");
                int typeIndex = cursor.getColumnIndex("type");

                //if the indexes are valid, ie >= 0, then grab the item
                if(idIndex != -1 && nameIndex != -1 && phoneIndex != -1 && descriptionIndex != -1 && dateIndex != -1 && locationIndex != -1 && typeIndex != -1){

                    //gets all the data from the database needed to make a new object
                    int id = cursor.getInt(idIndex);
                    String name = cursor.getString(nameIndex);
                    String phone = cursor.getString(phoneIndex);
                    String description = cursor.getString(descriptionIndex);
                    String date = cursor.getString(dateIndex);
                    String location = cursor.getString(locationIndex);
                    String type = cursor.getString(typeIndex);

                    //creates new LostObjectData item using the retrieved data
                    LostObjectData item = new LostObjectData(id, name, phone, description, date, location, type);
                    //adds new item to the lostItems list
                    lostItems.add(item);
                }
            } while (cursor.moveToNext());
        }
        //closes readable connection
        cursor.close();
        //returns the list of lost items
        return lostItems;
    }

    //Delete an item from the database with a given id
    public void deleteItem(int id){
        //creates an connection of the database that can be written too
        SQLiteDatabase db = this.getWritableDatabase();

        //SQL query that deletes the item from lostObjects where the id equals the one given to the function
        String deleteQuery = "DELETE FROM lostObjects WHERE id="+id;
        db.execSQL(deleteQuery);
    }

    //Insert a provided a lostItem object into the database
    public long insertItem(LostObjectData lostItem){

        //creates an connection of the database that can be written too
        SQLiteDatabase db = this.getWritableDatabase();

        //takes the values from the provided lost item and adds them to a ContentValues item to be inserted to the database
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", lostItem.getName());
        contentValues.put("phone", lostItem.getPhone());
        contentValues.put("description", lostItem.getDescription());
        contentValues.put("date", lostItem.getDate());
        contentValues.put("location", lostItem.getLocation());
        contentValues.put("type", lostItem.getType());

        //inserts new item into the lostObjects table
        long newRowId = db.insert("lostObjects", null, contentValues);
        //closes writable connection
        db.close();
        return newRowId;
    }

    //Find specific item based on the description, works like getAllItems but searches for a specific description and returns the id of said item
    public int findItemId(String data){

        //creates a connection to the database that can be read from
        SQLiteDatabase db = this.getReadableDatabase();
        //creates blank lostItem
        LostObjectData lostItem = null;

        //SQL query to find an item from the lostObjects table with the matching description
        String findQuery = "SELECT * FROM lostObjects WHERE description=\"" + data + "\"";
        Cursor cursor = db.rawQuery(findQuery, null);

        //scans the result of the query
        if (cursor.moveToNext()) {
            do {
                //initialises index values for each column
                int idIndex = cursor.getColumnIndex("id");
                int nameIndex = cursor.getColumnIndex("name");
                int phoneIndex = cursor.getColumnIndex("phone");
                int descriptionIndex = cursor.getColumnIndex("description");
                int dateIndex = cursor.getColumnIndex("date");
                int locationIndex = cursor.getColumnIndex("location");
                int typeIndex = cursor.getColumnIndex("type");

                //if the indexes are valid, ie >= 0, then grab the item
                if(idIndex != -1 && nameIndex != -1 && phoneIndex != -1 && descriptionIndex != -1 && dateIndex != -1 && locationIndex != -1 && typeIndex != -1){

                    int id = cursor.getInt(idIndex);
                    String name = cursor.getString(nameIndex);
                    String phone = cursor.getString(phoneIndex);
                    String description = cursor.getString(descriptionIndex);
                    String date = cursor.getString(dateIndex);
                    String location = cursor.getString(locationIndex);
                    String type = cursor.getString(typeIndex);

                    //creates new LostObjectData item using the retrieved data
                    LostObjectData item = new LostObjectData(id, name, phone, description, date, location, type);
                    lostItem = item;
                }
            } while (cursor.moveToNext());
        }
        //closes readable connection
        cursor.close();
        //returns the item id
        return lostItem.getId();
    }
}
