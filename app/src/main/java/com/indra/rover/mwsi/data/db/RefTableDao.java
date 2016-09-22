package com.indra.rover.mwsi.data.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.indra.rover.mwsi.data.pojo.meter_reading.references.DeliveryCode;
import com.indra.rover.mwsi.data.pojo.meter_reading.references.ObservationCode;
import com.indra.rover.mwsi.data.pojo.meter_reading.references.RangeTolerance;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO Helper class Use to query  data from reference tables
 * Created by Indra on 8/26/2016.
 */
public class RefTableDao extends ModelDao  {


    public RefTableDao(Context context){
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
          close();
        }
        return arryList;
    }

    public RangeTolerance getRangeTolerance(int range ,int type){
        RangeTolerance rangeTolerance=null;
        try {
            open();
            String sql_stmt = "select * from R_RANGE_TOLERANCE " +
                    "where minrange<"+range +" and "+range+"<maxrange and type ="+
                    type+" limit 1";
            Cursor cursor = database.rawQuery(sql_stmt,null);
            if (cursor.moveToFirst()) {
                do {
                    rangeTolerance = new RangeTolerance(cursor);

                } while (cursor.moveToNext());
            }
            cursor.close();
        }catch(SQLException sql){
            sql.printStackTrace();
        }finally {
          close();
        }
        return rangeTolerance;
    }
}
