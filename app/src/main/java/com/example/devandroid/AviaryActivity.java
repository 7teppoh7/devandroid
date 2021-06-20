package com.example.devandroid;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.devandroid.entities.Aviary;
import com.example.devandroid.entities.TypeAviary;
import com.example.devandroid.services.AviaryService;
import com.example.devandroid.services.TypeAviaryService;
import com.example.devandroid.utils.Updatable;
import com.example.devandroid.utils.UtilsModerator;

import java.util.Collections;
import java.util.stream.Collectors;

public class AviaryActivity extends ParentNavigationActivity implements Updatable {

    private final AviaryService aviaryService = new AviaryService();
    private final TypeAviaryService typeAviaryService = new TypeAviaryService();
    private String aviaryType = "";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aviary_activity);
        UtilsModerator.activity = this;

        update();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initAviary(){
        EditText name = (EditText) findViewById(R.id.edit_aviary_name);
        EditText capacity = (EditText) findViewById(R.id.edit_aviary_capacity);
        Spinner type = (Spinner) findViewById(R.id.spinner_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                aviaryType = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        String[] array = (String[]) typeAviaryService.getAll()
                .stream()
                .map(TypeAviary::getName)
                .collect(Collectors.toList())
                .toArray(new String[0]);
        type.setOnItemSelectedListener(itemSelectedListener);
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null && bundle.containsKey("id")  && aviaryService.getById(bundle.getInt("id"))!=null){
            Aviary aviary = aviaryService.getById(bundle.getInt("id"));
            name.setText(aviary.getName());
            capacity.setText(String.valueOf(aviary.getCapacity()));
            adapter.clear();
            if (UtilsModerator.isModerator){
                adapter.addAll(array);
            }else{
                adapter.addAll(Collections.singletonList(aviary.getType().getName()));
            }
        }else{
            LinearLayout editLayout = (LinearLayout) findViewById(R.id.edit_aviary_layout);
            Button back = (Button) findViewById(R.id.back_button);

            editLayout.setVisibility(View.VISIBLE);
            back.setVisibility(View.GONE);
            adapter.clear();
            adapter.addAll(array);
        }
        type.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void update(){
        initAviary();
        Bundle bundle = this.getIntent().getExtras();

        EditText name = (EditText) findViewById(R.id.edit_aviary_name);
        EditText capacity = (EditText) findViewById(R.id.edit_aviary_capacity);
        Button delete = (Button) findViewById(R.id.btn_delete_aviary);
        LinearLayout editLayout = (LinearLayout) findViewById(R.id.edit_aviary_layout);

        if (UtilsModerator.isModerator){
            name.setVisibility(View.VISIBLE);
            capacity.setVisibility(View.VISIBLE);
            editLayout.setVisibility(View.VISIBLE);
            if (bundle != null){
                delete.setVisibility(View.VISIBLE);
            }else{
                delete.setVisibility(View.GONE);
            }
        }else{
            delete.setVisibility(View.GONE);
            editLayout.setVisibility(View.GONE);
            name.setVisibility(View.GONE);
            capacity.setVisibility(View.GONE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteAviary(View view) {
        Bundle bundle = this.getIntent().getExtras();
        Aviary aviary = aviaryService.getById(bundle.getInt("id"));
        if (!aviary.getShelterDogs().isEmpty()){
            Toast.makeText(getApplicationContext(), "Вы не можете удалить вольер, в нем живут собаки", Toast.LENGTH_SHORT).show();
            return;
        }
        aviaryService.delete(aviary);
        Intent intent = new Intent(this, AviariesActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void saveAviary(View view) {
        Bundle bundle = this.getIntent().getExtras();

        EditText name = (EditText) findViewById(R.id.edit_aviary_name);
        EditText capacity = (EditText) findViewById(R.id.edit_aviary_capacity);
        Aviary aviary;

        if (bundle != null && bundle.containsKey("id")){
            aviary = aviaryService.getById(bundle.getInt("id"));
        }else{
            aviary = new Aviary();
        }

        aviary.setCapacity(Integer.parseInt(capacity.getText().toString()));
        aviary.setName(name.getText().toString());
        aviary.setType(typeAviaryService.getByName(aviaryType));

        if (bundle != null && bundle.containsKey("id")){
            aviaryService.update(aviary);
            Toast.makeText(this, "Изменения успешно сохранены", Toast.LENGTH_SHORT).show();
        }else{
            aviaryService.add(aviary);
        }
        Intent intent = new Intent(this, AviariesActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    public void backToAviaries(View view) {
        Intent intent = new Intent(this, AviariesActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    public void exit(View view) {
        Intent intent = new Intent(this, AviariesActivity.class);
        this.startActivity(intent);
        this.finish();
    }
}