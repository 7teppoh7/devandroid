package com.example.devandroid;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;

import com.example.devandroid.utils.UtilsModerator;

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

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch sw = (Switch) findViewById(R.id.switch_moderator);
        sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            UtilsModerator.isModerator = isChecked;
            if (UtilsModerator.activity != null) UtilsModerator.activity.update();
        });
        sw.setChecked(UtilsModerator.isModerator);

        findViewById(R.id.btn_news).setOnClickListener((View) -> openNewsActivity());
        findViewById(R.id.btn_pets).setOnClickListener((View) -> openPetsActivity());
        findViewById(R.id.btn_aviaries).setOnClickListener((View) -> openAviariesActivity());
        findViewById(R.id.btn_exit).setOnClickListener((View) -> exit());
    }

    private void exit() {
        this.finish();
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

    private void openAviariesActivity(){
        Intent intent = new Intent(this, AviariesActivity.class);
        this.startActivity(intent);
        this.finish();
    }
}