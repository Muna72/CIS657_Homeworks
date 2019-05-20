package com.example.geocalculatorapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    EditText distanceDisplay;
    EditText bearingDisplay;
    String distance;
    String bearing;

    //These are the two strings that input will be passed back to from the settings view
    static String bearingUnits;
    static String distanceUnits;
    public static final int SETTINGS_SELECTION = 1;

    Double lat1;
    Double lat2;
    Double lng1;
    Double lng2;

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
        Button calculate = (Button) findViewById(R.id.calculateButton);
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
        });

        clear.setOnClickListener(v-> {
            distanceDisplay.setText("");
            bearingDisplay.setText("");
            latitudeOne.setText("");
            longitudeOne.setText("");
            latitudeTwo.setText("");
            longitudeTwo.setText("");
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
        System.out.println("GOTIT");

        //Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.dualPane);
        //fragment.onActivityResult(requestCode, resultCode, data);

        if(resultCode == SETTINGS_SELECTION) {
            distanceUnits = (data.getStringExtra("distanceSelection"));
            bearingUnits = (data.getStringExtra("bearingSelection"));
            System.out.println(distanceUnits + " " + bearingUnits);
        }
    }


    protected String calculateBearing() {

        double longDiff= lng2-lng1;
        //all inputs to Math.sin(), Math.cos() and all the other trigonometric functions must be in radians.
        //If your inputs are degrees you'll need to convert them using Math.toRadians()
        double y = Math.sin(longDiff)*Math.cos(lat2);
        double x = Math.cos(lat1)*Math.sin(lat2)-Math.sin(lat1)*Math.cos(lat2)*Math.cos(longDiff);

        double bearingInDegrees = (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;
        double bearingInMils = (bearingInDegrees * 17.777777777778);

        if(bearingUnits == "Mils") {
            System.out.println("IN CONDITIONAL");
            return bearingInMils + " Mils";
        }

        return bearingInDegrees + " degrees";
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

        double distanceInKilometers = (Math.sqrt(distance) * 1000);
        double distanceInMiles = (distanceInKilometers * 0.621371);

        if(distanceUnits == "Miles") {
            return distanceInMiles + " miles";
        }
        return distanceInKilometers + " kilometers";
    }
}
