package com.indra.rover.mwsi.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.indra.rover.mwsi.data.db.MRUDao;
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
        String fileName =  file.getName();
        Log.i("Test",fileName);
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
                T_Download_Info t_download_info = new T_Download_Info();
                MeterReading currentReading = new MeterReading();
                CustomerInfo customerInfo = new CustomerInfo();

                //mru id
                String mru = record[0];
                t_download_info.setMru_id(mru);
                currentReading.setMru(mru);

                //seq number
                t_download_info.setSeq_number(record[1]);

                //customer account number
                String acct_number =  record[2];
                customerInfo.setAccn(acct_number);
                currentReading.setAcctnum(acct_number);

                //customer account name
                customerInfo.setCname(record[3]);

                //customer address
                customerInfo.setAddress(record[4]);
                t_download_info.setCustomer(customerInfo);

                //bill class
                t_download_info.setBill_class(record[5]);
                //rate type
                t_download_info.setRate_type(record[6]);
                //bulk flag
                t_download_info.setBulk_flag(record[7]);
                //account status
                t_download_info.setAcct_status(Integer.parseInt(record[8]));
                //NODIALS|
                //meter number
                t_download_info.setMeter_number(record[9]);
                currentReading.setMeterno(record[9]);
                //dlcdocno
                t_download_info.setDldocno(record[10]);
                currentReading.setCrdocno(record[10]);
                //material no
                t_download_info.setMterial_number(record[11]);
                //meter size
                t_download_info.setMeter_size(Integer.parseInt(record[12]));
                //num dials
                t_download_info.setNum_dials(Integer.parseInt(record[13]));
                //grp flag
                t_download_info.setGrp_flag(record[14]);
                //block flag
                t_download_info.setBlock_tag(record[15]);
                //disc tag flag
                t_download_info.setDisc_tag(record[16]);
                //previous reading date
                t_download_info.setPrevRDGdate(record[17]);
                //actual previous reading
                t_download_info.setActprevRDG(Integer.parseInt(record[18]));

                t_download_info.setPracflag(Short.parseShort(record[22]));
                t_download_info.setPconsavgflag(Short.parseShort(record[23]));
                t_download_info.setAve_consumpstions(Integer.parseInt(record[24]));
                t_download_info.setDpreplmtr_code(Integer.parseInt(record[25]));




                t_download_info.setNmintRDG(Integer.parseInt(record[26]));

                t_download_info.setNmConsfactor(Integer.parseInt(record[27]));
                t_download_info.setGt34factor(Integer.parseInt(record[31]));
                t_download_info.setGt34flag(Integer.parseInt(record[30]));


                t_download_info.setProrates(new ProRate(record));

                //Installation Misellanious
                InstallMisc installMisc = new InstallMisc(record);
                t_download_info.setInstallMisc(installMisc);
                currentReading.setInstallMisc(installMisc);

                //previous reading details
                PreviousData previousData = new PreviousData(record);
                t_download_info.setPrevReading(previousData);

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
