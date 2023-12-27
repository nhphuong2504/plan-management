package com.example.textapp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabs;
    private ViewPager2 pager;
    private TabsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabsorganizer);

        tabs = findViewById(R.id.tabsLayout);
        pager = findViewById(R.id.pager);
        adapter = new TabsAdapter(this);
        pager.setAdapter(adapter);

        new TabLayoutMediator(tabs, pager, (tabs, position) -> {
            if (position == 0) {
                tabs.setText("Login");
            } else if (position == 1) {
                tabs.setText("Stonks");
            } else {
                tabs.setText("Shares Placeholder");
            }
        }).attach();

    }
}