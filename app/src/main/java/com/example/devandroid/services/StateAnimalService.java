package com.example.devandroid.services;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.devandroid.entities.Aviary;
import com.example.devandroid.entities.StateAnimal;
import com.example.devandroid.entities.TypeAviary;

import java.util.ArrayList;
import java.util.List;

public class StateAnimalService {
    public List<StateAnimal> getAll(SQLiteDatabase db){
        List<StateAnimal> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM state_animal", null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++){
            StateAnimal stateAnimal = new StateAnimal(cursor.getString(0));
            list.add(stateAnimal);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public StateAnimal getByName(SQLiteDatabase db, String name){
        return getAll(db).stream()
                .filter(x -> x.getName().equals(name)).findFirst()
                .orElse(null);
    }

    public void add(SQLiteDatabase db, String name){
        db.execSQL("INSERT INTO state_animal VALUES (?)", new Object[]{name});
    }

    public void update(SQLiteDatabase db, String prev, String name){
        db.execSQL("UPDATE state_animal SET name = ? WHERE name = ?", new Object[]{name, prev});
    }

    public void delete(SQLiteDatabase db, String name){
        db.execSQL("DELETE FROM state_animal WHERE name = ?", new Object[]{name});
    }

    public void deleteAll(SQLiteDatabase db){
        db.execSQL("DELETE FROM state_animal");
    }
}
