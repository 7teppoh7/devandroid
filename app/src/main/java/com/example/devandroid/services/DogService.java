package com.example.devandroid.services;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.devandroid.entities.Aviary;
import com.example.devandroid.entities.Dog;
import com.example.devandroid.entities.StateAnimal;
import com.example.devandroid.utils.UtilsCalendar;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DogService {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Dog> getAll(SQLiteDatabase db) throws ParseException {
        StateAnimalService service = new StateAnimalService();
        AviaryService aviaryService = new AviaryService();
        List<Dog> dogs = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM dogs", null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++){
            int id = cursor.getInt(0);
            String state = cursor.getString(1);
            int aviary = cursor.getInt(2);
            String name = cursor.getString(3);
            String photo = cursor.getString(4);
            int age = cursor.getInt(5);
            String dateInDb = cursor.getString(6);
            Date dateIn = UtilsCalendar.parser.parse(dateInDb);
            String dateOutDb = cursor.getString(7);
            Date dateOut = UtilsCalendar.parser.parse(dateOutDb);
            Dog dog = new Dog(id, name, photo, age);
            dog.setAviary(aviaryService.getById(db, aviary));
            dog.setState(service.getByName(db, state));
            dog.setDateIn(UtilsCalendar.formatter.format(dateIn));
            dog.setDateOut(UtilsCalendar.formatter.format(dateOut));
            dogs.add(dog);
            cursor.moveToNext();
        }
        cursor.close();
        return dogs;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Dog getLastElement(SQLiteDatabase db) throws ParseException {
        List<Dog> dogs = getAll(db);
        return dogs.get(dogs.size()-1);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Dog getById(SQLiteDatabase db, int id) throws ParseException {
        List<Dog> dogs = getAll(db);
        return dogs.stream()
                .filter(x -> x.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void add(SQLiteDatabase db, Dog dog){
        db.execSQL("INSERT INTO dogs VALUES (?,?,?,?,?,?,?,?)", new Object[]{null,dog.getState().getName(), dog.getAviary().getId(),
                dog.getName(), dog.getPhoto(), dog.getAge(), dog.getDateIn(), dog.getDateOut()});

    }

    public void update(SQLiteDatabase db, Dog dog){
        db.execSQL("UPDATE dogs SET state = ?, aviary_id = ?, name = ?, photo = ?, age = ?, date_in = ?, date_out = ?  WHERE id = ?",
                new Object[]{dog.getState().getName(), dog.getAviary().getId(), dog.getName(),
                        dog.getPhoto(), dog.getAge(), dog.getDateIn(), dog.getDateOut()});
    }

    public void delete(SQLiteDatabase db, Dog dog){
        db.execSQL("DELETE FROM dogs WHERE id = ?", new Object[]{dog.getId()});
    }

    public void deleteAll(SQLiteDatabase db){
        db.execSQL("DELETE FROM dogs");
    }
}
