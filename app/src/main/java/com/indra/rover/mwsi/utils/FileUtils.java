package com.indra.rover.mwsi.utils;

import android.content.Context;

import java.io.File;

/**
 * Created by Indra on 9/2/2016.
 */
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
        //this will contain the capture images of reported OCs
        File imagesDir = new File(downloadDir,"images");
        if(!imagesDir.exists()){
            imagesDir.mkdirs();
        }

        //this will contain the signature of customer who received the receipt of meter reading
        File signatures = new File(downloadDir,"signatures");
        if(!signatures.exists()){
            signatures.mkdirs();
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

    }
}
