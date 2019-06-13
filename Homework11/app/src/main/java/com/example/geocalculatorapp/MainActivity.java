package com.example.geocalculatorapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
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

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import butterknife.ButterKnife;
import butterknife.OnClick;
import webservice.WeatherService;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.parceler.Parcels;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;



import static org.apache.commons.lang3.math.NumberUtils.isCreatable;
import static webservice.WeatherService.BROADCAST_WEATHER;

public class MainActivity extends AppCompatActivity   {

    EditText latitudeOne;
    EditText latitudeTwo;
    EditText longitudeOne;
    EditText longitudeTwo;
    EditText distanceDisplay;
    EditText bearingDisplay;
    String distance;
    String bearing;
    Button calculate;
    TextView tempOne;
    TextView tempTwo;
    TextView weatherOne;
    TextView weatherTwo;
    ImageView imgOne;
    ImageView imgTwo;

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
    public final static int NEW_LOCATION_REQUEST = 3;

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
        tempOne = (TextView) findViewById(R.id.temperatureOne);
        tempTwo = (TextView) findViewById(R.id.temperatureTwo);
        weatherOne = (TextView) findViewById(R.id.weatherOne);
        weatherTwo = (TextView) findViewById(R.id.weatherTwo);
        imgOne = (ImageView) findViewById(R.id.imgOne);
        imgTwo = (ImageView) findViewById(R.id.imgTwo);

        distance = String.valueOf(distanceDisplay.getText());
        bearing = String.valueOf(bearingDisplay.getText());
        search.setOnClickListener(v -> {
            Intent newLocation = new Intent(MainActivity.this, LocationSearchActivity.class);
            startActivityForResult(newLocation, NEW_LOCATION_REQUEST);
        });
        calculate.setOnClickListener(v -> {
            if (isCreatable(latitudeOne.getText().toString()) && isCreatable(latitudeTwo.getText().toString())
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
                entry.setTimestamp(fmt.print(DateTime.now()));
                topRef.push().setValue(entry);

                distanceDisplay.setText(calculateDistance(0.0, 0.0));
                bearingDisplay.setText(calculateBearing());

                WeatherService.startGetWeather(this, Double.toString(lat1), Double.toString(lng1), "p1");
                WeatherService.startGetWeather(this, Double.toString(lat2), Double.toString(lng2), "p2");
            }
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            //inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
               //     InputMethodManager.HIDE_NOT_ALWAYS);
        });

        clear.setOnClickListener(v -> {
            setWeatherViews(View.INVISIBLE);
            distanceDisplay.setText("");
            bearingDisplay.setText("");
            latitudeOne.setText("");
            longitudeOne.setText("");
            latitudeTwo.setText("");
            longitudeTwo.setText("");
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

           // inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                 //   InputMethodManager.HIDE_NOT_ALWAYS);
        });
        Places.initialize(getApplicationContext(), "AIzaSyAc7JN-795C-G1K-mJ0U2USu6xNMJgSSn0");
        PlacesClient placesClient = Places.createClient(this);

    }

    private BroadcastReceiver weatherReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("IN RECEIVER");
            Bundle bundle = intent.getExtras();
            double temp = bundle.getDouble("TEMPERATURE");
            String summary = bundle.getString("SUMMARY");
            String icon = bundle.getString("ICON").replaceAll("-", "_");
            String key = bundle.getString("KEY");
            int resID = getResources().getIdentifier(icon , "drawable", getPackageName());
            setWeatherViews(View.VISIBLE);
            if (key.equals("p1"))  {
                weatherOne.setText(summary);
                tempOne.setText(Double.toString(temp));
                imgOne.setImageResource(resID);
                imgOne.setVisibility(View.INVISIBLE);
            } else {
                weatherTwo.setText(summary);
                tempTwo.setText(Double.toString(temp));
                imgTwo.setImageResource(resID);
            }
        }
    };

    private void setWeatherViews(int visible) {
        tempOne.setVisibility(visible);
        tempTwo.setVisibility(visible);
        weatherTwo.setVisibility(visible);
        weatherOne.setVisibility(visible);
        imgOne.setVisibility(visible);
        imgTwo.setVisibility(visible);
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
        IntentFilter weatherFilter = new IntentFilter(BROADCAST_WEATHER);
        LocalBroadcastManager.getInstance(this).registerReceiver(weatherReceiver, weatherFilter);
        setWeatherViews(View.INVISIBLE);
    }

    @Override
    public void onPause(){
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(weatherReceiver);
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
        } else if(item.getItemId() == R.id.action_history) {
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
        }

        if (resultCode == HISTORY_RESULT) {
            LocationLookup loc = (LocationLookup) Parcels.unwrap(data.getParcelableExtra("item"));
            this.latitudeOne.setText(String.valueOf(loc.origLat));
            this.longitudeOne.setText(String.valueOf(loc.origLng));
            this.latitudeTwo.setText(String.valueOf(loc.endLat));
            this.longitudeTwo.setText(String.valueOf(loc.endLng));
            calculate.performClick();  // code that updates the calcs.
        }

       if (resultCode == NEW_LOCATION_REQUEST) {

                if (data != null && data.hasExtra("Location")) {

                    Parcelable parcel = data.getParcelableExtra("Location");
                    LocationLookup l = Parcels.unwrap(parcel);
                    lat1 = l.origLat;
                    lat2 = l.endLat;
                    lng1 = l.origLng;
                    lng2 = l.endLng;
                    latitudeOne.setText(new Double(lat1).toString());
                    latitudeTwo.setText(new Double(lat2).toString());
                    longitudeOne.setText(new Double(lng1).toString());
                    longitudeTwo.setText(new Double(lng2).toString());
                  calculate.performClick();
                    Log.d("MainActivity", "New Loc:" + l.endLng);
                    Log.d("MainActivity", "New Loc:" + l.origLat);
                    Log.d("MainActivity", "New Loc:" + l.endLat);
                    Log.d("MainActivity", "New Loc:" + l.origLng);
                }
            }
            else
                super.onActivityResult(requestCode, resultCode, data);

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
