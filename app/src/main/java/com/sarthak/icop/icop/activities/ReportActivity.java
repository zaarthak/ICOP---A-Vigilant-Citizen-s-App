package com.sarthak.icop.icop.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sarthak.icop.icop.R;
import com.sarthak.icop.icop.models.Report;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;

import id.zelory.compressor.Compressor;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener {

    String[] categoryItems = new String[11];
    String category = "";

    int imageFlag = 0;
    int reportCount = 0;

    private ArrayAdapter<String> adapter;

    private Uri mCropImageUri;
    private String thumbDownloadUrl = "";
    private byte[] thumb_byte;

    private Button mCategoryList;
    private EditText mInformationEt, mContactEt;
    private ImageView mImageView;
    private Button mSendBtn;
    private ImageButton mImageBtn;

    private ProgressDialog mProgressDialog;

    private StorageReference mImageStorage;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Reports");
        mImageStorage = FirebaseStorage.getInstance().getReference().child("Reports");
        getReportCount();

        mCategoryList = findViewById(R.id.category_list);
        mInformationEt = findViewById(R.id.complaint);
        mContactEt = findViewById(R.id.contact);
        mImageView = findViewById(R.id.report_image);
        mSendBtn = findViewById(R.id.send_btn);
        mImageBtn = findViewById(R.id.image_btn);
        mProgressDialog = new ProgressDialog(this);

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

                if (imageFlag == 0) {

                    String information = "";
                    String image = "";
                    String contact = "";

                    information = mInformationEt.getText().toString();
                    contact = mContactEt.getText().toString();

                    if (!information.equals("") && !category.equals("")) {

                        addDataToFirebase(category, information, image, contact);
                    }
                } else {

                    saveImageInFirebaseStorage();
                }
                break;

            case R.id.image_btn:

                CropImage.startPickImageActivity(ReportActivity.this);
                break;
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
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
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

                imageFlag = 1;
                // get profile image Uri
                final Uri resultUri = result.getUri();
                File thumb_filePath = new File(resultUri.getPath());

                // get compressed image bitmap from Uri
                Bitmap thumb_bitmap = new Compressor(this)
                        .setMaxHeight(200)
                        .setMaxWidth(200)
                        .setQuality(100)
                        .compressToBitmap(thumb_filePath);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                thumb_byte = baos.toByteArray();

                mImageView.setImageBitmap(thumb_bitmap);

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

    private void addDataToFirebase(String category, String information, final String image, String contact) {

        if (imageFlag == 0) {

            mProgressDialog.setTitle("Please wait...");
            mProgressDialog.setMessage("Please wait while we register your report");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        }

        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);

        Report report = new Report();
        report.setId(String.format("CT-%05d", reportCount + 1));
        report.setCategory(category);
        report.setInformation(information);
        report.setImage(image);
        report.setContact(contact);
        report.setStatus("Pending");

        mDatabase.child(timestamp).setValue(report).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    Toast.makeText(ReportActivity.this, "Your report has been registered.", Toast.LENGTH_SHORT).show();
                    mInformationEt.setText("");
                    mContactEt.setText("");
                    mImageView.setImageResource(android.R.color.transparent);
                    mProgressDialog.dismiss();
                    imageFlag = 0;
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

    /**
     * Start crop Image activity.
     *
     * @param imageUri is Uri of the image to be cropped
     */
    private void startCropImageActivity(Uri imageUri) {

        CropImage.activity(imageUri)
                .setMinCropWindowSize(700, 700)
                .start(this);
    }

    /**
     * Store image in firebase storage.
     */
    private void saveImageInFirebaseStorage() {

        // create storage path for profile image in firebase storage
        final StorageReference filepath = mImageStorage.child(String.format("CT-%05d", reportCount + 1) + ".jpg");
        // add image to firebase storage

        String information = "";
        String contact = "";

        information = mInformationEt.getText().toString();
        contact = mContactEt.getText().toString();

        if (!information.equals("") && !category.equals("")) {

            mProgressDialog.setTitle("Please wait...");
            mProgressDialog.setMessage("Please wait while we register your report");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

            UploadTask uploadTask = filepath.putBytes(thumb_byte);
            final String finalInformation = information;
            final String finalContact = contact;
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {

                    @SuppressWarnings("VisibleForTests") Uri Url = thumb_task.getResult().getDownloadUrl();

                    if (Url != null) {
                        thumbDownloadUrl = Url.toString();
                    }

                    if (thumb_task.isSuccessful()) {

                        addDataToFirebase(category, finalInformation, thumbDownloadUrl, finalContact);
                    } else {

                        Toast.makeText(ReportActivity.this, "An error occured. Please try again.", Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                    }
                }
            });
        } else {
            Log.d("TAG", "Enter category and information.");
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
