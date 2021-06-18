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
    private SQLiteDatabase db;

    public StateAnimalService(SQLiteDatabase db) {
        this.db = db;
    }

    public List<StateAnimal> getAll(){
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
    public StateAnimal getByName(String name){
        return getAll().stream()
                .filter(x -> x.getName().equals(name)).findFirst()
                .orElse(null);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public StateAnimal getFirstElement(){
        return getAll().stream()
                .findFirst()
                .orElse(null);
    }

    public void add(String name){
        db.execSQL("INSERT INTO state_animal VALUES (?)", new Object[]{name});
    }

    public void update(String prev, String name){
        db.execSQL("UPDATE state_animal SET name = ? WHERE name = ?", new Object[]{name, prev});
    }

    public void delete(String name){
        db.execSQL("DELETE FROM state_animal WHERE name = ?", new Object[]{name});
    }

    public void deleteAll(){
        db.execSQL("DELETE FROM state_animal");
    }
}
