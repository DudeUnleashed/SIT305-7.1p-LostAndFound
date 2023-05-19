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

    public static final String DATABASE = "database.db";
    public static final int VERSION = 1;


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE lostObjects (id INTEGER PRIMARY KEY, type TEXT, name TEXT, phone TEXT, description TEXT, date TEXT, location TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS lostObjects");
        onCreate(db);

    }

    public List<LostObjectData> getAllItems() {
        List<LostObjectData> lostItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM lostObjects";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToNext()) {
            do {
                int idIndex = cursor.getColumnIndex("id");
                int nameIndex = cursor.getColumnIndex("name");
                int phoneIndex = cursor.getColumnIndex("phone");
                int descriptionIndex = cursor.getColumnIndex("description");
                int dateIndex = cursor.getColumnIndex("date");
                int locationIndex = cursor.getColumnIndex("location");
                int typeIndex = cursor.getColumnIndex("type");

                if(idIndex != -1 && nameIndex != -1 && phoneIndex != -1 && descriptionIndex != -1 && dateIndex != -1 && locationIndex != -1 && typeIndex != -1){

                    int id = cursor.getInt(idIndex);
                    String name = cursor.getString(nameIndex);
                    String phone = cursor.getString(phoneIndex);
                    String description = cursor.getString(descriptionIndex);
                    String date = cursor.getString(dateIndex);
                    String location = cursor.getString(locationIndex);
                    String type = cursor.getString(typeIndex);

                    LostObjectData item = new LostObjectData(id, name, phone, description, date, location, type);
                    lostItems.add(item);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lostItems;
    }

    public void deleteItem(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM lostObjects WHERE id="+id;

        db.execSQL(deleteQuery);
    }

    public long insertItem(LostObjectData lostItem){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", lostItem.getName());
        contentValues.put("phone", lostItem.getPhone());
        contentValues.put("description", lostItem.getDescription());
        contentValues.put("date", lostItem.getDate());
        contentValues.put("location", lostItem.getLocation());
        contentValues.put("type", lostItem.getType());

        long newRowId = db.insert("lostObjects", null, contentValues);
        db.close();
        return newRowId;
    }

    public int findItemId(String data){
        SQLiteDatabase db = this.getReadableDatabase();
        LostObjectData lostItem = null;
        String findQuery = "SELECT * FROM lostObjects WHERE description=\"" + data + "\"";
        Cursor cursor = db.rawQuery(findQuery, null);

        if (cursor.moveToNext()) {
            do {
                int idIndex = cursor.getColumnIndex("id");
                int nameIndex = cursor.getColumnIndex("name");
                int phoneIndex = cursor.getColumnIndex("phone");
                int descriptionIndex = cursor.getColumnIndex("description");
                int dateIndex = cursor.getColumnIndex("date");
                int locationIndex = cursor.getColumnIndex("location");
                int typeIndex = cursor.getColumnIndex("type");

                if(idIndex != -1 && nameIndex != -1 && phoneIndex != -1 && descriptionIndex != -1 && dateIndex != -1 && locationIndex != -1 && typeIndex != -1){

                    int id = cursor.getInt(idIndex);
                    String name = cursor.getString(nameIndex);
                    String phone = cursor.getString(phoneIndex);
                    String description = cursor.getString(descriptionIndex);
                    String date = cursor.getString(dateIndex);
                    String location = cursor.getString(locationIndex);
                    String type = cursor.getString(typeIndex);

                    LostObjectData item = new LostObjectData(id, name, phone, description, date, location, type);
                    lostItem = item;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lostItem.getId();
    }
}
