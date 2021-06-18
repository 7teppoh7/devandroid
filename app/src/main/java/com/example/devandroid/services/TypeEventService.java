package com.example.devandroid.services;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.devandroid.entities.TypeEvent;
import com.example.devandroid.utils.UtilsDB;

import java.util.ArrayList;
import java.util.List;

public class TypeEventService {
    public List<TypeEvent> getAll(){
        SQLiteDatabase db = UtilsDB.openConnection();
        List<TypeEvent> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM type_event", null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++){
            TypeEvent typeAviary = new TypeEvent(cursor.getString(0));
            list.add(typeAviary);
            cursor.moveToNext();
        }
        cursor.close();
        UtilsDB.closeConnection(db);
        return list;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public TypeEvent getByName(String name){
        return getAll().stream()
                .filter(x -> x.getName().equals(name)).findFirst()
                .orElse(null);
    }

    public void add(String name){
        SQLiteDatabase db = UtilsDB.openConnection();
        db.execSQL("INSERT INTO type_event VALUES (?)", new Object[]{name});
        UtilsDB.closeConnection(db);
    }

    public void update(String prev, String name){
        SQLiteDatabase db = UtilsDB.openConnection();
        db.execSQL("UPDATE type_event SET name = ? WHERE name = ?", new Object[]{name, prev});
        UtilsDB.closeConnection(db);
    }

    public void delete(String name){
        SQLiteDatabase db = UtilsDB.openConnection();
        db.execSQL("DELETE FROM type_event WHERE name = ?", new Object[]{name});
        UtilsDB.closeConnection(db);
    }

    public void deleteAll(){
        SQLiteDatabase db = UtilsDB.openConnection();
        db.execSQL("DELETE FROM type_event");
        UtilsDB.closeConnection(db);
    }
}
