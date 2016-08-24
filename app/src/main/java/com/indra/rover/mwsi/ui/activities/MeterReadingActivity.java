package com.indra.rover.mwsi.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;

import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.adapters.StatusViewPagerAdapter;
import com.indra.rover.mwsi.ui.fragments.MRCustomerInfoFragment;
import com.indra.rover.mwsi.ui.fragments.MRRemarksFragment;
import com.indra.rover.mwsi.ui.fragments.MeterStatusFragment;

public class MeterReadingActivity extends AppCompatActivity {

    ViewPager mViewPager;
    TabLayout mTabLayout;
    ScrollView mScrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_reading);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        mScrollView = (ScrollView)findViewById(R.id.scroller);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(4);
        setupViewPager(mViewPager);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fabRight);
        fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

            }
        });

    }

    private int[] getViewLocations(View view) {
        int[] locations = new int[2];
        view.getLocationOnScreen(locations);
        return locations;
    }
    public void scrollUp(){
       // mScrollView.fullScroll(View.FOCUS_DOWN);
        int[] locations = getViewLocations(mTabLayout);
        int x = locations[0]; // x position of left
        int y = locations[1]-130; // y position of top
       mScrollView.smoothScrollBy(x,y);
    }

    public void setupViewPager(ViewPager mViewPager){
        StatusViewPagerAdapter adapter = new StatusViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(MRCustomerInfoFragment.newInstance("2"), "Customer Info");
        adapter.addFragment(MeterStatusFragment.newInstance(1), "OC");
        adapter.addFragment(new MRRemarksFragment(), "Remarks");
        adapter.addFragment(MeterStatusFragment.newInstance(2), "Delivery Remarks");
        mViewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch(id){
            // handle arrow click here
            case android.R.id.home:
                finish(); // close this activity and return to preview activity (if there is any)
                break;
            case R.id.action_search:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.action_customer:
                mViewPager.setCurrentItem(0);
                scrollUp();
                break;
            case R.id.action_oc:
                mViewPager.setCurrentItem(1);
                scrollUp();
                break;
            case R.id.action_remarks:
                mViewPager.setCurrentItem(2);
                scrollUp();
                break;
            case R.id.action_delivery:
                mViewPager.setCurrentItem(3);
                scrollUp();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }



}
