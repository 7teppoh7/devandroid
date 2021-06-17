package com.example.devandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class NavigationLayout extends LinearLayout {

    public NavigationLayout(Context context, LinearLayout parent) {
        super(context);
        initView(context, parent);
    }

    public void initView(final Context context, LinearLayout parent) {
        LayoutInflater.from(context).inflate(R.layout.menu_layout, parent, true);
    }
}
