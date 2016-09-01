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
import android.widget.Toast;

import com.indra.rover.mwsi.R;

public class LockedAppActivity extends AppCompatActivity {

    String TAG="rai";
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        img = (ImageView)findViewById(R.id.img);
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
                setDrawable(R.drawable.ic_download);
            }
            else if(action.equals("upload")){
                setDrawable(R.drawable.ic_upload);
            }

        }
    };
}
