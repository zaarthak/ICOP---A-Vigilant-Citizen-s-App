package com.sarthak.icop.icop.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sarthak.icop.icop.R;

public class SosActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mContactEt, mMessageEt;
    private Button mSaveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContactEt = findViewById(R.id.contact_et);
        mMessageEt = findViewById(R.id.message_et);
        mSaveBtn = findViewById(R.id.save_btn);

        mSaveBtn.setOnClickListener(this);

        setSavedData();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.save_btn:

                String contact = mContactEt.getText().toString();
                String message = mMessageEt.getText().toString();

                if (!TextUtils.isEmpty(contact) && !TextUtils.isEmpty(message)) {

                    SharedPreferences.Editor editor = getSharedPreferences("SOS", MODE_PRIVATE).edit();
                    editor.putString("contact", contact);
                    editor.putString("message", message);
                    editor.apply();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sos_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                onBackPressed();
                return true;

            case R.id.erase:

                SharedPreferences.Editor editor = getSharedPreferences("SOS", Context.MODE_PRIVATE).edit();

                editor.remove("contact");
                editor.remove("message");
                editor.apply();

                mContactEt.setText("");
                mMessageEt.setText("");
                break;
        }
        return true;
    }

    private void setSavedData() {

        SharedPreferences prefs = getSharedPreferences("SOS", MODE_PRIVATE);

        mContactEt.setText(prefs.getString("contact", null));
        mMessageEt.setText(prefs.getString("message", null));
    }
}
