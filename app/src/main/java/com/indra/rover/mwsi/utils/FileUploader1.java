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

public class FileUploader1 extends AsyncTask<String[],Integer,String> {

    private  UploadListener listener;
    private ConnectDao mrDao;
    private Context mContext;
    public FileUploader1(Context context){
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

            for(int i=0; i<files.length;i++){
                String filename = files[i];
                generate_uploadfile(filename);

            }

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

    private void generate_uploadfile(String mruNo){

        try {
            String fileName = mruNo+".txt";

            File contentDir=new File(android.os.Environment.getExternalStorageDirectory(),mContext.getPackageName()+"/datadump");
            if(!contentDir.exists()){
                contentDir.mkdir();
            }
            File file = new File(contentDir, fileName);

            CSVWriter writer = new CSVWriter(new FileWriter(file), '|',CSVWriter.NO_QUOTE_CHARACTER);

            String[] column_names =    mContext.getResources().getStringArray(R.array.upload_columns);
            writer.writeNext(column_names);
            List<String[]> mete = mrDao.query_upload(mruNo, false);
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
