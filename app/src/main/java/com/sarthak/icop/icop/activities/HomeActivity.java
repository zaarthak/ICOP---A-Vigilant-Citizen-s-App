package com.sarthak.icop.icop.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.sarthak.icop.icop.fragments.NavigationDrawerFragment;
import com.sarthak.icop.icop.fragments.PagerFragment;
import com.sarthak.icop.icop.R;
import com.sarthak.icop.icop.utils.ShakeEventListener;

public class HomeActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;

    private HeadsetReceiver headsetReceiver;

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

        if (getSharedPreferences("SOS", MODE_PRIVATE).getString("status", null) != null) {

            if (getSharedPreferences("SOS", MODE_PRIVATE).getString("status", null).equals("true")) {

                if (getSharedPreferences("SOS", MODE_PRIVATE).getString("contact", null) != null) {

                    Toast.makeText(HomeActivity.this,
                            "Alert message sent to " + getSharedPreferences("SOS", MODE_PRIVATE).getString("contact", null),
                            Toast.LENGTH_LONG).show();
                } else {

                    Toast.makeText(HomeActivity.this, "Please register a Contact and Alert message in SOS.", Toast.LENGTH_SHORT).show();
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
                editor.putBoolean("status", true);
                editor.apply();
                Toast.makeText(context, "Headphone plugged.", Toast.LENGTH_SHORT).show();
            }

            if (flag == 1) {

                if (!connectedHeadphones) {


                    SharedPreferences.Editor editor = getSharedPreferences("HEADSET", Context.MODE_PRIVATE).edit();
                    editor.putBoolean("status", false);
                    editor.apply();
                    Toast.makeText(context, "Headphone unplugged.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
