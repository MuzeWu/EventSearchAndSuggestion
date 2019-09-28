package com.example.audrey.eventsearch_hw9_muzewu;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class detailViewPagerAdapter extends FragmentPagerAdapter {
    public detailViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new detailEventFragment();
            case 1: return new detailArtistFragment();
            case 2: return new detailVenueFragment();
            case 3: return new detailUpcFragment();
        }
        return null;
    }
    @Override
    public int getCount() {
        return 4;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Event";
            case 1:
                return "ARTIST(S)";
            case 2:
                return "VENUE";
            case 3:
                return "UPCOMING";
            default:
                return null;
        }
    }
}
