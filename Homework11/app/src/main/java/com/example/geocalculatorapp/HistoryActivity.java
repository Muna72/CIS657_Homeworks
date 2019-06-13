package com.example.geocalculatorapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.parceler.Parcels;

public class HistoryActivity extends AppCompatActivity implements HistoryFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void onListFragmentInteraction(LocationLookup item) {
        System.out.println("ITEM INFOBEFORE INTENT PASSES: " + item.getOrigLng() + " " + item.getEndLat());
        Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
        Parcelable parcel = Parcels.wrap(item);
        intent.putExtra("item", parcel);
        setResult(MainActivity.HISTORY_RESULT,intent);
        finish();
    }

}
