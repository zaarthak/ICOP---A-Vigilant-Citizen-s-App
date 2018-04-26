package com.sarthak.icop.icop.activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sarthak.icop.icop.R;

public class TimerActivity extends AppCompatActivity implements OnClickListener {

    int timerFlag = 0;

    private Button buttonStartTime;

    private EditText mHourEt, mMinuteEt, mSecondEt;

    private CountDownTimer countDownTimer; // built in android class
    // CountDownTimer
    private long totalTimeCountInMilliseconds; // total count down time in
    // milliseconds
    private long timeBlinkInMilliseconds; // start time of start blinking
    private boolean blink; // controls the blinking .. on and off

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buttonStartTime = findViewById(R.id.btnStartTime);

        mHourEt = findViewById(R.id.hour_et);
        mMinuteEt = findViewById(R.id.minute_et);
        mSecondEt = findViewById(R.id.second_et);

        buttonStartTime.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnStartTime:

                if (timerFlag == 0) {

                    setTimer();
                    startTimer();
                    mHourEt.setFocusable(false);
                    mMinuteEt.setFocusable(false);
                    mSecondEt.setFocusable(false);
                    buttonStartTime.setText("STOP TIMER");
                    timerFlag = 1;
                } else {

                    countDownTimer.cancel();
                    mHourEt.setFocusable(true);
                    mMinuteEt.setFocusable(true);
                    mSecondEt.setFocusable(true);
                    mHourEt.setText("00");
                    mMinuteEt.setText("00");
                    mSecondEt.setText("00");
                    buttonStartTime.setText("START TIMER");
                    timerFlag = 0;
                }
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

    private void setTimer() {

        int time = 0;
        if (!mHourEt.getText().toString().equals("00") ||
                !mMinuteEt.getText().toString().equals("00") ||
                !mSecondEt.getText().toString().equals("00")) {

            time = Integer.parseInt(mHourEt.getText().toString()) * 60 * 60 +
                    Integer.parseInt(mMinuteEt.getText().toString()) * 60 +
                    Integer.parseInt(mSecondEt.getText().toString());
        }

        totalTimeCountInMilliseconds = time * 1000;

        timeBlinkInMilliseconds = 10 * 1000;
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds, 500) {
            // 500 means, onTick function will be called at every 500
            // milliseconds

            @Override
            public void onTick(long leftTimeInMilliseconds) {
                long time = leftTimeInMilliseconds / 1000;

                if (leftTimeInMilliseconds < timeBlinkInMilliseconds) {
                    mHourEt.setTextAppearance(getApplicationContext(), R.style.blinkText);
                    mMinuteEt.setTextAppearance(getApplicationContext(), R.style.blinkText);
                    mSecondEt.setTextAppearance(getApplicationContext(), R.style.blinkText);
                    // change the style of the textview .. giving a red
                    // alert style

                    if (blink) {
                        mHourEt.setVisibility(View.VISIBLE);
                        mMinuteEt.setVisibility(View.VISIBLE);
                        mSecondEt.setVisibility(View.VISIBLE);
                        // if blink is true, textview will be visible
                    } else {
                        mHourEt.setVisibility(View.INVISIBLE);
                        mMinuteEt.setVisibility(View.INVISIBLE);
                        mSecondEt.setVisibility(View.INVISIBLE);
                    }

                    blink = !blink; // toggle the value of blink
                }

                int hours = (int) time / 3600;
                int remainder = (int) time - hours * 3600;
                int minutes = remainder / 60;
                remainder = remainder - minutes * 60;
                int seconds = remainder;

                mHourEt.setText(String.format("%02d", hours));
                mMinuteEt.setText(String.format("%02d", minutes));
                mSecondEt.setText(String.format("%02d", seconds));
                // format the textview to show the easily readable format
            }

            @Override
            public void onFinish() {
                // this function will be called when the timecount is finished
                mHourEt.setTextAppearance(getApplicationContext(), R.style.normalText);
                mMinuteEt.setTextAppearance(getApplicationContext(), R.style.normalText);
                mSecondEt.setTextAppearance(getApplicationContext(), R.style.normalText);
                Toast.makeText(TimerActivity.this, "Message sent successfully.", Toast.LENGTH_SHORT).show();
            }

        }.start();

    }
}

