package com.indra.rover.mwsi.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.indra.rover.mwsi.data.db.MRUDao;
import com.indra.rover.mwsi.data.pojo.MRU;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by leonardoilagan on 04/09/2016.
 */

public class FileParser extends AsyncTask<File,Integer,String> {

    Context context;
    DownloadListener listener;
    MRUDao mruDao;

    public FileParser(Context context){
        this.context = context;
        mruDao = new MRUDao(context);
    }

    public void setListener(DownloadListener listener){
        this.listener =listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(File... files) {
        for (File file : files) {
            if (!file.isDirectory()) {
                if(file.getName().endsWith(".txt")){
                    parseFile(file);
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        listener.onPostDownloadResult(true);

    }

    public interface DownloadListener {
         void onPostDownloadResult(boolean status);

    }


    private void parseFile(File file){
        try {

            CSVReader reader = new CSVReader(new FileReader(file), '|', '\"', 1);

            String [] record;
            while ((record = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                Log.i("Test","im inserting"+record[0]);
                MRU mru = new MRU();
                mru.setId(record[0]);
                mru.setBc_code(record[1]);
                mru.setReader_code(record[2]);
                mru.setReader_name(record[3]);
                mru.setReading_date(record[4]);
                mru.setDue_date(record[5]);
                mru.setKam_mru(Short.parseShort(record[6]));
                mru.setMax_seq_no(record[7]);
                mru.setCustomer_count(Integer.parseInt(record[8]));
                mru.setActive_count(Integer.parseInt(record[9]));
                mru.setBlocked_count(Integer.parseInt(record[10]));
                mru.setRead(Integer.parseInt(record[11]));
                mru.setUnread(Integer.parseInt(record[12]));
                mru.setUndelivered(Integer.parseInt(record[13]));
                mruDao.insertMRU(mru);


            }


        }
        catch (IOException e) {

        }
    }


}
