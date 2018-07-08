package com.sarthak.icop.icop.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.sarthak.icop.icop.activities.HomeActivity;

import static android.content.Context.MODE_PRIVATE;

public class PowerButtonReceiver extends BroadcastReceiver {

    private static int countPowerOff = 0;

    String latitude, longitude;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    private Context mContext;

    public PowerButtonReceiver(Context context) {

        mContext = context;

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        getDeviceLocation();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF))
        {
            Log.e("In on receive", "In Method:  ACTION_SCREEN_OFF");
            countPowerOff++;

            if (countPowerOff > 1)
            {
                countPowerOff=0;
                configPowerButton(context);
            }
        }
        else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON))
        {
            Log.e("In on receive", "In Method:  ACTION_SCREEN_ON");
        }
        else if(intent.getAction().equals(Intent.ACTION_USER_PRESENT))
        {
            Log.e("In on receive", "In Method:  ACTION_USER_PRESENT");
        }
    }

    private void configPowerButton(final Context context) {

        if (context.getSharedPreferences("SOS", MODE_PRIVATE).getString("power", null) != null) {

            if (context.getSharedPreferences("SOS", MODE_PRIVATE).getString("power", null).equals("true")) {

                if (context.getSharedPreferences("SOS", MODE_PRIVATE).getString("contact", null) != null) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Do you want to send alert message to " + context.getSharedPreferences("SOS", MODE_PRIVATE).getString("contact", null) + "?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    String contact = context.getSharedPreferences("SOS", MODE_PRIVATE).getString("contact", "");
                                    String message = context.getSharedPreferences("SOS", MODE_PRIVATE).getString("message", "")
                                            + "\n\nhttp://www.google.com/maps/place/"+latitude+","+longitude;
                                    SmsManager smsManager = SmsManager.getDefault();
                                    smsManager.sendTextMessage(contact, null, message, null, null);
                                    Log.d("MESSAGE", message);
                                    Toast.makeText(context, "An alert message has been sent to " + context.getSharedPreferences("SOS", MODE_PRIVATE).getString("contact", null), Toast.LENGTH_SHORT).show();
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
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Please register a Contact and Alert message in SOS.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            } else {

                Toast.makeText(context, "This feature has been disabled.\nEnable it in the navigation drawer to continue.", Toast.LENGTH_SHORT).show();
            }
        } else {

            Toast.makeText(context, "This feature has been disabled.\nEnable it in the navigation drawer to continue.", Toast.LENGTH_SHORT).show();
        }
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
            locationResult.addOnCompleteListener(((Activity) mContext), new OnCompleteListener<Location>() {
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
    }
}
