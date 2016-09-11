package com.indra.rover.mwsi.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.utils.Constants;
import com.indra.rover.mwsi.utils.FileParser;
import com.indra.rover.mwsi.utils.FileUtils;
import com.indra.rover.mwsi.utils.PreferenceKeys;

import java.io.File;

public class SplashActivity extends AppCompatActivity  implements Constants,
        FileParser.DownloadListener {

    PreferenceKeys prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        prefs = PreferenceKeys.getInstance(this);
        init();

        //check first if there is downloaded files from DS that needs to insert on db
        boolean mhas_update = prefs.getData(HAS_ROVER_UPDATE,true);
        parseFiles();
        /*
        if(mhas_update){

            parseFiles();
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
        */


    }

    private void loadMainScreen(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    File[] files;
    int ctr =0;
    private void parseFiles(){
         String dir = android.os.Environment.getExternalStorageDirectory()+"/"+getPackageName()+"/uploads";
         File parentDir = new File(dir);
         files = parentDir.listFiles();
         FileParser fileParser = new FileParser(this);
        fileParser.setListener(this);
         fileParser.execute(files);
    }

    /**
     * initialize method
     * action that must execute first before loading/running the app
     */
    private void init(){
        FileUtils fileUtils = new FileUtils(this);
    }


    @Override
    public void onPostDownloadResult(boolean status) {
        prefs.setData(HAS_ROVER_UPDATE,false);
        loadMainScreen();

    }
}
