package com.sarthak.icop.icop.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sarthak.icop.icop.activities.CallActivity;
import com.sarthak.icop.icop.activities.FareCalculatorActivity;
import com.sarthak.icop.icop.activities.ReportFetchActivity;
import com.sarthak.icop.icop.activities.TowedVehicleActivity;
import com.sarthak.icop.icop.activities.WebActivity;
import com.sarthak.icop.icop.adapters.FragmentGridAdapter;
import com.sarthak.icop.icop.R;
import com.sarthak.icop.icop.utils.RecyclerViewOnClickListener;

import java.util.ArrayList;

public class SecondFragment extends Fragment implements RecyclerViewOnClickListener {

    ArrayList<String> listItems = new ArrayList<>();
    ArrayList<Integer> imageItems = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private FragmentGridAdapter adapter;

    public SecondFragment() {

        initList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_second, container, false);

        mRecyclerView = view.findViewById(R.id.second_rv);
        adapter = new FragmentGridAdapter(getActivity(), listItems, imageItems);
        adapter.setOnRecyclerViewItemClickListener(this);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mRecyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onItemClicked(View view, int position) {

        Intent callIntent = new Intent(getActivity(), CallActivity.class);
        Intent fetchIntent = new Intent(getActivity(), ReportFetchActivity.class);
        Intent webIntent = new Intent(getActivity(), WebActivity.class);
        Intent fareIntent = new Intent(getActivity(), FareCalculatorActivity.class);

        switch (position) {

            case 0:

                callIntent.putExtra("type", 3);
                startActivity(callIntent);
                break;

            case 1:

                webIntent.putExtra("type", 1);
                startActivity(webIntent);
                break;

            case 2:

                startActivity(new Intent(getActivity(), TowedVehicleActivity.class));
                break;

            case 3:

                startActivity(fareIntent);
                break;

            case 4:

                webIntent.putExtra("type", 2);
                startActivity(webIntent);
                break;

            case 5:

                webIntent.putExtra("type", 3);
                startActivity(webIntent);
                break;

            case 6:

                fetchIntent.putExtra("type", 1);
                startActivity(fetchIntent);
                break;

            case 7:

                fetchIntent.putExtra("type", 2);
                startActivity(fetchIntent);
                break;

            case 8:

                webIntent.putExtra("type", 4);
                startActivity(webIntent);
                break;

            default:

                Toast.makeText(getActivity(), listItems.get(position), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void initList() {

        listItems.add("Emergency Calls");
        listItems.add("News Updates");
        listItems.add("Towed Vehicle Search");
        listItems.add("Fare Calculation");
        listItems.add("Traffic Helpline");
        listItems.add("Public Grievance");
        listItems.add("Your Reports");
        listItems.add("Report Lookup");
        listItems.add("Anti Depression Helpline");

        imageItems.add(R.drawable.emergency_call);
        imageItems.add(R.drawable.news);
        imageItems.add(R.drawable.towing_vehicle_search);
        imageItems.add(R.drawable.fare_calculation);
        imageItems.add(R.drawable.traffic_helpline);
        imageItems.add(R.drawable.public_grievance);
        imageItems.add(R.drawable.your_report);
        imageItems.add(R.drawable.report_lookup);
        imageItems.add(R.drawable.suicide_prevention_helpline);
    }
}
