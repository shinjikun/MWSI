package com.indra.rover.mwsi.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.indra.rover.mwsi.R;

public class LockedAppActivity extends AppCompatActivity {

    ImageView img;
    TextView txtTitle,txtSubTitle,txtFiles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        img = (ImageView)findViewById(R.id.img);
        txtTitle = (TextView)findViewById(R.id.txtTitle);
        txtSubTitle = (TextView)findViewById(R.id.txtSubTitle);
        txtFiles =  (TextView)findViewById(R.id.txtFiles);
    }


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter deviceAttachedFilter = new IntentFilter("com.indra.rover.mwsi.LOCKED");
        registerReceiver(broadcastReceiver, deviceAttachedFilter);
    }

    public void setDrawable(int drawable){
        img.setImageDrawable(ContextCompat.getDrawable(this, drawable));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    // Add this inside your class
    BroadcastReceiver broadcastReceiver =  new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            Bundle b = intent.getExtras();

            String action = b.getString("action");
            String status = b.getString("status");
            if(action.equals("download")){
                setDrawable(R.drawable.ic_upload);
                txtSubTitle.setText(String.valueOf("Uploading files to DS"));
            }
            else if(action.equals("upload")){
                setDrawable(R.drawable.ic_download);
                txtSubTitle.setText(String.valueOf("Downloading files to DS"));
            }
            else if(action.equals("db_update")){
                setDrawable(R.drawable.ic_db_update);
                txtSubTitle.setText(String.valueOf("Fetching DB dump file to DS"));
            }

            if(status.equals("started")){
                txtTitle.setText(getResources().getText(R.string.lock_title_inprogress));
            }
            else if(status.equals("ended")){
                txtTitle.setText(getResources().getText(R.string.lock_title_completed));
                txtSubTitle.setText("");
                txtFiles.setText("");
                setDrawable(R.drawable.ic_completed);
            }

        }
    };
}
