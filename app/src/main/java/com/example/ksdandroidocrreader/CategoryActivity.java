package com.example.ksdandroidocrreader;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_main);
        CategoryFragment categoryFragment = new CategoryFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, categoryFragment).commit();
    }
}