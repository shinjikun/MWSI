package com.indra.rover.mwsi.ui.activities;


import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.indra.rover.mwsi.MainApp;
import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.data.db.MRUDao;
import com.indra.rover.mwsi.data.db.MeterBillDao;
import com.indra.rover.mwsi.data.db.MeterReadingDao;
import com.indra.rover.mwsi.data.pojo.meter_reading.MeterPrint;
import com.indra.rover.mwsi.print.PrintPage;
import com.indra.rover.mwsi.print.utils.BluetoothHelper;
import com.indra.rover.mwsi.print.utils.ConnectThread;
import com.indra.rover.mwsi.ui.fragments.PrinterConnectionDialog;
import com.indra.rover.mwsi.utils.Constants;
import com.indra.rover.mwsi.utils.DialogUtils;
import com.indra.rover.mwsi.utils.PreferenceKeys;
import com.indra.rover.mwsi.utils.Utils;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener,
        Constants,DialogUtils.DialogListener,BluetoothHelper.BluetoothHelperEventListener,PrintPage.PrintPageListener {

    PreferenceKeys prefs;
    DialogUtils dialogUtils;
    Button btnPair,btnUnPair;
    final int STAT_CHANGE_PASS =888;
    final int REQUEST_BLUETOOTH = 700;
    boolean isBluetoothOn = true;
    final int DLG_UNPAIR =123;

    ImageButton btnPrint;
    BluetoothHelper btHelper;
    private static final String DIALOG_PRINTER_CONNECT_TAG = "printer_connect";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        btHelper = BluetoothHelper.instance();
        btHelper.setOnBuetoothHelperEventListener(this);
        prefs = PreferenceKeys.getInstance(this);
        dialogUtils = new DialogUtils(this);
        dialogUtils.setListener(this);
        btnPair =  (Button)findViewById(R.id.btnPair);
        btnPair.setOnClickListener(this);
        btnUnPair =  (Button)findViewById(R.id.btnUnPair);
        btnUnPair.setOnClickListener(this);
        btnPrint = (ImageButton)findViewById(R.id.btnPrint);
        btnPrint.setOnClickListener(this);
        registerReceiver(mPairReceiver, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED));

        init();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        else if(item.getItemId() == R.id.mnuChangeStatus ){
            Intent intent =new Intent(this,ChangeStatusActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        int id =  view.getId();
        switch (id){
            case R.id.btnLogin:
                showLogin();
                break;
            case R.id.btnPair:
                if(!isBluetoothOn){
                    Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBT, REQUEST_BLUETOOTH);
                }
                else {
                  showPrinterList();
                }
                break;
            case R.id.btnPrint:
               chkBluetoothConn();
                break;

            case R.id.btnUnPair:
                dialogUtils.showYesNoDialog(DLG_UNPAIR,"UnPair this Device?", new Bundle());
                break;


        }
    }

    Dialog dialog;
    public void showLogin(){
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_login);
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
               validate(value);
            }
        });

        btn =  (Button)dialog.findViewById(R.id.dlg_btn_no);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    private void validate(String value){
        String password = prefs.getData(ADMIN_PASSWORD,ADMIN_DEFAULT_PASS);
        if(password.equals(value)){
            findViewById(R.id.pnl_admin_settings).setVisibility(View.VISIBLE);
            findViewById(R.id.pnl_admin_login).setVisibility(View.GONE);
            dialog.dismiss();
        }
        else {
            DialogUtils dlg = new DialogUtils(this);
            dlg.showOKDialog("Wrong Password");
        }

    }

    private void init(){
        Switch switch1 = (Switch)findViewById(R.id.swtgps);
        switch1.setChecked(prefs.getData(GPS_LOGGING_ENABLED,true));
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               prefs.setData(GPS_LOGGING_ENABLED,isChecked);
                String message = isChecked?"Enabled":"Disabled";
               dialogUtils.showOKDialog("GPS LOGGING: "+message);
            }
        });

       Switch switch2 = (Switch)findViewById(R.id.swtmrStub);
        switch2.setChecked(prefs.getData(PRINT_STUB_ENABLED,true));
        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefs.setData(PRINT_STUB_ENABLED,isChecked);
                String message = isChecked?"Enabled":"Disabled";

                dialogUtils.showOKDialog("MR PRINT STUB: "+message);
            }
        });

        Switch switch3 = (Switch)findViewById(R.id.swteod);
        boolean isEODEnabled =prefs.getData(PRINT_EOD_ENABLED,true);
        if(isEODEnabled){
            findViewById(R.id.pnl_eod).setVisibility(View.VISIBLE);
        }
        else {
            findViewById(R.id.pnl_eod).setVisibility(View.GONE);
        }
        switch3.setChecked(prefs.getData(PRINT_EOD_ENABLED,true));
        switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefs.setData(PRINT_EOD_ENABLED,isChecked);
                String message = isChecked?"Enabled":"Disabled";

                dialogUtils.showOKDialog("EOD PRINTING : "+message);
                if(isChecked){
                    findViewById(R.id.pnl_eod).setVisibility(View.VISIBLE);
                }
                else {
                    findViewById(R.id.pnl_eod).setVisibility(View.GONE);
                }
            }
        });

        Button  btn = (Button)findViewById(R.id.btnChangePass);
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                loadChangePass();
            }
        });

        final EditText editText = (EditText) findViewById(R.id.txtEODcount);
        MRUDao mruDao = new MRUDao(this);
       int val = mruDao.countRecords();
        editText.setText(String.valueOf(prefs.getData(PRINT_EOD_COUNT,val)));
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String value = v.getText().toString();
                    if(value.equals("")){
                        dialogUtils.showOKDialog("Can't be empty");

                    }
                    else {
                        prefs.setData(PRINT_EOD_COUNT,Integer.parseInt(value));
                        dialogUtils.showOKDialog("EOD Count Changed Successfully!");
                    }

                    handled = true;
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN);
                }

                return handled;
            }
        });
    }

    private void loadChangePass(){
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        startActivityForResult(intent,STAT_CHANGE_PASS);

    }



    /**
     * check if bluetooth is turn on if not request to turn on... the result will be catch by onactivityResult method
     */
    private void bluetoothCon(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null) {
            if (!mBluetoothAdapter.isEnabled()) {
                // Bluetooth is not enable :)
                //request to turn on
                isBluetoothOn = false;
                btnPair.setText("Turn On Bluetooth");
            }
            else {
                isBluetoothOn = true;
                boolean isFound = false;
                String btName = prefs.getData(BTNAME,"");
                String btMac = prefs.getData(BTADDRESS,"");

                BluetoothDevice btDevice =   btHelper.getBluetoothDevice(btMac);



                if(btDevice!=null){
                    btnPair.setVisibility(View.GONE);
                    btnUnPair.setVisibility(View.VISIBLE);
                    btnUnPair.setText(btName+"\n"+btMac);
                }else {
                    btnPair.setText("Pair to Bluetooth Printer");
                }

            } //end if
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_BLUETOOTH){
            if(resultCode ==  Activity.RESULT_OK){
               bluetoothCon();
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void dialog_confirm(int dialog_id, Bundle params) {
        switch (dialog_id){
            case DLG_UNPAIR:
                String btAddress = prefs.getData(BTADDRESS);
                BluetoothDevice btDevice =  btHelper.getBluetoothDevice(btAddress);

                if(btDevice!=null){
                    btHelper.unpairDevice(btDevice);
                }

                break;
        }
    }

    @Override
    public void dialog_cancel(int dialog_id, Bundle params) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        bluetoothCon();
    }



    private void showPrinterList(){
        // Create an instance of the dialog fragment and show it
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        PrinterConnectionDialog printerConnectionDialog = (PrinterConnectionDialog)
                getFragmentManager().findFragmentByTag(DIALOG_PRINTER_CONNECT_TAG);

        if (printerConnectionDialog == null) {
            printerConnectionDialog = new PrinterConnectionDialog();
            printerConnectionDialog.show(ft, DIALOG_PRINTER_CONNECT_TAG);
        }


    }

    @Override
    public void bluetoothEventChange(BluetoothHelper.BluetoothHelperEvent event) {
        switch (event) {
            case NOT_ENABLED:
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_BLUETOOTH);
                break;
            case NOT_SUPPORTED:
                // if there is no bluetooth
                break;
            case CONNECTION_FAILED:
                break;
            case CONNECTION_STABLISHED:

                break;
        }
    }

    @Override
    public void bluetoothConnectionStart(ConnectThread connection) {

    }

    @Override
    public void bluetoothSelected(BluetoothDevice bluetoothDevice) {
       prefs.setData(BTNAME,bluetoothDevice.getName());
        prefs.setData(BTADDRESS,bluetoothDevice.getAddress());





            String btName = bluetoothDevice.getName();
            String btMac = bluetoothDevice.getAddress();

            if(!Utils.isNotEmpty(btName)){
                btName="";
            }
            prefs.setData(BTNAME,btName);
            prefs.setData(BTADDRESS,btMac);

        if(bluetoothDevice.getBondState()!= BluetoothDevice.BOND_BONDED){
            btHelper.pairDevice(bluetoothDevice);
        } else if(bluetoothDevice.getBondState()== BluetoothDevice.BOND_BONDED){
            btnPair.setVisibility(View.GONE);
            btnUnPair.setVisibility(View.VISIBLE);
            btnUnPair.setText(btName+"\n"+btMac);
        }


    }


    @Override
    protected void onDestroy() {
        BluetoothHelper.instance().destroy();
        unregisterReceiver(mPairReceiver);
        super.onDestroy();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.statusmenu, menu);
        return true;
    }


    private void chkBluetoothConn(){
        BluetoothAdapter   BTAdapter = BluetoothAdapter.getDefaultAdapter();
        if(BTAdapter == null){
            dialogUtils.showOKDialog("BLUETOOTH NOT SUPPORTED","Your phone " +
                    "does not support bluetooth");
        }else {
            if (!BTAdapter.isEnabled()) {
                // Bluetooth is not enable :)
                Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBT, REQUEST_BLUETOOTH);
            }
            else {
                String btAddress = prefs.getData(BTADDRESS);
                BluetoothDevice btDevice =  btHelper.getBluetoothDevice(btAddress);
                if(btDevice!=null){
                   btHelper.connectTo(btDevice);
                    printEODReport();

                }
                else {
                    dialogUtils.showOKDialog("Please setup a BLUETOOTH PRINTER in Settings before printing");
                }
                //
            }
        }
    }

    private void printEODReport(){
        MeterReadingDao mtrDao = new MeterReadingDao(this);
        ArrayList<MeterPrint> arry = mtrDao.getEODReport();
        PrintPage printPage = new PrintPage(this,this);
        printPage.printEOD(arry);
    }



    private final BroadcastReceiver mPairReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final int state 		= intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                final int prevState	= intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);
                Log.i("Test","Previous State"+ prevState);
                if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
                    //showToast("Paired");
                  String btMac =  prefs.getData(BTADDRESS);
                  String btName =   prefs.getData(BTNAME);
                    btnPair.setVisibility(View.GONE);
                    btnUnPair.setVisibility(View.VISIBLE);
                    btnUnPair.setText(btName+"\n"+btMac);
                } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED){
                   // showToast("Unpaired");
                    //reset save variable after the device is succesfully unpained
                    prefs.setData(BTADDRESS,"");
                    prefs.setData(BTNAME,"");
                    btnPair.setVisibility(View.VISIBLE);
                    btnUnPair.setVisibility(View.GONE);
                } else if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_NONE) {
                    //showToast("Paired");
                    String btMac =  prefs.getData(BTADDRESS);
                    String btName =   prefs.getData(BTNAME);
                    btnPair.setVisibility(View.GONE);
                    btnUnPair.setVisibility(View.VISIBLE);
                    btnUnPair.setText(btName+"\n"+btMac);
                }


            }
        }
    };

    @Override
    public void onPrintPageResult(String meterPrintPage) {

        btHelper.sendData(meterPrintPage.getBytes());
    }

    @Override
    public void onPrintPageAndMRStub(String meterPrintPage, String mrStubPage) {

    }
}
