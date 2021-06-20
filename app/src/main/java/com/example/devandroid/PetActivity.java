package com.example.devandroid;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

import com.example.devandroid.entities.Aviary;
import com.example.devandroid.entities.Dog;
import com.example.devandroid.entities.Event;
import com.example.devandroid.entities.StateAnimal;
import com.example.devandroid.services.AviaryService;
import com.example.devandroid.services.DogService;
import com.example.devandroid.services.EventService;
import com.example.devandroid.services.StateAnimalService;
import com.example.devandroid.services.TypeEventService;
import com.example.devandroid.utils.Updatable;
import com.example.devandroid.utils.UtilsCalendar;
import com.example.devandroid.utils.UtilsDB;
import com.example.devandroid.utils.UtilsModerator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Formatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PetActivity extends ParentNavigationActivity implements Updatable {

    private final DogService dogService = new DogService();
    private final EventService eventService = new EventService();
    private final AviaryService aviaryService = new AviaryService();
    private final StateAnimalService stateAnimalService = new StateAnimalService();
    private final TypeEventService typeEventService = new TypeEventService();

    Calendar calendar = UtilsCalendar.calendar;
    SimpleDateFormat formatter = UtilsCalendar.formatter;
    String aviaryName;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pet_activity);
        UtilsModerator.activity = this;

        update();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initPet(){
        EditText name = (EditText) findViewById(R.id.edit_pet_name);
        EditText age = (EditText) findViewById(R.id.edit_pet_age);
        EditText in = (EditText) findViewById(R.id.edit_pet_in);
        EditText out = (EditText) findViewById(R.id.edit_pet_out);
        Spinner spinner  = (Spinner) findViewById(R.id.spinner_aviary);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                aviaryName = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        String[] array = (String[]) aviaryService.getAll()
                .stream()
                .map(Aviary::getName)
                .collect(Collectors.toList())
                .toArray(new String[0]);
        spinner.setOnItemSelectedListener(itemSelectedListener);
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null && bundle.containsKey("id") && dogService.getById(bundle.getInt("id"))!=null) {
            int id = bundle.getInt("id");
            Dog dog = dogService.getById(id);
            age.setText(formatAge(dog.getAge()));
            name.setText(dog.getName());
            in.setText(dog.getDateIn());
            out.setText(dog.getDateOut());
            adapter.clear();
            if (UtilsModerator.isModerator){
                adapter.addAll(array);
            }else{
                adapter.addAll(Collections.singletonList(aviaryService.getByDog(dog).getName()));
            }
        }else{
            LinearLayout editLayout = (LinearLayout) findViewById(R.id.edit_pet_layout);
            Button back = (Button) findViewById(R.id.back_button);
            Button take = (Button) findViewById(R.id.btn_take);

            back.setVisibility(View.GONE);
            take.setVisibility(View.GONE);
            editLayout.setVisibility(View.VISIBLE);
            adapter.clear();
            adapter.addAll(array);
        }
        spinner.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deletePet(View view){
        Bundle bundle = this.getIntent().getExtras();
        Dog dog = dogService.getById(bundle.getInt("id"));
        dogService.delete(dog);

        Intent intent = new Intent(this, PetsActivity.class);
        if (bundle.containsKey("aviary")){
            intent.putExtra("id", bundle.getInt("aviary"));
        }
        this.startActivity(intent);
        this.finish();
    }

    public void backToPets(View view){
        Bundle bundle = this.getIntent().getExtras();
        Intent intent = new Intent(this, PetsActivity.class);
        if (bundle.containsKey("aviary")){
            intent.putExtra("id", bundle.getInt("aviary"));
        }
        this.startActivity(intent);
        this.finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void savePet(View view) {
        Bundle bundle = this.getIntent().getExtras();
        EditText name = (EditText) findViewById(R.id.edit_pet_name);
        EditText age = (EditText) findViewById(R.id.edit_pet_age);
        EditText in = (EditText) findViewById(R.id.edit_pet_in);
        EditText out = (EditText) findViewById(R.id.edit_pet_out);
        Dog dog;

        if (bundle != null && bundle.containsKey("id")) {
            dog = dogService.getById(bundle.getInt("id"));
        }else{
            dog = new Dog();
        }

        dog.setAge(convertAge(age.getText().toString()));
        try {
            dog.setDateIn(UtilsCalendar.parser.parse(in.getText().toString()).toString());
        }catch (ParseException e){
            e.printStackTrace();
        }
        dog.setName(name.getText().toString());

        /*
            место для добавления  фотки
        */
        dog.setPhoto(UtilsDB.base64Example);//удалить после добавления загрузки фото

        if (!out.getText().toString().equals("")){
            try {
                dog.setDateOut(UtilsCalendar.parser.parse(out.getText().toString()).toString());
            }catch (ParseException e){
                e.printStackTrace();
            }
            dog.setState(stateAnimalService.getByName("Home"));
        }else{
            dog.setState(stateAnimalService.getByName("Shelter"));
            dog.setDateOut(null);
        }

        Aviary aviary = aviaryService.getByName(aviaryName);

        if (bundle != null && bundle.containsKey("id")){
            dogService.update(dog);
            Toast.makeText(this, "Изменения успешно сохранены", Toast.LENGTH_SHORT).show();
        }else{
            if (aviary.isFull()){
                Toast.makeText(this, "Вольер в который вы хотите добавить собаку, заполнен, выберите другой"
                        , Toast.LENGTH_LONG).show();
                return;
            }
            dogService.add(dog);

            Event event = new Event();
            event.setType(typeEventService.getByName("In"));
            event.setDog(dog);
            try {
                event.setDate(UtilsCalendar.parser.parse(in.getText().toString()).toString());
            }catch (ParseException e){
                e.printStackTrace();
            }
            eventService.add(event);
        }

        aviaryService.rewriteDog(aviary, dog);

        Intent intent = new Intent(this, PetsActivity.class);
        if (bundle != null && bundle.containsKey("aviary")){
            intent.putExtra("id", bundle.getInt("aviary"));
        }
        this.startActivity(intent);
        this.finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void update(){
        initPet();
        Bundle bundle = this.getIntent().getExtras();
        EditText name = (EditText) findViewById(R.id.edit_pet_name);
        EditText age = (EditText) findViewById(R.id.edit_pet_age);
        EditText in = (EditText) findViewById(R.id.edit_pet_in);
        EditText out = (EditText) findViewById(R.id.edit_pet_out);
        Button delete = (Button) findViewById(R.id.btn_delete_pet);
        LinearLayout editLayout = (LinearLayout) findViewById(R.id.edit_pet_layout);
        Button back = (Button) findViewById(R.id.back_button);

        if (UtilsModerator.isModerator){
            name.setEnabled(true);
            age.setEnabled(true);
            in.setEnabled(true);
            out.setEnabled(true);
            back.setVisibility(View.GONE);
            editLayout.setVisibility(View.VISIBLE);
            if (bundle != null){
                delete.setVisibility(View.VISIBLE);
            }else{
                delete.setVisibility(View.GONE);
            }
            in.setOnClickListener((View) -> inDate(in));
            out.setOnClickListener((View) -> outDate(out));
        }else{
            name.setEnabled(false);
            age.setEnabled(false);
            in.setEnabled(false);
            out.setEnabled(false);
            back.setVisibility(View.VISIBLE);
            editLayout.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
            in.setOnClickListener((View) -> System.out.println());
            out.setOnClickListener((View) -> System.out.println());
        }

        if (bundle != null && dogService.getById(bundle.getInt("id")) != null){
            Button take = (Button) findViewById(R.id.btn_take);
            Dog dog = dogService.getById(bundle.getInt("id"));
            if (dog.getDateOut() != null){
                take.setVisibility(View.GONE);
            }else{
                take.setVisibility(View.VISIBLE);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void takePet(View view){
        Bundle bundle = this.getIntent().getExtras();
        Dog dog = dogService.getById(bundle.getInt("id"));
        EditText out = (EditText) findViewById(R.id.edit_pet_out);
        try {
            dog.setDateOut(UtilsCalendar.parser.parse(out.getText().toString()).toString());
        }catch (ParseException e){
            e.printStackTrace();
        }

        dog.setState(stateAnimalService.getByName("Home"));
        dogService.update(dog);
        Event event = new Event();
        event.setDog(dog);
        event.setType(typeEventService.getByName("Out"));
        try {
            event.setDate(UtilsCalendar.parser.parse(out.getText().toString()).toString());
        }catch (ParseException e){
            e.printStackTrace();
        }
        eventService.add(event);
        Intent intent = new Intent(this, PetsActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void inDate(View view) {
        DatePickerDialog dialog = new DatePickerDialog(this, (view1, year, month, dayOfMonth) -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view2, hourOfDay, minute) -> {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                updateDateIn(calendar.getTime());
            }, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true);
            timePickerDialog.show();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    public void outDate(View view){
        DatePickerDialog dialog = new DatePickerDialog(this, (view1, year, month, dayOfMonth) -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view2, hourOfDay, minute) -> {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                updateDateOut(calendar.getTime());
            }, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true);
            timePickerDialog.show();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void updateDateOut(Date date) {
        calendar.setTime(date);
        EditText in = (EditText) findViewById(R.id.edit_pet_out);
        in.setText(formatter.format(calendar.getTime()));
    }

    private void updateDateIn(Date date) {
        calendar.setTime(date);
        EditText in = (EditText) findViewById(R.id.edit_pet_in);
        in.setText(formatter.format(calendar.getTime()));
    }

    private String formatAge(int age){
        String str = age + " лет";
        switch (age){
            case 0:
                str = "Меньше года";
                break;
            case 1:
                str = age + " год";
                break;
            case 2:
            case 3:
            case 4:
                str = age + " года";
                break;
        }
        return str;
    }

    private int convertAge(String str){
        String[] arr = str.split(" ");
        int age = Integer.parseInt(arr[0]);
        return age;
    }

    public void exit(View view) {
        Bundle bundle = this.getIntent().getExtras();
        Intent intent = new Intent(this, PetsActivity.class);
        if (bundle.containsKey("aviary")){
            intent.putExtra("id", bundle.getInt("aviary"));
        }
        this.startActivity(intent);
        this.finish();
    }
}