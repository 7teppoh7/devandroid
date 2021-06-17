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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AviaryService {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Aviary> getAll(SQLiteDatabase db){
        TypeAviaryService service = new TypeAviaryService();
        List<Aviary> aviaries = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM aviary", null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++){
            int id = cursor.getInt(0);
            String type = cursor.getString(1);
            String name = cursor.getString(2);
            int capacity = cursor.getInt(3);

            Aviary aviary = new Aviary(id, service.getByName(db, type), name, capacity);
            aviaries.add(aviary);
            cursor.moveToNext();
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Aviary getLastElement(SQLiteDatabase db){
        List<Aviary> aviaries = getAll(db);
        return aviaries.get(aviaries.size()-1);
    }

    public void add(SQLiteDatabase db, Aviary aviary){
        db.execSQL("INSERT INTO aviary VALUES (?,?,?,?)", new Object[]{null,aviary.getType().getName(), aviary.getName(), aviary.getCapacity()});
    }

    public void update(SQLiteDatabase db, Aviary aviary){
        db.execSQL("UPDATE aviary SET type_aviary = ?, name = ?, capacity = ? WHERE id = ?",
                new Object[]{aviary.getType().getName(), aviary.getName(), aviary.getCapacity(), aviary.getId()});
    }

    public void delete(SQLiteDatabase db, Aviary aviary){
        db.execSQL("DELETE FROM aviary WHERE id = ?", new Object[]{aviary.getId()});
    }

    public void deleteAll(SQLiteDatabase db){
        db.execSQL("DELETE FROM aviary");
    }
}
