package com.indra.rover.mwsi.utils;

import android.content.Context;

import java.io.File;

/**
 * Created by Indra on 9/2/2016.
 */
public class FileUtils {

     File contentDir;
     File uploadDir;
     File downloadDir;
     File db;


    public FileUtils(Context context){
        //create root content folder
        contentDir=new File(android.os.Environment.getExternalStorageDirectory(),context.getPackageName());
        if(!contentDir.exists()){
            contentDir.mkdirs();
        }
        //create directory for uploads files
        uploadDir = new File(contentDir,"uploads");
        if(!uploadDir.exists()){
            uploadDir.mkdirs();
        }
        //create directory for downloaded files
        downloadDir = new File(contentDir,"downloads");
        if(!downloadDir.exists()){
            downloadDir.mkdirs();
        }
        //create directory for downloaded files
        db = new File(contentDir,"db");
        if(!db.exists()){
            db.mkdirs();
        }
    }
}
