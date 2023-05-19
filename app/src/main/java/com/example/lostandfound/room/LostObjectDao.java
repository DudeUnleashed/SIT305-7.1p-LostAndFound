package com.example.lostandfound.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LostObjectDao {

    @Insert
    void insert(LostObjectData lostObject);

    @Update
    void update(LostObjectData lostObject);

    @Delete
    void delete(LostObjectData lostObject);

    @Query("SELECT * FROM lostObjects")
    List<LostObjectData> getAllData();

}
