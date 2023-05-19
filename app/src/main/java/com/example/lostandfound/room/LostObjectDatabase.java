package com.example.lostandfound.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {LostObjectData.class}, version = 1)
public abstract class LostObjectDatabase extends RoomDatabase {
    public abstract LostObjectDao lostObjectDao();
}
