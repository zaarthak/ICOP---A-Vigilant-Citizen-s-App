package com.sarthak.icop.icop.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sarthak.icop.icop.R;
import com.sarthak.icop.icop.adapters.ReportsAdapter;
import com.sarthak.icop.icop.databases.ReportDatabase;
import com.sarthak.icop.icop.models.Report;
import com.sarthak.icop.icop.utils.RecyclerViewOnClickListener;
import com.sarthak.icop.icop.utils.SimpleDividerItemDecoration;

import java.util.ArrayList;

public class ReportFetchActivity extends AppCompatActivity implements View.OnClickListener, RecyclerViewOnClickListener {

    private ArrayList<Report> reportList = new ArrayList<>();

    private RelativeLayout mLayout;
    private ImageButton mSearchBtn;
    private EditText mSearchEt;

    private RecyclerView mReportsList;
    private ReportsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_fetch);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        int type = getIntent().getIntExtra("type", 0);

        mLayout = findViewById(R.id.layout);
        mSearchEt = findViewById(R.id.fetch_et);
        mSearchBtn = findViewById(R.id.search_btn);

        if (type == 1) {
            getSupportActionBar().setTitle("Your Reports");
            reportList = new ReportDatabase(ReportFetchActivity.this).getAll();
            mLayout.setVisibility(View.GONE);
        } else if (type == 2) {
            getSupportActionBar().setTitle("Report Lookup");
            mSearchBtn.setOnClickListener(this);
            mLayout.setVisibility(View.VISIBLE);
        }

        mReportsList = findViewById(R.id.reports_list);
        mReportsList.setLayoutManager(new LinearLayoutManager(this));
        mReportsList.addItemDecoration(new SimpleDividerItemDecoration(ReportFetchActivity.this));

        adapter = new ReportsAdapter(reportList);
        adapter.setOnRecyclerViewItemClickListener(this);
        mReportsList.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.search_btn:

                String id = mSearchEt.getText().toString();

                if (!id.equals("")) {

                    reportList.clear();

                    Report report = new ReportDatabase(this).getReportDetails(id);

                    if (report != null) {

                        reportList.add(new ReportDatabase(this).getReportDetails(id));
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
        }
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
}
