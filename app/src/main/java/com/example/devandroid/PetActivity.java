package com.example.devandroid;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.devandroid.entities.Aviary;
import com.example.devandroid.entities.Dog;
import com.example.devandroid.entities.Event;
import com.example.devandroid.services.AviaryService;
import com.example.devandroid.services.DogService;
import com.example.devandroid.services.EventService;
import com.example.devandroid.services.StateAnimalService;
import com.example.devandroid.services.TypeEventService;
import com.example.devandroid.utils.Updatable;
import com.example.devandroid.utils.UtilsCalendar;
import com.example.devandroid.utils.UtilsDB;
import com.example.devandroid.utils.UtilsModerator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PetActivity extends ParentNavigationActivity implements Updatable {

    private final DogService dogService = new DogService();
    private final EventService eventService = new EventService();
    private final AviaryService aviaryService = new AviaryService();
    private final StateAnimalService stateAnimalService = new StateAnimalService();
    private final TypeEventService typeEventService = new TypeEventService();

    private final Calendar calendarIn = Calendar.getInstance();
    private final Calendar calendarOut = Calendar.getInstance();
    SimpleDateFormat formatter = UtilsCalendar.newsDateFormatter;
    String aviaryName;
    private String currentPhoto = UtilsDB.NO_PHOTO_IMAGE;
    private ImageView photoView;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pet_activity);
        UtilsModerator.activity = this;
        photoView = findViewById(R.id.photo_view);
        update();
        photoView.setImageBitmap(decodePhoto(currentPhoto));
    }

    private Bitmap decodePhoto(String base64Photo) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            byte[] decodedBytes = Base64.getDecoder().decode(base64Photo);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length, options);
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initPet() {
        EditText name = (EditText) findViewById(R.id.edit_pet_name);
        EditText age = (EditText) findViewById(R.id.edit_pet_age);
        EditText in = (EditText) findViewById(R.id.edit_pet_in);
        EditText out = (EditText) findViewById(R.id.edit_pet_out);
        Spinner spinner = (Spinner) findViewById(R.id.spinner_aviary);
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
        if (bundle != null && bundle.containsKey("id") && dogService.getById(bundle.getInt("id")) != null) {
            int id = bundle.getInt("id");
            Dog dog = dogService.getById(id);
            age.setText(formatAge(dog.getAge()));
            name.setText(dog.getName());
            in.setText(dog.getDateIn());
            out.setText(dog.getDateOut());
            currentPhoto = dog.getPhoto();
            if (!TextUtils.isEmpty(dog.getDateOut())) {
                findViewById(R.id.aviary_layout).setVisibility(View.GONE);
            }
            adapter.clear();
            if (UtilsModerator.isModerator && TextUtils.isEmpty(dog.getDateOut())) {
                adapter.addAll(array);
            } else if (TextUtils.isEmpty(dog.getDateOut())) {
                adapter.addAll(Collections.singletonList(aviaryService.getByDog(dog).getName()));
            }
        } else {
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
    public void deletePet(View view) {
        Bundle bundle = this.getIntent().getExtras();
        Dog dog = dogService.getById(bundle.getInt("id"));
        dogService.delete(dog);

        Intent intent = new Intent(this, PetsActivity.class);
        if (bundle.containsKey("aviary")) {
            intent.putExtra("id", bundle.getInt("aviary"));
        }
        this.startActivity(intent);
        this.finish();
    }

    public void backToPets(View view) {
        Bundle bundle = this.getIntent().getExtras();
        Intent intent = new Intent(this, PetsActivity.class);
        if (bundle.containsKey("aviary")) {
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
        } else {
            dog = new Dog();
        }

        dog.setAge(convertAge(age.getText().toString()));
        try {
            dog.setDateIn(UtilsCalendar.parser.format(formatter.parse(in.getText().toString())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dog.setName(name.getText().toString());

        dog.setPhoto(currentPhoto);

        if (!out.getText().toString().equals("")) {
            try {
                dog.setDateOut(UtilsCalendar.parser.format(formatter.parse(out.getText().toString())));
            } catch (Exception e) {
                e.printStackTrace();
            }
            dog.setState(stateAnimalService.getByName("Home"));
        } else {
            dog.setState(stateAnimalService.getByName("Shelter"));
            dog.setDateOut(null);
        }

        Aviary aviary = null;
        if (!TextUtils.isEmpty(aviaryName)) {
            aviary = aviaryService.getByName(aviaryName);
        }

        if (bundle != null && bundle.containsKey("id")) {
            dogService.update(dog);
            Toast.makeText(this, "Изменения успешно сохранены", Toast.LENGTH_SHORT).show();
        } else {
            if (aviary != null && aviary.isFull()) {
                Toast.makeText(this, "Вольер в который вы хотите добавить собаку, заполнен, выберите другой"
                        , Toast.LENGTH_LONG).show();
                return;
            }
            dogService.add(dog);

            Event event = new Event();
            event.setType(typeEventService.getByName("In"));
            event.setDate(UtilsCalendar.parser.format(Calendar.getInstance().getTime()));
            event.setDog(dog);
            try {
                event.setDate(UtilsCalendar.parser.parse(in.getText().toString()).toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            eventService.add(event);
        }

        if (aviary != null) {
            aviaryService.rewriteDog(aviary, dog);
        }

        Intent intent = new Intent(this, PetsActivity.class);
        if (bundle != null && bundle.containsKey("aviary")) {
            intent.putExtra("id", bundle.getInt("aviary"));
        }
        this.startActivity(intent);
        this.finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void update() {
        initPet();
        Bundle bundle = this.getIntent().getExtras();
        EditText name = (EditText) findViewById(R.id.edit_pet_name);
        EditText age = (EditText) findViewById(R.id.edit_pet_age);
        EditText in = (EditText) findViewById(R.id.edit_pet_in);
        EditText out = (EditText) findViewById(R.id.edit_pet_out);
        Button delete = (Button) findViewById(R.id.btn_delete_pet);
        LinearLayout editLayout = (LinearLayout) findViewById(R.id.edit_pet_layout);
        LinearLayout photoLayout = (LinearLayout) findViewById(R.id.photo_layout);
        Button back = (Button) findViewById(R.id.back_button);
        Button changePhoto = findViewById(R.id.btn_change_image);
        Button deletePhoto = findViewById(R.id.btn_delete_image);

        if (UtilsModerator.isModerator) {
            name.setEnabled(true);
            age.setEnabled(true);
            in.setEnabled(true);
            out.setEnabled(true);
            back.setVisibility(View.GONE);
            editLayout.setVisibility(View.VISIBLE);
            photoLayout.setVisibility(View.VISIBLE);
            if (bundle != null) {
                delete.setVisibility(View.VISIBLE);
            } else {
                delete.setVisibility(View.GONE);
            }
            in.setOnClickListener((View) -> inDate(in));
            changePhoto.setOnClickListener(View -> changePhoto());
            deletePhoto.setOnClickListener(View -> deletePhoto());
        } else {
            name.setEnabled(false);
            age.setEnabled(false);
            in.setEnabled(false);
            out.setEnabled(false);
            back.setVisibility(View.VISIBLE);
            editLayout.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
            photoLayout.setVisibility(View.GONE);
            in.setOnClickListener((View) -> System.out.println());
            out.setOnClickListener((View) -> System.out.println());
        }

        if (bundle != null && dogService.getById(bundle.getInt("id")) != null) {
            Button take = (Button) findViewById(R.id.btn_take);
            Dog dog = dogService.getById(bundle.getInt("id"));
            try {
                calendarIn.setTime(UtilsCalendar.parser.parse(dog.getDateIn()));
                updateDateIn(calendarIn.getTime());
                if (dog.getDateOut() != null) {
                    calendarOut.setTime(UtilsCalendar.parser.parse(dog.getDateOut()));
                    updateDateOut(calendarOut.getTime());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!TextUtils.isEmpty(dog.getDateOut())) {
                take.setVisibility(View.GONE);
            } else {
                take.setVisibility(View.VISIBLE);
            }
        }

    }

    private void deletePhoto() {
        currentPhoto = UtilsDB.NO_PHOTO_IMAGE;
        photoView.setImageBitmap(decodePhoto(currentPhoto));
    }

    private void changePhoto() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    static final int GALLERY_REQUEST = 1;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        Bitmap bitmap = null;

        switch (requestCode) {
            case GALLERY_REQUEST:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            currentPhoto = Base64.getEncoder().encodeToString(outputStream.toByteArray());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    photoView.setImageBitmap(bitmap);
                }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void takePet(View view) {
        Bundle bundle = this.getIntent().getExtras();
        Dog dog = dogService.getById(bundle.getInt("id"));
        EditText out = (EditText) findViewById(R.id.edit_pet_out);
        dog.setDateOut(UtilsCalendar.parser.format(Calendar.getInstance().getTime()));

        dog.setState(stateAnimalService.getByName("Home"));
        dogService.update(dog);
        Event event = new Event();
        event.setDog(dog);
        event.setType(typeEventService.getByName("Out"));
        event.setDate(UtilsCalendar.parser.format(Calendar.getInstance().getTime()));
        eventService.add(event);
        aviaryService.deleteDog(aviaryService.findByDog(dog), dog);
        List<Aviary> aviaryList = aviaryService.getAll();
        Intent intent = new Intent(this, PetsActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void inDate(View view) {
        DatePickerDialog dialog = new DatePickerDialog(this, (view1, year, month, dayOfMonth) -> {
            Calendar inDateCalendar = Calendar.getInstance();
            inDateCalendar.set(Calendar.YEAR, year);
            inDateCalendar.set(Calendar.MONTH, month);
            inDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateIn(inDateCalendar.getTime());
        }, calendarIn.get(Calendar.YEAR), calendarIn.get(Calendar.MONTH), calendarIn.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    public void outDate(View view) {
        DatePickerDialog dialog = new DatePickerDialog(this, (view1, year, month, dayOfMonth) -> {
            Calendar outDateCalendar = Calendar.getInstance();
            outDateCalendar.set(Calendar.YEAR, year);
            outDateCalendar.set(Calendar.MONTH, month);
            outDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateOut(outDateCalendar.getTime());
        }, calendarOut.get(Calendar.YEAR), calendarOut.get(Calendar.MONTH), calendarOut.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void updateDateOut(Date date) {
        calendarOut.setTime(date);
        EditText in = (EditText) findViewById(R.id.edit_pet_out);
        in.setText(formatter.format(calendarOut.getTime()));
    }

    private void updateDateIn(Date date) {
        calendarIn.setTime(date);
        EditText in = (EditText) findViewById(R.id.edit_pet_in);
        in.setText(formatter.format(calendarIn.getTime()));
    }

    private String formatAge(int age) {
        String str = age + " лет";
        switch (age) {
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

    private int convertAge(String str) {
        String[] arr = str.split(" ");
        int age = Integer.parseInt(arr[0]);
        return age;
    }

    public void exit(View view) {
        Bundle bundle = this.getIntent().getExtras();
        Intent intent = new Intent(this, PetsActivity.class);
        if (bundle.containsKey("aviary")) {
            intent.putExtra("id", bundle.getInt("aviary"));
        }
        this.startActivity(intent);
        this.finish();
    }
}