package com.example.devandroid;

import androidx.annotation.RequiresApi;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.devandroid.entities.Aviary;
import com.example.devandroid.services.AviaryService;
import com.example.devandroid.utils.Updatable;
import com.example.devandroid.utils.UtilsModerator;

import java.util.List;

public class AviariesActivity extends ParentNavigationActivity implements Updatable {

    private AviaryService aviaryService = new AviaryService();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aviaries_activity);
        UtilsModerator.activity = this;

        update();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initAviaries(){
        List<Aviary> aviaries = aviaryService.getAll();
        aviaries.forEach(this::visualizeEvent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void update(){
        Button add = (Button) findViewById(R.id.add_aviary);
        if (UtilsModerator.isModerator){
            add.setVisibility(View.VISIBLE);
        }else{
            add.setVisibility(View.GONE);
        }

        LinearLayout layout = ((LinearLayout) findViewById(R.id.aviaries_scroll));
        View view = findViewById(R.id.item_example);
        layout.removeAllViews();
        layout.addView(view);
        initAviaries();
    }

    public void addAviary(View view){
        Intent intent = new Intent(this, AviaryActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    private void visualizeEvent(Aviary aviary) {
        LinearLayout itemExample = findViewById(R.id.item_example);
        LinearLayout item = new LinearLayout(this);
        item.setLayoutParams(itemExample.getLayoutParams());
        item.setOrientation(itemExample.getOrientation());
        item.setWeightSum(itemExample.getWeightSum());
        item.setBackground(itemExample.getBackground());

        LinearLayout innerItemExample = findViewById(R.id.inner_item_example);
        LinearLayout innerItem = new LinearLayout(this);
        innerItem.setLayoutParams(innerItemExample.getLayoutParams());
        innerItem.setOrientation(innerItemExample.getOrientation());
        innerItem.setWeightSum(innerItemExample.getWeightSum());
        innerItem.setBackground(innerItemExample.getBackground());

        TextView headerExample = findViewById(R.id.header_aviary);
        TextView header = new TextView(this);
        header.setLayoutParams(headerExample.getLayoutParams());
        header.setTextColor(headerExample.getCurrentTextColor());
        header.setText(aviary.getName());
        header.setTextSize(25f); //todo resources
        header.setTypeface(headerExample.getTypeface());

        TextView capacityExample = findViewById(R.id.capacity_aviary);
        TextView capacity = new TextView(this);
        capacity.setLayoutParams(capacityExample.getLayoutParams());
        capacity.setTextColor(capacityExample.getCurrentTextColor());
        capacity.setTextSize(20f); //todo resources
        capacity.setText(formatCapacity(aviary));
        capacity.setTypeface(capacity.getTypeface());

        TextView typeExample = findViewById(R.id.type_aviary);
        TextView type = new TextView(this);
        type.setLayoutParams(typeExample.getLayoutParams());
        type.setTextColor(typeExample.getCurrentTextColor());
        type.setTextSize(20f); //todo resources
        type.setText(formatType(aviary));
        type.setTypeface(typeExample.getTypeface());

        Button changeExample = findViewById(R.id.change_example);
        Button change = new Button(this);
        change.setLayoutParams(changeExample.getLayoutParams());
        change.setTextColor(changeExample.getCurrentTextColor());
        change.setTextSize(20f); //todo resources
        change.setText("редактировать");//todo resources
        change.setTypeface(changeExample.getTypeface());
        change.setBackgroundTintList(changeExample.getBackgroundTintList());
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AviaryActivity.class);
                intent.putExtra("id", aviary.getId());
                startActivity(intent);
                finish();
            }
        });

        innerItem.addView(header);
        innerItem.addView(capacity);
        innerItem.addView(type);
        if (UtilsModerator.isModerator) innerItem.addView(change);
        item.removeAllViews();
        item.addView(innerItem);

        ((LinearLayout) findViewById(R.id.aviaries_scroll)).addView(item);
        item.setOnClickListener((View) -> openAviaryActivity(aviary));
    }

    public void openAviaryActivity(Aviary aviary){
        Intent intent = new Intent(this, PetsActivity.class);
        intent.putExtra("id", aviary.getId());
        this.startActivity(intent);
        this.finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String formatCapacity(Aviary aviary){
        int capacity = aviary.getCapacity();
        int current = aviary.getShelterDogs().size();
        return "Загруженность вольера : " + current + " из " + capacity;
    }

    private String formatType(Aviary aviary){
        String body = "invalid type";
        switch (aviary.getType().getName()){
            case "Aggressive":
                body = "Предназначен для агрессивных собак";
                break;
            case "Patient":
                body = "Предназначен для спокойных собак";
                break;
        }
        return body;
    }
}