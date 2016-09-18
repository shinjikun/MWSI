package com.indra.rover.mwsi.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.indra.rover.mwsi.data.pojo.T_Download_Info;
import com.indra.rover.mwsi.data.pojo.meter_reading.MeterRHistory;
import com.indra.rover.mwsi.data.pojo.meter_reading.MeterRemarks;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leonardoilagan on 12/09/2016.
 */

public class MeterReadingDao extends ModelDao {

    public MeterReadingDao(Context context){
        super(context);
    }

    @Override
    public void open() {
        database = dbHelper.openDB();
    }

    @Override
    public void close() {
        database.close();
    }

    public List<T_Download_Info> fetchInfos(String mruID){
        List<T_Download_Info> arry = new ArrayList<>();
        String sql_stmt = "select r.BILL_CLASS_DESC,  t.*,c.* from T_DOWNLOAD t, R_BILL_CLASS r,T_CURRENT_RDG c " +
                "where t.BILL_CLASS = r.BILL_CLASS and t.DLDOCNO = c.CRDOCNO and MRU="+mruID;
        try{
            open();
            Cursor cursor = database.rawQuery(sql_stmt,null);
            if (cursor.moveToFirst()) {
                do {
                    T_Download_Info  download_info = new T_Download_Info(cursor);
                    String readStat = cursor.getString(cursor.getColumnIndexOrThrow("READSTAT"));
                    download_info.setReadStat(readStat);
                    arry.add(download_info);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            close();
        }


        return arry;
    }


    public List<T_Download_Info> fetchInfos(String mruID,String column,String searchValue){
        List<T_Download_Info> arry = new ArrayList<>();
        String sql_stmt = "select r.BILL_CLASS_DESC,  t.*,c.* from T_DOWNLOAD t, R_BILL_CLASS r,T_CURRENT_RDG c " +
                "where t.BILL_CLASS = r.BILL_CLASS and t.DLDOCNO = c.CRDOCNO and MRU="+mruID+ " and "+column+" like '%"+searchValue+"%'";
        try{
            open();
            Cursor cursor = database.rawQuery(sql_stmt,null);
            if (cursor.moveToFirst()) {
                do {
                    T_Download_Info  download_info = new T_Download_Info(cursor);
                    String readStat = cursor.getString(cursor.getColumnIndexOrThrow("READSTAT"));
                    download_info.setReadStat(readStat);
                    arry.add(download_info);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            close();
        }


        return arry;
    }


    public MeterRHistory fetchConHistory(String dldocno){
        MeterRHistory meterRHistory = null;
        String sql_stmt = "Select MRU,DLDOCNO,METERNO,ACCTNUM,CUSTNAME,CUSTADDRESS," +
                "PREV_REMARKS,PREVRDGDATE,PREVFF1,PREVFF2,ACTPREVRDG " +
                "from T_DOWNLOAD where DLDOCNO="+dldocno;
        try{
            open();
            Cursor cursor = database.rawQuery(sql_stmt,null);
            if (cursor.moveToFirst()) {
                do {
                    meterRHistory = new MeterRHistory(cursor);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close();
        }
        return meterRHistory;
    }


    /**
     * insert the specified messsage/remarks to Current Reading table
     * @param message
     * @param crdocid id of current reading on which remarks/message should be added
     */
    public void addRemarks(String message , String crdocid){
        //String sql_stmt = "UPDATE T_CURRENT_RDG set REMARKS='"+message+"' where CRDOCNO="+crdocid;
        try{
            open();
            ContentValues contentValues = new ContentValues();
            contentValues.put("REMARKS",message);
            String where= "CRDOCNO=?";
            database.update("T_CURRENT_RDG",contentValues,where,new String[]{crdocid});
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            close();
        }

    }

    public MeterRemarks getRemarks(String crdocno){
        MeterRemarks meterRemarks = null;
        try {
            open();
            String sql_stmt = "SELECT CRDOCNO, ACCTNUM,METERNO,REMARKS  from  T_CURRENT_RDG where CRDOCNO="+crdocno;
            Log.i("Test",sql_stmt);
            Cursor cursor = database.rawQuery(sql_stmt,null);
            if (cursor.moveToFirst()) {
                do {
                    meterRemarks = new MeterRemarks(cursor);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close();
        }
        return meterRemarks;
    }






}
