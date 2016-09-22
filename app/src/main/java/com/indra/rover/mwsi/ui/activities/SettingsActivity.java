package com.indra.rover.mwsi.ui.activities;


import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;

import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.utils.Constants;
import com.indra.rover.mwsi.utils.DialogUtils;
import com.indra.rover.mwsi.utils.PreferenceKeys;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener,Constants {

    PreferenceKeys prefs;
    DialogUtils dialogUtils;
    Button btnPair;
    final int STAT_CHANGE_PASS =888;
    final int REQUEST_BLUETOOTH = 700;
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
                btPair();
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
        switch3.setChecked(prefs.getData(PRINT_EOD_ENABLED,true));
        switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefs.setData(PRINT_EOD_ENABLED,isChecked);
                String message = isChecked?"Enabled":"Disabled";

                dialogUtils.showOKDialog("EOD PRINTING : "+message);
            }
        });

        Button  btn = (Button)findViewById(R.id.btnChangePass);
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                loadChangePass();
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
    private void btPair(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                // Bluetooth is not enable :)
                //request to turn on
                Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBT, REQUEST_BLUETOOTH);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_BLUETOOTH){
            if(resultCode == RESULT_OK){
                
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }
}
