package com.indra.rover.mwsi.data.db;

import android.content.Context;
import android.database.Cursor;

import com.indra.rover.mwsi.data.pojo.T_Download_Info;

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
        String sql_stmt = "select r.BILL_CLASS_DESC,  t.* from T_DOWNLOAD t, R_BILL_CLASS r " +
                "where t.BILL_CLASS = r.BILL_CLASS and MRU="+mruID;
        try{
            open();
            Cursor cursor = database.rawQuery(sql_stmt,null);
            if (cursor.moveToFirst()) {
                do {
                    T_Download_Info  download_info = new T_Download_Info(cursor);
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
}
