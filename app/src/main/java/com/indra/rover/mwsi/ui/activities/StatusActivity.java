package com.indra.rover.mwsi.ui.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.adapters.StatusViewPagerAdapter;
import com.indra.rover.mwsi.ui.fragments.MeterStatusFragment;

public class StatusActivity extends AppCompatActivity {

     ViewPager mViewPager;
     TabLayout mTabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    public void setupViewPager(ViewPager mViewPager){
        StatusViewPagerAdapter adapter = new StatusViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(MeterStatusFragment.newInstance(0), "Info");
        adapter.addFragment(MeterStatusFragment.newInstance(1), "Meter");
        adapter.addFragment(MeterStatusFragment.newInstance(2), "Delivery");
        mViewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
