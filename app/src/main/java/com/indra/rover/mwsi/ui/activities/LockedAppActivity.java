package com.indra.rover.mwsi.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.data.db.ConnectDao;
import com.indra.rover.mwsi.utils.Constants;
import com.indra.rover.mwsi.utils.FileUploader;
import com.indra.rover.mwsi.utils.PreferenceKeys;
import com.indra.rover.mwsi.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.List;

public class LockedAppActivity extends AppCompatActivity implements Constants,FileUploader.UploadListener {

    ImageView img;
    TextView txtTitle,txtSubTitle,txtFiles;
    PreferenceKeys prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        img = (ImageView)findViewById(R.id.img);
        txtTitle = (TextView)findViewById(R.id.txtTitle);
        txtSubTitle = (TextView)findViewById(R.id.txtSubTitle);
        txtFiles =  (TextView)findViewById(R.id.txtFiles);
        prefs = new PreferenceKeys(this);
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



    private void extractDB(){
        try {

            File    contentDir=new File(android.os.Environment.getExternalStorageDirectory(),getPackageName()+"/dbdump");

            if (contentDir.canWrite()) {
                String currentDBPath = "/data/data/" + getPackageName() + "/databases/MCFSRNB.db";
                String backupDBPath = "db-"+ Utils.getCurrentDate1()+".db";
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
            e.printStackTrace();
        }


    }

    private void downloadAction(String status, Bundle b){
        if(status.equals("started")){
            txtTitle.setText(getResources().getText(R.string.lock_title_inprogress));
           fileUploader();
        } else if(status.equals("ended")){

            prefs.setData(Constants.APP_STATUS,"UPLOADED");
            txtTitle.setText(getResources().getText(R.string.lock_title_completed));
            txtSubTitle.setText("");
            txtFiles.setText("");
            setDrawable(R.drawable.ic_completed);
            prefs.setData(HAS_ROVER_UPDATE,false);
        }
    }

    private void uploadAction(String status, Bundle b){
        if(status.equals("started")){
            txtTitle.setText(getResources().getText(R.string.lock_title_inprogress));
        }
         if(status.equals("ended")){
            String appStatus =   prefs.getData(Constants.APP_STATUS,"DOWNLOADED");
            if(appStatus.equals("DOWNLOADED")|| appStatus.equals("UPLOADED")){
                txtTitle.setText(getResources().getText(R.string.lock_title_completed));
                txtSubTitle.setText("");
                txtFiles.setText("");
                setDrawable(R.drawable.ic_completed);
                prefs.setData(Constants.APP_STATUS,"DOWNLOADED");
                prefs.setData(HAS_ROVER_UPDATE,true);
                resetAppStatus();
            }
            else if(appStatus.equals("MODIFIED")){
                txtTitle.setText("Halted");
                txtSubTitle.setText("Can't Load New MRU.\n There  are still unread meters");
                txtFiles.setText("");
                setDrawable(R.drawable.ic_error);

                prefs.setData(HAS_ROVER_UPDATE,false);
            }
             else if(appStatus.equals("ALL READ")){
                txtTitle.setText("Halted");
                txtSubTitle.setText("Can't Load New MRU.\n Upload your Reading to DS");
                txtFiles.setText("");
                setDrawable(R.drawable.ic_error);
                prefs.setData(HAS_ROVER_UPDATE,false);
            }


        }
    }

    private void resetAppStatus(){
        prefs.setData(READ_START_TIME,"00:00:00");
        prefs.setData(READ_END_TIME,"00:00:00");
        prefs.setData(IS_FIRST_RDG,false);
        prefs.setData(IS_END_RDG,false);
        prefs.setData(APP_STATUS,"DOWNLOADED");
    }

    private void updatedbAction(String status, Bundle b){
        if(status.equals("started")){
            txtTitle.setText(getResources().getText(R.string.lock_title_inprogress));
        }
        if(status.equals("ended")){
            txtTitle.setText(getResources().getText(R.string.lock_title_completed));
            txtSubTitle.setText("");
            txtFiles.setText("");
            prefs.setData(HAS_ROVER_DBUPDATE,true);
            setDrawable(R.drawable.ic_completed);
        }
    }

    private void pulldbAction(String status , Bundle b){
        if(status.equals("started")){
            txtTitle.setText(getResources().getText(R.string.lock_title_inprogress));
            extractDB();
        } else if(status.equals("ended")){

            txtTitle.setText(getResources().getText(R.string.lock_title_completed));
            txtSubTitle.setText("");
            txtFiles.setText("");
            setDrawable(R.drawable.ic_completed);

        }
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
                downloadAction(status,b);
            }
            else if(action.equals("upload")){
                setDrawable(R.drawable.ic_download);
                txtSubTitle.setText(String.valueOf("Downloading files to DS"));
                uploadAction(status,b);
            }
            else if(action.equals("updatedb")){
                setDrawable(R.drawable.ic_db_update);
                txtSubTitle.setText(String.valueOf("Fetching DB dump file to DS"));
                updatedbAction(status,b);
            }
            else if(action.equals("pulldb")){
                setDrawable(R.drawable.ic_db_update);
                txtSubTitle.setText(String.valueOf("Dumping Rover DB to DS"));
                pulldbAction(status,b);
            }



        }
    };

    @Override
    public void onPostUploadResult(boolean status) {

    }

    private void fileUploader(){

            FileUploader fileUploader = new FileUploader(this);
            fileUploader.setListener(this);
            ConnectDao mr = new ConnectDao(this);
            List<String> arry =  mr.fetchMRUs();
            String[] lst = new String[arry.size()];
            lst = arry.toArray(lst);
            fileUploader.execute(lst);

    }
}
