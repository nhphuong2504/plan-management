package com.example.socialmedia_swipe;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
    public SimpleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new screen1();
        } else if (position == 1) {
            return new screen2();
        } else {
            return new screen3();
        }
    }
    @Override
    public int getCount() {
        return 3;
    }
}
