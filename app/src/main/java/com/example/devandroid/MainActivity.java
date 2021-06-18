package com.example.devandroid;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import com.example.devandroid.entities.Aviary;
import com.example.devandroid.entities.Dog;
import com.example.devandroid.entities.Event;
import com.example.devandroid.entities.StateAnimal;
import com.example.devandroid.entities.TypeAviary;
import com.example.devandroid.entities.TypeEvent;
import com.example.devandroid.services.AviaryService;
import com.example.devandroid.services.DogService;
import com.example.devandroid.services.EventService;
import com.example.devandroid.services.StateAnimalService;
import com.example.devandroid.services.TypeAviaryService;
import com.example.devandroid.services.TypeEventService;
import com.example.devandroid.utils.UtilsCalendar;
import com.example.devandroid.utils.UtilsDB;

import java.text.ParseException;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UtilsDB.context = this;
        SQLiteDatabase db = this.openOrCreateDatabase("shelter.db", MODE_PRIVATE, null);
        UtilsDB.deleteAll();
        UtilsDB.doMigrate();
    }
}