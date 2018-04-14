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

import com.sarthak.icop.icop.activities.MapsActivity;
import com.sarthak.icop.icop.activities.ReportLostArticleActivity;
import com.sarthak.icop.icop.activities.SosActivity;
import com.sarthak.icop.icop.adapters.FragmentGridAdapter;
import com.sarthak.icop.icop.R;
import com.sarthak.icop.icop.utils.RecyclerViewOnClickListener;
import com.sarthak.icop.icop.activities.CallActivity;
import com.sarthak.icop.icop.activities.ReportActivity;

import java.util.ArrayList;

public class FirstFragment extends Fragment implements RecyclerViewOnClickListener {

    ArrayList<String> listItems = new ArrayList<>();
    ArrayList<Integer> imageItems = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private FragmentGridAdapter adapter;

    public FirstFragment() {

        initList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_first, container, false);

        mRecyclerView = view.findViewById(R.id.first_rv);
        adapter = new FragmentGridAdapter(getActivity(), listItems, imageItems);
        adapter.setOnRecyclerViewItemClickListener(this);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mRecyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onItemClicked(View view, int position) {

        Toast.makeText(getActivity(), listItems.get(position), Toast.LENGTH_SHORT).show();

        Intent callIntent = new Intent(getActivity(), CallActivity.class);

        switch (position) {

            case 0:

                startActivity(new Intent(getActivity(), ReportActivity.class));
                break;

            case 1:

                callIntent.putExtra("type", 1);
                startActivity(callIntent);
                break;

            case 2:

                callIntent.putExtra("type", 2);
                startActivity(callIntent);
                break;

            case 3:

                startActivity(new Intent(getActivity(), MapsActivity.class));
                break;

            case 4:

                startActivity(new Intent(getActivity(), SosActivity.class));
                break;

            case 5:

                startActivity(new Intent(getActivity(), ReportLostArticleActivity.class));
                break;
        }
    }

    private void initList() {

        listItems.add("Report an Incident");
        listItems.add("Call Police");
        listItems.add("Call Administration");
        listItems.add("My Safe Zone");
        listItems.add("Help Me !!");
        listItems.add("Report Lost Article");
        listItems.add("Aalamban");
        listItems.add("Vehicle Search");
        listItems.add("My Close Group");

        imageItems.add(R.drawable.report_an_incident);
        imageItems.add(R.drawable.call_police);
        imageItems.add(R.drawable.call_administration);
        imageItems.add(R.drawable.my_safe_zone);
        imageItems.add(R.drawable.sos);
        imageItems.add(R.drawable.report_lost_article);
        imageItems.add(R.drawable.aalamban);
        imageItems.add(R.drawable.vehicle_search);
        imageItems.add(R.drawable.my_close_group);
    }
}
