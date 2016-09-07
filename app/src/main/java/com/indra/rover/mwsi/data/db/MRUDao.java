package com.indra.rover.mwsi.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import com.indra.rover.mwsi.data.pojo.MRU;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leonardoilagan on 04/09/2016.
 */

public class MRUDao  extends  ModelDao{

    public MRUDao(Context context){
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

    public long insertMRU(MRU mru){
        open();
        long rowInsert = 0;
        if(isExistData("T_MRU_INFO","MRU",mru.getId())){
            return -1 ;
        }

        ContentValues values = new ContentValues();
        values.put("MRU", mru.getId());
        values.put("BC_CODE",mru.getBc_code());
        values.put("READER_CODE",mru.getReader_code());
        values.put("READER_NAME",mru.getReader_name());
        values.put("SCHED_RDG_DATE",mru.getReading_date());
        values.put("DUE_DATE",mru.getDue_date());
        values.put("KAM_MRU_FLAG",mru.getKam_mru());
        values.put("MAX_SEQNO",mru.getMax_seq_no());
        values.put("CUST_COUNT",mru.getCustomer_count());
        values.put("ACTIVE_COUNT",mru.getActive_count());
        values.put("BLOCKED_COUNT",mru.getBlocked_count());
        values.put("READ_METERS",mru.getRead());
        values.put("UNREAD_METERS",mru.getUnread());
        values.put("UNDELIV_BILLS",mru.getUndelivered());
        rowInsert = database.insert("T_MRU_INFO", null, values);
        close();
        return rowInsert;
    }

    public MRU getMRUStats(String mruID){
        MRU mru=null;
        try{
            open();
            boolean isStatus = false;
            String sql_stmt = "SELECT * from T_MRU_INFO where MRU ="+mruID ;
            if(mruID.equals("All")){
                sql_stmt = "select  sum(CUST_COUNT) as CUST_COUNT, sum(ACTIVE_COUNT) as ACTIVE_COUNT, " +
                        "sum(BLOCKED_COUNT) as BLOCKED_COUNT, sum(READ_METERS) as READ_METERS, " +
                        "sum(UNREAD_METERS) as UNREAD_METERS ,sum(UNDELIV_BILLS) as" +
                        " UNDELIV_BILLS from T_MRU_INFO";
                isStatus = true;
            }

            Cursor cursor = database.rawQuery(sql_stmt,null);
            if (cursor.moveToFirst()) {
                do {
                    mru = new MRU(cursor,isStatus);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }catch(SQLException sql){
            sql.printStackTrace();
        }finally {
            close();
        }
        return mru;

    }


    public MRU getMRU(String mruID){
        MRU mru=null;
        try{
            open();
            String sql_stmt = "SELECT * from T_MRU_INFO where MRU ="+mruID ;
            Cursor cursor = database.rawQuery(sql_stmt,null);
            if (cursor.moveToFirst()) {
                do {
                    mru = new MRU(cursor);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }catch(SQLException sql){
            sql.printStackTrace();
        }finally {
            close();
        }
        return mru;
    }

    public List<MRU>  getMRUs(){
        List<MRU> arryList= new ArrayList<>();
        try {
            open();
            String sql_stmt = "SELECT * from T_MRU_INFO";
            Cursor cursor = database.rawQuery(sql_stmt,null);
            if (cursor.moveToFirst()) {
                do {
                    MRU mru = new MRU(cursor);
                    arryList.add(mru);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }catch(SQLException sql){
            sql.printStackTrace();
        }finally {
            close();
        }
        return arryList;
    }

    /**
     * returns how many MRUs are stored in T_MRU_INFO table
     */
    public int countMRUs(){
        int count =0;
        try {
            open();
            String sql_stmt = "SELECT count(*) as count  from T_MRU_INFO";
            Cursor cursor = database.rawQuery(sql_stmt,null);
            if(cursor.moveToFirst()){
                count =2;
            }
            cursor.close();
        }catch (SQLException sql){
            sql.printStackTrace();
        }finally {
            close();
        }
        return  count;
    }


    public boolean isExistData(String tablename,String columnname,String param){
        String selectSql = String.format(
                "SELECT "+columnname+" from "+tablename+" where "+columnname+" = \"%s\" limit 1", param);
        Cursor cursor = database.rawQuery(selectSql, null);
        boolean result = cursor.moveToFirst();
        return result;
    }



}
