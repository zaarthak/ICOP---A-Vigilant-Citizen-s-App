package com.sarthak.icop.icop.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.sarthak.icop.icop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class InformActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String LOG_TAG = "ExampleApp";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String DIST_API_BASE = "https://maps.googleapis.com/maps/api/distancematrix";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyCyxXzmczqpntbENM64pk4NcPuifYm9mkU";

    private AutoCompleteTextView mBoardEt;
    private AutoCompleteTextView mDestinationEt;

    private EditText mVehicleEt;
    private EditText mContactEt;

    private Button mInformBtn;

    String vehicleNo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        vehicleNo = getIntent().getStringExtra("vehicle");

        mBoardEt = findViewById(R.id.boarding_et);
        mDestinationEt = findViewById(R.id.destination_et);

        mBoardEt.requestFocus();
        mBoardEt.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        mBoardEt.setOnItemClickListener(this);
        mDestinationEt.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        mDestinationEt.setOnItemClickListener(this);

        mVehicleEt = findViewById(R.id.vehicle_et);
        mVehicleEt.setText(vehicleNo);

        mContactEt = findViewById(R.id.contact_et);
        mInformBtn = findViewById(R.id.inform_btn);

        mInformBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.inform_btn:

                String boarding = mBoardEt.getText().toString();
                String destination = mDestinationEt.getText().toString();
                String vehicle = mVehicleEt.getText().toString();
                String contact = mContactEt.getText().toString();

                if (!TextUtils.isEmpty(boarding) &&
                        !TextUtils.isEmpty(destination) &&
                        !TextUtils.isEmpty(vehicle) &&
                        !TextUtils.isEmpty(contact)) {

                    String message = "Place of boarding: " + boarding +
                            "\n\nPlace of destination: " + destination +
                            "\n\nVehicle number: " + vehicle;

                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(contact, null, message, null, null);
                    mBoardEt.setText("");
                    mDestinationEt.setText("");
                    mVehicleEt.setText("");
                    mContactEt.setText("");
                    Toast.makeText(this, "Message sent successfully.", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(this, "Please enter complete details to continue.", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    }

    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            //sb.append("&components=country:in");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

            System.out.println("URL: "+url);
            Log.d("URL", url.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Log.d("FILTER", "get filter called");
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    Log.d("FILTER", "performFiltering called");
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        Log.d("TAG", constraint.toString());
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    Log.d("FILTER", "publishResults called");
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }
}
