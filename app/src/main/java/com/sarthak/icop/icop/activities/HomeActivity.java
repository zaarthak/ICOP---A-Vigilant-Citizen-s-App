package com.sarthak.icop.icop.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.sarthak.icop.icop.fragments.NavigationDrawerFragment;
import com.sarthak.icop.icop.fragments.PagerFragment;
import com.sarthak.icop.icop.R;
import com.sarthak.icop.icop.utils.PowerButtonReceiver;
import com.sarthak.icop.icop.utils.ShakeEventListener;

public class HomeActivity extends AppCompatActivity {

    String latitude, longitude;

    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;

    private HeadsetReceiver headsetReceiver;
    private PowerButtonReceiver powerReceiver;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        displaySelectedScreen();
        launchNavigationDrawerFragment();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {

                configDeviceShake();
            }
        });

        headsetReceiver = new HeadsetReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.HEADSET_PLUG");
        registerReceiver(headsetReceiver, intentFilter);

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        powerReceiver = new PowerButtonReceiver(HomeActivity.this);
        registerReceiver(powerReceiver, filter);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(HomeActivity.this);

        getDeviceLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        if (headsetReceiver != null) {
            unregisterReceiver(headsetReceiver);
            headsetReceiver = null;
        }

        if (powerReceiver != null) {
            unregisterReceiver(powerReceiver);
            powerReceiver = null;
        }

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
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
                locationResult.addOnCompleteListener(HomeActivity.this, new OnCompleteListener<Location>() {
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

    private void displaySelectedScreen() {

        Fragment fragment = new PagerFragment();

        //replacing the fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
    }

    public void launchNavigationDrawerFragment() {

        Fragment navigationDrawerFragment = new NavigationDrawerFragment();

        FragmentTransaction navigationDrawerFragmentTransaction = getSupportFragmentManager().beginTransaction();
        navigationDrawerFragmentTransaction.replace(R.id.home_frame, navigationDrawerFragment);
        navigationDrawerFragmentTransaction.commit();
    }

    private void configDeviceShake() {

        if (getSharedPreferences("SOS", MODE_PRIVATE).getString("shake", null) != null) {

            if (getSharedPreferences("SOS", MODE_PRIVATE).getString("shake", null).equals("true")) {

                if (getSharedPreferences("SOS", MODE_PRIVATE).getString("contact", null) != null) {

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
                                    Toast.makeText(HomeActivity.this, "An alert message has been sent to " + getSharedPreferences("SOS", MODE_PRIVATE).getString("contact", null), Toast.LENGTH_SHORT).show();
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

                Toast.makeText(HomeActivity.this, "Enable Shake to send message", Toast.LENGTH_SHORT).show();
            }
        } else {

            Toast.makeText(HomeActivity.this, "Enable Shake to send message", Toast.LENGTH_SHORT).show();
        }
    }

    public class HeadsetReceiver extends BroadcastReceiver {

        int flag = 0;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (!intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                return;
            }

            boolean connectedHeadphones = (intent.getIntExtra("state", 0) == 1);
            //boolean connectedMicrophone = (intent.getIntExtra("microphone", 0) == 1) && connectedHeadphones;

            if (connectedHeadphones) {

                flag = 1;

                SharedPreferences.Editor editor = getSharedPreferences("HEADSET", Context.MODE_PRIVATE).edit();
                editor.putBoolean("shake", true);
                editor.apply();
                Toast.makeText(context, "Headphone plugged.", Toast.LENGTH_SHORT).show();
            }

            if (flag == 1) {

                if (!connectedHeadphones) {

                    SharedPreferences.Editor editor = getSharedPreferences("HEADSET", Context.MODE_PRIVATE).edit();
                    editor.putBoolean("shake", false);
                    editor.apply();
                    Toast.makeText(context, "Headphone unplugged.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
