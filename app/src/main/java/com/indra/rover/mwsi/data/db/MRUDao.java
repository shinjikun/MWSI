package com.indra.rover.mwsi.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.indra.rover.mwsi.data.pojo.MRU;

import java.util.ArrayList;
import java.util.List;



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


    public MRU getMRUStats(String mruID){
        MRU mru=null;
        try{
            open();
            boolean isStatus = false;
            String sql_stmt = "SELECT * from T_MRU_INFO where MRU ='"+mruID+"'" ;
            if(mruID.equals("All")){
                sql_stmt = "select  sum(CUST_COUNT) as CUST_COUNT, sum(ACTIVE_COUNT) as ACTIVE_COUNT, " +
                        "sum(BLOCKED_COUNT) as BLOCKED_COUNT, sum(READ_METERS) as READ_METERS, " +
                        "sum(UNREAD_METERS) as UNREAD_METERS from T_MRU_INFO";
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
            String sql_stmt = "SELECT * from T_MRU_INFO where MRU ='"+mruID+"'" ;
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



    public int countUnRead(String mruid,String type){
        int count =0;
        try {
            open();
            StringBuilder str_b_stmt = new StringBuilder();
            str_b_stmt.append("Select count(READSTAT)  as COUNTNUM from T_CURRENT_RDG  t, T_DOWNLOAD d  " +
                    "where  t.READSTAT='");
            str_b_stmt.append(type);
            str_b_stmt.append("' and t.CRDOCNO = d.DLDOCNO ");
            if(!mruid.equals("All")){
                str_b_stmt.append(" and d.MRU = ");
                str_b_stmt.append(mruid);
            }
            Cursor cursor = database.rawQuery(str_b_stmt.toString(),null);
            if (cursor.moveToFirst()) {
                do {
                    count = cursor.getInt(cursor.getColumnIndexOrThrow("COUNTNUM"));
                } while (cursor.moveToNext());
            }
            cursor.close();

        }catch (Exception sql){
            sql.printStackTrace();
        }finally {
            close();
        }
        return count;
    }


    public int countPrinted(String mruid){
        int count =0;
        try {
            open();
            StringBuilder str_b_stmt = new StringBuilder();
            str_b_stmt.append("Select count(READSTAT)  as COUNTNUM from T_CURRENT_RDG  t, T_DOWNLOAD d  " +
                    "where  (t.READSTAT='P' or t.READSTAT='Q')");

            str_b_stmt.append(" and t.CRDOCNO = d.DLDOCNO ");
            if(!mruid.equals("All")){
                str_b_stmt.append(" and d.MRU = ");
                str_b_stmt.append(mruid);
            }
            Cursor cursor = database.rawQuery(str_b_stmt.toString(),null);
            if (cursor.moveToFirst()) {
                do {
                    count = cursor.getInt(cursor.getColumnIndexOrThrow("COUNTNUM"));
                } while (cursor.moveToNext());
            }
            cursor.close();

        }catch (Exception sql){
            sql.printStackTrace();
        }finally {
            close();
        }
        return count;
    }


    public int countPrintable(String mruid){
        int count =0;
        try {
            open();
            StringBuilder str_b_stmt = new StringBuilder();
            str_b_stmt.append("Select count(READSTAT)  as COUNTNUM from T_CURRENT_RDG  t, T_DOWNLOAD " +
                    "d , T_UPLOAD u where   t.CRDOCNO = d.DLDOCNO  and d.DLDOCNO = u.ULDOCNO and " +
                    "u.PRINT_TAG =3 and (t.READSTAT='R' or t.READSTAT='E')");
            if(!mruid.equals("All")){
                str_b_stmt.append(" and d.MRU = ");
                str_b_stmt.append(mruid);
            }
            Cursor cursor = database.rawQuery(str_b_stmt.toString(),null);
            if (cursor.moveToFirst()) {
                do {
                    count = cursor.getInt(cursor.getColumnIndexOrThrow("COUNTNUM"));
                } while (cursor.moveToNext());
            }
            cursor.close();

        }catch (Exception sql){
            sql.printStackTrace();
        }finally {
            close();
        }
        return count;
    }

    /**
     *  query for count of number out of range
     * @param mruid id
     * @return number of out of range
     */
    public int countOutofRange(String mruid){
        int count =0;
        try {
            open();
            StringBuilder str_b_stmt = new StringBuilder();
            str_b_stmt.append("Select count(READSTAT)  as COUNTNUM from T_CURRENT_RDG  t, T_DOWNLOAD d  " +
                    "where  t.RANGE_CODE='3' or t.RANGE_CODE='4'");
            str_b_stmt.append(" and t.CRDOCNO = d.DLDOCNO ");
            if(!mruid.equals("All")){
                str_b_stmt.append(" and d.MRU = ");
                str_b_stmt.append(mruid);
            }
            Cursor cursor = database.rawQuery(str_b_stmt.toString(),null);
            if (cursor.moveToFirst()) {
                do {
                    count = cursor.getInt(cursor.getColumnIndexOrThrow("COUNTNUM"));
                } while (cursor.moveToNext());
            }
            cursor.close();

        }catch (Exception sql){
            sql.printStackTrace();
        }finally {
            close();
        }
        return count;
    }

    /**
     *  query for count of number zero consumption
     * @param mruid id
     * @return number of zero consumption
     */
    public int countZeroCons(String mruid) {
        int count = 0;
        try {
            open();
            StringBuilder str_b_stmt = new StringBuilder();
            str_b_stmt.append("Select count(READSTAT)  as COUNTNUM from T_CURRENT_RDG  t, T_DOWNLOAD d  " +
                    "where  t.RANGE_CODE='Z'");
            str_b_stmt.append(" and t.CRDOCNO = d.DLDOCNO ");
            if (!mruid.equals("All")) {
                str_b_stmt.append(" and d.MRU = ");
                str_b_stmt.append(mruid);
            }
            Cursor cursor = database.rawQuery(str_b_stmt.toString(), null);
            if (cursor.moveToFirst()) {
                do {
                    count = cursor.getInt(cursor.getColumnIndexOrThrow("COUNTNUM"));
                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception sql) {
            sql.printStackTrace();
        } finally {
            close();
        }
        return count;
    }

    /**
     *  query for count of delivered number
     * @param mruid id
     * @return number of zero consumption
     */
    public int countDelivered(String mruid) {
        int count = 0;
        try {
            open();
            StringBuilder str_b_stmt = new StringBuilder();
            str_b_stmt.append("Select count(READSTAT)  as COUNTNUM from T_CURRENT_RDG  t, T_DOWNLOAD d  " +
                    "where  t.DEL_CODE IS NOT NULL and t.DEL_CODE !='07'");
            str_b_stmt.append(" and t.CRDOCNO = d.DLDOCNO ");
            if (!mruid.equals("All")) {
                str_b_stmt.append(" and d.MRU = ");
                str_b_stmt.append(mruid);
            }
            Cursor cursor = database.rawQuery(str_b_stmt.toString(), null);
            if (cursor.moveToFirst()) {
                do {
                    count = cursor.getInt(cursor.getColumnIndexOrThrow("COUNTNUM"));
                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception sql) {
            sql.printStackTrace();
        } finally {
            close();
        }
        return count;
    }


    /**
     *  query for count of undelivered number
     * @param mruid id
     * @return number of zero consumption
     */
    public int countUnDelivered(String mruid) {
        int count = 0;
        try {
            open();
            StringBuilder str_b_stmt = new StringBuilder();
            str_b_stmt.append("Select count(READSTAT)  as COUNTNUM from T_CURRENT_RDG  t, T_DOWNLOAD d  " +
                    "where  t.DEL_CODE IS NOT NULL and t.DEL_CODE ='07'");
            str_b_stmt.append(" and t.CRDOCNO = d.DLDOCNO ");
            if (!mruid.equals("All")) {
                str_b_stmt.append(" and d.MRU = ");
                str_b_stmt.append(mruid);
            }
            Cursor cursor = database.rawQuery(str_b_stmt.toString(), null);
            if (cursor.moveToFirst()) {
                do {
                    count = cursor.getInt(cursor.getColumnIndexOrThrow("COUNTNUM"));
                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception sql) {
            sql.printStackTrace();
        } finally {
            close();
        }
        return count;
    }


    /**
     *  query for count of OC
     * @param mruid id
     * @return number of oc
     */
    public int countOC(String mruid) {
        int count = 0;
        try {
            open();
            StringBuilder str_b_stmt = new StringBuilder();
            str_b_stmt.append("select count(*) as COUNTNUM  from T_CURRENT_RDG t, T_DOWNLOAD d  where " +
                    "t.CRDOCNO = d.DLDOCNO and  t.FFCODE1 IS NOT NULL or t.FFCODE2 IS NOT NULL");
            if (!mruid.equals("All")) {
                str_b_stmt.append(" and d.MRU = '");
                str_b_stmt.append(mruid);
                str_b_stmt.append("'");
            }
            Log.i("Test",str_b_stmt.toString());
            Cursor cursor = database.rawQuery(str_b_stmt.toString(), null);
            if (cursor.moveToFirst()) {
                do {
                    count = cursor.getInt(cursor.getColumnIndexOrThrow("COUNTNUM"));
                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception sql) {
            sql.printStackTrace();
        } finally {
            close();
        }
        return count;
    }

    public int countRecords(){
        int count =0;
        try {
            open();

            String sql_stmt = "Select count(*)  as COUNTNUM from T_CURRENT_RDG ";
            Cursor cursor = database.rawQuery(sql_stmt, null);
            if (cursor.moveToFirst()) {
                do {
                    count = cursor.getInt(cursor.getColumnIndexOrThrow("COUNTNUM"));
                } while (cursor.moveToNext());
            }
            cursor.close();

        } catch (Exception sql) {
            sql.printStackTrace();
        } finally {
            close();
        }
        return count;
    }


}
