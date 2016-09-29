package com.indra.rover.mwsi.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.indra.rover.mwsi.data.db.MRUDao;
import com.indra.rover.mwsi.data.db.ConnectDao;
import com.indra.rover.mwsi.data.pojo.MRU;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileParser extends AsyncTask<File,Integer,String> {

    Context context;
    DownloadListener listener;
    MRUDao mruDao;
    ConnectDao mRDao;
    boolean isResourceFile;
    public FileParser(Context context){
        this.context = context;
        mruDao = new MRUDao(context);
        mRDao = new ConnectDao(context);
        this.isResourceFile = false;
    }

    public FileParser(Context context,boolean isResourceFile){
        this.context = context;
        mruDao = new MRUDao(context);
        mRDao = new ConnectDao(context);
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
                    file.delete();
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
            CSVReader reader = new CSVReader(new FileReader(file), '|', '\"', 1);


            String [] record;
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

            CSVReader reader = new CSVReader(new FileReader(file), '|', '\"', 1);

            String [] record;
            while ((record = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line

                MRU mru = new MRU();
                mru.setId(record[0]);
                mru.setBc_code(record[5]);
                mru.setReader_code(record[1]);
                mru.setReader_name(record[2]);
                mru.setReading_date(record[3]);
                mru.setDue_date(record[4]);
                mru.setKam_mru(Short.parseShort(record[5]));
                mru.setMax_seq_no(record[6]);
                if(!record[8].isEmpty())
                    mru.setCustomer_count(Integer.parseInt(record[8]));
                if(!record[9].isEmpty())
                     mru.setActive_count(Integer.parseInt(record[9]));
                if(!record[10].isEmpty())
                    mru.setBlocked_count(Integer.parseInt(record[10]));
                if(!record[11].isEmpty())
                    mru.setRead(Integer.parseInt(record[11]));
                if(!record[12].isEmpty())
                    mru.setUnread(Integer.parseInt(record[12]));
                mruDao.insertMRU(mru);


            }


        }
        catch (IOException e) {

        }
    }

    public interface DownloadListener {
        void onPostDownloadResult(boolean status,boolean isResourceFile);
        void onPreDownloadResult(boolean  isResourceFile);
    }


}
