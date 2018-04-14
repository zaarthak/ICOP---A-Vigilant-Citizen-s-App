package com.sarthak.icop.icop.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.sarthak.icop.icop.adapters.CallAdapter;
import com.sarthak.icop.icop.databases.CallDatabase;
import com.sarthak.icop.icop.models.Police;
import com.sarthak.icop.icop.R;
import com.sarthak.icop.icop.utils.SimpleDividerItemDecoration;

import java.util.ArrayList;

public class CallActivity extends AppCompatActivity {

    private ArrayList<Police> policeList = new ArrayList<>();

    private RecyclerView mCallList;
    private CallAdapter adapter;

    private CallDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_police);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int type = getIntent().getIntExtra("type", 0);

        if (type == 1) {

            getSupportActionBar().setTitle("Call Police");
        } else {

            getSupportActionBar().setTitle("Call Administration");
        }

        mDatabase = new CallDatabase(CallActivity.this);
        policeList = mDatabase.getPoliceDetails(type);

        adapter = new CallAdapter(CallActivity.this, policeList);
        mCallList = findViewById(R.id.call_police_list);

        mCallList.setLayoutManager(new LinearLayoutManager(this));
        mCallList.addItemDecoration(new SimpleDividerItemDecoration(CallActivity.this));
        mCallList.setAdapter(adapter);
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
