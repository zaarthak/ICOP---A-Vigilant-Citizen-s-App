package com.sarthak.icop.icop.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.sarthak.icop.icop.R;
import com.sarthak.icop.icop.databases.ReportDatabase;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import id.zelory.compressor.Compressor;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener {

    String[] categoryItems = new String[11];
    String category = "";

    private ArrayAdapter<String> adapter;

    private Bitmap reportImage;

    private Uri mCropImageUri;

    private Button mCategoryList;
    private EditText mInformationEt, mContactEt;
    private ImageView mImageView;
    private Button mSendBtn;
    private ImageButton mImageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCategoryList = findViewById(R.id.category_list);
        mInformationEt = findViewById(R.id.complaint);
        mContactEt = findViewById(R.id.contact);
        mImageView = findViewById(R.id.report_image);
        mSendBtn = findViewById(R.id.send_btn);
        mImageBtn = findViewById(R.id.image_btn);

        initCategoryItems();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoryItems);
        mCategoryList.setOnClickListener(this);

        mSendBtn.setOnClickListener(this);
        mImageBtn.setOnClickListener(this);
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
                String image = saveToInternalStorage(reportImage);
                Log.e("imma", image);

                information = mInformationEt.getText().toString();
                contact = mContactEt.getText().toString();

                if (!information.equals("") && !category.equals("")) {

                    ReportDatabase database = new ReportDatabase(ReportActivity.this);

                    database.addReport(category, information, contact, image);
                    mInformationEt.setText("");
                    mContactEt.setText("");
                }
                break;

            case R.id.image_btn:

                CropImage.startPickImageActivity(ReportActivity.this);
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

    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},   CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
            } else {
                // no permissions required or already granted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }

        // handle result of crop image activity
        // obtain Uri of cropped image and save image in firebase storage
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                // get profile image Uri
                final Uri resultUri = result.getUri();
                File thumb_filePath = new File(resultUri.getPath());

                // get compressed image bitmap from Uri
                Bitmap thumb_bitmap = new Compressor(this)
                        .setMaxHeight(200)
                        .setMaxWidth(200)
                        .setQuality(100)
                        .compressToBitmap(thumb_filePath);

                reportImage = thumb_bitmap;
                mImageView.setImageBitmap(reportImage);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
                error.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {

            if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // required permissions granted, start crop image activity
                startCropImageActivity(mCropImageUri);
            } else {
                Toast.makeText(this, "Cancelling, required permissions are not granted.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage){

        if (bitmapImage != null) {

            Long time = System.currentTimeMillis()/1000;
            String timestamp = time.toString();

            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            // path to /data/data/yourapp/app_data/imageDir
            File directory = cw.getDir("ICOP Images", Context.MODE_PRIVATE);
            // Create imageDir
            File mypath=new File(directory,timestamp + ".jpg");

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(mypath);
                // Use the compress method on the BitMap object to write image to the OutputStream
                bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return timestamp;
        } else {
            return "";
        }
    }

    /**
     * Start crop Image activity.
     *
     * @param imageUri is Uri of the image to be cropped
     */
    private void startCropImageActivity(Uri imageUri) {

        CropImage.activity(imageUri)
                .setAspectRatio(1,1)
                .setMinCropWindowSize(700, 700)
                .start(this);
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
