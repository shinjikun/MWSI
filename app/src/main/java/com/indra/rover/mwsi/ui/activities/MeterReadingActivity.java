package com.indra.rover.mwsi.ui.activities;

import android.app.Dialog;
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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.indra.rover.mwsi.MainApp;
import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.adapters.StatusViewPagerAdapter;
import com.indra.rover.mwsi.data.db.MeterReadingDao;
import com.indra.rover.mwsi.data.pojo.meter_reading.misc.CustomerInfo;
import com.indra.rover.mwsi.data.pojo.T_Download_Info;
import com.indra.rover.mwsi.ui.fragments.MRCustomerInfoFragment;
import com.indra.rover.mwsi.ui.fragments.MRDeliveryRFragment;
import com.indra.rover.mwsi.ui.fragments.MROCFragment;
import com.indra.rover.mwsi.ui.fragments.MRRemarksFragment;
import com.indra.rover.mwsi.utils.MessageTransport;

import java.util.ArrayList;
import java.util.List;

public class MeterReadingActivity extends AppCompatActivity implements View.OnClickListener
{

    ViewPager mViewPager;
    TabLayout mTabLayout;
    ScrollView mScrollView;
    Dialog dialog;
    String mru_id;
    MeterReadingDao meterDao;
    int current =0;
    T_Download_Info currentDisplay;
    List<T_Download_Info> arry;
    FloatingActionButton fabLeft, fabRight;
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


       fabRight = (FloatingActionButton)findViewById(R.id.fabRight);
        fabRight.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(current< arry.size()-1){
                    current++;
                    prepareData(current);
                    fabLeft.setEnabled(true);
                }
                else {
                    fabRight.setEnabled(false);
                }
            }
        });



        fabLeft = (FloatingActionButton) findViewById(R.id.fabLeft);
        fabLeft.setEnabled(false);
        fabLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(current!=0){
                    current--;
                    prepareData(current);
                }
            }
        });

        Button btn =  (Button)findViewById(R.id.btnMREdit);
        btn.setOnClickListener(this);
        Bundle extras = getIntent().getExtras();
        meterDao = new MeterReadingDao(this);
        arry = new ArrayList<>();
        if (extras != null) {
            this.mru_id = extras.getString("mru_id");
            this.arry = meterDao.fetchInfos(this.mru_id);
            fabLeft.setEnabled(false);
            prepareData(current);
        }

        mScrollView = (ScrollView)findViewById(R.id.scroller);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager.setOffscreenPageLimit(4);
        if(!arry.isEmpty()){
            setupViewPager(mViewPager);
            mTabLayout.setupWithViewPager(mViewPager);

        }




    }

    private void navigate(String dldocno){
        MainApp.bus.post(new MessageTransport("navigate",dldocno));
    }

    private void prepareData(int index){
       if(!arry.isEmpty()){
           currentDisplay = this.arry.get(index);
           CustomerInfo  customerInfo = currentDisplay.getCustomer();
           TextView txt = (TextView)findViewById(R.id.txtCAN);
           txt.setText(customerInfo.getAccn());
           txt = (TextView)findViewById(R.id.txtName);
           txt.setText(customerInfo.getCname());
           txt = (TextView)findViewById(R.id.txtAddress);
           txt.setText(customerInfo.getAddress());
           txt = (TextView)findViewById(R.id.txtMeterNumber);
           txt.setText(currentDisplay.getMeter_number());
           txt = (TextView)findViewById(R.id.txtMeterNumber);
           txt.setText(currentDisplay.getMeter_number());
           txt = (TextView)findViewById(R.id.txtMRUID);
           txt.setText(currentDisplay.getMru_id());
           txt = (TextView)findViewById(R.id.txtSeqNum);
           txt.setText(currentDisplay.getSeq_number());
           String page = (current+1)+"/"+arry.size();

           txt = (TextView)findViewById(R.id.txtPagination);
           txt.setText(page);
           txt = (TextView)findViewById(R.id.txtRateCode);
           txt.setText(currentDisplay.getBillClass().getDesc());
           navigate(currentDisplay.getDldocno());
       }
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
         String dldcono = currentDisplay.getDldocno();
        adapter.addFragment(MRCustomerInfoFragment.newInstance(dldcono), "Customer Info");
        adapter.addFragment(MROCFragment.newInstance(dldcono), "OC");
        adapter.addFragment(new MRRemarksFragment(), "Remarks");
        adapter.addFragment(MRDeliveryRFragment.newInstance("2"), "Delivery Remarks");
        mViewPager.setAdapter(adapter);
    }


    public void showMeterRdgDialog(){
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_change_reading);
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
                setReadingValue(value);
                dialog.dismiss();
            }
        });



        dialog.show();
    }



    public void setReadingValue(String value){
        TextView txt = (TextView)findViewById(R.id.txtReading);
        txt.setText(value);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        int id = item.getItemId();
        switch(id){
            // handle arrow click here
            case android.R.id.home:
                finish(); // close this activity and return to preview activity (if there is any)
                break;
            case R.id.action_search:
               intent = new Intent(this, SearchActivity.class);
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
            case R.id.action_new_seq:
                showNewSeqDialog();
                break;
            case R.id.action_new:
                intent = new Intent(this,NewFoundMeterActivity.class);
                startActivity(intent);
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


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch(id){
            case R.id.btnMREdit:
                showMeterRdgDialog();
                break;
        }
    }



    Dialog dlgSeqNumber;
    public void showNewSeqDialog(){
        dlgSeqNumber = new Dialog(this);
        dlgSeqNumber.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlgSeqNumber.setContentView(R.layout.dialog_new_sequence);
        dlgSeqNumber.setCancelable(false);
        final EditText txtDlg = (EditText)dlgSeqNumber.findViewById(R.id.dlg_body);
        ImageButton dlgBtnClose = (ImageButton)dlgSeqNumber.findViewById(R.id.dlg_btn_close);
        dlgBtnClose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dlgSeqNumber.dismiss();
            }
        });
        Button btn =  (Button)dlgSeqNumber.findViewById(R.id.dlg_btn_yes);
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String value =   txtDlg.getText().toString();

                dlgSeqNumber.dismiss();
            }
        });

        btn =  (Button)dlgSeqNumber.findViewById(R.id.dlg_btn_no);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dlgSeqNumber.dismiss();
            }
        });


        dlgSeqNumber.show();
    }




}
