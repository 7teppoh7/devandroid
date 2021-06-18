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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AviaryService {
    private SQLiteDatabase db;

    public AviaryService(SQLiteDatabase db) {
        this.db = db;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Aviary> getAll() throws ParseException {
        TypeAviaryService service = new TypeAviaryService(db);
        DogService dogService = new DogService(db);
        List<Aviary> aviaries = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM aviary", null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++){
            int id = cursor.getInt(0);
            String type = cursor.getString(1);
            String name = cursor.getString(2);
            int capacity = cursor.getInt(3);

            Aviary aviary = new Aviary(id, service.getByName(type), name, capacity);
            aviaries.add(aviary);
            cursor.moveToNext();
        }
        cursor.close();
        return aviaries;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Aviary getById(int id) throws ParseException {
        List<Aviary> aviaries = getAll();
        return aviaries.stream()
                .filter(x -> x.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Aviary getLastElement() throws ParseException {
        List<Aviary> aviaries = getAll();
        return aviaries.get(aviaries.size()-1);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void add(Aviary aviary) throws ParseException {
        db.execSQL("INSERT INTO aviary VALUES (?,?,?,?)", new Object[]{null,aviary.getType().getName(), aviary.getName(), aviary.getCapacity()});
        aviary.setId(getLastElement().getId());
    }

    public void update(Aviary aviary){
        db.execSQL("UPDATE aviary SET type_aviary = ?, name = ?, capacity = ? WHERE id = ?",
                new Object[]{aviary.getType().getName(), aviary.getName(), aviary.getCapacity(), aviary.getId()});
    }

    public void delete(Aviary aviary){
        db.execSQL("DELETE FROM aviary WHERE id = ?", new Object[]{aviary.getId()});
    }

    public void deleteAll(){
        db.execSQL("DELETE FROM aviary");
    }
}
