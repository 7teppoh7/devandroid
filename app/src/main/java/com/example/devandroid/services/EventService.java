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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventService {
    private SQLiteDatabase db;

    public EventService(SQLiteDatabase db) {
        this.db = db;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Event> getAll() throws ParseException {
        TypeEventService service = new TypeEventService(db);
        DogService dogService = new DogService(db);
        List<Event> events = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM event", null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++){
            int id = cursor.getInt(0);
            String type = cursor.getString(1);
            int dog = cursor.getInt(2);
            String dateFromDb = cursor.getString(3);
            Date date = UtilsCalendar.parser.parse(dateFromDb);
            Event event = new Event();
            event.setId(id);
            event.setType(service.getByName(type));
            event.setDog(dogService.getById(dog));
            event.setDate(UtilsCalendar.formatter.format(date));
            events.add(event);
            cursor.moveToNext();
        }
        cursor.close();
        return events;
    }

    public void add(Event event){
        db.execSQL("INSERT INTO event VALUES (?,?,?,?)", new Object[]{null,event.getType().getName(), event.getDog().getId(),
                event.getDate()});
    }

    public void update(Event event){
        db.execSQL("UPDATE event SET type_event = ?, id_dog = ?, date = ? WHERE id = ?",
                new Object[]{event.getType().getName(), event.getDog().getId(), event.getDate(), event.getId()});
    }

    public void delete(Event event){
        db.execSQL("DELETE FROM event WHERE id = ?", new Object[]{event.getId()});
    }

    public void deleteAll(){
        db.execSQL("DELETE FROM event");
    }
}
