package com.example.devandroid;

import android.widget.LinearLayout;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;

public class ParentNavigationActivity extends AppCompatActivity {

    NavigationLayout navigationLayout;
    LinearLayout left_drawer;


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        setupMenu();
    }

    public void setupMenu() {
        left_drawer = findViewById(R.id.left_drawer);
        navigationLayout = new NavigationLayout(getApplicationContext(), left_drawer);
        left_drawer.addView(navigationLayout);
    }
}