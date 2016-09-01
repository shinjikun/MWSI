package com.indra.rover.mwsi.ui.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.adapters.MRUListAdapter;
import com.indra.rover.mwsi.adapters.MReaderListAdapter;
import com.indra.rover.mwsi.data.pojo.Item;
import com.indra.rover.mwsi.data.pojo.MRU;
import com.indra.rover.mwsi.data.pojo.Meter_Reader;
import com.indra.rover.mwsi.utils.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MReadersListActivity extends AppCompatActivity  implements OnItemClickListener {

    RecyclerView mRecycleview;

    private List<Meter_Reader> mrList = new ArrayList<>();
    private MReaderListAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mreaders_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mRecycleview = (RecyclerView) findViewById(R.id.listView);

        mAdapter = new MReaderListAdapter(mrList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecycleview.setLayoutManager(mLayoutManager);
        mRecycleview.setItemAnimator(new DefaultItemAnimator());
        mRecycleview.setAdapter(mAdapter);
        prepareData();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void prepareData(){
        Meter_Reader meter_reader = new Meter_Reader("id1","Juan Dela Cruz","xxxXXX");
        mrList.add(meter_reader);


        meter_reader = new Meter_Reader("id1","User1","232ddfggd");
        mrList.add(meter_reader);
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



    @Override
    public void onItemClick(Item item, int position) {

    }

    @Override
    public void onItemClick(MRU item, int position) {

    }


}
