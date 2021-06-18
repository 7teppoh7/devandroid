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
import com.example.devandroid.utils.UtilsDB;

import java.io.UTFDataFormatException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DogService {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Dog> getAll() {
        SQLiteDatabase db = UtilsDB.openConnection();
        StateAnimalService service = new StateAnimalService();
        List<Dog> dogs = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM dogs", null);
        cursor.moveToFirst();
        try {
            for (int i = 0; i < cursor.getCount(); i++){
                int id = cursor.getInt(0);
                String state = cursor.getString(1);
                String name = cursor.getString(2);
                String photo = cursor.getString(3);
                int age = cursor.getInt(4);
                String dateInDb = cursor.getString(5);
                Date dateIn = UtilsCalendar.parser.parse(dateInDb);
                String dateOutDb = cursor.getString(6);
                Date dateOut = UtilsCalendar.parser.parse(dateOutDb);
                Dog dog = new Dog(id, name, photo, age);
                dog.setState(service.getByName(state));
                dog.setDateIn(UtilsCalendar.formatter.format(dateIn));
                dog.setDateOut(UtilsCalendar.formatter.format(dateOut));
                dogs.add(dog);
                cursor.moveToNext();
            }
        }catch (ParseException e){
            e.printStackTrace();
        }
        cursor.close();
        UtilsDB.closeConnection(db);
        return dogs;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Dog> getAllByAviaryId(int id){
        SQLiteDatabase db = UtilsDB.openConnection();
        List<Dog> dogs = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM dog_aviary WHERE id_aviary = ?", new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++){
            Dog dog = getById(cursor.getInt(0));
            dogs.add(dog);
            cursor.moveToNext();
        }
        cursor.close();
        UtilsDB.closeConnection(db);
        return dogs;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Dog getLastElement() {
        List<Dog> dogs = getAll();
        return dogs.get(dogs.size()-1);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Dog getById(int id) {
        List<Dog> dogs = getAll();
        return dogs.stream()
                .filter(x -> x.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void add(Dog dog) {
        SQLiteDatabase db = UtilsDB.openConnection();
        db.execSQL("INSERT INTO dogs VALUES (?,?,?,?,?,?,?)", new Object[]{null,dog.getState().getName(),
                dog.getName(), dog.getPhoto(), dog.getAge(), dog.getDateIn(), dog.getDateOut()});
        dog.setId(getLastElement().getId());
        UtilsDB.closeConnection(db);
    }

    public void update(Dog dog){
        SQLiteDatabase db = UtilsDB.openConnection();
        db.execSQL("UPDATE dogs SET state = ?, name = ?, photo = ?, age = ?, date_in = ?, date_out = ?  WHERE id = ?",
                new Object[]{dog.getState().getName(),  dog.getName(),
                        dog.getPhoto(), dog.getAge(), dog.getDateIn(), dog.getDateOut()});
        UtilsDB.closeConnection(db);
    }

    public void delete(Dog dog){
        SQLiteDatabase db = UtilsDB.openConnection();
        db.execSQL("DELETE FROM dogs WHERE id = ?", new Object[]{dog.getId()});
        UtilsDB.closeConnection(db);
    }

    public void deleteAll(){
        SQLiteDatabase db = UtilsDB.openConnection();
        db.execSQL("DELETE FROM dogs");
        UtilsDB.closeConnection(db);
    }
}
