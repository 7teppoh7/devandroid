package com.example.devandroid;

import android.content.Intent;
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

        findViewById(R.id.btn_news).setOnClickListener((View) -> openNewsActivity());
        findViewById(R.id.btn_pets).setOnClickListener((View) -> openPetsActivity());

    }

    private void openNewsActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    private void openPetsActivity() {
        Intent intent = new Intent(this, PetsActivity.class);
        this.startActivity(intent);
        this.finish();
    }
}