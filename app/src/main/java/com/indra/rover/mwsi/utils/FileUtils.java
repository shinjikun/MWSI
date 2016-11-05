package com.indra.rover.mwsi.utils;

import android.content.Context;

import java.io.File;


public class FileUtils {



    public FileUtils(Context context){
        //create root content folder
       File contentDir=new File(android.os.Environment.getExternalStorageDirectory(),context.getPackageName());
        if(!contentDir.exists()){
            contentDir.mkdirs();
        }
        //create directory for uploads files
      File  uploadDir = new File(contentDir,"uploads");
        if(!uploadDir.exists()){
            uploadDir.mkdirs();
        }
        //create directory for downloaded files
       File  downloadDir = new File(contentDir,"downloads");
        if(!downloadDir.exists()){
            downloadDir.mkdirs();
        }
      String str =  PreferenceKeys.getInstance(context).getData(Constants.contentFolder,"");
        if(Utils.isNotEmpty(str)){
            File dataCont =  new File(uploadDir,str);
            if(!dataCont.exists()){
                dataCont.mkdir();
            }

        //this will contain the capture images of reported OCs
        File imagesDir = new File(dataCont,"images");
        if(!imagesDir.exists()){
            imagesDir.mkdirs();
        }

        //this will contain the signature of customer who received the receipt of meter reading
        File signatures = new File(dataCont,"signatures");
        if(!signatures.exists()){
            signatures.mkdirs();
        }
    }



        //create directory for database files
      File  db = new File(contentDir,"db");
        if(!db.exists()){
            db.mkdirs();

        }
        //create directory for db dump files
        db = new File(contentDir,"dbdump");
        if(!db.exists()){
            db.mkdirs();
        }
        //create directory for datadump
        db = new File(contentDir,"datadump");
        if(!db.exists()){
            db.mkdir();
        }
    }
}
