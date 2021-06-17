package com.example.devandroid.services;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.devandroid.entities.Aviary;
import com.example.devandroid.entities.Dog;
import com.example.devandroid.entities.StateAnimal;
import com.example.devandroid.entities.TypeAviary;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class AviaryService {
    public List<Aviary> getAll(SQLiteDatabase db){
        List<Aviary> aviaries = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM dogs", null);
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String type = cursor.getString(1);
            String name = cursor.getString(2);
            int capacity = cursor.getInt(3);
            Aviary aviary = new Aviary(id, TypeAviary.valueOf(type), name, capacity);
            aviaries.add(aviary);
        }
        cursor.close();
        return aviaries;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Aviary getById(SQLiteDatabase db, int id){
        List<Aviary> aviaries = getAll(db);
        return aviaries.stream()
                .filter(x -> x.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void update(SQLiteDatabase db, Aviary aviary){
        db.execSQL("UPDATE aviary SET type_aviary = ?, name = ?, capacity = ? WHERE id = ?",
                new Object[]{aviary.getType().toString(), aviary.getName(), aviary.getCapacity(), aviary.getId()});
    }

    public void delete(SQLiteDatabase db, Aviary aviary){
        db.execSQL("DELETE FROM aviary WHERE id = ?", new Object[]{aviary.getId()});
    }
}
