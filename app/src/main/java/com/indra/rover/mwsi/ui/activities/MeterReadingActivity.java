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
import com.indra.rover.mwsi.compute.bill.BCompute;
import com.indra.rover.mwsi.compute.bill.BillCompute;
import com.indra.rover.mwsi.compute.consumption.CompCSScheme;
import com.indra.rover.mwsi.compute.consumption.CompConsumption;
import com.indra.rover.mwsi.compute.consumption.CompMBScheme;
import com.indra.rover.mwsi.compute.consumption.Compute;
import com.indra.rover.mwsi.data.db.MeterReadingDao;
import com.indra.rover.mwsi.data.pojo.meter_reading.MeterBill;
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
        DialogUtils.DialogListener, Constants, Compute.ConsumptionListener,BCompute.BillComputeListener
{

    ViewPager mViewPager;
    TabLayout mTabLayout;
    ScrollView mScrollView;
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
    final int INPUT_REQ =68;
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

     void navigate(String dldocno){
        MainApp.bus.post(new MessageTransport("navigate",dldocno));
    }

     void prepareData(int index){
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
           txt = (TextView)findViewById(R.id.txtReading);
           if(Utils.isNotEmpty(meterInfo.getPresRdg())){

               txt.setText(meterInfo.getPresRdg());
           }
           else {
               txt.setText("");
           }
       }
    }


     void meterStatus(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(meterInfo.getReadStat());

        if(Utils.isNotEmpty(meterInfo.getBlock_tag())){
            stringBuilder.append(' ');
            stringBuilder.append('B');


        }
        if(Utils.isNotEmpty(meterInfo.getGrp_flag())){
            stringBuilder.append(' ');
            stringBuilder.append('G');
        }
        TextView txt = (TextView)findViewById(R.id.txtMeterStatus);
        txt.setText(stringBuilder.toString());
    }

     int[] getViewLocations(View view) {
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

     void setupViewPager(ViewPager mViewPager){
        StatusViewPagerAdapter adapter = new StatusViewPagerAdapter(getSupportFragmentManager());
         String dldcono = meterInfo.getDldocno();
        adapter.addFragment(MRCustomerInfoFragment.newInstance(dldcono), "Customer Info");
        adapter.addFragment(MROCFragment.newInstance(dldcono), "OC");
        adapter.addFragment(MRDeliveryRFragment.newInstance(dldcono), "Delivery Remarks");
        adapter.addFragment(MRRemarksFragment.newInstance(dldcono), "Remarks");
        mViewPager.setAdapter(adapter);
    }




     void updateReadStatusDisplay(String readStatus){
        meterInfo.setReadStat(readStatus);
        meterStatus();
    }




     void updateMeterReading(){
        String bill_str = meterInfo.getBill_scheme();
        String readStat = meterInfo.getReadStat();
     if(readStat.equals("P")||readStat.equals("Q")){
            rdg_disabled(0);
            return;
        }
        if(Utils.isNotEmpty(bill_str)){
            int bill_scheme =  Integer.parseInt(bill_str);
            String accoutNumb = meterInfo.getCustomer().getAccn();
            switch(bill_scheme){
                case CS_CHILD:
                case REG_SCHEME:
                        loadMeterInput();
                    break;
                case MB_MOTHER:
                    int countUnRead = meterDao.countChildUnRead(accoutNumb);
                    if(countUnRead!=0){
                        rdg_disabled(3);
                        return ;
                    }
                    else {
                        int countChildPrinted = meterDao.countChildBilled(accoutNumb);
                        if(countChildPrinted!=0){
                            rdg_disabled(3);
                            return;
                        }
                        loadMeterInput();
                    }
                    break;
                case CS_MOTHER:
                    int countP = meterDao.countChildBilled(accoutNumb);
                    if(countP!=0){
                        rdg_disabled(2);
                        return;
                    }
                    loadMeterInput();
                    break;
                case MB_CHILD:
                    String parent_id = meterInfo.getParentID();
                    int countChildPrinted = meterDao.countChildBilled(parent_id);
                    if(countChildPrinted!=0){
                        rdg_disabled(4);
                        return;
                    }
                   loadMeterInput();
                    break;
            }



        }

    }


     void loadMeterInput(){
         Intent    intent = new Intent(this, InputValueActivity.class);
        intent.putExtra("id",meterInfo.getDldocno());
        intent.putExtra("type",1);
        startActivityForResult(intent, INPUT_REQ);
    }




    public void snackbar(String message){
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("Re Enter", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                      //  showMeterRdgDialog();
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

     void setReadingValue(String value){
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

    void updateReadingInDB(String value){
        String latitude = null;
        String longtitude = null;
        if(gpsTracker.canGetLocation()){
            latitude = String.valueOf(gpsTracker.getLatitude());
            longtitude = String.valueOf(gpsTracker.getLongitude());

        }
        int tries =0;
        String str_tries = meterInfo.getRdg_tries();
        if(Utils.isNotEmpty(str_tries)){
            tries = Integer.parseInt(str_tries);
        }

        meterDao.updateReading(Utils.getFormattedDate(),
                Utils.getFormattedTime(),
                value,latitude,longtitude,tries, meterInfo.getDldocno(),meterInfo.getReadStat()
        );
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
                updateMeterReading();
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
                String bill_str = meterInfo.getBill_scheme();
                if(readstat.equals("U")){
                  noPrint_Bill(0);
                }
                else {
                    if(Utils.isNotEmpty(bill_str)) {
                        int bill_scheme = Integer.parseInt(bill_str);
                        //String accoutNumb = meterInfo
                        switch (bill_scheme){
                            case REG_SCHEME:
                            case CS_MOTHER:
                            case CS_CHILD:
                            case MB_CHILD:
                            case MB_MOTHER:
                                if(!readstat.equals("P")||!readstat.equals("Q")){
                                    BillCompute bill = new BillCompute(this,this);
                                    bill.compute(meterDao.getMeterBill(meterInfo.getDldocno()));
                                }
                                break;
                        }

                    }

                }

                //
                break;
        }
    }

    void changeToPrinted(String readstat){
        String newReadStat="P";
        if(readstat.equals("E")){
            newReadStat="Q";
        }
        meterDao.updateReadStatus(newReadStat,meterInfo.getDldocno());
        updateReadStatusDisplay(newReadStat);
        MainApp.bus.post(new MessageTransport("readstat",newReadStat));
        mViewPager.setCurrentItem(2);
        scrollUp();
    }

     void movePrevious(){
        if(current!=0){
            current--;
            prepareData(current);
            mViewPager.setCurrentItem(0);

            mScrollView.smoothScrollBy(0,0);
            mScrollView.fullScroll(ScrollView.FOCUS_UP);
        }
        else{
            btnPrev.setEnabled(false);
        }
        btnNext.setEnabled(true);

    }
     void moveNext(){
        if(current< arry.size()-1){
            current++;
            prepareData(current);
            btnPrev.setEnabled(true);
            mViewPager.setCurrentItem(0);
            mScrollView.smoothScrollBy(0,0);
            mScrollView.fullScroll(ScrollView.FOCUS_UP);
        }
        else {
            btnNext.setEnabled(false);

        }
    }



    Dialog dlgSeqNumber;
     void showNewSeqDialog(){
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
        } else  if(requestCode == INPUT_REQ){
            if(resultCode ==  Activity.RESULT_OK){
                Bundle  bundle = data.getExtras();
                String value = bundle.getString("value");
                String id =  meterInfo.getDldocno();
                meterInfo = meterDao.fetchInfo(id);
                updateReadStatusDisplay(meterInfo.getReadStat());
                setReadingValue(value);
                computeConsumption(meterInfo.getDldocno());
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
        meterDao.updateConsumption(meterConsumption,meterInfo.getDldocno());
        comp_cons_range(meterConsumption);
    }

    @Override
    public void onPrintChildMeters(MeterConsumption meterConsumption,
                                   List<MeterConsumption> childMeters) {
        //print child meters
        meterDao.updateConsumption(meterConsumption,meterInfo.getDldocno());
        comp_cons_range(meterConsumption);
        meterInfo.setPresent_reading(meterConsumption.getPresent_rdg());
        int size = childMeters.size();
        for(int i= 0; i<size;i++){
            MeterConsumption childMeter = childMeters.get(i);
            meterDao.updateConsumption(childMeter,childMeter.getId());

            //print child
        }
        //print child meter
    }

     void comp_cons_range(MeterConsumption mtrCons){
         int consumption = mtrCons.getBilled_cons();
         String strrange_code="-";
         int range_code =0 ;
         if(consumption!=0) {
             String str = mtrCons.getAve_consumption();
             int ave_cons ;
             if(Utils.isNotEmpty(str)){
                 ave_cons = Integer.parseInt(str);
                 if(ave_cons != 0){
                     ave_cons = consumption;
                     // Compute percentage difference and return range code

                 }
                 // Compute percentage difference and return range code
                 int   pcntDiff = ((consumption - ave_cons)/ave_cons) * 100;
                 int type = -1;
                 if (pcntDiff >= 0){
                     type= 1;
                 }

                 int devi = meterDao.getRTolerance(ave_cons,type);

                 // pcntDiff is positive
                 if(type == 1){
                     if (pcntDiff > devi){
                         snackbar("Consumption Very High");
                         range_code = VERY_HIGH;
                     }
                     else {
                         range_code = NORMAL;
                     }
                 }
                 else  {
                     if (-pcntDiff > devi){
                         snackbar("Consumption Very Low");
                         range_code = VERY_LOW;
                     }
                     else {
                         range_code = NORMAL;
                     }
                 }
         }



            strrange_code = String.valueOf(range_code);
         }else {
             Snackbar snackbar = Snackbar
                     .make(coordinatorLayout, "Zero Consumption", Snackbar.LENGTH_LONG);

             snackbar.show();
         }
         mtrCons.setRange_code(strrange_code);
         meterInfo.setRange_code(strrange_code);
         meterDao.updateRangeCode(strrange_code,meterInfo.getDldocno());

    }

    @Subscribe
    public void getMessage(MessageTransport msgTransport) {
        String action = msgTransport.getAction();
        if(action.equals("reading")){

                String readStat = meterInfo.getReadStat();
                if(readStat.equals("U")) {
                    meterInfo.setReadStat("R");
                }
            updateReadingInDB(meterInfo.getPresRdg());
            //update read status
            updateReadStatusDisplay(meterInfo.getReadStat());
            computeConsumption(meterInfo.getDldocno());
        }

    }

     void computeConsumption(String dldocno){
        MeterConsumption mterCons = meterDao.getConsumption(dldocno);
         if(mterCons==null)
             return;
        String bill_str = mterCons.getCsmb_type_code();
        if(Utils.isNotEmpty(bill_str)){
            int bill_scheme = Integer.parseInt(bill_str);
            switch(bill_scheme){
                case REG_SCHEME:
                case CS_CHILD:
                case MB_CHILD:
                    CompConsumption comConsumption = new CompConsumption(this);
                    comConsumption.compute(mterCons);
                    break;
                case CS_MOTHER:
                    CompCSScheme compCSScheme = new CompCSScheme(this);
                    compCSScheme.compute(mterCons,meterDao);
                    break;
                case MB_MOTHER:
                    CompMBScheme compMBScheme = new CompMBScheme(this);
                    compMBScheme.compute(mterCons,meterDao);
                    break;


            }
        }

    }

    /**
     *  Message based on parameter: 0=unread,  2=blocked, 3=unread MB mother meter
     */
     void noPrint_Bill(int type){
          StringBuilder  strBuilder = new StringBuilder();
          strBuilder.append("Cannot print bill for ");
          strBuilder.append('\n');
           switch(type){
               case 0: strBuilder.append("Unread Meters!"); break;
               case 1: strBuilder.append("More than 9x!"); break;
               case 2: strBuilder.append("Blocked Acct!"); break;
               case 3:
                   strBuilder.append("Child Mtr w/");
                   strBuilder.append('\n');
                   strBuilder.append("Unread MB Mother Mtr");
                   break;
               case 4: strBuilder.append("KAM Acct!"); break;
           }
        dlgUtils.showOKDialog("BILL GENERATION",strBuilder.toString());
    }

    /**
     *  message dialog shown when entering meter reading based on parameter
     *  0  - for already billed
     *  1 -  edited account
     *  2 -  for check meteradb
     *  3 -  for mb parent meter
     *  4 -  for mb child meter
     * @param type type
     */
     void rdg_disabled(int type){
        StringBuilder  strBuilder = new StringBuilder();
        strBuilder.append("Cannot enter reading ");
        strBuilder.append('\n');
        switch(type){
            case 0: strBuilder.append("for already billed accounts!");
                break;
            case 1: strBuilder.append("for Edited Account");
                break;
            case 2:
                strBuilder.append("for Checked Meter\n with unbilled");
                strBuilder.append('\n');
                strBuilder.append("Subsidiary Meters!");
                break;
            case 3:
                strBuilder.append("for MB Parent Meter");
                strBuilder.append('\n');
                strBuilder.append("with unread or");
                strBuilder.append('\n');
                strBuilder.append("billed Child Meters!");
                break;
            case 4:
                strBuilder.append("for MB Child Meter");
                strBuilder.append('\n');
                strBuilder.append("with already billed");
                strBuilder.append('\n');
                strBuilder.append("sibling meters!");

                break;
        }
        dlgUtils.showOKDialog("READING ENTRY",strBuilder.toString());
    }

    @Override
    public void onPostBillResult(MeterBill mtrBill) {
        changeToPrinted(meterInfo.getReadStat());
        meterDao.updateMeterBill(mtrBill);
    }
}
