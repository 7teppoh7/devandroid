package com.example.devandroid.services;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.devandroid.entities.Aviary;
import com.example.devandroid.entities.Dog;
import com.example.devandroid.utils.UtilsDB;

import java.util.ArrayList;
import java.util.List;

public class AviaryService {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Aviary> getAll() {
        SQLiteDatabase db = UtilsDB.openConnection();
        TypeAviaryService service = new TypeAviaryService();
        DogService dogService = new DogService();
        List<Aviary> aviaries = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM aviary", null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            int id = cursor.getInt(0);
            String type = cursor.getString(1);
            String name = cursor.getString(2);
            int capacity = cursor.getInt(3);
            Aviary aviary = new Aviary(id, service.getByName(type), name, capacity);
            aviary.setDogs(dogService.getAllByAviaryId(id));
            aviaries.add(aviary);
            cursor.moveToNext();
        }
        cursor.close();
        UtilsDB.closeConnection(db);
        return aviaries;
    }

    public void addDog(Aviary aviary, Dog dog) {
        SQLiteDatabase db = UtilsDB.openConnection();
        db.execSQL("INSERT INTO dog_aviary VALUES (?,?)", new Object[]{dog.getId(), aviary.getId()});
        aviary.addDog(dog);
        UtilsDB.closeConnection(db);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteDog(Aviary aviary, Dog dog) {
        SQLiteDatabase db = UtilsDB.openConnection();
        db.execSQL("DELETE FROM dog_aviary WHERE id_aviary = ? AND id_dog = ?", new Object[]{aviary.getId(), dog.getId()});
        aviary.getDogs().removeIf(x -> x.getId() == dog.getId());
        UtilsDB.closeConnection(db);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Aviary getById(int id) {
        List<Aviary> aviaries = getAll();
        return aviaries.stream()
                .filter(x -> x.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Aviary getByName(String name){
        List<Aviary> aviaries = getAll();
        return aviaries.stream()
                .filter(x -> x.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Aviary getLastElement() {
        List<Aviary> aviaries = getAll();
        return aviaries.get(aviaries.size() - 1);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void add(Aviary aviary) {
        SQLiteDatabase db = UtilsDB.openConnection();
        db.execSQL("INSERT INTO aviary VALUES (?,?,?,?)", new Object[]{null, aviary.getType().getName(), aviary.getName(), aviary.getCapacity()});
        aviary.setId(getLastElement().getId());
        UtilsDB.closeConnection(db);
    }

    public void update(Aviary aviary) {
        SQLiteDatabase db = UtilsDB.openConnection();
        db.execSQL("UPDATE aviary SET type_aviary = ?, name = ?, capacity = ? WHERE id = ?",
                new Object[]{aviary.getType().getName(), aviary.getName(), aviary.getCapacity(), aviary.getId()});
        UtilsDB.closeConnection(db);
    }

    public void delete(Aviary aviary) {
        SQLiteDatabase db = UtilsDB.openConnection();
        db.execSQL("DELETE FROM aviary WHERE id = ?", new Object[]{aviary.getId()});
        UtilsDB.closeConnection(db);
    }

    public void deleteAll() {
        SQLiteDatabase db = UtilsDB.openConnection();
        db.execSQL("DELETE FROM aviary");
        UtilsDB.closeConnection(db);
    }

    public Aviary findByDog(Dog dog) {
        SQLiteDatabase db = UtilsDB.openConnection();
        Cursor cursor = db.rawQuery("SELECT * FROM aviary JOIN dog_aviary ON dog_aviary.id_dog = ?", new String[]{String.valueOf(dog.getId())});
        TypeAviaryService service = new TypeAviaryService();
        Aviary aviary = null;
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            int id = cursor.getInt(0);
            String type = cursor.getString(1);
            String name = cursor.getString(2);
            int capacity = cursor.getInt(3);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                aviary = new Aviary(id, service.getByName(type), name, capacity);
            }
            cursor.moveToNext();
        } else {
            aviary = new Aviary();
        }
        cursor.close();
        UtilsDB.closeConnection(db);
        return aviary;
    }
}
