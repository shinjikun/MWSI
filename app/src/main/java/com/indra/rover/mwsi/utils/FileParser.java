package com.indra.rover.mwsi.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import com.indra.rover.mwsi.data.db.ConnectDao;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

                    if(isResourceFile){
                        String fileName =  file.getName();
                        if(fileName.startsWith("BR")){
                            parseRFile(file, false);
                        }
                        else {
                            parseRFile(file, true);
                        }


                    } else
                        parseFile(file);
                    //remove the parsed file in directory
                    file.delete();

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

    private void parseRFile(File file, boolean isResource){
        try{
            //get the file name of the file this will be the tablename
            String tableName = file.getName();

            //remove the extension from the fileNmae
            tableName = tableName.substring(0, tableName.lastIndexOf('.'));

            if(!isResource)
                tableName="T_BILL_REPRINT";
            //upper case the tableName
            tableName = tableName.toUpperCase();
            String [] header = mRDao.getColumnNames(tableName);
            CSVReader reader = new CSVReader(new FileReader(file), '|', '\"');

            String [] record;

            //int i=0;
            while ((record = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                if(isResource){
                        mRDao.insertResourceData(tableName,header,record);

                }else {
                    mRDao.insertResourceData("T_BILL_REPRINT",header,record);
                   // mRDao.insertBillReprintData(record);
                }

              //  i++;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /*
      This is old code but im keeping this code
      What is does is inserting to a table with a given header or arrays of columns names
      array of column names are in the index 0 of the parsed file
    private void parseRFile(File file, boolean isResource){
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
                if(isResource){
                    if(i==0){
                        header = record;
                    }
                    else {
                        mRDao.insertResourceData(tableName,header,record);
                    }
                }else {
                    mRDao.insertBillReprintData(record);
                }

                i++;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    */




    private void parseMRUFile(File file){
        try {

            CSVReader reader = new CSVReader(new FileReader(file), '|', '\"');
            //truncate T_MRU table
            mRDao.truncateMRUTable();
            PreferenceKeys.getInstance(context).setData(Constants.contentFolder,"");
            String [] record;
            while ((record = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                mRDao.insertTMRU(record);
            }



        }
        catch (IOException e) {

        }finally {
            List<String> arry =       mRDao.fetchMRUs();
            if(!arry.isEmpty()){
                if(arry.size()>1){
                    String str = Build.SERIAL+"_"+Utils.getCurrentDate("MMdd");
                    PreferenceKeys.getInstance(context).setData(Constants.contentFolder,str);
                }
                else {
                    String mru =  arry.get(0);
                    PreferenceKeys.getInstance(context).setData(Constants.contentFolder,mru);
                }
            }
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
        deleteFiles("/uploads/");

    }


}
