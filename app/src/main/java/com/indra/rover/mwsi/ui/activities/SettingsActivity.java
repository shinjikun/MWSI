package com.indra.rover.mwsi.ui.activities;


import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.utils.Constants;
import com.indra.rover.mwsi.utils.DialogUtils;
import com.indra.rover.mwsi.utils.PreferenceKeys;

import java.util.ArrayList;
import java.util.Set;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener,
        Constants,DialogUtils.DialogListener {

    PreferenceKeys prefs;
    DialogUtils dialogUtils;
    Button btnPair,btnUnPair;
    final int STAT_CHANGE_PASS =888;
    final int REQUEST_BLUETOOTH = 700;
    boolean isBluetoothOn = true;
    ArrayList<BPrinters> arryPrinters;
    ImageButton btnPrint;

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
        prefs = PreferenceKeys.getInstance(this);
        dialogUtils = new DialogUtils(this);
        btnPair =  (Button)findViewById(R.id.btnPair);
        btnPair.setOnClickListener(this);
        btnUnPair =  (Button)findViewById(R.id.btnUnPair);
        btnUnPair.setOnClickListener(this);
        btnPrint = (ImageButton)findViewById(R.id.btnPrint);
        btnPrint.setOnClickListener(this);
        init();
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
                  dlg();
                }
                break;
            case R.id.btnPrint:

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
        editText.setText(prefs.getData(PRINT_EOD_COUNT,"30"));
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
                        prefs.setData(PRINT_EOD_COUNT,value);
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
                String btName = prefs.getData(BLUEDNAME,"");
                String btMac = prefs.getData(BLUEMAC,"");
                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                for (BluetoothDevice device : pairedDevices) {

                    if(device.getBondState() == BluetoothDevice.BOND_BONDED){
                        if(btMac.equals(device.getAddress())){

                            btName = device.getName();
                            btMac = device.getAddress();
                            prefs.setData(BLUEDNAME,btName);
                            prefs.setData(BLUEMAC,btMac);
                            isFound = true;
                            break;
                        }

                    }
                }//end of for
                if(isFound){
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

    }

    @Override
    public void dialog_cancel(int dialog_id, Bundle params) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        bluetoothCon();
    }

    private void dlg(){
        arryPrinters = new ArrayList<>();
        BluetoothAdapter   bTAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bTAdapter != null){
            Set<BluetoothDevice> pairedDevices = bTAdapter.getBondedDevices();
            for (BluetoothDevice device : pairedDevices) {
                BPrinters bPrinters = new BPrinters(device.getName(),device.getAddress());
                arryPrinters.add(bPrinters);
            }
        }

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setTitle("Select One to Pair:");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.select_dialog_singlechoice);
        for(int i=0;i<arryPrinters.size();i++){
           arrayAdapter.add(arryPrinters.get(i).getName());
        }

        builderSingle.setNegativeButton(
                "cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(
                                SettingsActivity.this);
                        builderInner.setMessage(strName);
                        builderInner.setTitle("Your Selected Item is");
                        builderInner.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        dialog.dismiss();
                                    }
                                });
                        builderInner.show();
                    }
                });
        builderSingle.show();
    }


    private class BPrinters{
        String name;
        String macAddress;
        public BPrinters(String name,String macAddress){
            this.macAddress = macAddress;
            this.name = name;
        }

        public String getMacAddress() {
            return macAddress;
        }

        public String getName() {
            return name;
        }
    }
}
