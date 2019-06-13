package com.example.geocalculatorapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.parceler.Parcels;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LocationSearchActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE1 = 2;

    private static final String TAG = "New location ";
    @BindView(R.id.loc1)
    TextView loc1;
    @BindView(R.id.loc2)
    TextView loc2;
    @BindView(R.id.calc_date)
    TextView calc_date;
    @BindView(R.id.date)
    TextView dateView;
    @BindView(R.id.loc_info)
    TextView loc_info;

    private DateTime date;
    private DatePickerDialog dpDialog;
    Place pl;
    Place p2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DateTime today = DateTime.now();
//        dpDialog = DatePickerDialog.newInstance (this, today.getYear(), today.getMonthOfYear() - 1, today.getDayOfMonth());
        dpDialog = new DatePickerDialog(this, this, today.getYear(), today.getMonthOfYear() - 1, today.getDayOfMonth());


        // DatePickerDialog.OnDateSetListener;
        dateView.setText(formatted(today));
        date = today;
    }

    @OnClick(R.id.loc1)
    public void location1Pressed() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this);
        startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
    }

    @OnClick(R.id.loc2)
    public void location2Pressed() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this);
        startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE1);
    }


    @OnClick(R.id.date)
    public void datePressed() {
        dpDialog.show();
        //dpDialog.show(getFragmentManager(), "daterangeddialog");
    }

    //LocationLookup alocationlookup = new LocationLookup();

    @OnClick(R.id.fab)
    public void FABPressed() {

        Intent result = new Intent(LocationSearchActivity.this, MainActivity.class);
        LocationLookup alocationlookup = new LocationLookup();
        alocationlookup.origLat = pl.getLatLng().latitude;
        alocationlookup.origLng = pl.getLatLng().longitude;
        alocationlookup.endLat = p2.getLatLng().latitude;
        alocationlookup.endLng = p2.getLatLng().longitude;
        // DateTimeFormatter fmt = ISODateTimeFormat.dateTime(
        //        result.putExtra("Location", parcel););
        // alocationlookup.date = fmt.print(date);
        alocationlookup.getTimestamp();
        // alocationlookup.endLng = endLat.getText().toString();
        Parcelable parcel = Parcels.wrap(alocationlookup);
        result.putExtra("Location", parcel);
        setResult(MainActivity.NEW_LOCATION_REQUEST, result);
       // startActivity(result);
        finish();
    }

    private String formatted(DateTime d) {
        return d.monthOfYear().

                getAsShortText(Locale.getDefault()) + " " + d.getDayOfMonth() + " , " + d.getYear();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                pl = Autocomplete.getPlaceFromIntent(data);
                loc1.setText(pl.getName());
                Log.i(TAG, "Place:" + pl.getName());

            }
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status stat = Autocomplete.getStatusFromIntent(data);
            Log.d(TAG, stat.getStatusMessage());
        } else if (requestCode == RESULT_CANCELED) {
            System.out.println("Cancelled");
        }



       else if(requestCode ==PLACE_AUTOCOMPLETE_REQUEST_CODE1)

    {
        if (resultCode == RESULT_OK) {
            p2 = Autocomplete.getPlaceFromIntent(data);
            loc2.setText(p2.getName());
            Log.i(TAG, "Place:" + p2.getName());

        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status stat = Autocomplete.getStatusFromIntent(data);
            Log.d(TAG, stat.getStatusMessage());
        }
    } else if(requestCode ==RESULT_CANCELED)

    {
        System.out.println("Cancelled");
    }

             else
                     super.onActivityResult(requestCode, resultCode, data);


}


        @Override
        public void onDateSet (DatePicker view,int year, int month, int dayOfMonth){
            date = new DateTime(year, month + 1, dayOfMonth, 0, 0);
            dateView.setText(formatted(date));
        }
    }



