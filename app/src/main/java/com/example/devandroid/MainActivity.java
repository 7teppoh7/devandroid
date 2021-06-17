package com.example.devandroid;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.devandroid.utils.UtilsDB;

public class MainActivity extends ParentNavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SQLiteDatabase db = this.openOrCreateDatabase("shelter.db", MODE_PRIVATE, null);
        UtilsDB.initDb(this);
    }
}