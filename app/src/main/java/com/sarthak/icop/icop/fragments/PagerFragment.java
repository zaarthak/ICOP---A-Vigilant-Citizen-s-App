package com.sarthak.icop.icop.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sarthak.icop.icop.R;
import com.sarthak.icop.icop.adapters.ViewPagerAdapter;

public class PagerFragment extends Fragment {

    private ImageView dotOne, dotTwo;

    private ViewPager mViewPager;
    private ViewPagerAdapter pagerAdapter;

    public PagerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pager, container, false);

        setUpView(view);
        setTab();

        return view;
    }

    private void setUpView(View view){

        mViewPager = view.findViewById(R.id.viewPager);
        pagerAdapter = new ViewPagerAdapter(getActivity(), getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(0);

        initImageView(view);
    }
    private void setTab(){

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrollStateChanged(int position) {}

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {}

            @Override
            public void onPageSelected(int position) {

                btnAction(position);
            }
        });
    }

    private void btnAction(int action){

        switch(action){

            case 0:

                dotOne.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.rounded_cell_dark));
                dotTwo.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.rounded_cell_light));
                break;

            case 1:

                dotOne.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.rounded_cell_light));
                dotTwo.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.rounded_cell_dark));
                break;
        }
    }

    private void initImageView(View view){

        dotOne = view.findViewById(R.id.dot_one);
        dotTwo = view.findViewById(R.id.dot_two);
    }
}
