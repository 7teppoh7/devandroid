package com.example.devandroid.services;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.devandroid.entities.Aviary;
import com.example.devandroid.entities.Event;
import com.example.devandroid.entities.TypeAviary;
import com.example.devandroid.entities.TypeEvent;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class EventService {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Event> getAll(SQLiteDatabase db){
        DogService dogService = new DogService();
        List<Event> events = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM dogs", null);
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String type = cursor.getString(1);
            int dog = cursor.getInt(2);
            String date = cursor.getString(3);
            Event event = new Event();
            event.setId(id);
            event.setType(TypeEvent.valueOf(type));
            event.setDog(dogService.getById(db, dog));
            event.setDate(Date.valueOf(date));
            events.add(event);
        }
        cursor.close();
        return events;
    }

    public void update(SQLiteDatabase db, Event event){
        db.execSQL("UPDATE event SET type_event = ?, id_dog = ?, date = ? WHERE id = ?",
                new Object[]{event.getType().toString(), event.getDog().getId(), event.getDate(), event.getId()});
    }

    public void delete(SQLiteDatabase db, Event event){
        db.execSQL("DELETE FROM event WHERE id = ?", new Object[]{event.getId()});
    }
}
