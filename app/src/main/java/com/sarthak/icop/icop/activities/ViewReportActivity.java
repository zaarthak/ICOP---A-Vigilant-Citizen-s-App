package com.sarthak.icop.icop.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.sarthak.icop.icop.R;
import com.sarthak.icop.icop.models.Report;

public class ViewReportActivity extends AppCompatActivity {

    private TextView mCategoryTv, mInfoTv, mContactTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Report report = (Report) getIntent().getSerializableExtra("object");
        getSupportActionBar().setTitle(report.getId());

        mCategoryTv = findViewById(R.id.view_report_category);
        mInfoTv = findViewById(R.id.view_report_info);
        mContactTv = findViewById(R.id.view_report_contact);

        mCategoryTv.setText(report.getCategory());
        mInfoTv.setText(report.getInformation());
        mContactTv.setText("Issued by: " + report.getContact());
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
