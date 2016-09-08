package com.indra.rover.mwsi.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.indra.rover.mwsi.data.db.MRUDao;
import com.indra.rover.mwsi.data.db.MeterReadingDao;
import com.indra.rover.mwsi.data.pojo.T_Upload;
import com.indra.rover.mwsi.data.pojo.meter_reading.misc.InstallMisc;
import com.indra.rover.mwsi.data.pojo.MeterReading;
import com.indra.rover.mwsi.data.pojo.CustomerInfo;
import com.indra.rover.mwsi.data.pojo.MRU;
import com.indra.rover.mwsi.data.pojo.meter_reading.misc.PreviousData;
import com.indra.rover.mwsi.data.pojo.meter_reading.misc.ProRate;
import com.indra.rover.mwsi.data.pojo.T_Download_Info;
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
    MeterReadingDao mRDao;
    public FileParser(Context context){
        this.context = context;
        mruDao = new MRUDao(context);
        mRDao = new MeterReadingDao(context);
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
            int i =0;
            while ((record = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                T_Download_Info t_download_info = new T_Download_Info(record);
                MeterReading currentReading = new MeterReading();
                CustomerInfo customerInfo = new CustomerInfo();

                //mru id
                String mru = record[0];
                currentReading.setMru(mru);


                //customer account number
                String acct_number =  record[2];
                customerInfo.setAccn(acct_number);
                currentReading.setAcctnum(acct_number);

                //customer account name
                customerInfo.setCname(record[3]);

                //customer address
                customerInfo.setAddress(record[4]);
                t_download_info.setCustomer(customerInfo);

                currentReading.setMeterno(record[9]);
                currentReading.setCrdocno(record[10]);


                //Installation Misellanious
                InstallMisc installMisc = new InstallMisc(record);
                t_download_info.setInstallMisc(installMisc);
                currentReading.setInstallMisc(installMisc);

                //previous reading details
                PreviousData previousData = new PreviousData(record);
                t_download_info.setPrevReading(previousData);
                T_Upload t_upload = new T_Upload(record);
                mRDao.insertTUploadData(t_upload,i++);

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
                mru.setBc_code(record[1]);
                mru.setReader_code(record[2]);
                mru.setReader_name(record[3]);
                mru.setReading_date(record[4]);
                mru.setDue_date(record[5]);
                mru.setKam_mru(Short.parseShort(record[6]));
                mru.setMax_seq_no(record[7]);
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
                if(!record[13].isEmpty())
                    mru.setUndelivered(Integer.parseInt(record[13]));
                mruDao.insertMRU(mru);


            }


        }
        catch (IOException e) {

        }
    }


}
