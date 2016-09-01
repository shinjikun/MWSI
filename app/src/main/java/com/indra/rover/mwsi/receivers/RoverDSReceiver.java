package com.indra.rover.mwsi.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.widget.Toast;

public class RoverDSReceiver extends BroadcastReceiver {
    public RoverDSReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
            Toast.makeText(context, "Device connected", Toast.LENGTH_LONG).show();
        }
        else if(intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
            // Do your thing ...
            Toast.makeText(context, "Device disconnected", Toast.LENGTH_LONG).show();
        }

        Bundle bundle = intent.getExtras();
        if(bundle!=null){
            String action =  bundle.getString("action");
            String status = bundle.getString("status");
            if(action!=null){
                if(action.equals("download")){
                    Intent i = new Intent();
                    i.setClassName("com.indra.rover.mwsi","com.indra.rover.mwsi.ui.activities.LockedAppActivity");
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                   // download_content(context,status);
                }
                else if(action.equals("upload")){
                    upload_content(context,status);
                }
                else if(action.equals("updatedb")){
                    update_db(context,status);
                }
            }
        }

    }



    private void download_content(Context context, String status){
        Toast.makeText(context, "Intent Detected. "+status, Toast.LENGTH_LONG).show();
    }

    private void upload_content(Context context,String status){
        Toast.makeText(context, "Intent  "+status, Toast.LENGTH_LONG).show();
    }

    private void update_db(Context context,String status){
        Toast.makeText(context, "DB Detected. "+status, Toast.LENGTH_LONG).show();
    }


}
