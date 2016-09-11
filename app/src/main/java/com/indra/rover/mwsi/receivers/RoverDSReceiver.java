package com.indra.rover.mwsi.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class RoverDSReceiver extends BroadcastReceiver {
    public RoverDSReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        if(bundle!=null){
            String action =  bundle.getString("action");
            String status = bundle.getString("status");
            if(action!=null){
                sendBroadcast(context,status,action);
            }
        }

    }

    /*
    Intent i = new Intent();
    i.setClassName("com.indra.rover.mwsi","com.indra.rover.mwsi.ui.activities.LockedAppActivity");
    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP |
    Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);

    context.startActivity(i);
    */

    private void sendBroadcast(Context context, String status, String action){

        Intent i = new Intent("com.indra.rover.mwsi.LOCKED");
        // Data you need to pass to activity
        i.putExtra("status", status);
        i.putExtra("action",action);

        context.sendBroadcast(i);

    }

    private void upload_content(Context context,String status){
        Toast.makeText(context, "Intent  "+status, Toast.LENGTH_LONG).show();
    }

    private void update_db(Context context,String status){
        Toast.makeText(context, "DB Detected. "+status, Toast.LENGTH_LONG).show();
    }


}
