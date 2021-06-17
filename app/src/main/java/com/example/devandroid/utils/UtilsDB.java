package com.example.devandroid.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.devandroid.entities.TypeAviary;
import com.example.devandroid.entities.TypeEvent;
import com.example.devandroid.services.StateAnimalService;
import com.example.devandroid.services.TypeAviaryService;
import com.example.devandroid.services.TypeEventService;

import static android.content.Context.MODE_PRIVATE;

public class UtilsDB {
    public static void initDb(Context context){
        SQLiteDatabase db = null;
        try {
            db = context.openOrCreateDatabase("shelter.db", MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS state_animal (name TEXT PRIMARY KEY NOT NULL)");
            db.execSQL("CREATE TABLE IF NOT EXISTS type_aviary (name TEXT PRIMARY KEY NOT NULL)");
            db.execSQL("CREATE TABLE IF NOT EXISTS type_event (name TEXT PRIMARY KEY NOT NULL)");
            db.execSQL("CREATE TABLE IF NOT EXISTS aviary (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "type_aviary TEXT NOT NULL," +
                    "name TEXT," +
                    "capacity INTEGER," +
                    "FOREIGN KEY (type_aviary) REFERENCES type_aviary(name))");
            db.execSQL("CREATE TABLE IF NOT EXISTS dogs (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "state TEXT NOT NULL," +
                    "aviary_id INTEGER NOT NULL," +
                    "name TEXT," +
                    "photo TEXT," +
                    "age INTEGER," +
                    "date_in TEXT," +
                    "date_out TEXT," +
                    "FOREIGN KEY (state) REFERENCES state_animal(name)," +
                    "FOREIGN KEY (aviary_id) REFERENCES aviary(id))");
            db.execSQL("CREATE TABLE IF NOT EXISTS event (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "type_event TEXT NOT NULL," +
                    "id_dog INTEGER NOT NULL," +
                    "date TEXT," +
                    "FOREIGN KEY (type_event) REFERENCES type_event(name)," +
                    "FOREIGN KEY (id_dog) REFERENCES dogs(id))");
        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }

    public static void dropAll(Context context){
        SQLiteDatabase db = null;
        try {
            db = context.openOrCreateDatabase("shelter.db", MODE_PRIVATE, null);
            db.execSQL("DROP TABLE state_animal");
            db.execSQL("DROP TABLE type_aviary");
            db.execSQL("DROP TABLE type_event");
            db.execSQL("DROP TABLE aviary");
            db.execSQL("DROP TABLE dogs");
            db.execSQL("DROP TABLE event");
        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }

    public static void doMigrate(Context context){
        SQLiteDatabase db = context.openOrCreateDatabase("shelter.db", MODE_PRIVATE, null);
        TypeEventService typeEventService = new TypeEventService();
        TypeAviaryService typeAviaryService = new TypeAviaryService();
        StateAnimalService stateAnimalService = new StateAnimalService();
        typeEventService.add(db, "Event");

        typeAviaryService.add(db, "Aggressive");
        typeAviaryService.add(db, "Patient");

        stateAnimalService.add(db, "Home");
        stateAnimalService.add(db, "Shelter");
    }
}
