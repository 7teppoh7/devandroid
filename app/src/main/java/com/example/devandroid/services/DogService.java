package com.example.devandroid.services;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.devandroid.entities.Aviary;
import com.example.devandroid.entities.Dog;
import com.example.devandroid.entities.StateAnimal;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class DogService {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Dog> getAll(SQLiteDatabase db){
        AviaryService aviaryService = new AviaryService();
        List<Dog> dogs = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM dogs", null);
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String state = cursor.getString(1);
            int aviary = cursor.getInt(2);
            String name = cursor.getString(3);
            String photo = cursor.getString(4);
            int age = cursor.getInt(5);
            String dateIn = cursor.getString(6);
            String dateOut = cursor.getString(7);
            Dog dog = new Dog(id, name, photo, age);
            dog.setAviary(aviaryService.getById(db, aviary));
            dog.setState(StateAnimal.valueOf(state));
            dog.setDateIn(Date.valueOf(dateIn));
            dog.setDateOut(Date.valueOf(dateOut));
            dogs.add(dog);
        }
        cursor.close();
        return dogs;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Dog getById(SQLiteDatabase db, int id){
        List<Dog> dogs = getAll(db);
        return dogs.stream()
                .filter(x -> x.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void update(SQLiteDatabase db, Dog dog){
        db.execSQL("UPDATE dogs SET state = ?, aviary_id = ?, name = ?, photo = ?, age = ?, date_in = ?, date_out = ?  WHERE id = ?",
                new Object[]{dog.getState().toString(), dog.getAviary().getId(), dog.getName(),
                        dog.getPhoto(), dog.getAge(), dog.getDateIn(), dog.getDateOut()});
    }

    public void delete(SQLiteDatabase db, Dog dog){
        db.execSQL("DELETE FROM dogs WHERE id = ?", new Object[]{dog.getId()});
    }
}
