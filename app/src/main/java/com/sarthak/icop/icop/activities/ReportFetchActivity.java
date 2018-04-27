package com.sarthak.icop.icop.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sarthak.icop.icop.R;
import com.sarthak.icop.icop.adapters.ReportsAdapter;
import com.sarthak.icop.icop.models.Report;
import com.sarthak.icop.icop.utils.RecyclerViewOnClickListener;
import com.sarthak.icop.icop.utils.SimpleDividerItemDecoration;

import java.util.ArrayList;

public class ReportFetchActivity extends AppCompatActivity implements View.OnClickListener, RecyclerViewOnClickListener, TextWatcher {

    private ArrayList<Report> reportList = new ArrayList<>();

    private RelativeLayout mLayout;
    private ImageButton mSearchBtn;
    private EditText mSearchEt;

    private ProgressDialog progressDialog;

    private RecyclerView mReportsList;
    private ReportsAdapter adapter;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_fetch);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        int type = getIntent().getIntExtra("type", 0);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Reports");

        mLayout = findViewById(R.id.layout);
        mSearchEt = findViewById(R.id.fetch_et);
        mSearchBtn = findViewById(R.id.search_btn);

        if (type == 1) {
            getSupportActionBar().setTitle("Your Reports");
            getReports();
            mLayout.setVisibility(View.GONE);
        } else if (type == 2) {
            getSupportActionBar().setTitle("Report Lookup");
            mLayout.setVisibility(View.VISIBLE);
            mSearchBtn.setOnClickListener(this);
        }

        mReportsList = findViewById(R.id.reports_list);
        mReportsList.setLayoutManager(new LinearLayoutManager(this));
        mReportsList.addItemDecoration(new SimpleDividerItemDecoration(ReportFetchActivity.this));

        adapter = new ReportsAdapter(reportList);
        adapter.setOnRecyclerViewItemClickListener(this);
        mReportsList.setAdapter(adapter);

        mSearchEt.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.search_btn:

                String id = mSearchEt.getText().toString();

                if (!id.equals("")) {

                    searchForReports(id);
                }
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        if (charSequence.length() != 0) {
            searchForReports(charSequence.toString());
        } else {
            searchForReports(" ");
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onItemClicked(View view, int position) {

        Intent viewReportIntent = new Intent(ReportFetchActivity.this, ViewReportActivity.class);
        viewReportIntent.putExtra("object", reportList.get(position));
        startActivity(viewReportIntent);
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

    private void getReports() {

        progressDialog = new ProgressDialog(ReportFetchActivity.this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Please wait while we fetch your reports");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                reportList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    if (snapshot != null) {

                        reportList.add(snapshot.getValue(Report.class));
                        adapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                }

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(ReportFetchActivity.this, "Unable to fetch reports. Please try again.", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void searchForReports(String id) {

        mDatabase.orderByChild("id").startAt(id).endAt(id + "\uf8ff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                reportList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    if (snapshot != null) {

                        reportList.add(snapshot.getValue(Report.class));
                    } else {

                        reportList.clear();
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
