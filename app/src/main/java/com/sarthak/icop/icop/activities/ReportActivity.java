package com.sarthak.icop.icop.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sarthak.icop.icop.R;
import com.sarthak.icop.icop.models.Report;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener {

    String[] categoryItems = new String[11];
    String category = "";

    int reportCount = 0;

    private ArrayAdapter<String> adapter;

    private Button mCategoryList;
    private EditText mInformationEt, mContactEt;
    private Button mSendBtn;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Reports");
        getReportCount();

        mCategoryList = findViewById(R.id.category_list);
        mInformationEt = findViewById(R.id.complaint);
        mContactEt = findViewById(R.id.contact);
        mSendBtn = findViewById(R.id.send_btn);

        initCategoryItems();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoryItems);
        mCategoryList.setOnClickListener(this);

        mSendBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.category_list:

                new AlertDialog.Builder(this)
                        .setTitle("Select category")
                        .setAdapter(adapter, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                category = categoryItems[which];
                                mCategoryList.setText(categoryItems[which]);
                                dialog.dismiss();
                            }
                        }).create().show();
                break;

            case R.id.send_btn:

                String information = "";
                String contact = "";

                information = mInformationEt.getText().toString();
                contact = mContactEt.getText().toString();

                if (!information.equals("") && !category.equals("")) {

                    addDataToFirebase(category, information, contact);
                }
                break;
        }
    }

    private void getReportCount() {

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                reportCount = (int) dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addDataToFirebase(String category, String information, String contact) {

        // setup progress dialog
        final ProgressDialog mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setTitle("Please wait...");
        mProgressDialog.setMessage("Please wait while we register your report");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        String timestamp = String.valueOf(System.currentTimeMillis()/1000);

        Report report = new Report();
        report.setId(String.format("CT-%05d", reportCount + 1));
        report.setCategory(category);
        report.setInformation(information);
        report.setContact(contact);
        report.setStatus("Pending");

        mDatabase.child(timestamp).setValue(report).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    Toast.makeText(ReportActivity.this, "Your report has been registered.", Toast.LENGTH_SHORT).show();
                    mInformationEt.setText("");
                    mContactEt.setText("");
                    mProgressDialog.dismiss();
                } else {

                    Toast.makeText(ReportActivity.this, "Report registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                }
            }
        });
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

        categoryItems[0] = "Crime";
        categoryItems[1] = "Traffic";
        categoryItems[2] = "Municipal Issues";
        categoryItems[3] = "Harassment";
        categoryItems[4] = "Corruption";
        categoryItems[5] = "Domestic Violence";
        categoryItems[6] = "Feedback/Suggestions";
        categoryItems[7] = "Complaints against Police";
        categoryItems[8] = "Transport Department Related";
        categoryItems[9] = "Child Labour/Child Trafficking";
        categoryItems[10] = "Other";
    }
}
