package com.example.geocalculatorapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    String distance;
    String bearing;

    //These are the two strings that input will be passed back to from the settings view
    String bearingUnits;
    String distanceUnits;

    Double lat1;
    Double lat2;
    Double lng1;
    Double lng2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText distanceDisplay = (EditText) findViewById(R.id.distance);
        EditText bearingDisplay = (EditText) findViewById(R.id.bearing);
        EditText latitudeOne = (EditText) findViewById(R.id.lat);
        EditText longitudeOne = (EditText) findViewById(R.id.long1);
        EditText latitudeTwo = (EditText) findViewById(R.id.lat2);
        EditText longitudeTwo = (EditText) findViewById(R.id.long2);
        Button calculate = (Button) findViewById(R.id.calculateButton);
        Button clear = (Button) findViewById(R.id.clearButton);

        lat1 = Double.valueOf(String.valueOf(latitudeOne.getText()));

        distance = String.valueOf(distanceDisplay.getText());
        bearing = String.valueOf(bearingDisplay.getText());

        calculate.setOnClickListener(v-> {

            if()
            distanceDisplay.setText(String.valueOf(calculateDistance(0.0,0.0)));
            bearingDisplay.setText(String.valueOf(calculateBearing()));
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



    protected Double calculateBearing() {

        double longDiff= lng2-lng1;
        //all inputs to Math.sin(), Math.cos() and all the other trigonometric functions must be in radians.
        //If your inputs are degrees you'll need to convert them using Math.toRadians()
        double y = Math.sin(longDiff)*Math.cos(lat2);
        double x = Math.cos(lat1)*Math.sin(lat2)-Math.sin(lat1)*Math.cos(lat2)*Math.cos(longDiff);

        double bearingInDegrees = (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;
        double bearingInMils = (bearingInDegrees * 17.777777777778);

        if(bearingUnits == "Mils") {
            return bearingInMils;
        }

        return bearingInDegrees;
    }

    protected Double calculateDistance(Double el1, Double el2) {

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
            return distanceInMiles;
        }
        return distanceInKilometers;
    }
}
