package com.indra.rover.mwsi.ui.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_reprint);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dlgUtils = new DialogUtils(this);
        dlgUtils.setListener(this);
        prefs =  PreferenceKeys.getInstance(this);
        zebraUtils = ZebraPrinterUtils.getInstance(this);
        zebraUtils.setListener(this);
        ConnectDao dbDao = new ConnectDao(this);
        arrayList = dbDao.getReprintItems();
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
    public void dialog_confirm(int dialog_id, Bundle params) {

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
                }
                break;
        }
    }

    @Override
    public void onFinishPrinting(int type) {
        currentIndex++;
        if(currentIndex!=arrayList.size()){
            MeterPrint meterPrint = arrayList.get(currentIndex);
            startPrinting(meterPrint);

        }
    }

    @Override
    public void onErrorPrinting() {

    }

    @Override
    public void onStartPrinting() {

    }

    void startPrinting(MeterPrint meterPrint){
        PrintPage printPage = new PrintPage(this,this);

        printPage.execute(meterPrint);
    }

    @Override
    public void onPrintPageResult(String meterPrintPage, boolean isMeterprint) {
        zebraUtils.sendData(meterPrintPage.getBytes());
    }

    @Override
    public void onPrintPageAndMRStub(String meterPrintPage, String mrStubPage) {

    }
}
