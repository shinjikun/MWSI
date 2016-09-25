package com.indra.rover.mwsi.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.indra.rover.mwsi.data.pojo.meter_reading.MeterConsumption;
import com.indra.rover.mwsi.data.pojo.meter_reading.display.MeterDelivery;
import com.indra.rover.mwsi.data.pojo.meter_reading.display.MeterInfo;
import com.indra.rover.mwsi.data.pojo.meter_reading.display.MeterRHistory;
import com.indra.rover.mwsi.data.pojo.meter_reading.display.MeterRemarks;
import com.indra.rover.mwsi.data.pojo.meter_reading.references.RangeTolerance;
import com.indra.rover.mwsi.utils.Utils;

import java.util.ArrayList;
import java.util.List;



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

    public List<MeterInfo> fetchInfos(String mruID){
        List<MeterInfo> arry = new ArrayList<>();
        String sql_stmt = "select r.BILL_CLASS_DESC,  t.*,c.* from T_DOWNLOAD t, R_BILL_CLASS r,T_CURRENT_RDG c " +
                "where t.BILL_CLASS = r.BILL_CLASS and t.DLDOCNO = c.CRDOCNO and MRU="+mruID;
        try{
            open();
            Cursor cursor = database.rawQuery(sql_stmt,null);
            if (cursor.moveToFirst()) {
                do {
                    MeterInfo download_info = new MeterInfo(cursor);
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


    public List<MeterInfo> fetchInfos(String mruID, String column, String searchValue){
        List<MeterInfo> arry = new ArrayList<>();
        String sql_stmt = "select r.BILL_CLASS_DESC,  t.*,c.* from T_DOWNLOAD t, R_BILL_CLASS r,T_CURRENT_RDG c " +
                "where t.BILL_CLASS = r.BILL_CLASS and t.DLDOCNO = c.CRDOCNO and MRU="+mruID+ " and "+column+" like '%"+searchValue+"%'";
        try{
            open();
            Cursor cursor = database.rawQuery(sql_stmt,null);
            if (cursor.moveToFirst()) {
                do {
                    MeterInfo download_info = new MeterInfo(cursor);
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
     * @param message remark message
     * @param crdocid id of current reading on which remarks/message should be added
     */
    public void addRemarks(String message , String crdocid){
        //String sql_stmt = "UPDATE T_CURRENT_RDG set REMARKS='"+message+"' where CRDOCNO="+crdocid;
        try{
            open();
            ContentValues contentValues = new ContentValues();
            contentValues.put("REMARKS",message.trim());
            String where= "CRDOCNO=?";
            database.update("T_CURRENT_RDG",contentValues,where,new String[]{crdocid});
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            close();
        }

    }


    /**
     *  add delivery remarks and code to current reading table
     * @param deliv_code delivery code
     * @param deliv_remarks delivery remarks
     * @param crdocid id
     * @param isNewDeliv is new inserted record?
     */
    public void addDelivRemarks(String deliv_code, String deliv_remarks, String crdocid, boolean isNewDeliv){
        try {
            open();
            ContentValues contentValues = new ContentValues();
            if(isNewDeliv){
                contentValues.put("DELIV_DATE", Utils.getFormattedDate());
                contentValues.put("DELIV_TIME",Utils.getFormattedTime());

            }
            contentValues.put("DEL_CODE",deliv_code);
            contentValues.put("DELIV_REMARKS", Utils.formatString(deliv_remarks));
            String where= "CRDOCNO=?";
            database.update("T_CURRENT_RDG",contentValues,where,new String[]{crdocid});
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close();
        }
    }


    public MeterDelivery getDeliveryStat(String crdocno){
        MeterDelivery meterRemarks =null;
        try {
            open();
            String sql_stmt = "SELECT t.CRDOCNO, t.DEL_CODE, t.DELIV_DATE, t.DELIV_TIME," +
                    "t.DELIV_REMARKS, t.READSTAT" +
                    " from  T_CURRENT_RDG t where " +
                    "t.CRDOCNO="+crdocno;
            Cursor cursor = database.rawQuery(sql_stmt,null);
            if (cursor.moveToFirst()) {
                do {
                    meterRemarks = new MeterDelivery(cursor);
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


    /**
     *
     * @param rdgdate current reading date
     * @param rdgtime current reading time
     * @param prsent_rdg present reading
     * @param latitude latitude of meter reading activities
     * @param longitude longitude of meter reading activity
     * @param rdg_tries reading tries
     * @param crdocid id
     * @param readstat read status
     */
    public void updateReading(String rdgdate,String rdgtime,String prsent_rdg,String latitude,
                              String longitude,int rdg_tries,String crdocid,String readstat){
        try{
            open();
            ContentValues contentValues = new ContentValues();
            contentValues.put("RDG_DATE",rdgdate);
            contentValues.put("RDG_TIME",rdgtime);
            contentValues.put("PRESRDG",prsent_rdg);
            contentValues.put("GPS_LATITUDE",latitude);
            contentValues.put("GPS_LONGITUDE",longitude);
            contentValues.put("RDG_TRIES",rdg_tries);
            contentValues.put("READSTAT",readstat);
            String where= "CRDOCNO=?";
            database.update("T_CURRENT_RDG",contentValues,where,new String[]{crdocid});
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            close();
        }
    }

    public void updateReadStatus(String status,String crdocid ){
        try{
            open();
            ContentValues contentValues = new ContentValues();
            contentValues.put("READSTAT",status);
            String where= "CRDOCNO=?";
            database.update("T_CURRENT_RDG",contentValues,where,new String[]{crdocid});
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            close();
        }
    }

    public void updateSequenceNumber(String seqNumber,String crdocid){
        try{
            open();
            ContentValues contentValues = new ContentValues();
            contentValues.put("RECMD_SEQNO",seqNumber);
            String where= "CRDOCNO=?";
            database.update("T_CURRENT_RDG",contentValues,where,new String[]{crdocid});
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            close();
        }
    }


    /**
     * updates the bill consumption
     *

    public void updateConsumption(String bill_consumption){
        try{
            open();
            ContentValues contentValues = new ContentValues();
            contentValues.put("REMARKS",message.trim());
            String where= "CRDOCNO=?";
            database.update("T_CURRENT_RDG",contentValues,where,new String[]{crdocid});
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            close();
        }
    }
*/
    public MeterRemarks getRemarks(String crdocno){
        MeterRemarks meterRemarks = null;
        try {
            open();
            String sql_stmt = "SELECT CRDOCNO, ACCTNUM,METERNO,REMARKS,READSTAT  from  T_CURRENT_RDG where CRDOCNO="+crdocno;
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


    public MeterConsumption getConsumption(String dldocno){
        MeterConsumption meterConsumption =null;
        try{
            open();
            String sql_stmt ="select d.DLDOCNO,d.ACCT_STATUS,d.METERNO,d.GRP_FLAG,d.BLOCK_TAG," +
                    "d.BLOCK_TAG,d.DISC_TAG,d.PREVRDGDATE,d.ACTPREVRDG,d.BILLPREVRDG," +
                    "d.BILLPREVRDG2,d.BILLPREVACTTAG,d.PRACTFLAG,d.AVECONS,d.NMINITRDG," +
                    "d.NMCONSFACTOR,d.PREVFF1,d.PREVFF2,d.NODIALS,nd.maxcap,c.FFCODE1,c.FFCODE2,c.PRESRDG," +
                    "c.BILLED_CONS,c.CONSTYPE_CODE,d.PCONSAVGFLAG,d. " +
                    "from T_DOWNLOAD d,R_NUM_DIALS nd ,T_CURRENT_RDG c " +
                    "where d.nodials = nd.nodials and d.DLDOCNO=c.CRDOCNO and d.DLDOCNO="+dldocno+";";
            Cursor cursor =database.rawQuery(sql_stmt,null);
            if (cursor.moveToFirst()) {
                do {
                    meterConsumption = new MeterConsumption(cursor);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close();
        }

        return meterConsumption;
    }


    public RangeTolerance getRangeTolerance(int range , int type){
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
