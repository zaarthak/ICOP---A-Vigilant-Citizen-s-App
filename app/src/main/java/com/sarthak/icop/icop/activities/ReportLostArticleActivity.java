package com.sarthak.icop.icop.activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.sarthak.icop.icop.R;

public class ReportLostArticleActivity extends AppCompatActivity implements View.OnClickListener {

    String[] categoryItems = new String[16];
    String category = "";

    private ArrayAdapter<String> adapter;

    private Button mListBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_lost_article);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initCategoryItems();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoryItems);
        mListBtn = findViewById(R.id.list);
        mListBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.list:
                new AlertDialog.Builder(this)
                        .setTitle("Select Lost Article/Document")
                        .setAdapter(adapter, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                category = categoryItems[which];
                                mListBtn.setText(categoryItems[which]);
                                dialog.dismiss();
                            }
                        }).create().show();
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

    private void initCategoryItems() {

        categoryItems[0] = "Missing person";
        categoryItems[1] = "Mobile Phone/SIM Card";
        categoryItems[2] = "Driving License";
        categoryItems[3] = "ATM/Debit/Credit Card";
        categoryItems[4] = "Passport";
        categoryItems[5] = "PAN Card";
        categoryItems[6] = "Aadhar Card";
        categoryItems[7] = "Vehicle";
        categoryItems[8] = "Fixed Deposit Receipt";
        categoryItems[9] = "Voter ID Card";
        categoryItems[10] = "Bank Demand Draft";
        categoryItems[11] = "Cheques";
        categoryItems[12] = "Laptop";
        categoryItems[13] = "Ration Card";
        categoryItems[14] = "Tablet";
        categoryItems[15] = "Other";
    }
}
