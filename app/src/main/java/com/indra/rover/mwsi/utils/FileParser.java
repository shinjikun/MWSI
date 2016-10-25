package com.indra.rover.mwsi.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.indra.rover.mwsi.data.db.ConnectDao;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileParser extends AsyncTask<File,Integer,String> {

    private DownloadListener listener;
    private ConnectDao mRDao;
    private boolean isResourceFile;
    Context context;
    public FileParser(Context context){
        mRDao = new ConnectDao(context);
        this.isResourceFile = false;
        this.context =context;
    }

    public FileParser(Context context,boolean isResourceFile){
        mRDao = new ConnectDao(context);
        this.context = context;
        this.isResourceFile = isResourceFile;
    }

    public void setListener(DownloadListener listener){
        this.listener =listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onPreDownloadResult(isResourceFile);

    }

    @Override
    protected String doInBackground(File... files) {
        for (File file : files) {
            if (!file.isDirectory()) {
                if(file.getName().endsWith(".txt")){
                    if(isResourceFile){
                        parseRFile(file);
                    } else
                        parseFile(file);
                    //remove the parsed file in directory
                  //  file.delete();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        listener.onPostDownloadResult(true,isResourceFile);

    }


    private void parseFile(File file){
        String fileName =  file.getName();
        if(fileName.startsWith("BK")){
            parseBKFile(file);
        }
        //MRU file
        else if(fileName.startsWith("BNFO"))  {
           parseMRUFile(file);
        }

    }

    private void parseBKFile(File file){
        try {
            CSVReader reader = new CSVReader(new FileReader(file), '|', '\"');
            String [] record;
            //truncate table
            mRDao.truncateTables();
            //delete files recursively
            deleteFiles();
            while ((record = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line

                mRDao.insertTUploadData(record);
                mRDao.insertTDLData(record);
                mRDao.insertTCurrRDGData(record);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void parseRFile(File file){
        try{
            //get the file name of the file this will be the tablename
            String tableName = file.getName();
            //remove the extension from the fileNmae
            tableName = tableName.substring(0, tableName.lastIndexOf('.'));
            //upper case the tableName

            CSVReader reader = new CSVReader(new FileReader(file), '|', '\"');

            String [] record;
            String [] header=null;
            int i=0;
            while ((record = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                if(i==0){
                    header = record;
                }
                else {
                 mRDao.insertResourceData(tableName,header,record);
                }
                i++;


            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }


    private void parseMRUFile(File file){
        try {

            CSVReader reader = new CSVReader(new FileReader(file), '|', '\"');
            //truncate T_MRU table
            mRDao.truncateMRUTable();

            String [] record;
            while ((record = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                mRDao.insertTMRU(record);
            }


        }
        catch (IOException e) {

        }
    }

    public interface DownloadListener {
        void onPostDownloadResult(boolean status,boolean isResourceFile);
        void onPreDownloadResult(boolean  isResourceFile);
    }

    private void deleteFiles(String  path){
        File    contentDir=new File(android.os.Environment.getExternalStorageDirectory()
                ,context.getPackageName()+path);
        if(contentDir.exists()){
            if (contentDir.isDirectory())
                for (File child : contentDir.listFiles())
                    child.delete();
        }
    }
    private void deleteFiles(){
        deleteFiles("/uploads/signatures");
        deleteFiles("/uploads/images");
    }


}
