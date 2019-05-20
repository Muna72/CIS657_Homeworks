package com.example.geocalculatorapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.*;
import android.widget.*;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {

    private String distanceSelection;
    private String bearingSelection;
    private FloatingActionButton fab;
    public Intent intent;
    public static final int MAIN_SELECTION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Spinner distanceList = (Spinner) findViewById(R.id.distanceList);
        Spinner bearingList = (Spinner) findViewById(R.id.bearingList);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        distanceList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                distanceSelection = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bearingList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bearingSelection = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fab.setOnClickListener(v -> {
                System.out.println("OK");
                intent = new Intent(this, MainActivity.class);
                intent.putExtra("distanceSelection", distanceSelection);
                intent.putExtra("bearingSelection", bearingSelection);
                startActivityForResult(intent, MAIN_SELECTION);
                setResult(MAIN_SELECTION, intent);
        });
    }

}
