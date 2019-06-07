package com.example.geocalculatorapp;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import com.google.android.libraries.places.api.Places;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.parceler.Parcels;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.math.NumberUtils.isCreatable;

public class MainActivity extends AppCompatActivity {

    EditText latitudeOne;
    EditText latitudeTwo;
    EditText longitudeOne;
    EditText longitudeTwo;
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
    public static int HISTORY_RESULT = 2;
    DatabaseReference topRef;
    public static List<LocationLookup> allHistory;
    final int NEW_LOCATION_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        allHistory = new ArrayList<LocationLookup>();

        distanceDisplay = (EditText) findViewById(R.id.distance);
        bearingDisplay = (EditText) findViewById(R.id.bearing);
        latitudeOne = (EditText) findViewById(R.id.lat);
        longitudeOne = (EditText) findViewById(R.id.long1);
        latitudeTwo = (EditText) findViewById(R.id.lat2);
        longitudeTwo = (EditText) findViewById(R.id.long2);
        calculate = (Button) findViewById(R.id.calculateButton);
        Button clear = (Button) findViewById(R.id.clearButton);
        Button search = (Button) findViewById(R.id.search);
         search.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent newLocation = new Intent(MainActivity.this,LocationLookup.class);
                 startActivityForResult(newLocation,NEW_LOCATION_REQUEST);
             }
         });

        distance = String.valueOf(distanceDisplay.getText());
        bearing = String.valueOf(bearingDisplay.getText());

        calculate.setOnClickListener(v-> {
            if(isCreatable(latitudeOne.getText().toString()) && isCreatable(latitudeTwo.getText().toString())
            && isCreatable(longitudeOne.getText().toString()) && isCreatable(longitudeTwo.getText().toString())) {
                lat1 = Double.valueOf(String.valueOf(latitudeOne.getText()));
                lat2 = Double.valueOf(String.valueOf(latitudeTwo.getText()));
                lng1 = Double.valueOf(String.valueOf(longitudeOne.getText()));
                lng2 = Double.valueOf(String.valueOf(longitudeTwo.getText()));
                // remember the calculation.

                LocationLookup entry = new LocationLookup();
                entry.setOrigLat(lat1);
                entry.setOrigLng(lng1);
                entry.setEndLat(lat2);
                entry.setEndLng(lng2);
                DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
                entry.setTimestamp(DateTime.now());
                topRef.push().setValue(entry);

                distanceDisplay.setText(calculateDistance(0.0, 0.0));
                bearingDisplay.setText(calculateBearing());
            }
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

            );
        });

        Places.initialize(getApplicationContext(),"Your-Key-Goes-Here");
    }

    private ChildEventListener chEvListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            LocationLookup entry = (LocationLookup) dataSnapshot.getValue(LocationLookup.class);
            entry._key = dataSnapshot.getKey();
            allHistory.add(entry);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            LocationLookup entry = (LocationLookup) dataSnapshot.getValue(LocationLookup.class);
            List<LocationLookup> newHistory = new ArrayList<LocationLookup>();
            for (LocationLookup t : allHistory) {
                if (!t._key.equals(dataSnapshot.getKey())) {
                    newHistory.add(t);
                }
            }
            allHistory = newHistory;
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    public void onResume(){
        super.onResume();
        allHistory.clear();
        topRef = FirebaseDatabase.getInstance().getReference("history");
        topRef.addChildEventListener (chEvListener);
        //topRef.addValueEventListener(valEvListener);
    }

    @Override
    public void onPause(){
        super.onPause();
        topRef.removeEventListener(chEvListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menus, menu); //your file name
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.settingsOption) {
            Intent intent = new Intent(MainActivity.this,
                    SettingsActivity.class);
            startActivityForResult(intent, SETTINGS_SELECTION );
            return true;
        } else if(item.getItemId() == R.id.action_history) { //TODO use parcels!!
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivityForResult(intent, HISTORY_RESULT );
            return true;
        }
        return false;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == SETTINGS_SELECTION) {
            this.bearingUnits = data.getStringExtra("bearingUnits");
            this.distanceUnits = data.getStringExtra("distanceUnits");
            calculate.performClick();
        } else if (resultCode == HISTORY_RESULT) {
            String[] vals = data.getStringArrayExtra("item");
            this.latitudeOne.setText(vals[0]);
            this.longitudeOne.setText(vals[1]);
            this.latitudeTwo.setText(vals[2]);
            this.longitudeTwo.setText(vals[3]);
            calculate.performClick();  // code that updates the calcs.
        }

            else if (resultCode == NEW_LOCATION_REQUEST) {
                if (data != null && data.hasExtra("Location")) {
                    Parcelable parcel = data.getParcelableExtra("Location");
                    LocationLookup l = Parcels.unwrap(parcel);
                    Log.d("MainActivity", "New Loc:" + l.endLng);
                }
            }
            else
                super.onActivityResult(requestCode, resultCode, data);

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

        if("Mils".equals(bearingUnits)) {
            return "Bearing: " + df.format(bearingInMils) + " Mils";
        }

        return "Bearing: " + df.format(bearingInDegrees) + " Degrees";
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

        if("Miles".equals(distanceUnits)) {
            return "Distance: " + df.format(distanceInMiles) + " Miles";
        }
        return "Distance: " + df.format(distanceInKilometers) + " Kilometers";
    }
}
