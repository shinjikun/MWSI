package com.indra.rover.mwsi.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.indra.rover.mwsi.R;

public class LockedAppActivity extends AppCompatActivity {

    String TAG="rai";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
    }


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter deviceAttachedFilter = new IntentFilter();
        deviceAttachedFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        deviceAttachedFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(mUsbReceiver, deviceAttachedFilter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mUsbReceiver);
    }

    BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "broadcastReceiver action:" + action.toString() + " taskID=" + getTaskId() + " this=" + this);

            if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (device != null) {
                    Log.d(TAG, "device attached");
                    Toast.makeText(context, "Device disconnected", Toast.LENGTH_LONG).show();
                }
            } else if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (device != null) {
                    Log.d(TAG, "device detached");
                    Toast.makeText(context, "Device attached", Toast.LENGTH_LONG).show();
                }
            }
        }
    };
}
