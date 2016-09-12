package com.indra.rover.mwsi.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.data.db.MeterReadingDao;
import com.indra.rover.mwsi.utils.DialogUtils;
import com.indra.rover.mwsi.utils.FileUploader;
import com.indra.rover.mwsi.utils.GPSTracker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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
               extractDB();
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




    private void extractDB(){
        try {
            File    contentDir=new File(android.os.Environment.getExternalStorageDirectory(),getPackageName()+"/dbdump");

            if (contentDir.canWrite()) {
                String currentDBPath = "/data/data/" + getPackageName() + "/databases/MCFSRNB";
                String backupDBPath = "backupname.db";
                File currentDB = new File(currentDBPath);
                File backupDB = new File(contentDir, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {

        }


    }


    public void test(){
        FileUploader fileUploader = new FileUploader(this);
        fileUploader.setListener(this);
        MeterReadingDao mr = new MeterReadingDao(this);
        List<String> arry =  mr.fetchMRUs();
        String[] lst = new String[arry.size()];
        lst = arry.toArray(lst);
        fileUploader.execute(lst);
    }

    @Override
    public void onPostUploadResult(boolean status) {

    }
}
