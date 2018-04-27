package com.sarthak.icop.icop.activities;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sarthak.icop.icop.R;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class WebActivity extends AppCompatActivity {

    int type;

    private MaterialProgressBar mProgressBar;
    private WebView mWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        type = getIntent().getIntExtra("type", 0);

        mProgressBar = findViewById(R.id.web_progress_bar);
        mWeb = findViewById(R.id.web);

        mWeb.setWebViewClient(new myWebClient());
        mWeb.getSettings().setJavaScriptEnabled(true);
        mWeb.loadUrl(getUrl());
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

    private String getUrl() {

        String url = "";

        switch (type) {

            case 1:

                getSupportActionBar().setTitle("News Updates");
                url = "https://news.google.co.in/";
                break;

            case 2:

                getSupportActionBar().setTitle("Traffic Helpline");
                url = "https://delhitrafficpolice.nic.in/traffic-helpline/";
                break;

            case 3:

                getSupportActionBar().setTitle("Public Grievance");
                url = "http://pgms.delhi.gov.in/Entrygrv.aspx?deptcode=";
                break;

            case 4:

                getSupportActionBar().setTitle("Anti Depression Helpline");
                url = "http://www.aasra.info/";
                break;
        }

        return url;
    }

    class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            mProgressBar.setVisibility(View.VISIBLE);
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
