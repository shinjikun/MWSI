package com.indra.rover.mwsi.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.indra.rover.mwsi.data.pojo.meter_reading.references.GLCharge;
import com.indra.rover.mwsi.data.pojo.meter_reading.references.SAPData;
import com.indra.rover.mwsi.data.pojo.meter_reading.references.SPBillRule;
import com.indra.rover.mwsi.data.pojo.meter_reading.references.Tariff;
import com.indra.rover.mwsi.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

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


    public HashMap<String,SPBillRule> getSPBill(){
            HashMap<String,SPBillRule> hashMap =  new HashMap<>();
        try{
            open();
            String sql_stmt = "Select * from R_SPBILL_RULE ";
            Cursor cursor = database.rawQuery(sql_stmt,null);
            if (cursor.moveToFirst()) {
                do {
                    SPBillRule spBillRule = new SPBillRule(cursor);
                    hashMap.put(spBillRule.getId(),spBillRule);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close();
        }
        return hashMap;
    }


    public HashMap<String ,GLCharge> getGLRates(){
        HashMap<String,GLCharge> hashMap = new HashMap();
        try{
            open();
            String sql_stmt = "select * from R_GLOBAL_CHARGES ";
            Cursor cursor = database.rawQuery(sql_stmt,null);
            if (cursor.moveToFirst()) {
                do {
                   GLCharge glCharge = new GLCharge(cursor);
                    hashMap.put(glCharge.getGl_code(),glCharge);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close();
        }
        return hashMap;
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


    public void updateBasicCharge(double basic_charge,double discount,String dldocno){
        try{
            open();
            ContentValues contentValues = new ContentValues();
            contentValues.put("BASIC_CHARGE",basic_charge);
            contentValues.put("DISCOUNT",discount);
            String where= "ULDOCNO=?";
            database.update("T_UPLOAD",contentValues,where,new String[]{dldocno});
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            close();
        }
    }

    public double getTotalAmount(String sapdocno){
        double total =0.0;
        try {
            open();
            String sql_stmt = "select sum(TOTAL_AMOUNT) as TOTAL from T_SAP_DETAILS where SAPDOCNO='"+sapdocno+"'";
            Cursor cursor = database.rawQuery(sql_stmt,null);
            if (cursor.moveToFirst()) {
                do {
                  total =  cursor.getDouble(cursor.getColumnIndexOrThrow("TOTAL"));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close();
        }

        return total;
    }


    public void deleteZBasic(String sapdocno){
       try {
           open();
           String sql_stmt = "delete from T_SAP_DETAILS where  SAP_LINE_CODE='ZBASIC' " +
                   "and SAPDOCNO='"+sapdocno+"'";
           Cursor cursor = database.rawQuery(sql_stmt,null);
           cursor.moveToFirst();
           cursor.close();
       }catch (Exception e){
            e.printStackTrace();
       }finally {
           close();
       }
    }



    public ArrayList<String> getPromoMessages(){
        ArrayList<String> arry = new ArrayList<>();
        try {
            open();
            String sql_stmt = "Select PROMO_MSG from T_PROMO_MESSAGE where PROMO_STARTDT < '"
                    + Utils.getFormattedDate()
                    +"' and '"+Utils.getFormattedDate()+"' < PROMO_ENDDT";
            Cursor cursor = database.rawQuery(sql_stmt,null);
            if (cursor.moveToFirst()) {
                do {
                    String message =  cursor.getString(cursor.getColumnIndexOrThrow("PROMO_MSG"));
                    arry.add(message);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }finally {
            close();
        }
        return arry;
    }


}
