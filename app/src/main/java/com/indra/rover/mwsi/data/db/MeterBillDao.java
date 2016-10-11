package com.indra.rover.mwsi.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.indra.rover.mwsi.data.pojo.meter_reading.references.GLCharge;
import com.indra.rover.mwsi.data.pojo.meter_reading.references.SAPData;
import com.indra.rover.mwsi.data.pojo.meter_reading.references.Tariff;

import java.util.ArrayList;

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


    /**
     * get all tariff based on the given Bill Class type
     * @param billClass bill class type either Residential ,commercial etc
     * @return array of tariff object
     */
    public ArrayList<Tariff> getTariffs(String billClass){
        ArrayList<Tariff> arry= new ArrayList<>();
        try{
            open();
            String sql_stmt = "select * from R_TARIFF where BILL_CLASS='"+billClass+"'";
            Cursor cursor = database.rawQuery(sql_stmt,null);
            if (cursor.moveToFirst()) {
                do {
                    Tariff tariff = new Tariff(cursor);
                    arry.add(tariff);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close();
        }
        return arry;
    }



    public void insertSAPData(SAPData sapData){
        try {
            open();
            ContentValues values = new ContentValues();
            values.put("SAP_LINE_CODE",sapData.getLineCode());
            values.put("SAPDOCNO",sapData.getDocNO());
            values.put("ACCTNUM",sapData.getAcctNum());
            values.put("QUANTITY",sapData.getQuantity());
            values.put("PRICE",sapData.getPrice());
            values.put("AMOUNT",sapData.getAmount());
            values.put("OLD_PRiCE",sapData.getOld_price());
            values.put("OLD_AMOUNT",sapData.getOld_amount());
            values.put("TOTAL_AMOUNT",sapData.getTotal_amount());
            database.insert("T_SAP_DETAILS", null, values);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close();
        }
    }



}
