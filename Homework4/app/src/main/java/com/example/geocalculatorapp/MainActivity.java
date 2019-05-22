package com.example.geocalculatorapp;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    EditText distanceDisplay;
    EditText bearingDisplay;
    String distance;
    String bearing;
    Button calculate;

    //These are the two strings that input will be passed back to from the settings view
    static String bearingUnits = "";
    static String distanceUnits = "";
    public static final int SETTINGS_SELECTION = 1;

    Double lat1;
    Double lat2;
    Double lng1;
    Double lng2;
    DecimalFormat df = new DecimalFormat("#.###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        distanceDisplay = (EditText) findViewById(R.id.distance);
        bearingDisplay = (EditText) findViewById(R.id.bearing);
        EditText latitudeOne = (EditText) findViewById(R.id.lat);
        EditText longitudeOne = (EditText) findViewById(R.id.long1);
        EditText latitudeTwo = (EditText) findViewById(R.id.lat2);
        EditText longitudeTwo = (EditText) findViewById(R.id.long2);
        calculate = (Button) findViewById(R.id.calculateButton);
        Button clear = (Button) findViewById(R.id.clearButton);

        distance = String.valueOf(distanceDisplay.getText());
        bearing = String.valueOf(bearingDisplay.getText());

        calculate.setOnClickListener(v-> {
            lat1 = Double.valueOf(String.valueOf(latitudeOne.getText()));
            lat2 = Double.valueOf(String.valueOf(latitudeTwo.getText()));
            lng1 = Double.valueOf(String.valueOf(longitudeOne.getText()));
            lng2 = Double.valueOf(String.valueOf(longitudeTwo.getText()));
            distanceDisplay.setText(calculateDistance(0.0,0.0));
            bearingDisplay.setText(calculateBearing());
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        });

        clear.setOnClickListener(v-> {
            distanceDisplay.setText("");
            bearingDisplay.setText("");
            latitudeOne.setText("");
            longitudeOne.setText("");
            latitudeTwo.setText("");
            longitudeTwo.setText("");
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menus, menu); //your file name
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //View actionView = menu.getItem(viewId).getActionView();
        //View viewFromMyLayout = actionView.findViewById(R.id.viewFromMyLayout);

        switch (item.getItemId()) {
            case R.id.settingsOption:
                Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                startActivityForResult(intent,SETTINGS_SELECTION);
        }
        return false;
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode == SETTINGS_SELECTION) {
            distanceUnits = (data.getStringExtra("distanceSelection"));
            bearingUnits = (data.getStringExtra("bearingSelection"));
            calculate.performClick();
        }
    }


    protected String calculateBearing() {

        //Get the current location
        Location startingLocation = new Location("starting point");
        startingLocation.setLatitude(lat1);
        startingLocation.setLongitude(lng1);

        //Get the target location
        Location endingLocation = new Location("ending point");
        endingLocation.setLatitude(lat2);
        endingLocation.setLongitude(lng2);

        double bearingInDegrees = startingLocation.bearingTo(endingLocation);
        double bearingInMils = (bearingInDegrees * 17.777777777778);

        if(bearingUnits.equals("Mils")) {
            return df.format(bearingInMils) + " Mils";
        }

        return df.format(bearingInDegrees) + " Degrees";
    }

    protected String calculateDistance(Double el1, Double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lng2 - lng1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        double distanceInKilometers = (Math.sqrt(distance));
        double distanceInMiles = (distanceInKilometers * 0.621371);

        if(distanceUnits.equals("Miles")) {
            return df.format(distanceInMiles) + " Miles";
        }
        return df.format(distanceInKilometers) + " Kilometers";
    }
}
