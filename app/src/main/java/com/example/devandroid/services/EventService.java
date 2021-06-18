package com.example.devandroid.services;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.devandroid.entities.Aviary;
import com.example.devandroid.entities.Event;
import com.example.devandroid.entities.TypeAviary;
import com.example.devandroid.entities.TypeEvent;
import com.example.devandroid.utils.UtilsCalendar;
import com.example.devandroid.utils.UtilsDB;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventService {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Event> getAll(){
        SQLiteDatabase db = UtilsDB.openConnection();
        TypeEventService service = new TypeEventService();
        DogService dogService = new DogService();
        List<Event> events = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM event", null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++){
            int id = cursor.getInt(0);
            String type = cursor.getString(1);
            int dog = cursor.getInt(2);
            String dateFromDb = cursor.getString(3);
            Event event = new Event();
            event.setId(id);
            event.setType(service.getByName(type));
            event.setDog(dogService.getById(dog));
            event.setDate(dateFromDb);
            events.add(event);
            cursor.moveToNext();
        }
        cursor.close();
        UtilsDB.closeConnection(db);
        return events;
    }

    public void add(Event event){
        SQLiteDatabase db = UtilsDB.openConnection();
        db.execSQL("INSERT INTO event VALUES (?,?,?,?)", new Object[]{null,event.getType().getName(), event.getDog().getId(),
                event.getDate()});
        UtilsDB.closeConnection(db);
    }

    public void update(Event event){
        SQLiteDatabase db = UtilsDB.openConnection();
        db.execSQL("UPDATE event SET type_event = ?, id_dog = ?, date = ? WHERE id = ?",
                new Object[]{event.getType().getName(), event.getDog().getId(), event.getDate(), event.getId()});
        UtilsDB.closeConnection(db);
    }

    public void delete(Event event){
        SQLiteDatabase db = UtilsDB.openConnection();
        db.execSQL("DELETE FROM event WHERE id = ?", new Object[]{event.getId()});
        UtilsDB.closeConnection(db);
    }

    public void deleteAll(){
        SQLiteDatabase db = UtilsDB.openConnection();
        db.execSQL("DELETE FROM event");
        UtilsDB.closeConnection(db);
    }
}
