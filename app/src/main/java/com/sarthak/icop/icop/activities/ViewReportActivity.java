package com.sarthak.icop.icop.activities;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sarthak.icop.icop.R;
import com.sarthak.icop.icop.models.Report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ViewReportActivity extends AppCompatActivity {

    private ImageView mImage;
    private TextView mCategoryTv, mInfoTv, mContactTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Report report = (Report) getIntent().getSerializableExtra("object");
        getSupportActionBar().setTitle(report.getId());

        mImage = findViewById(R.id.view_report_image);
        mCategoryTv = findViewById(R.id.view_report_category);
        mInfoTv = findViewById(R.id.view_report_info);
        mContactTv = findViewById(R.id.view_report_contact);

        mCategoryTv.setText(report.getCategory());
        mInfoTv.setText(report.getInformation());
        mContactTv.setText("Issued by: " + report.getContact());

        if (report.getImage().equals("")) {
            mImage.setVisibility(View.GONE);
        } else {
            loadImageFromStorage(report.getImage());
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

    private void loadImageFromStorage(String image)
    {

        try {
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            // path to /data/data/yourapp/app_data/imageDir
            File directory = cw.getDir("ICOP Images", Context.MODE_PRIVATE);
            File f=new File(directory, image + ".jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            mImage.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }
}
