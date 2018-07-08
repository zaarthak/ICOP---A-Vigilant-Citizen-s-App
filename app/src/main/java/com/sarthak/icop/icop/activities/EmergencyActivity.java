package com.sarthak.icop.icop.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.sarthak.icop.icop.R;

public class EmergencyActivity extends AppCompatActivity implements View.OnClickListener {

    String latitude, longitude;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView mSosBtn = findViewById(R.id.sos_btn);
        mSosBtn.setOnClickListener(this);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(EmergencyActivity.this);

        getDeviceLocation();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.sos_btn:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Do you want to send alert message to " + getSharedPreferences("SOS", MODE_PRIVATE).getString("contact", null) + "?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                String contact = getSharedPreferences("SOS", MODE_PRIVATE).getString("contact", "");
                                String message = getSharedPreferences("SOS", MODE_PRIVATE).getString("message", "")
                                        + "\n\nhttp://www.google.com/maps/place/"+latitude+","+longitude;
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(contact, null, message, null, null);
                                Log.d("MESSAGE", message);
                                Toast.makeText(EmergencyActivity.this, "An alert message has been sent to " + getSharedPreferences("SOS", MODE_PRIVATE).getString("contact", null), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //do nothing
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sos_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                onBackPressed();
                return true;

            case R.id.erase:

                SharedPreferences.Editor editor = getSharedPreferences("SOS", Context.MODE_PRIVATE).edit();

                editor.remove("contact");
                editor.remove("message");
                editor.apply();
                startActivity(new Intent(EmergencyActivity.this, SosActivity.class));
                finish();
                break;
        }
        return true;
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    public void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(EmergencyActivity.this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {

                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        mLastKnownLocation = task.getResult();
                        if (mLastKnownLocation != null) {

                            latitude = String.valueOf(mLastKnownLocation.getLatitude());
                            longitude = String.valueOf(mLastKnownLocation.getLongitude());
                        } else {

                            getDeviceLocation();
                        }
                    } else {
                        Log.e("TAG", "Exception: %s", task.getException());
                    }
                }
            });

        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }}
