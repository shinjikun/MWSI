package com.indra.rover.mwsi.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.indra.rover.mwsi.BuildConfig;
import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.utils.Constants;
import com.indra.rover.mwsi.utils.FileParser;
import com.indra.rover.mwsi.utils.FileUtils;
import com.indra.rover.mwsi.utils.PreferenceKeys;

import java.io.File;

public class SplashActivity extends AppCompatActivity  implements Constants,
        FileParser.DownloadListener {

    TextView txtUpdates;
    PreferenceKeys prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init();

        //check first if there is downloaded files from DS that needs to insert on db
        boolean mhas_update = prefs.getData(HAS_ROVER_UPDATE,true);
        boolean mhas_db_update = prefs.getData(HAS_ROVER_DBUPDATE,false);

        if(mhas_db_update){

            parseRFiles();
        }
        else if(mhas_update){
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




    }

    private void loadMainScreen(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void parseFiles(){
         String dir = android.os.Environment.getExternalStorageDirectory()+"/"+getPackageName()+"/uploads";
         File parentDir = new File(dir);
         File[] files = parentDir.listFiles();
         FileParser fileParser = new FileParser(this);
         fileParser.setListener(this);
         fileParser.execute(files);
    }

    private void parseRFiles(){
        String dir = android.os.Environment.getExternalStorageDirectory()+"/"+getPackageName()+"/db";
        File parentDir = new File(dir);
        File[] files = parentDir.listFiles();
        FileParser fileParser = new FileParser(this,true);
        fileParser.setListener(this);
        fileParser.execute(files);
    }

    /**
     * initialize variables
     * action that must execute first before loading/running the app
     */
    private void init(){
        new FileUtils(this);
        prefs = PreferenceKeys.getInstance(this);
        TextView txt =  (TextView)findViewById(R.id.txtVersion);
        String versionCode = "ver "+ BuildConfig.VERSION_NAME;
        txt.setText(versionCode);
        txtUpdates = (TextView)findViewById(R.id.txtUpdates);
    }


    @Override
    public void onPostDownloadResult(boolean status,boolean isResourceFile) {

        if(isResourceFile){
            prefs.setData(HAS_ROVER_DBUPDATE,false);
            boolean mhas_update = prefs.getData(HAS_ROVER_UPDATE,false);
            if(mhas_update){
                parseFiles();
            }else {
                txtUpdates.setText("");
                loadMainScreen();
            }
        }
        else {
            prefs.setData(HAS_ROVER_UPDATE,false);
            txtUpdates.setText("");
            loadMainScreen();
        }



    }

    @Override
    public void onPreDownloadResult(boolean isResourceFile) {
        if(isResourceFile){
            txtUpdates.setText("Updating Resource Tables\nPlease Wait...");
        }
        else {
            txtUpdates.setText("Updating  Please Wait...");
        }

    }
}
