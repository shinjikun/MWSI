package com.indra.rover.mwsi.ui.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.adapters.StatusViewPagerAdapter;
import com.indra.rover.mwsi.data.db.MRUDao;
import com.indra.rover.mwsi.data.pojo.MRU;
import com.indra.rover.mwsi.ui.fragments.MeterStatusFragment;

import java.util.ArrayList;
import java.util.List;

public class StatusActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

     ViewPager mViewPager;
     TabLayout mTabLayout;
     MRUDao mruDao;
     List<MRU> mruList;
    Spinner spn;
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
        prepareData();
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

    }


    private void prepareData(){
        mruDao = new MRUDao(this);
        mruList = mruDao.getMRUs();
        spn  = (Spinner) findViewById(R.id.spnMRUs);

        // Spinner Drop down elements
        List<String> arryIDs = new ArrayList<>();
        for(int i = 0;i<mruList.size();i++){
            MRU mru = mruList.get(i);
           arryIDs.add(mru.getId());

        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arryIDs);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spn.setAdapter(dataAdapter);
        spn.setOnItemSelectedListener(this);
    }

    public void setupViewPager(ViewPager mViewPager){
        MRU selectedMRU = mruList.get(spn.getSelectedItemPosition());
        StatusViewPagerAdapter adapter = new StatusViewPagerAdapter(getSupportFragmentManager());
        //adapter.addFragment(MeterStatusFragment.newInstance(0,selectedMRU.getId() ), "Info");
        adapter.addFragment(MeterStatusFragment.newInstance(1,selectedMRU.getId() ), "Meter");
        adapter.addFragment(MeterStatusFragment.newInstance(2,selectedMRU.getId() ), "Delivery");
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


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String mruID =   adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
