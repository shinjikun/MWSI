package com.indra.rover.mwsi.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.utils.Constants;
import com.indra.rover.mwsi.utils.FileUtils;
import com.indra.rover.mwsi.utils.GPSTracker;
import com.indra.rover.mwsi.utils.PreferenceKeys;
import com.indra.rover.mwsi.utils.Utils;

public class SplashActivity extends AppCompatActivity  implements Constants{

    PreferenceKeys prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        prefs = PreferenceKeys.getInstance(this);
        init();

        //check first if there is downloaded files from DS that needs to insert on db
        boolean mhas_update = prefs.getData(HAS_ROVER_UPDATE,false);
        if(mhas_update){
            Log.i("test","has update");
        }
        else {
            Thread startThread =  new Thread(){
                @Override
                public void run() {
                    try{
                        //Display for 3 seconds
                        sleep(3000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }finally {
                        loadMainScreen();
                    }
                }
            };
            startThread.start();
        }


    }

    private void loadMainScreen(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * initialize method
     * action that must execute first before loading/running the app
     */
    private void init(){
        FileUtils fileUtils = new FileUtils(this);
    }





}
