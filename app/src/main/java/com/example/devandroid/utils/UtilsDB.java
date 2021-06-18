package com.example.devandroid.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.devandroid.entities.Aviary;
import com.example.devandroid.entities.Dog;
import com.example.devandroid.entities.TypeAviary;
import com.example.devandroid.entities.TypeEvent;
import com.example.devandroid.services.AviaryService;
import com.example.devandroid.services.DogService;
import com.example.devandroid.services.EventService;
import com.example.devandroid.services.StateAnimalService;
import com.example.devandroid.services.TypeAviaryService;
import com.example.devandroid.services.TypeEventService;

import java.text.ParseException;

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
                    "name TEXT," +
                    "photo TEXT," +
                    "age INTEGER," +
                    "date_in TEXT," +
                    "date_out TEXT," +
                    "FOREIGN KEY (state) REFERENCES state_animal(name))");
            db.execSQL("CREATE TABLE IF NOT EXISTS event (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "type_event TEXT NOT NULL," +
                    "id_dog INTEGER NOT NULL," +
                    "date TEXT," +
                    "FOREIGN KEY (type_event) REFERENCES type_event(name)," +
                    "FOREIGN KEY (id_dog) REFERENCES dogs(id))");
            db.execSQL("CREATE TABLE IF NOT EXISTS dog_aviary (id_dog INTEGER NOT NULL," +
                    "id_aviary INTEGER NOT NULL," +
                    "FOREIGN KEY (id_dog) REFERENCES dogs(id)," +
                    "FOREIGN KEY (id_aviary) REFERENCES aviary(id_aviary))");
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

    public static void deleteAll(Context context){
        SQLiteDatabase db = context.openOrCreateDatabase("shelter.db", MODE_PRIVATE, null);
        TypeEventService typeEventService = new TypeEventService(db);
        TypeAviaryService typeAviaryService = new TypeAviaryService(db);
        StateAnimalService stateAnimalService = new StateAnimalService(db);
        DogService dogService = new DogService(db);
        EventService eventService = new EventService(db);
        AviaryService aviaryService = new AviaryService(db);

        typeAviaryService.deleteAll();
        typeEventService.deleteAll();
        stateAnimalService.deleteAll();
        dogService.deleteAll();
        eventService.deleteAll();
        aviaryService.deleteAll();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void doMigrate(Context context) throws ParseException {
        SQLiteDatabase db = context.openOrCreateDatabase("shelter.db", MODE_PRIVATE, null);
        TypeEventService typeEventService = new TypeEventService(db);
        TypeAviaryService typeAviaryService = new TypeAviaryService(db);
        StateAnimalService stateAnimalService = new StateAnimalService(db);
        DogService dogService = new DogService(db);
        EventService eventService = new EventService(db);
        AviaryService aviaryService = new AviaryService(db);
        String date = UtilsCalendar.getTestExample();

        typeEventService.add("Event");

        typeAviaryService.add("Aggressive");
        typeAviaryService.add("Patient");

        stateAnimalService.add("Home");
        stateAnimalService.add("Shelter");

        Aviary aviary = new Aviary(typeAviaryService.getFirstElement(), "AV1", 20);
        aviaryService.add(aviary);

        Dog dog = new Dog("Bob", "photo", 3, date, date);
        dog.setState(stateAnimalService.getFirstElement());
        dogService.add(dog);

        System.out.println(aviary);
        System.out.println(dog);
    }
}
