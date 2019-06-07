package com.example.geocalculatorapp;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.parceler.Parcels;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LocationSearchActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final String TAG = "New location ";
    @BindView(R.id.Gvsu)
    TextView gvsu;
    @BindView(R.id.msu)
    TextView msu;
    @BindView(R.id.calc_date)
    TextView calc_date;
    @BindView(R.id.date)
    TextView dateview;
    @BindView(R.id.loc_info)
    TextView loc_info;

    private DateTime datepick;
    private DatePickerDialog dpDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);
        ButterKnife.bind(:this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DateTime today = DateTime.now()
        dpDialog = DatePickerDialog.newInstance(this, today.getYear(), today.getMonthOfYear() - 1, today.getDayOfMonth());
        dateview.setText(formatted(today));
    }

    @OnClick(R.id.date)
    public void datePressed() {
        dpDialog.show(getSupportFragmentManager(), "daterangeddialog");
    }

    @OnClick(R.id.fab)
    public void FABPressed() {
        Intent result = new Intent();
        LocationLookup alocationlookup = new LocationLookup();
        alocationlookup.origLat = origLat.getText().toString();
        alocationlookup.endLat = endLat.getText().toString();
        alocationlookup.origLng = origLng.getText().toString();
        alocationlookup.endLng = endLng.getText().toString();
        Parcelable parcel = Parcels.wrap(alocationlookup);
        result.putExtra("Location", parcel);
        setResult(RESULT_OK, result);
        finish():
    }

    private String formatted(DateTime d) {
        return d.monthOfYear().

                getAsShortText()(Locale.getDefault()) + " " + d.getDayOfMonth() + " , " + d.getYear();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place pl = Autocomplete.getPlaceFromIntent(data)
                        Location.setText(pl.getAddress())
                Log.i(TAG, "onActivityResult:" + pl.getAddress());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                //                   Status stat = Autocomplete.getStatusFromIntent(data);
                Log.d(TAG, "onActivityResult: ");
            } else if (requestCode == RESULT_CANCELED) {
                System.out.println("Cancelled");
            }

        } else
            super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth)

    Date =new

    DateTime( year, monthOfYear +1, dayOfMOnth,0,0);
            dateView.setText(

    formatted(Date));
}
});



