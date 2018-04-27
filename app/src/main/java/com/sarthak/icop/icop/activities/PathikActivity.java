package com.sarthak.icop.icop.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sarthak.icop.icop.R;

public class PathikActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String BADGE_NO = "DL-545400";
    private static final String VEHICLE_NO = "DL 5S AX 8775";

    private EditText mBadgeEt;
    private EditText mVehicleEt;

    private Button mSearchBtn;
    private Button mInformBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pathik);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mBadgeEt = findViewById(R.id.badge_et);
        mVehicleEt = findViewById(R.id.vehicle_et);
        mSearchBtn = findViewById(R.id.search_btn);
        mInformBtn = findViewById(R.id.inform_btn);

        mSearchBtn.setOnClickListener(this);
        mInformBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.search_btn:

                String vehicleNo = mVehicleEt.getText().toString();
                String badgeNo = mBadgeEt.getText().toString();

                if (!TextUtils.isEmpty(vehicleNo)) {

                    if (vehicleNo.equals(VEHICLE_NO)) {

                        Toast.makeText(this, "1 item found.\n" +
                                "Vehicle No.: DL 5S AX 8775\n" +
                                "Badge No.: DL-545400\n" +
                                "Vehicle Type: Car\n" +
                                "Vehicle Model: Hyundai\n" +
                                "Vehicle Registration Date: 24 March 2015", Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(this, "No results found.", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    if (!TextUtils.isEmpty(badgeNo)) {

                        if (badgeNo.equals(BADGE_NO)) {

                            Toast.makeText(this, "1 item found.\n" +
                                    "Badge No.: DL-545400\n" +
                                    "Vehicle No.: DL 5S AX 8775\n" +
                                    "Vehicle Type: Car\n" +
                                    "Vehicle Model: Hyundai\n" +
                                    "Vehicle Registration Date: 24 March 2015", Toast.LENGTH_LONG).show();
                        } else {

                            Toast.makeText(this, "No results found.", Toast.LENGTH_LONG).show();
                        }
                    } else {

                        Toast.makeText(this, "No data entered. Please input some data to search", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.inform_btn:

                String vehicle = mVehicleEt.getText().toString();

                Intent informIntent = new Intent(PathikActivity.this, InformActivity.class);
                informIntent.putExtra("vehicle", vehicle);
                startActivity(informIntent);
                break;
        }
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
