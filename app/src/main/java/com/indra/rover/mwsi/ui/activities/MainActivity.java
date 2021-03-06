package com.indra.rover.mwsi.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.utils.DialogUtils;
import com.indra.rover.mwsi.utils.FileUploader;
import com.indra.rover.mwsi.utils.GPSTracker;




public class MainActivity extends AppCompatActivity implements View.OnClickListener ,
        DialogUtils.DialogListener, FileUploader.UploadListener{

    DialogUtils mDialogUtils;
    private final int DLG_CLOSE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mDialogUtils = new DialogUtils(this);
        mDialogUtils.setListener(this);
        GPSTracker gpsTracker = new GPSTracker(this);

        if(!gpsTracker.canGetLocation()){
            gpsTracker.showSettingsAlert();
        }

        TextView txt = (TextView)findViewById(R.id.txtDeviceID);
        txt.setText(Build.SERIAL);

    }




    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent;
        switch(id){
            case R.id.btnExit:
             mDialogUtils.showYesNoDialog(DLG_CLOSE,"Close the app?", new Bundle());
                break;

            case R.id.btnStatus:
                intent = new Intent(this, StatusActivity.class);
                startActivity(intent);
                break;
            case R.id.btnMeterReading:
                intent = new Intent(this, MRUListActivity.class);
                startActivity(intent);
                break;
            case R.id.btnSettings:
            //    Log.i("Test","android.os.Build.SERIAL: " + Build.SERIAL);
                intent =  new Intent(this,SettingsActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void dialog_confirm(int dialog_id, Bundle params) {
        switch (dialog_id){
            case DLG_CLOSE:
                finish();
                break;
        }
    }

    @Override
    public void dialog_cancel(int dialog_id, Bundle params) {

    }

    @Override
    public void onPostUploadResult(boolean status) {

    }

    @Override
    public void onBackPressed() {
        mDialogUtils.showYesNoDialog(DLG_CLOSE,"Close the app?", new Bundle());
    }
}
