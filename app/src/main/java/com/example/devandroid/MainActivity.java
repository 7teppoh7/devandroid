package com.example.devandroid;

import android.os.Build;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.devandroid.entities.Dog;
import com.example.devandroid.entities.Event;
import com.example.devandroid.services.AviaryService;
import com.example.devandroid.services.EventService;
import com.example.devandroid.utils.UtilsCalendar;
import com.example.devandroid.utils.UtilsDB;

import java.util.List;

public class MainActivity extends ParentNavigationActivity {

    private EventService eventService = new EventService();
    private AviaryService aviaryService = new AviaryService();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UtilsDB.context = this;
        UtilsDB.dropAll();
        UtilsDB.initDb();
        UtilsDB.doMigrate();

        initNews();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initNews() {
        List<Event> events = eventService.getAll();
        events.forEach(this::visualizeEvent);
    }

    private void visualizeEvent(Event event) {
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

        TextView dateExample = findViewById(R.id.date_example);
        TextView date = new TextView(this);
        date.setLayoutParams(dateExample.getLayoutParams());
        date.setTextColor(dateExample.getCurrentTextColor());
        date.setText(UtilsCalendar.formatForNews(event.getDate()));
        date.setTextSize(15f); //todo resources
        date.setTypeface(dateExample.getTypeface());

        TextView bodyExample = findViewById(R.id.text_example);
        TextView body = new TextView(this);
        body.setLayoutParams(bodyExample.getLayoutParams());
        body.setTextColor(bodyExample.getCurrentTextColor());
        body.setTextSize(20f); //todo resources
        body.setText(formatBody(event));
        body.setTypeface(bodyExample.getTypeface());

        innerItem.addView(date);
        innerItem.addView(body);
        item.addView(innerItem);
        ((LinearLayout) findViewById(R.id.news_scroll)).addView(item);
        //todo onclick listener
    }

    private String formatBody(Event event) {
        String body = "invalid event";
        Dog dogFromEvent = event.getDog();
        switch (event.getType().getName()) {
            case "In":
                body = "В наш приют была принята собака по имени " + dogFromEvent.getName() + " Она находится в вольере " + aviaryService.findByDog(dogFromEvent).getName();
                break;
            case "Out":
                body = "В добрые руки была передана собака по имени " + dogFromEvent.getName() + ". Желаем ей удачи с новыми хозяином!";
                break;
        }
        return body;
    }
}