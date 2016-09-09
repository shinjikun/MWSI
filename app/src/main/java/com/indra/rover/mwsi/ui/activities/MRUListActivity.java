package com.indra.rover.mwsi.ui.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.adapters.MRUListAdapter;
import com.indra.rover.mwsi.data.db.MRUDao;
import com.indra.rover.mwsi.data.pojo.Item;
import com.indra.rover.mwsi.data.pojo.MRU;
import com.indra.rover.mwsi.utils.DialogUtils;
import com.indra.rover.mwsi.utils.OnItemClickListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MRUListActivity extends AppCompatActivity  implements OnItemClickListener,
        DialogUtils.DialogListener {

    RecyclerView mRecycleview;

    private List<MRU> mruList = new ArrayList<>();
    private MRUListAdapter mAdapter;
    DialogUtils dialogUtils;
    final int DLG_SUCCESS=1;
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
        dialogUtils = new DialogUtils(this);
        dialogUtils.setListener(this);
        prepareData();


    }


    private void prepareData(){
        MRUDao mruDao = new MRUDao(this);
       List<MRU> mMRU = mruDao.getMRUs();
        for (MRU mru : mMRU) {
            mruList.add(mru);
        }
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
        intent.putExtra("mru_id", item.getId());
        startActivity(intent);
    }


    @Override
    public void onItemClick(Item item, int position) {

    }

    @Override
    public void onItemClick(MRU item, int position) {
        MRU  mru =  mruList.get(position);
      showMeterCodeDialog(mru);
    }



    Dialog dialog;
    public void showMeterCodeDialog(final MRU mru){
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_meter_code);
        dialog.setCancelable(false);
        final EditText txtDlg = (EditText)dialog.findViewById(R.id.dlg_body);
        ImageButton dlgBtnClose = (ImageButton)dialog.findViewById(R.id.dlg_btn_close);
        dlgBtnClose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Button btn =  (Button)dialog.findViewById(R.id.dlg_btn_yes);
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String value =   txtDlg.getText().toString();
                if(value.equals(mru.getReader_code())){
                    //dialogUtils.showOKDialog(DLG_SUCCESS,"Welcome!!",mru.getReader_name(),new Bundle());
                    dialog.dismiss();
                    loadConsumerInfo(mru);
                }
                else {
                    dialogUtils.showOKDialog(111,"","Wrong Meter Code",new Bundle());
                    dialog.dismiss();
                }

            }
        });

        TextView txt = (TextView)dialog.findViewById(R.id.dlg_title);

        dialog.show();
    }

    @Override
    public void dialog_confirm(int dialog_id, Bundle params) {

    }

    @Override
    public void dialog_cancel(int dialog_id, Bundle params) {

    }
}
