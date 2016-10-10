package com.indra.rover.mwsi.data.db;

import android.content.Context;
import android.database.Cursor;

import com.indra.rover.mwsi.data.pojo.MRU;
import com.indra.rover.mwsi.data.pojo.meter_reading.references.GLCharge;

/**
 * Created by Indra on 10/10/2016.
 */
public class MeterBillDao extends ModelDao {

    public MeterBillDao(Context context){
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


    public GLCharge getGLRate(String gl_code){
        GLCharge glCharge = null;
        try{
            open();
            String sql_stmt = "select * from R_GLOBAL_CHARGES where GL_CHARGE_CODE ='"+gl_code+"'";
            Cursor cursor = database.rawQuery(sql_stmt,null);
            if (cursor.moveToFirst()) {
                do {
                    glCharge = new GLCharge(cursor);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close();
        }
        return glCharge;
    }
}
