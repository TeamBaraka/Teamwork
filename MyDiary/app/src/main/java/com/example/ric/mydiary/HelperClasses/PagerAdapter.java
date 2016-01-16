package com.example.ric.mydiary.HelperClasses;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.ric.mydiary.MainActivityFragment;
import com.example.ric.mydiary.MainSearchFragment;

public class PagerAdapter extends FragmentPagerAdapter {
    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new MainActivityFragment();
                break;
            case 1:
                fragment = new MainSearchFragment();
                break;
        }

        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String pageTitle = "";
        switch (position) {
            case 0:
                pageTitle = "Today's events";
                break;
            case 1:
                pageTitle = "Search events";
                break;
        }
        return pageTitle;
    }
}
