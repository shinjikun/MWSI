package com.indra.rover.mwsi.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Indra on 8/22/2016.
 */
public class StatusViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();


    public StatusViewPagerAdapter(FragmentManager fragmentManager){
        super(fragmentManager);

    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }


    public void addFragment(Fragment fragment, String tabTitle){
        mFragmentTitleList.add(tabTitle);
        mFragmentList.add(fragment);
    }
}
