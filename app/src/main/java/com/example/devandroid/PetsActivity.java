package com.example.devandroid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.devandroid.entities.Aviary;
import com.example.devandroid.entities.Dog;
import com.example.devandroid.services.AviaryService;
import com.example.devandroid.services.DogService;
import com.example.devandroid.utils.Updatable;
import com.example.devandroid.utils.UtilsCalendar;
import com.example.devandroid.utils.UtilsModerator;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class PetsActivity extends ParentNavigationActivity implements Updatable {

    private DogService dogService = new DogService();
    private AviaryService aviaryService = new AviaryService();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pets_activity);
        UtilsModerator.activity = this;

        initDogs();
        findViewById(R.id.btn_filter).setOnClickListener(View -> openFilterDialog());
        update();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void update(){
        Bundle bundle = this.getIntent().getExtras();
        TextView header = (TextView) findViewById(R.id.pets_header);
        Button add = (Button) findViewById(R.id.add_pet);
        if (bundle != null){
            add.setVisibility(View.GONE);
            Aviary aviary = aviaryService.getById(bundle.getInt("id"));
            header.setText(aviary.getName());
        }else{
            if (UtilsModerator.isModerator){
                add.setVisibility(View.VISIBLE);
            }else{
                add.setVisibility(View.GONE);
            }
            header.setText("Наши жильцы");
        }
    }

    public void addPet(View view){
        Intent intent = new Intent(this, PetActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    private void openFilterDialog() {
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initDogs() {
        Bundle bundle = this.getIntent().getExtras();
        List<Dog> dogs;
        if (bundle != null){
            Aviary aviary = aviaryService.getById(bundle.getInt("id"));
            dogs = aviary.getDogs();
        }else{
            dogs = dogService.getAll();
        }
        dogs.forEach(this::visualizeEvent);
    }

    private void visualizeEvent(Dog dog) {
        if (dog == null) return;
        LinearLayout itemExample = findViewById(R.id.item_example_pets);
        LinearLayout item = new LinearLayout(this);
        item.setLayoutParams(itemExample.getLayoutParams());
        item.setOrientation(itemExample.getOrientation());
        item.setWeightSum(itemExample.getWeightSum());
        item.setBackground(itemExample.getBackground());

        ImageView photoExample = findViewById(R.id.photo_example);
        ImageView photo = new ImageView(this);
        photo.setLayoutParams(photoExample.getLayoutParams());
        photo.setBackground(photoExample.getBackground());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            byte[] decodedBytes = Base64.getDecoder().decode(dog.getPhoto());
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            Bitmap bmp = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length, options);
            photo.setImageBitmap(bmp);
        }

        LinearLayout innerItemExample = findViewById(R.id.pets_description_example);
        LinearLayout innerItem = new LinearLayout(this);
        innerItem.setLayoutParams(innerItemExample.getLayoutParams());
        innerItem.setOrientation(innerItemExample.getOrientation());
        innerItem.setWeightSum(innerItemExample.getWeightSum());
        innerItem.setBackground(innerItemExample.getBackground());

        TextView nameExample = findViewById(R.id.pet_name_example);
        TextView name = new TextView(this);
        name.setLayoutParams(nameExample.getLayoutParams());
        name.setTextColor(nameExample.getCurrentTextColor());
        name.setTextSize(20f); //todo resources
        name.setText(dog.getName());
        name.setTypeface(nameExample.getTypeface());

        TextView ageExample = findViewById(R.id.pet_age_example);
        TextView age = new TextView(this);
        age.setLayoutParams(ageExample.getLayoutParams());
        age.setTextColor(ageExample.getCurrentTextColor());
        age.setTextSize(15f); //todo resources
        String ageTxt = "Возраст: " + dog.getAge() + " лет";
        age.setText(ageTxt);
        age.setTypeface(ageExample.getTypeface());

        TextView dateInExample = findViewById(R.id.pet_in_example);
        TextView dateIn = new TextView(this);
        dateIn.setLayoutParams(dateInExample.getLayoutParams());
        dateIn.setTextColor(dateInExample.getCurrentTextColor());
        String dateInTxt = "Прибыл: " + UtilsCalendar.formatForNews(dog.getDateIn());
        dateIn.setText(dateInTxt);
        dateIn.setTextSize(15f); //todo resources
        dateIn.setTypeface(dateInExample.getTypeface());

        TextView stateExample = findViewById(R.id.pet_state_example);
        TextView state = new TextView(this);
        state.setLayoutParams(stateExample.getLayoutParams());
        state.setTextColor(stateExample.getCurrentTextColor());
        state.setTextSize(15f); //todo resources
        String stateTxt;
        if (dog.getState().getName().equals("Home")){
            stateTxt = "Состояние: у хозяев";
        }
        else {
            stateTxt = "Состояние: в приюте";
        }
        state.setText(stateTxt);
        state.setTypeface(stateExample.getTypeface());

        TextView dateOutExample = findViewById(R.id.pet_out_example);
        TextView dateOut = new TextView(this);
        dateOut.setLayoutParams(dateOutExample.getLayoutParams());
        dateOut.setTextColor(dateOutExample.getCurrentTextColor());
        String text;
        if (dog.getState().getName().equals("Home")) {
            text = "Забрали: " + UtilsCalendar.formatForNews(dog.getDateOut());
        } else {
            text = "Номер вольера: " + aviaryService.findByDog(dog).getName();
        }
        dateOut.setText(text);
        dateOut.setTextSize(15f); //todo resources
        dateOut.setTypeface(dateOutExample.getTypeface());

        innerItem.addView(name);
        innerItem.addView(age);
        innerItem.addView(dateIn);
        innerItem.addView(state);
        innerItem.addView(dateOut);
        item.addView(photo);
        item.addView(innerItem);
        ((LinearLayout) findViewById(R.id.pets_scroll)).addView(item);
        item.setOnClickListener((View) -> openPetActivity(dog));
    }

    public void openPetActivity(Dog pet){
        Intent intent = new Intent(this, PetActivity.class);
        intent.putExtra("id", pet.getId());
        this.startActivity(intent);
        this.finish();
    }
}