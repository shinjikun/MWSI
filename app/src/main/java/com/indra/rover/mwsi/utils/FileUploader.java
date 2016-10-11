package com.indra.rover.mwsi.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.data.db.ConnectDao;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

/**
 * Created by leonardoilagan on 11/09/2016.
 */

public class FileUploader  extends AsyncTask<String[],Integer,String> {

    private  UploadListener listener;
    private ConnectDao mrDao;
    private Context mContext;
    public FileUploader(Context context){
        mrDao = new ConnectDao(context);
        this.mContext = context;
    }

    public void setListener(UploadListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String[]... param) {

        int size = param[0].length;
        if(size!=0){
            String[] files = param[0];
            boolean isMultiBook = false;
            if(size>1)
                isMultiBook= true;
            String filename = files[0];
            generate_uploadfile(filename, isMultiBook);
            generate_fconnfile(filename, isMultiBook);
            generate_sapfile(filename,isMultiBook);
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        listener.onPostUploadResult(true);
    }

    public interface UploadListener {
        void onPostUploadResult(boolean status);

    }

    private void generate_uploadfile(String mruNo, boolean isMultiBook){

        try {
            String fileName = "BK"+mruNo.substring(mruNo.length()-4)+"U.txt";
            if(isMultiBook){
                fileName = "BKMBU.txt";
            }
            File contentDir=new File(android.os.Environment.getExternalStorageDirectory(),mContext.getPackageName()+"/downloads");
            File file = new File(contentDir, fileName);

            CSVWriter writer = new CSVWriter(new FileWriter(file), '|',CSVWriter.NO_QUOTE_CHARACTER);

           // String[] column_names =    mContext.getResources().getStringArray(R.array.upload_columns);
          //  writer.writeNext(column_names);
            List<String[]> mete = mrDao.query_upload(mruNo, isMultiBook);
            int size = mete.size();
            for(int i=0;i<size;i++){
                String[] record = mete.get(i);
                writer.writeNext(record);
            }
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private void generate_fconnfile(String mruNo, boolean isMultiBook){
        try{
            String fileName = "FC"+mruNo.substring(mruNo.length()-4)+".txt";
            if(isMultiBook){
                fileName = "FCMB.txt";
            }
            File contentDir=new File(android.os.Environment.getExternalStorageDirectory(),mContext.getPackageName()+"/downloads");
            File file = new File(contentDir, fileName);
            CSVWriter writer = new CSVWriter(new FileWriter(file), '|',CSVWriter.NO_QUOTE_CHARACTER);
            //String[] column_names =    mContext.getResources().getStringArray(R.array.fc_columns);
            //writer.writeNext(column_names);
            List<String[]> mete = mrDao.query_fconn();
            int size = mete.size();
            for(int i=0;i<size;i++){
                String[] record = mete.get(i);
                writer.writeNext(record);
            }
            writer.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void generate_sapfile(String mruNo, boolean isMultiBook){
        try{


            String fileName = "SAP"+mruNo.substring(mruNo.length()-4)+".txt";
            if(isMultiBook){
                fileName = "SAPMB.txt";
            }
            File contentDir=new File(android.os.Environment.getExternalStorageDirectory(),mContext.getPackageName()+"/downloads");
            File file = new File(contentDir, fileName);
            CSVWriter writer = new CSVWriter(new FileWriter(file), '|',CSVWriter.NO_QUOTE_CHARACTER);
            //String[] column_names =    mContext.getResources().getStringArray(R.array.fc_columns);
            //writer.writeNext(column_names);
            List<String[]> mete = mrDao.query_sap();
            int size = mete.size();
            for(int i=0;i<size;i++){
                String[] record = mete.get(i);
                writer.writeNext(record);
            }
            writer.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
