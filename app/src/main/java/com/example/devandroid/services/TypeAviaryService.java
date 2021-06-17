package com.example.devandroid.services;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.devandroid.entities.StateAnimal;
import com.example.devandroid.entities.TypeAviary;

import java.util.ArrayList;
import java.util.List;

public class TypeAviaryService {
    public List<TypeAviary> getAll(SQLiteDatabase db){
        List<TypeAviary> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM type_aviary", null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++){
            TypeAviary typeAviary = new TypeAviary(cursor.getString(0));
            list.add(typeAviary);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public TypeAviary getByName(SQLiteDatabase db, String name){
        return getAll(db).stream()
                .filter(x -> x.getName().equals(name)).findFirst()
                .orElse(null);
    }

    public void add(SQLiteDatabase db, String name){
        db.execSQL("INSERT INTO type_aviary VALUES (?)", new Object[]{name});
    }

    public void update(SQLiteDatabase db, String prev, String name){
        db.execSQL("UPDATE type_aviary SET name = ? WHERE name = ?", new Object[]{name, prev});
    }

    public void delete(SQLiteDatabase db, String name){
        db.execSQL("DELETE FROM type_aviary WHERE name = ?", new Object[]{name});
    }

    public void deleteAll(SQLiteDatabase db){
        db.execSQL("DELETE FROM type_aviary");
    }
}
