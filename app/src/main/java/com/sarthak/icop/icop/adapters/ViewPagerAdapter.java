package com.sarthak.icop.icop.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sarthak.icop.icop.fragments.FirstFragment;
import com.sarthak.icop.icop.fragments.SecondFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    private static final int TOTAL_PAGES = 2;

    public ViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);

        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = new Fragment();

        switch (position) {

            case 0:

                fragment = new FirstFragment();
                break;

            case 1:

                fragment = new SecondFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return TOTAL_PAGES;
    }
}
