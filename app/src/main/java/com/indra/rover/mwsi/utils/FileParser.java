package com.indra.rover.mwsi.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.indra.rover.mwsi.data.db.MRUDao;
import com.indra.rover.mwsi.data.pojo.MRU;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by leonardoilagan on 04/09/2016.
 */

public class FileParser extends AsyncTask<String,Integer,String> {

    Context context;
    Downloadlistener listener;
    MRUDao mruDao;
    File file;
    public FileParser(File file, Context context,Downloadlistener listener){
        this.context = context;
        this.listener = listener;
        this.file = file;
        mruDao = new MRUDao(context);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        String path =  file.getAbsolutePath();
        parseFile(file);
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        listener.onPostDownloadResult(true);
    }

    public interface  Downloadlistener {
        public void onPostDownloadResult(boolean status);

    }


    private void parseFile(File file){
        try {

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
               String record[] = line.split("\\|");
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
            br.close();
        }
        catch (IOException e) {

        }
    }


}
