package com.indra.rover.mwsi.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.adapters.MRUListAdapter;
import com.indra.rover.mwsi.data.pojo.MRU;
import com.indra.rover.mwsi.utils.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MRUListActivity extends AppCompatActivity  implements OnItemClickListener {

    RecyclerView mRecycleview;

    private List<MRU> mruList = new ArrayList<>();
    private MRUListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mru_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mRecycleview = (RecyclerView) findViewById(R.id.listView);

        mAdapter = new MRUListAdapter(mruList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecycleview.setLayoutManager(mLayoutManager);
        mRecycleview.setItemAnimator(new DefaultItemAnimator());
        mRecycleview.setAdapter(mAdapter);
        prepareData();


    }


    private void prepareData(){
        MRU mru = new MRU("324342",3,3,3,9);
        mruList.add(mru);

        mru = new MRU("3433",3,3,3,9);
        mruList.add(mru);

        mru = new MRU("3433",3,3,3,9);
        mruList.add(mru);

        mru = new MRU("3433",3,3,3,9);
        mruList.add(mru);

        mru = new MRU("3433",3,3,3,9);
        mruList.add(mru);
        mru = new MRU("3433",3,3,3,9);
        mruList.add(mru);
        mAdapter.notifyDataSetChanged();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }




    public void loadConsumerInfo(MRU item){
        Intent intent = new Intent(this,MeterReadingActivity.class);
        startActivity(intent);
    }


    @Override
    public void onItemClick(MRU item,int position) {
        MRU  mru =  mruList.get(position);
        loadConsumerInfo(item);
    }
}
