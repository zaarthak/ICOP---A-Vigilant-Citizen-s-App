package com.sarthak.icop.icop.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sarthak.icop.icop.R;
import com.sarthak.icop.icop.activities.DeveloperInfoActivity;
import com.sarthak.icop.icop.activities.HomeActivity;
import com.sarthak.icop.icop.adapters.NavigationDrawerAdapter;
import com.sarthak.icop.icop.utils.RecyclerViewOnClickListener;

import java.util.ArrayList;

public class NavigationDrawerFragment extends Fragment implements RecyclerViewOnClickListener {

    private ArrayList<String> itemList = new ArrayList<>();
    private ArrayList<Integer> imageList = new ArrayList<>();

    private RecyclerView mNavigationList;
    private NavigationDrawerAdapter adapter;

    public NavigationDrawerFragment() {

        initList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        mNavigationList = view.findViewById(R.id.nav_drawer_list);
        mNavigationList.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new NavigationDrawerAdapter(getActivity(), itemList, imageList);
        adapter.setOnRecyclerViewItemClickListener(this);
        mNavigationList.setAdapter(adapter);

        return view;
    }

    private void initList() {

        itemList.add("Shake to send Help\nMe! message");
        itemList.add("Proximity Sensor");
        itemList.add("My Safe Zone");
        itemList.add("Headset Plugged and\nUnplugged");
        itemList.add("Share ICOP App");
        itemList.add("About ICOP");
        itemList.add("Developer Info");
        itemList.add("Disclaimer");
        itemList.add("Usage Tips");

        imageList.add(R.drawable.shake_to_send);
        imageList.add(R.drawable.shake_to_send);
        imageList.add(R.drawable.shareicons);
        imageList.add(R.drawable.abouticon);
        imageList.add(R.drawable.shareicons);
        imageList.add(R.drawable.abouticon);
        imageList.add(R.drawable.developericon);
        imageList.add(R.drawable.disclaimericon);
        imageList.add(R.drawable.usagetips);
    }

    @Override
    public void onItemClicked(View view, int position) {

        startActivity(new Intent(getActivity(), DeveloperInfoActivity.class));
    }
}
