package com.indra.rover.mwsi.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;


import com.indra.rover.mwsi.data.pojo.meter_reading.MeterConsumption;
import com.indra.rover.mwsi.data.pojo.meter_reading.display.MeterDelivery;
import com.indra.rover.mwsi.data.pojo.meter_reading.display.MeterInfo;
import com.indra.rover.mwsi.data.pojo.meter_reading.display.MeterOC;
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
        String sql_stmt = "select  t.MRU,t.SEQNO,t.METERNO,t.DLDOCNO,t.GRP_FLAG,t.BLOCK_TAG, " +
                "c.RDG_TRIES,c.PRESRDG,t.ACCTNUM,t.CUSTNAME,t.CUSTADDRESS,t.BILL_CLASS, " +
                "r.BILL_CLASS_DESC, c.READSTAT,c.CSMB_TYPE_CODE,c.CSMB_PARENT from T_DOWNLOAD t," +
                " R_BILL_CLASS r,T_CURRENT_RDG c where t.BILL_CLASS = r.BILL_CLASS " +
                "and t.DLDOCNO = c.CRDOCNO  and c.MRU="+mruID;
        Log.i("Test",sql_stmt);
        try{
            open();
            Cursor cursor = database.rawQuery(sql_stmt,null);
            if (cursor.moveToFirst()) {
                do {
                    MeterInfo download_info = new MeterInfo(cursor);
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


    public MeterInfo fetchInfo(String id){
        MeterInfo meterInfo= null;
        String sql_stmt = "select  t.MRU,t.SEQNO,t.METERNO,t.DLDOCNO,t.GRP_FLAG,t.BLOCK_TAG, " +
                "c.RDG_TRIES,c.PRESRDG,t.ACCTNUM,t.CUSTNAME,t.CUSTADDRESS,t.BILL_CLASS, " +
                "r.BILL_CLASS_DESC, c.READSTAT,c.CSMB_TYPE_CODE,c.CSMB_PARENT from T_DOWNLOAD t," +
                " R_BILL_CLASS r,T_CURRENT_RDG c where t.BILL_CLASS = r.BILL_CLASS " +
                "and t.DLDOCNO = c.CRDOCNO  and c.CRDOCNO="+id;
        try{
            open();
            Cursor cursor = database.rawQuery(sql_stmt,null);
            if (cursor.moveToFirst()) {
                do {
                     meterInfo = new MeterInfo(cursor);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            close();
        }


        return meterInfo;
    }


    public List<MeterInfo> fetchInfos(String mruID, String column, String searchValue){
        List<MeterInfo> arry = new ArrayList<>();
        String sql_stmt = "select  t.MRU,t.SEQNO,t.METERNO,t.DLDOCNO,t.GRP_FLAG,t.BLOCK_TAG, " +
                "c.RDG_TRIES,c.PRESRDG,t.ACCTNUM,t.CUSTNAME,t.CUSTADDRESS,t.BILL_CLASS, " +
                "r.BILL_CLASS_DESC, c.READSTAT,c.CSMB_TYPE_CODE,c.CSMB_PARENT from T_DOWNLOAD t, " +
                "R_BILL_CLASS r,T_CURRENT_RDG c where t.BILL_CLASS = r.BILL_CLASS and " +
                "t.DLDOCNO = c.CRDOCNO   and " +
                "MRU="+mruID+ " and "+column+" like '%"+searchValue+"%'";
        try{
            open();
            Cursor cursor = database.rawQuery(sql_stmt,null);
            if (cursor.moveToFirst()) {
                do {
                    MeterInfo download_info = new MeterInfo(cursor);
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

    public void addOC(String oc1,String oc2, String crdocid){
        try {
            open();
            ContentValues contentValues = new ContentValues();

            contentValues.put("FFCODE1",oc1);
            contentValues.put("FFCODE2",oc2);
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

    public void updateConsumption(MeterConsumption meterCons,String crdocid ){
        try{
            open();
            ContentValues contentValues = new ContentValues();
            contentValues.put("BILLED_CONS",meterCons.getBilled_cons());
            contentValues.put("CONSTYPE_CODE",meterCons.getConstype_code());
            contentValues.put("SP_COMP",meterCons.getSpComp());
            String where= "CRDOCNO=?";
            database.update("T_CURRENT_RDG",contentValues,where,new String[]{crdocid});
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            close();
        }
    }

    /**
     * update's the bill print date of the printed meter bill in DB
     * @param printDate print date
     * @param crdocid id
     */
    public void updatePrintDate(String printDate,String crdocid ){
        try{
            open();
            ContentValues contentValues = new ContentValues();
            contentValues.put("BILLPRINT_DATE",printDate);
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
     * Update reading's range code
     * @param rangeCode rangeCode could be very high = 4 or very low =3
     * @param crdocid record to be updated
     */
    public void updateRangeCode(String rangeCode,String crdocid){
        try{
            open();
            ContentValues contentValues = new ContentValues();
            contentValues.put("RANGE_CODE",rangeCode);
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
            String sql_stmt = "SELECT CRDOCNO, ACCTNUM,METERNO,REMARKS,READSTAT  " +
                    "from  T_CURRENT_RDG where CRDOCNO="+crdocno;
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

    public MeterOC getMeterOCs(String crdocno){
        MeterOC meterOC = null;
        try {
            open();
            String sql_stmt = "SELECT CRDOCNO, FFCODE1,FFCODE2,READSTAT  from " +
                    " T_CURRENT_RDG where CRDOCNO="+crdocno;
            Cursor cursor = database.rawQuery(sql_stmt,null);
            if (cursor.moveToFirst()) {
                do {
                    meterOC = new MeterOC(cursor);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close();
        }
        return meterOC;
    }



    public MeterConsumption getConsumption(String dldocno){
        MeterConsumption meterConsumption =null;
        try{
            open();
            String sql_stmt ="select d.DLDOCNO,d.ACCT_STATUS,d.METERNO,d.GRP_FLAG,d.BLOCK_TAG," +
                    "d.DISC_TAG,d.PREVRDGDATE,d.ACTPREVRDG,d.BILLPREVRDG," +
                    "d.BILLPREVRDG2,d.BILLPREVACTTAG,d.PRACTFLAG,d.AVECONS,d.NMINITRDG," +
                    "d.NMCONSFACTOR,d.PREVFF1,d.PREVFF2,d.NODIALS,nd.maxcap,c.FFCODE1,c.FFCODE2,c.PRESRDG," +
                    "c.BILLED_CONS,c.CONSTYPE_CODE,d.PCONSAVGFLAG,d.DREPLMTR_CODE, c.SP_COMP,c.CSMB_TYPE_CODE,c.CSMB_PARENT,c.ACCTNUM " +
                    "from T_DOWNLOAD d,R_NUM_DIALS nd ,T_CURRENT_RDG c " +
                    "where d.nodials = nd.nodials and d.DLDOCNO=c.CRDOCNO and d.DLDOCNO="+dldocno+";";
            Log.i("Test",sql_stmt);
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

    public int countUnRead(){
        int count =0;
        try {
            open();
            String str ="Select count(READSTAT)  as COUNTNUM from T_CURRENT_RDG  t where  t.READSTAT !='U'";
            Cursor cursor = database.rawQuery(str,null);
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

    public int countChildUnRead(String parent_id){
        int count=0;
        try {
            open();
            String str ="Select count(*) as COUNTNUM from T_CURRENT_RDG where  CSMB_PARENT="+parent_id
                    +" and  BILLED_CONS IS   NULL";
            Cursor cursor = database.rawQuery(str,null);
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


    public int countChildBilled(String parent_id){
        int count=0;
        try {
            open();
            String str ="select count(*) as countnum from T_CURRENT_RDG where  csmb_parent="+
                    parent_id+" and  ( READSTAT = 'P' OR READSTAT='Q' )";
            Cursor cursor = database.rawQuery(str,null);
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


    public int countCSChildBilled(String parent_id){
        int count =0;
        try {
            open();
            String str ="select count(*) as countnum from T_CURRENT_RDG where  csmb_parent="+
                    parent_id+" and  ( READSTAT != 'P' OR READSTAT !='Q' )";
            Cursor cursor = database.rawQuery(str,null);
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

    public List<MeterConsumption> getCSChilds(String parent_code){
        List<MeterConsumption> arry = new ArrayList<>();

        return arry;
    }


}
