package com.example.ksdandroidocrreader;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
public class ResultsActivity extends AppCompatActivity {
    private TextView textViewer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_main);
        textViewer = findViewById(R.id.result_title_text);
        textViewer.setText(getIntent().getStringExtra("name"));
        ResultsFragment categoryFragment = new ResultsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, categoryFragment).commit();
    }


}