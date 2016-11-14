package com.indra.rover.mwsi.ui.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.data.db.ConnectDao;
import com.indra.rover.mwsi.data.pojo.meter_reading.MeterPrint;
import com.indra.rover.mwsi.print.PrintPage;
import com.indra.rover.mwsi.print.utils.ZebraPrinterUtils;
import com.indra.rover.mwsi.utils.Constants;
import com.indra.rover.mwsi.utils.DialogUtils;
import com.indra.rover.mwsi.utils.PreferenceKeys;
import com.indra.rover.mwsi.utils.Utils;

import java.util.ArrayList;

public class BillReprintActivity extends AppCompatActivity implements Constants,
        DialogUtils.DialogListener,View.OnClickListener,ZebraPrinterUtils.ZebraPrintListener,
        PrintPage.PrintPageListener {


    DialogUtils dlgUtils;
    PreferenceKeys prefs;
    ZebraPrinterUtils zebraUtils;
    int currentIndex =0;
    ArrayList<MeterPrint> arrayList;
    final int DLG_NOITEMS = 1;
    TextView txtCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_reprint);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        dlgUtils = new DialogUtils(this);
        dlgUtils.setListener(this);
        ConnectDao dbDao = new ConnectDao(this);
        arrayList = dbDao.getReprintItems();
        if(arrayList.isEmpty()){
            dlgUtils.showOKDialog(DLG_NOITEMS,"","There is no items to be printed!",new Bundle());
            return;
        }
        else {
            showPanels(0);
            //display the total number of items to be printed
            TextView txt =  (TextView)findViewById(R.id.txtInstruction);
            String str =   getString(R.string.billreprint_txt1,arrayList.size());
            txt.setText(str);
            txt =  (TextView)findViewById(R.id.txtTotal);
             str =   getString(R.string.billreprint_total,arrayList.size());
            txt.setText(str);
        }
        txtCounter =  (TextView)findViewById(R.id.txtCounter);
        prefs =  PreferenceKeys.getInstance(this);
        zebraUtils = ZebraPrinterUtils.getInstance(this);
        zebraUtils.setListener(this);

        String btAddress = prefs.getData(BTADDRESS,"");
        if(Utils.isNotEmpty(btAddress)){
            // try to connect to this device
            zebraUtils.setBtAddress(btAddress);

        }
        else {
            dlgUtils.showOKDialog("Please connect a Printer via Bluetooth  in Settings");
        }

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
    public void dialog_confirm(int dialog_id, Bundle params) {
        switch (dialog_id){

            case DLG_NOITEMS:
                finish();
                break;
        }
    }

    @Override
    public void dialog_cancel(int dialog_id, Bundle params) {

    }

    @Override
    public void onClick(View view) {
        int id  = view.getId();
        switch(id){
            case R.id.btnPrint:
                if(!arrayList.isEmpty()){
                    MeterPrint meterPrint = arrayList.get(currentIndex);
                    startPrinting(meterPrint);
                    showPanels(2);
                }
                break;
        }
    }

    @Override
    public void onFinishPrinting(int type) {





        if(progressDialog!=null)
            progressDialog.dismiss();
        currentIndex++;
        //print until it reaches the last item
        if(currentIndex!=arrayList.size()){
            final MeterPrint meterPrint = arrayList.get(currentIndex);
            runOnUiThread(new Runnable() {
                public void run() {
                    startPrinting(meterPrint);
                }
            });


        }
        else {
            //this ends the printing...display the completed screen
            runOnUiThread(new Runnable() {
                public void run() {
                    showPanels(1);
                }
            });


        }
    }
    ProgressDialog progressDialog;
    @Override
    public void onErrorPrinting() {
        if(progressDialog!=null)
            progressDialog.dismiss();
        dlgUtils.showOKDialog("Please check the status of the Bluetooth Printer");
        //if error occur reset the counter to zero and then display panel 1
        runOnUiThread(new Runnable() {
            public void run() {
                currentIndex =0;
                showPanels(0);
            }
        });

    }

    @Override
    public void onStartPrinting() {
        progressDialog = ProgressDialog.show(this, "", "Printing! Please wait...");
    }

    void startPrinting(MeterPrint meterPrint){
        txtCounter.setText(String.valueOf((currentIndex+1)));
        PrintPage printPage = new PrintPage(this,this);
        printPage.execute(meterPrint);
    }

    @Override
    public void onPrintPageResult(String meterPrintPage, boolean isMeterprint) {
        zebraUtils.sendData("hello".getBytes());
    }

    @Override
    public void onPrintPageAndMRStub(String meterPrintPage, String mrStubPage) {

    }

    private void showPanels(int paneltypes){

        switch(paneltypes){

            case 0:
                //display the panel whick ask the user to start printing
                findViewById(R.id.pnl1).setVisibility(View.VISIBLE);
                findViewById(R.id.pnl2).setVisibility(View.GONE);
                findViewById(R.id.pnl3).setVisibility(View.GONE);
                break;
            case 1:
                //panel 2 display the completed status screen
                findViewById(R.id.pnl2).setVisibility(View.VISIBLE);
                findViewById(R.id.pnl1).setVisibility(View.GONE);
                findViewById(R.id.pnl3).setVisibility(View.GONE);
                break;
            case 2:
                //panel that display the current number of printed bill
                findViewById(R.id.pnl3).setVisibility(View.VISIBLE);
                findViewById(R.id.pnl1).setVisibility(View.GONE);
                findViewById(R.id.pnl2).setVisibility(View.GONE);
                break;

        }
    }
}
