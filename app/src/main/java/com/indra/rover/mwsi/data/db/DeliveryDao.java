package com.indra.rover.mwsi.data.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.indra.rover.mwsi.data.pojo.DeliveryCode;
import com.indra.rover.mwsi.data.pojo.ObservationCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Indra on 8/26/2016.
 */
public class DeliveryDao extends ModelDao  {


    public DeliveryDao(Context context){
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


    /**
     * get all delivery codes
     *
     */
    public List<DeliveryCode> getDeliveryCodes(){
        List<DeliveryCode> arryList= new ArrayList<>();
        try {
            open();
            String sql_stmt = "SELECT * from R_DELIVERY_CODES";
            Cursor cursor = database.rawQuery(sql_stmt,null);
            if (cursor.moveToFirst()) {
                do {
                    DeliveryCode deliveryCode = new DeliveryCode(cursor);
                    arryList.add(deliveryCode);
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
     * get all observation codes
     *
     */
    public List<ObservationCode> getOCodes(){
        List<ObservationCode> arryList= new ArrayList<>();
        try {
            open();
            String sql_stmt = "SELECT * from R_FF";
            Cursor cursor = database.rawQuery(sql_stmt,null);
            if (cursor.moveToFirst()) {
                do {
                    ObservationCode observationCode = new ObservationCode(cursor);
                    arryList.add(observationCode);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }catch(SQLException sql){
            sql.printStackTrace();
        }finally {
            database.close();
        }
        return arryList;
    }

}
