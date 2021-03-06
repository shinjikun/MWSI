package com.indra.rover.mwsi.ui.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
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
import com.indra.rover.mwsi.data.pojo.meter_reading.MeterPrint;
import com.indra.rover.mwsi.data.pojo.meter_reading.misc.CustomerInfo;
import com.indra.rover.mwsi.data.pojo.meter_reading.display.MeterInfo;
import com.indra.rover.mwsi.print.PrintPage;
import com.indra.rover.mwsi.print.utils.ZebraPrinterUtils;
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
        DialogUtils.DialogListener, Constants, Compute.ConsumptionListener,
        BCompute.BillComputeListener, PrintPage.PrintPageListener,
         ZebraPrinterUtils.ZebraPrintListener
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

    DialogUtils dlgUtils;
    TextView txtFilter;
    //Button btnPrint;
    CoordinatorLayout coordinatorLayout;
    GPSTracker gpsTracker;
    ZebraPrinterUtils zebraUtils;

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


        zebraUtils = ZebraPrinterUtils.getInstance(this);
        zebraUtils.setListener(this);

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
         try{
             MainApp.bus.post(new MessageTransport("navigate",dldocno));
         }catch (Exception e){
             e.printStackTrace();
         }

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
           txt.setOnClickListener(this);
           meterStatus();
           navigate(meterInfo.getDldocno());
           int textColor = Color.BLACK;
           if(Utils.isNotEmpty(meterInfo.getRange_code())){
              String rcodestr = meterInfo.getRange_code();
               char rangecode = rcodestr.charAt(0);
               switch (rangecode){
                   case '-':
                       textColor = getResources().getColor(R.color.red_colr);
                       break;
                   case '3':
                       textColor = getResources().getColor(R.color.red_colr);
                       break;
                   case '4':
                       textColor = getResources().getColor(R.color.red_colr);
                       break;

               }
           }

           txt = (TextView)findViewById(R.id.txtReading);
           txt.setTextColor(textColor);
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
         arry.get(current).setReadStat(readStatus);
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
                        int countChildPrinted = meterDao.countMBChildBilled(accoutNumb);
                        if(countChildPrinted!=0){
                            rdg_disabled(3);
                            return;
                        }
                        loadMeterInput();
                    }
                    break;
                case CS_MOTHER:
                    //disallow from editing if subsidiary meter is still unbilled
                    int countP = meterDao.countCSChildBilled(accoutNumb);
                    if(countP!=0){
                        rdg_disabled(2);
                        return;
                    }
                    loadMeterInput();
                    break;
                case MB_CHILD:
                    //dont edit the reading if its siblings are already printed
                    String parent_id = meterInfo.getParentID();
                    int countChildPrinted = meterDao.countMBChildBilled(parent_id);
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
                .make(coordinatorLayout, message, Snackbar.LENGTH_LONG);




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

         arry.get(current).setPresent_reading(value);
        meterInfo.setPresent_reading(value);

        String formattedTime = Utils.getFormattedTime();
        if(!prefs.getData(IS_FIRST_RDG,false)){
            prefs.setData(READ_START_TIME,formattedTime);
            prefs.setData(IS_FIRST_RDG,true);
            prefs.setData(APP_STATUS,"MODIFIED");
        }
        if(!prefs.getData(IS_END_RDG,false)){
            int countUnRead = meterDao.countUnRead();

            if(prefs.getData(PRINT_EOD_ENABLED,false)){
                if(!prefs.getData(PRINT_EOD_PRINTED,false)){
                    int count = prefs.getData(PRINT_EOD_COUNT,MainApp.total_records);
                    if(count == countUnRead){
                        dlgUtils.showYesNoDialog(DLG_EOD,"Print EOD Report?",new Bundle());
                    }
                }
            }
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
                intent.putExtra("mru_id",mru_id);
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
                bundle.putString("action","previous");
                if(MainApp.isEditMode){
                    dlgUtils.showYesNoDialog(DLG_EDITMODE,"There are still unsave record.\n" +
                            "Proceed to the Previous Record?",bundle);
                }
                else {

                    if(meterInfo.getReadStat().equals("P")||meterInfo.getReadStat().equals("Q")){

                        if(meterInfo.getPrintTag() == MeterInfo.BILLABLE){
                            if(!Utils.isNotEmpty(meterInfo.getDelCode())){
                                dlgUtils.showOKDialog(DLG_DELIV,null,"You have to enter a delivery code " +
                                        "before proceeding to the previous record",new Bundle());
                                return;
                            }
                        }

                    }

                    movePrevious();
                }
                break;
            case R.id.fabRight:
                bundle = new Bundle();
                bundle.putString("action","next");
                if(MainApp.isEditMode){
                    dlgUtils.showYesNoDialog(DLG_EDITMODE,"There are still unsave record.\n" +
                            "Proceed to the Next Record?",bundle);
                }else {


                    if(meterInfo.getPrintTag() == MeterInfo.BILLABLE){
                        if(meterInfo.getReadStat().equals("P")||meterInfo.getReadStat().equals("Q")){
                            if(!Utils.isNotEmpty(meterInfo.getDelCode())){
                                dlgUtils.showOKDialog(DLG_DELIV,null,"You have to enter a delivery code " +
                                        "before proceeding to the next record",new Bundle());
                                return;
                            }
                        }
                    }


                    moveNext();
                }
                break;
            case R.id.btnPrint:
               computeBill();
                break;
            case R.id.txtReading:
                if(Utils.isNotEmpty(meterInfo.getRange_code())){
                    String rcodestr = meterInfo.getRange_code();
                    char rangecode = rcodestr.charAt(0);
                    switch (rangecode){
                        case '-':
                           snackbar("Negative Consumption");
                            break;
                        case '3':
                           snackbar("Very Low Consumption");
                            break;
                        case '4':
                            snackbar("Very High Consumption");
                            break;

                    }
                }
                break;
        }
    }

    /**
     * change the current read status to status printed 'P'
     * @param reqDelivCode
     */
    void changeToPrinted(boolean reqDelivCode){
        String readstatstr = meterInfo.getReadStat();
        char readstat = readstatstr.charAt(0);
        String newReadStat;
        switch(readstat){
            case 'P':
            case 'Q':

                break;
            case 'E':
                newReadStat="Q";
                meterDao.updateReadStatus(newReadStat,meterInfo.getDldocno());
                meterDao.updatePrintDate(Utils.getFormattedDate(),meterInfo.getDldocno());
                updateReadStatusDisplay(newReadStat);
                if(reqDelivCode){
                    mViewPager.setCurrentItem(2);
                    scrollUp();
                }

                try{
                    MainApp.bus.post(new MessageTransport("readstat",newReadStat));
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;
            case 'R':
                newReadStat="P";
                meterDao.updateReadStatus(newReadStat,meterInfo.getDldocno());
                meterDao.updatePrintDate(Utils.getFormattedDate(),meterInfo.getDldocno());
                updateReadStatusDisplay(newReadStat);
                if(reqDelivCode){
                    mViewPager.setCurrentItem(2);
                    scrollUp();
                }

                try{
                    MainApp.bus.post(new MessageTransport("readstat",newReadStat));
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }



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
        } else if(requestCode == BLUETOOTH_REQ){
            if(resultCode ==  Activity.RESULT_OK){
                String btAddress = prefs.getData(BTADDRESS,"");
                if(Utils.isNotEmpty(btAddress)){
                    // try to connect to this device
                    zebraUtils.setBtAddress(btAddress);

                }
                else {
                    dlgUtils.showOKDialog("Please connect a Printer via Bluetooth  in Settings");
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
            case DLG_DELIV:
                mViewPager.setCurrentItem(2);
                scrollUp();
                break;
            case DLG_PRINTMRSTUB:
                String value = params.getString("value");
                if(Utils.isNotEmpty(value)){
                      zebraUtils.sendData(value.getBytes(),0);
                }
                break;
            case DLG_EOD:
                eodReportPrint();
                break;
            case DLG_ENTER_DELIVCODE:
                mViewPager.setCurrentItem(2);
                scrollUp();
                break;
        }
    }

    @Override
    public void dialog_cancel(int dialog_id, Bundle params) {

    }




    @Subscribe
    public void getMessage(MessageTransport msgTransport) {
        String action = msgTransport.getAction();
        if(action.equals("reading")){

                String readStat = meterInfo.getReadStat();
                if(readStat.equals("U")) {
                    meterInfo.setReadStat("R");
                    arry.get(current).setReadStat("R");
                }
            updateReadingInDB(meterInfo.getPresRdg());
            //update read status
            updateReadStatusDisplay(meterInfo.getReadStat());
            computeConsumption(meterInfo.getDldocno());
        } else if(action.equals("delcode")){
            String delcode = msgTransport.getMessage();
            meterInfo.setDelCode(delcode);
            arry.get(current).setDelCode(delcode);
        }

    }

    /**
     * Compute Consumption
     * @param dldocno
     */
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

    void computeConsRange(MeterConsumption mtrCons){
        int consumption = mtrCons.getBilled_cons();
        int textColor = Color.BLACK;
        String range_code ="0" ;
        if(consumption!=0) {
            String str = mtrCons.getAve_consumption();
            int ave_cons ;
            if(Utils.isNotEmpty(str)){
                ave_cons = Integer.parseInt(str);
                if(ave_cons == 0){
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
                        snackbar("Very High Consumption");
                        range_code = VERY_HIGH;
                        textColor =  getResources().getColor(R.color.red_colr);
                    }
                    else {
                        range_code = NORMAL;
                    }
                }
                else  {
                    if (-pcntDiff > devi){
                        snackbar("Very Low Consumption");
                        range_code = VERY_LOW;
                        textColor =  getResources().getColor(R.color.red_colr);
                    }
                    else {
                        range_code = NORMAL;
                    }
                }
            }


            if(range_code.equals(NORMAL)){
                String presRdg= mtrCons.getPresent_rdg();
                String prevRdg = mtrCons.getPrev_rdg();
                if(Utils.isNotEmpty(presRdg)){
                    if(Utils.isNotEmpty(prevRdg)){
                        int pres_rdg =  Integer.parseInt(presRdg);
                        int prev_rdg = Integer.parseInt(prevRdg);
                        //if present reading is greater than previous reading
                        if(pres_rdg<prev_rdg){
                            range_code = "-";
                            snackbar("Negative Consumption");
                            textColor =  getResources().getColor(R.color.red_colr);

                        }
                    }
                }
                else {
                    snackbar("Negative Consumption");
                    range_code = "-";
                    textColor =  getResources().getColor(R.color.red_colr);
                }
            }




        }else {
            snackbar("Zero Consumption");
            range_code = NORMAL;
            textColor =  getResources().getColor(R.color.red_colr);

        }

        mtrCons.setRange_code(range_code);
        meterInfo.setRange_code(range_code);
        arry.get(current).setRange_code(range_code);
        meterDao.updateRangeCode(range_code,meterInfo.getDldocno());
        TextView txt = (TextView)findViewById(R.id.txtReading);
        txt.setOnClickListener(this);
        txt.setTextColor(textColor);
        txt.setText(meterInfo.getPresRdg());

    }



    void computeBill(){

        String readstatstr = meterInfo.getReadStat();
        int norecompute = 0;
        char readstat = readstatstr.charAt(0);

        switch(readstat){

            case 'U':
                noPrint_Bill(0);
                break;
            case 'P':
            case 'Q':
                //if 2nd attempt to print, status is already 'p', KAM Account should still not be printed.
                if(Utils.isNotEmpty(meterInfo.getGrp_flag())){
                    if(meterInfo.getGrp_flag().equals("K")){
                        noPrint_Bill(4);
                        return;
                    }
                }
                // bill already printed, no recompute required, just reprint!
                norecompute =1;
                break;
            case 'R':
            case 'E':
                switch (meterInfo.getPrintTag()){
                    case MeterInfo.BILLNOPRINT:
                    case MeterInfo.BILLABLE:
                        norecompute =0;
                        break;
                    case MeterInfo.NONBILLABLE:
                        noPrint_Bill(2);
                        break;

                }
                break;

        }

    if(meterInfo.getPrintTag()!=MeterInfo.NONBILLABLE){
        String bill_str = meterInfo.getBill_scheme();
        int bill_scheme =  Integer.parseInt(bill_str);
        //dont print the bill if the mb child's parent is not yet read
        if(bill_scheme == MB_CHILD){
            String parent_id = meterInfo.getParentID();
            boolean  isRead =meterDao.isParentMeterRead(parent_id,mru_id);
            if(!isRead){
                noPrint_Bill(3);
                return;
            }
        }
        if(norecompute==0){
            BillCompute bill = new BillCompute(this,this);
            bill.compute(meterDao.getMeterBill(meterInfo.getDldocno()));
        }
        else {
           if(meterInfo.getPrintTag()==MeterInfo.BILLNOPRINT){
               noPrint_Bill(5);
           }
            else {
                chkBluetoothConn();
           }

        }
    }



    }

    void startPrinting(){

        PrintPage printPage = new PrintPage(this,this);
        MeterPrint meterPrint = meterDao.getMeterPrint(meterInfo.getDldocno());
        printPage.execute(meterPrint);

        //byPassPrinting();
    }

    void eodReportPrint(){
        MeterReadingDao mtrDao = new MeterReadingDao(this);
        ArrayList<MeterPrint> arry = mtrDao.getEODReport();
        PrintPage printPage = new PrintPage(this,this);
        printPage.printEOD(arry);
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
                   strBuilder.append("Child Meter w/");
                   strBuilder.append('\n');
                   strBuilder.append("Unread MB Mother Meter");
                   break;
               case 4: strBuilder.append("KAM Acct!"); break;
               case 5: strBuilder.append("CS Meters Acct!"); break;
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
    public void onPostConsResult(MeterConsumption meterConsumption) {
        meterDao.updateConsumption(meterConsumption,meterInfo.getDldocno());
        meterInfo.setPrintTag(meterConsumption.getPrintTag());
        arry.get(current).setPrintTag(meterConsumption.getPrintTag());
        meterDao.updatePrintTag(meterConsumption,meterInfo.getDldocno());
        meterInfo.setBilled_cons(String.valueOf(meterConsumption.getBilled_cons()));
        arry.get(current).setBilled_cons(String.valueOf(meterConsumption.getBilled_cons()));
        computeConsRange(meterConsumption);
    }

    private void updateArry(MeterConsumption childMeter){
        int size = arry.size();
        for(int i=0;i<size;i++){
            MeterInfo mtrInfo = arry.get(i);
            if(mtrInfo.getMru_id().equals(childMeter.getId())){
               mtrInfo.setPrintTag(childMeter.getPrintTag());
                arry.set(i,mtrInfo);
                break;
            }
        }
    }

    @Override
    public void onPrintChildMeters(MeterConsumption meterConsumption,
                                   List<MeterConsumption> childMeters) {
        //print child meters
        meterDao.updateConsumption(meterConsumption,meterInfo.getDldocno());
        meterInfo.setPrintTag(meterConsumption.getPrintTag());
        arry.get(current).setPrintTag(meterConsumption.getPrintTag());
        meterDao.updatePrintTag(meterConsumption,meterInfo.getDldocno());
        computeConsRange(meterConsumption);
        meterInfo.setPresent_reading(meterConsumption.getPresent_rdg());
        arry.get(current).setPresent_reading(meterConsumption.getPresent_rdg());
        int size = childMeters.size();
        for(int i= 0; i<size;i++){
            MeterConsumption childMeter = childMeters.get(i);
            updateArry(childMeter);
            meterDao.updateConsumption(childMeter,childMeter.getId());

            //print child
        }
        //print child meter
    }

    @Override
    public void onPostBillResult(MeterBill mtrBill) {

        meterDao.updateMeterBill(mtrBill);
        if(meterInfo.getGrp_flag().equals("K")){
            changeToPrinted(false);
            return;
        }
        switch(meterInfo.getPrintTag()){
            case MeterInfo.BILLABLE:


                 chkBluetoothConn();
                break;
            case MeterInfo.BILLNOPRINT:
                changeToPrinted(false);
                break;
        }

    }

    @Override
    public void onPrintPageResult(String meterPrintPage, boolean isMeterprint) {
        if(isMeterprint){

            zebraUtils.printMeterReading(meterPrintPage.getBytes());
        }
        else {
            zebraUtils.sendData(meterPrintPage.getBytes());
        }
    }

    String mrStubPage;
    @Override
    public void onPrintPageAndMRStub(String meterPrintPage, final String mrStubPage) {
        this.mrStubPage = mrStubPage;
        zebraUtils.sendData(meterPrintPage.getBytes(),ZebraPrinterUtils.PRINT_W_MRSTUB);


    }

    /**
     * check if the device bluetooth is enable/on
     * if not force the user to turn it on
     */
    private void chkBluetoothConn(){
        BluetoothAdapter   BTAdapter = BluetoothAdapter.getDefaultAdapter();
        if(BTAdapter == null){
            dlgUtils.showOKDialog("BLUETOOTH NOT SUPPORTED","Your phone " +
                    "does not support bluetooth");
        }else {
            if (!BTAdapter.isEnabled()) {
                // Bluetooth is not enable :)
                Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBT, BLUETOOTH_REQ);
            }
            else {
                String btAddress = prefs.getData(BTADDRESS,"");
                if(Utils.isNotEmpty(btAddress)){
                    zebraUtils.setBtAddress(btAddress);
                    startPrinting();
                }
                else {
                   dlgUtils.showOKDialog("Please setup a BLUETOOTH PRINTER in Option before printing");
                }
               //
            }
        }
    }


    @Override
    public void onFinishPrinting(int type) {
        if(progressDialog!=null)
            progressDialog.dismiss();
        switch (type){
            case ZebraPrinterUtils.PRINT_ONLY:
                break;

            case ZebraPrinterUtils.PRINT_READING:

                runOnUiThread(new Runnable() {
                    public void run() {
                        changeToPrinted(true);
                        int tries = meterInfo.getPrintCount();
                        tries = tries+1;
                        meterDao.updatePrintCount(tries,meterInfo.getDldocno());
                        meterInfo.setPrintCount(tries);
                        arry.get(current).setPrintCount(tries);
                    }
                });




                break;

            case ZebraPrinterUtils.PRINT_W_MRSTUB:

                runOnUiThread(new Runnable() {
                    public void run() {
                        changeToPrinted(true);
                        Bundle b = new Bundle();
                        b.putString("value",mrStubPage);
                        dlgUtils.showYesNoDialog(DLG_PRINTMRSTUB,"MR Stub",b);
                    }
                });

                break;

        }
    }

    private void byPassPrinting(){
        changeToPrinted(true);
        int tries = meterInfo.getPrintCount();
        tries = tries+1;
        meterDao.updatePrintCount(tries,meterInfo.getDldocno());
        meterInfo.setPrintCount(tries);
        arry.get(current).setPrintCount(tries);
    }

    @Override
    public void onErrorPrinting() {
        if(progressDialog!=null)
          progressDialog.dismiss();
        dlgUtils.showOKDialog("Please check the status of the Bluetooth Printer");
    }

    ProgressDialog progressDialog;
    @Override
    public void onStartPrinting() {
        progressDialog = ProgressDialog.show(this, "", "Printing! Please wait...");

    }
}
