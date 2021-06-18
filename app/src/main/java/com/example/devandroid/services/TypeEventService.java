package com.example.devandroid.services;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.devandroid.entities.TypeEvent;

import java.util.ArrayList;
import java.util.List;

public class TypeEventService {
    private SQLiteDatabase db;

    public TypeEventService(SQLiteDatabase db) {
        this.db = db;
    }

    public List<TypeEvent> getAll(){
        List<TypeEvent> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM type_event", null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++){
            TypeEvent typeAviary = new TypeEvent(cursor.getString(0));
            list.add(typeAviary);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public TypeEvent getByName(String name){
        return getAll().stream()
                .filter(x -> x.getName().equals(name)).findFirst()
                .orElse(null);
    }

    public void add(String name){
        db.execSQL("INSERT INTO type_event VALUES (?)", new Object[]{name});
    }

    public void update(String prev, String name){
        db.execSQL("UPDATE type_event SET name = ? WHERE name = ?", new Object[]{name, prev});
    }

    public void delete(String name){
        db.execSQL("DELETE FROM type_event WHERE name = ?", new Object[]{name});
    }

    public void deleteAll(){
        db.execSQL("DELETE FROM type_event");
    }
}
