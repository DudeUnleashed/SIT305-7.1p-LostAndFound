package com.example.lostandfound.room;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;

@Entity(tableName = "lostObjects")
public class LostObjectData {

    @PrimaryKey(autoGenerate = true)
    int id;
    String name;
    String phone;
    String description;
    String date;
    String location;
    String type;

    //creates object type and assigns all given values to itself, used for retrieving the appropriate data for any specific item
    public LostObjectData(int id, String name, String phone, String description, String date, String location, String type) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.location = location;
        this.type = type;
    }

    @Ignore
    public LostObjectData(){

    }

    //getters and setters to retrieve and set any specific data for any lost object, instead of interacting with the item directly
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }
}
