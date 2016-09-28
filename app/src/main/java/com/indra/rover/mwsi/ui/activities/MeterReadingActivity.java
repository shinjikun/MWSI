package com.indra.rover.mwsi.ui.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
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
import com.indra.rover.mwsi.compute.CompCSScheme;
import com.indra.rover.mwsi.compute.CompConsumption;
import com.indra.rover.mwsi.compute.CompMBScheme;
import com.indra.rover.mwsi.compute.Compute;
import com.indra.rover.mwsi.data.db.MeterReadingDao;
import com.indra.rover.mwsi.data.pojo.meter_reading.MeterConsumption;
import com.indra.rover.mwsi.data.pojo.meter_reading.misc.CustomerInfo;
import com.indra.rover.mwsi.data.pojo.meter_reading.display.MeterInfo;
import com.indra.rover.mwsi.ui.fragments.MRCustomerInfoFragment;
import com.indra.rover.mwsi.ui.fragments.MRDeliveryRFragment;
import com.indra.rover.mwsi.ui.fragments.MROCFragment;
import com.indra.rover.mwsi.ui.fragments.MRRemarksFragment;
import com.indra.rover.mwsi.utils.Constants;
import com.indra.rover.mwsi.utils.DialogUtils;
import com.indra.rover.mwsi.utils.GPSTracker;
import com.indra.rover.mwsi.utils.MessageTransport;
import com.indra.rover.mwsi.utils.PreferenceKeys;
import com.indra.rover.mwsi.utils.Utils;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class MeterReadingActivity extends AppCompatActivity implements View.OnClickListener ,
        DialogUtils.DialogListener, Constants, Compute.ConsumptionListener
{

    ViewPager mViewPager;
    TabLayout mTabLayout;
    ScrollView mScrollView;
    Dialog dialog;
    String mru_id;
    MeterReadingDao meterDao;
    int current =0;
    MeterInfo meterInfo;
    List<MeterInfo> arry;
    PreferenceKeys prefs;
    /**
     * Button for navigating previous or next records
     */
    ImageButton btnPrev, btnNext;
    final int SEARCH_REQ =99;
    final int DLG_RESET=75;
    final int DLG_EDITMODE=67;
    DialogUtils dlgUtils;
    TextView txtFilter;
    //Button btnPrint;
    CoordinatorLayout coordinatorLayout;
    GPSTracker gpsTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_reading);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MainApp.bus.register(this);
        gpsTracker =  new GPSTracker(this);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }
        prefs = PreferenceKeys.getInstance(this);
        dlgUtils = new DialogUtils(this);
        dlgUtils.setListener(this);

        txtFilter  = (TextView)findViewById(R.id.txtFiltered);
        txtFilter.setOnClickListener(this);
        btnNext = (ImageButton)findViewById(R.id.fabRight);
        btnNext.setOnClickListener(this);



        btnPrev = (ImageButton) findViewById(R.id.fabLeft);
        btnPrev.setEnabled(false);
        btnPrev.setOnClickListener(this);

        findViewById(R.id.btnPrint).setOnClickListener(this);

        Button btn =  (Button)findViewById(R.id.btnMREdit);
        btn.setOnClickListener(this);
        Bundle extras = getIntent().getExtras();
        meterDao = new MeterReadingDao(this);
        arry = new ArrayList<>();
        if (extras != null) {
            this.mru_id = extras.getString("mru_id");
            this.arry = meterDao.fetchInfos(this.mru_id);
            btnPrev.setEnabled(false);
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
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                mViewPager.setCurrentItem(tab.getPosition());
                scrollUp();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




    }

    private void navigate(String dldocno){
        MainApp.bus.post(new MessageTransport("navigate",dldocno));
    }

    private void prepareData(int index){
       if(!arry.isEmpty()){
           meterInfo = this.arry.get(index);
           CustomerInfo  customerInfo = meterInfo.getCustomer();
           TextView txt = (TextView)findViewById(R.id.txtCAN);
           txt.setText(customerInfo.getAccn());
           txt = (TextView)findViewById(R.id.txtName);
           txt.setText(customerInfo.getCname());
           txt = (TextView)findViewById(R.id.txtAddress);
           txt.setText(customerInfo.getAddress());
           txt = (TextView)findViewById(R.id.txtMeterNumber);
           txt.setText(meterInfo.getMeter_number());
           txt = (TextView)findViewById(R.id.txtMeterNumber);
           txt.setText(meterInfo.getMeter_number());
           txt = (TextView)findViewById(R.id.txtMRUID);
           txt.setText(meterInfo.getMru_id());
           txt = (TextView)findViewById(R.id.txtSeqNum);
           txt.setText(meterInfo.getSeq_number());
           String page = (current+1)+"/"+arry.size();

           txt = (TextView)findViewById(R.id.txtPagination);
           txt.setText(page);
           txt = (TextView)findViewById(R.id.txtRateCode);
           txt.setText(meterInfo.getBillClass().getDesc());
           meterStatus();
           navigate(meterInfo.getDldocno());
           if(Utils.isNotEmpty(meterInfo.getPresRdg())){
               setReadingValue(meterInfo.getPresRdg());
           }
           else {
               setReadingValue("");
           }
       }
    }


    private void meterStatus(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(meterInfo.getReadStat());

        if(Utils.isNotEmpty(meterInfo.getBlock_tag())){
            stringBuilder.append(' ');
            stringBuilder.append(meterInfo.getBlock_tag());
        }
        if(Utils.isNotEmpty(meterInfo.getGrp_flag())){
            stringBuilder.append(' ');
            stringBuilder.append('G');
        }
        TextView txt = (TextView)findViewById(R.id.txtMeterStatus);
        txt.setText(stringBuilder.toString());
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
         String dldcono = meterInfo.getDldocno();
        adapter.addFragment(MRCustomerInfoFragment.newInstance(dldcono), "Customer Info");
        adapter.addFragment(MROCFragment.newInstance(dldcono), "OC");
        adapter.addFragment(MRDeliveryRFragment.newInstance(dldcono), "Delivery Remarks");
        adapter.addFragment(MRRemarksFragment.newInstance(dldcono), "Remarks");
        mViewPager.setAdapter(adapter);
    }


    public void showMeterRdgDialog(){
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_change_reading);
        dialog.setCancelable(false);
        final EditText txtDlg = (EditText)dialog.findViewById(R.id.dlg_body);
        if(Utils.isNotEmpty(meterInfo.getPresRdg())){
            txtDlg.setText(meterInfo.getPresRdg());
        }
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
                if(!meterInfo.getPresRdg().equals(value)){
                    updateReading(value, true);
                }

              //  comp_cons_range(value);
                dialog.dismiss();
            }
        });



        dialog.show();
    }

    public void updateReadStatus(String readStatus){
        meterInfo.setReadStat(readStatus);
        meterStatus();
    }

    public void updateReading(String value, boolean isRdgTries){
        int tries =0 ;
        if(Utils.isNotEmpty(meterInfo.getRdg_tries())){
              tries = Integer.parseInt(meterInfo.getRdg_tries());
        }
        //update read status
        //if the reading is empty reset the read status as READ
        //otherwise R
        if(value.isEmpty()){
            tries =0;
            updateReadStatus("R");
        }else {
            String readStat = meterInfo.getReadStat();
            if(readStat.equals("R")){
                updateReadStatus("E");
            }
            else {
                updateReadStatus("R");
            }
        }

        if(isRdgTries){
            tries = tries+1;
        }

        String latitude = null;
        String longtitude = null;

        if(gpsTracker.canGetLocation()){
            latitude = String.valueOf(gpsTracker.getLatitude());
            longtitude = String.valueOf(gpsTracker.getLongitude());

        }
        meterDao.updateReading(Utils.getFormattedDate(),
                Utils.getFormattedTime(),
                value,latitude,longtitude,tries, meterInfo.getDldocno(),meterInfo.getReadStat()
        );
        meterInfo.setRdg_tries(String.valueOf(tries));
        setReadingValue(value);
        //start computing the bill consumption
        computeConsumption(meterInfo.getDldocno());
    }


    public void snackbar(String message){
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("Re Enter", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showMeterRdgDialog();
                    }
                });


        // Changing message text color
        snackbar.setActionTextColor(Color.WHITE);


        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTypeface(null, Typeface.BOLD);

        sbView.setBackgroundColor(getResources().getColor(R.color.red_colr));
        snackbar.show();

        //vibrate
        Utils.vibrate(this);
    }

    public void setReadingValue(String value){
        TextView txt = (TextView)findViewById(R.id.txtReading);
        txt.setText(value);
        meterInfo.setPresent_reading(value);
        String formattedTime = Utils.getFormattedTime();
        if(!prefs.getData(IS_FIRST_RDG,false)){
            prefs.setData(READ_START_TIME,formattedTime);
            prefs.setData(IS_FIRST_RDG,true);
            prefs.setData(APP_STATUS,"MODIFIED");
        }
        if(!prefs.getData(IS_END_RDG,false)){
            int countUnRead = meterDao.countUnRead();
            if(MainApp.total_records == countUnRead){
                prefs.setData(READ_END_TIME,formattedTime);
                prefs.setData(IS_END_RDG,true);
                prefs.setData(APP_STATUS,"ALL READ");
            }
        }



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
                startActivityForResult(intent,SEARCH_REQ);
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
        Bundle  bundle;
        switch(id){
            case R.id.btnMREdit:
                showMeterRdgDialog();
                break;
            case R.id.txtFiltered:
                String str = txtFilter.getText().toString();
                if(!str.isEmpty()){
                    dlgUtils.showOKDialog(DLG_RESET,"","Resetting Search...", new Bundle());
                }
                break;
            case R.id.fabLeft:
                bundle = new Bundle();
                bundle.putString("action","next");
                if(MainApp.isEditMode){
                    dlgUtils.showYesNoDialog(DLG_EDITMODE,"There are still unsave record.\n" +
                            "Proceed to the Previous Record?",bundle);
                }
                else {
                    movePrevious();
                }
                break;
            case R.id.fabRight:
                bundle = new Bundle();
                bundle.putString("action","previous");
                if(MainApp.isEditMode){
                    dlgUtils.showYesNoDialog(DLG_EDITMODE,"There are still unsave record.\n" +
                            "Proceed to the Next Record?",bundle);
                }else {
                    moveNext();
                }
                break;
            case R.id.btnPrint:
                String readstat = meterInfo.getReadStat();
                if(readstat.equals("U")){
                    dlgUtils.showOKDialog(2,"Print ","Cannot Print Reading " +
                            "for an Unread Bill!",new Bundle());
                }
                else {
                    String newReadStat="P";
                    if(readstat.equals("E")){
                        newReadStat="Q";
                    }
                    meterDao.updateReadStatus(newReadStat,meterInfo.getDldocno());
                    updateReadStatus(newReadStat);
                    MainApp.bus.post(new MessageTransport("readstat",newReadStat));
                    }
                break;
        }
    }

    private void movePrevious(){
        if(current!=0){
            current--;
            prepareData(current);
        }
        else{
            btnPrev.setEnabled(false);
        }
        btnNext.setEnabled(true);
    }
    private void moveNext(){
        if(current< arry.size()-1){
            current++;
            prepareData(current);
            btnPrev.setEnabled(true);
        }
        else {
            btnNext.setEnabled(false);
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
                meterDao.updateSequenceNumber(value,meterInfo.getDldocno());
                dlgSeqNumber.dismiss();
                dlgUtils.showOKDialog("Sequence Number Updated");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == SEARCH_REQ){
            if(resultCode ==  Activity.RESULT_OK){
               Bundle  bundle = data.getExtras();
                String columnSearch = bundle.getString("key");
                String searchValue = bundle.getString("value");
                List<MeterInfo> temp= meterDao.fetchInfos(this.mru_id,columnSearch,searchValue);
                if(temp.isEmpty()){
                    dlgUtils.showOKDialog("No Search Found");
                    showFilterSign(false);
                }
                else {
                    dlgUtils.showOKDialog(temp.size()+ " record(s) Found!!");
                    this.arry = temp;
                    current = 0;
                    btnPrev.setEnabled(false);
                    prepareData(current);
                    showFilterSign(true);
                }


            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showFilterSign(boolean isShow){

        if(isShow){
            txtFilter.setText("Filtered");
            txtFilter.setBackgroundColor(getResources().getColor(R.color.red_colr));
        }
        else {
            txtFilter.setText("");
            txtFilter.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }
    }

    @Override
    public void dialog_confirm(int dialog_id, Bundle params) {
        switch(dialog_id){
            case DLG_RESET:
                 this.arry =   meterDao.fetchInfos(this.mru_id);
                showFilterSign(false);
                current =0;
                btnPrev.setEnabled(false);
                prepareData(current);
                break;
            case DLG_EDITMODE:
                String action = params.getString("action");
                if(action.equals("next")){
                    moveNext();
                }
                else{
                    movePrevious();
                }
                break;
        }
    }

    @Override
    public void dialog_cancel(int dialog_id, Bundle params) {

    }

    @Override
    public void onPostConsResult(MeterConsumption meterConsumption) {
        Log.i("Test","meter consumption"+meterConsumption.getBilled_cons());
        meterDao.updateConsumption(meterConsumption,meterInfo.getDldocno());
        comp_cons_range(meterConsumption);
        meterInfo.setPresent_reading(meterConsumption.getPresent_rdg());
    }

    public void comp_cons_range(MeterConsumption meterConsumption){
      //  snackbar("Consumption Very Low");
    }

    @Subscribe
    public void getMessage(MessageTransport msgTransport) {
        String action = msgTransport.getAction();
        if(action.equals("reading")){
          updateReading(meterInfo.getPresRdg(), false);
        }

    }

    private void computeConsumption(String dldocno){
        MeterConsumption mterCons = meterDao.getConsumption(dldocno);

        String bill_scheme = mterCons.getCsmb_type_code();
        //check for the type of billing scene
        //if set to zero  then use thr regular Read and Bill Computer
        if(bill_scheme.equals("0")){
            CompConsumption comConsumption = new CompConsumption(this);
            comConsumption.compute(mterCons);
        }
        //CS
        else if(bill_scheme.equals("1") ||bill_scheme.equals("4")){
            CompCSScheme compCSScheme = new CompCSScheme(this);
            compCSScheme.compute(mterCons,meterDao);
        }
        //MB
        else if(bill_scheme.equals("2")||bill_scheme.equals("5")){
            CompMBScheme compMBScheme = new CompMBScheme(this);
            compMBScheme.compute(mterCons,meterDao);
        }
    }

    /**
     *  Message based on parameter: 0=unread,  2=blocked, 3=unread MB mother meter
     */
    private void noPrint_Bill(int type){
          StringBuilder  strBuilder = new StringBuilder();
          strBuilder.append("Cannot print bill ");
           switch(type){
               case 0: strBuilder.append("Unread Meters!"); break;
               case 1: strBuilder.append("More than 9x!"); break;
               case 2: strBuilder.append("Blocked Acct!"); break;
               case 3:
                   strBuilder.append("Child Mtr w/");
                   strBuilder.append("Unread MB Mother Mtr");
                   break;
               case 4: strBuilder.append("KAM Acct!"); break;
           }
        dlgUtils.showOKDialog("BILL GENERATION",strBuilder.toString());
    }

}
