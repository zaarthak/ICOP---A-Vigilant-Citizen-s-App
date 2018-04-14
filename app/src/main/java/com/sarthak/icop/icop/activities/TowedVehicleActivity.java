package com.sarthak.icop.icop.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import com.sarthak.icop.icop.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TowedVehicleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_towed_vehicle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText mDateEt = findViewById(R.id.date);

        Calendar calendar = Calendar.getInstance();
        String strDate = new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime());
        mDateEt.setText(strDate);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                onBackPressed();
                return true;

            default:

                return super.onOptionsItemSelected(item);
        }
    }
}
