package com.indra.rover.mwsi.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;


import com.indra.rover.mwsi.data.pojo.meter_reading.MeterBill;
import com.indra.rover.mwsi.data.pojo.meter_reading.MeterConsumption;
import com.indra.rover.mwsi.data.pojo.meter_reading.MeterPrint;
import com.indra.rover.mwsi.data.pojo.meter_reading.display.MeterDelivery;
import com.indra.rover.mwsi.data.pojo.meter_reading.display.MeterInfo;
import com.indra.rover.mwsi.data.pojo.meter_reading.display.MeterOC;
import com.indra.rover.mwsi.data.pojo.meter_reading.display.MeterRHistory;
import com.indra.rover.mwsi.data.pojo.meter_reading.display.MeterRemarks;
import com.indra.rover.mwsi.data.pojo.meter_reading.display.NewMeterInfo;
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

    boolean isExistData(String tablename,String columnname,String param){
        String selectSql = String.format(
                "SELECT "+columnname+" from "+tablename+" where "+columnname+" = '\"%s\"' limit 1", param);
        Cursor cursor = database.rawQuery(selectSql, null);

        boolean result = cursor.moveToFirst();
        cursor.close();
        return result;
    }



    public List<MeterInfo> fetchInfos(String mruID){
        List<MeterInfo> arry = new ArrayList<>();
        String sql_stmt = "select  t.MRU,t.SEQNO,t.METERNO,t.DLDOCNO,t.GRP_FLAG,t.BLOCK_TAG, " +
                "c.RDG_TRIES,c.PRESRDG,t.ACCTNUM,t.CUSTNAME,t.CUSTADDRESS,t.BILL_CLASS,t.NODIALS, " +
                "r.BILL_CLASS_DESC, c.READSTAT,c.CSMB_TYPE_CODE,c.CSMB_PARENT,c.RANGE_CODE,u.PRINT_TAG," +
                "u.PRINT_COUNT,c.DEL_CODE,t.NODIALS,c.BILLED_CONS " +
                "from T_DOWNLOAD t," +
                " R_BILL_CLASS r,T_CURRENT_RDG c,T_UPLOAD u where t.BILL_CLASS = r.BILL_CLASS " +
                "and t.DLDOCNO = c.CRDOCNO  and t.DLDOCNO = u.ULDOCNO and c.MRU='"+mruID+"'";

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



    public ArrayList<NewMeterInfo> fetchNewMeters(String mru_id){
        ArrayList<NewMeterInfo> arryList = new ArrayList<>();
        String sql_stmt = "select * from T_FCONN where FCMRU='"+mru_id+"'";
        try{
            open();
            Cursor cursor = database.rawQuery(sql_stmt,null);
            if (cursor.moveToFirst()) {
                do {
                    NewMeterInfo meterInfo = new NewMeterInfo(cursor);
                    arryList.add(meterInfo);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            close();
        }



        return arryList;
    }


    public MeterInfo fetchInfo(String id){
        MeterInfo meterInfo= null;
        String sql_stmt = "select  t.MRU,t.SEQNO,t.METERNO,t.DLDOCNO,t.GRP_FLAG,t.BLOCK_TAG, " +
                "c.RDG_TRIES,c.PRESRDG,t.ACCTNUM,t.CUSTNAME,t.CUSTADDRESS,t.BILL_CLASS, " +
                "r.BILL_CLASS_DESC, c.READSTAT,c.CSMB_TYPE_CODE,c.CSMB_PARENT,c.RANGE_CODE," +
                "u.PRINT_TAG,u.PRINT_COUNT,c.DEL_CODE,t.NODIALS,c.BILLED_CONS " +
                " from T_DOWNLOAD t," +
                " R_BILL_CLASS r,T_CURRENT_RDG c, T_UPLOAD u where t.BILL_CLASS = r.BILL_CLASS " +
                "and t.DLDOCNO = c.CRDOCNO and t.DLDOCNO = u.ULDOCNO and c.CRDOCNO='"+id+"'";
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
                "r.BILL_CLASS_DESC, c.READSTAT,c.CSMB_TYPE_CODE,c.CSMB_PARENT,c.RANGE_CODE," +
                "u.PRINT_TAG,u.PRINT_COUNT,c.DEL_CODE,t.NODIALS,c.BILLED_CONS from T_DOWNLOAD t, " +
                "R_BILL_CLASS r,T_CURRENT_RDG c, T_UPLOAD u where t.BILL_CLASS = r.BILL_CLASS and " +
                "t.DLDOCNO = c.CRDOCNO   and t.DLDOCNO = u.ULDOCNO and " +
                "t.MRU='"+mruID+ "' and "+column+" like '%"+searchValue+"%'";
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


    /**
     * fetch from the db the Meter reading History
     * @param dldocno id to be search
     * @return
     */
    public MeterRHistory fetchConHistory(String dldocno){
        MeterRHistory meterRHistory = null;
        String sql_stmt = "Select MRU,DLDOCNO,METERNO,ACCTNUM,CUSTNAME,CUSTADDRESS," +
                "PREV_REMARKS,PREVRDGDATE,PREVFF1,PREVFF2,ACTPREVRDG " +
                "from T_DOWNLOAD where DLDOCNO='"+dldocno+"'";
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

    public void addOC(String oc1, String oc2, String crdocid, String remarks){
        try {
            open();
            ContentValues contentValues = new ContentValues();

            contentValues.put("FFCODE1",oc1);
            contentValues.put("FFCODE2",oc2);
            contentValues.put("REMARKS",remarks);
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
                    "t.CRDOCNO='"+crdocno+"'";
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

    public void updatePrintTag(MeterConsumption meterCons , String uldocid){
        try{
            open();
            ContentValues contentValues = new ContentValues();


            contentValues.put("PRINT_TAG",meterCons.getPrintTag());
            String where= "ULDOCNO=?";
            database.update("T_UPLOAD",contentValues,where,new String[]{uldocid});
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
            contentValues.put("MR_TYPE_CODE",meterCons.getMrType());
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
     * @param rangeCode rangeCode could be very high = 4 or very low =3 or normal =0
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


    public void updateMeterBill(MeterBill meterBill){
        try {
            open();
            ContentValues contentValues = new ContentValues();
            contentValues.put("BASIC_CHARGE",meterBill.getBasicCharge());
            contentValues.put("DISCOUNT",meterBill.getDiscount());
            contentValues.put("CERA",meterBill.getCera());
            contentValues.put("FCDA",meterBill.getFcda());
            contentValues.put("ENV_CHARGE",meterBill.getEnv_charge());
            contentValues.put("SEWER_CHARGE",meterBill.getSewer_charge());
            contentValues.put("MSC_AMOUNT",meterBill.getMsc_amount());
            contentValues.put("SC_DISCOUNT",meterBill.getSc_discount());
            contentValues.put("TOTCHRG_WO_TAX",meterBill.getTotcurb4tax());
            contentValues.put("VAT_CHARGE",meterBill.getVat());
            contentValues.put("TOT_CURR_CHARGE",meterBill.getVat()+meterBill.getTotcurb4tax());
            //get the total amout due by adding the ff
            // VAT_CHARGE
            // PREVUNPAID
            // OTHER_CHARGES
            //TOTCHRG_WO_TAX
            double total_amount_due =  meterBill.getVat()+meterBill.getPrevUnpaid()+
                    meterBill.getOther_charges() + meterBill.getTotcurb4tax();
            contentValues.put("TOTAL_AMT_DUE",total_amount_due);
            contentValues.put("SUBTOTAL_AMT",total_amount_due);
            String where= "ULDOCNO=?";
            database.update("T_UPLOAD",contentValues,where,new String[]{meterBill.getId()});
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close();
        }
    }

    /**
     * Reset Reading
     * @param crdocid
     */
    public void revert_reading(String crdocid){
        try {
            open();
            ContentValues contentValues = new ContentValues();
            contentValues.put("READSTAT","U");
            contentValues.put("RDG_DATE","");
            contentValues.put("RDG_TIME","");
            contentValues.put("FFCODE1","");
            contentValues.put("FFCODE2","");
            contentValues.put("PRESRDG","");
            contentValues.put("RANGE_CODE","");
            contentValues.put("BILLED_CONS","");
            contentValues.put("SP_COMP","");
            contentValues.put("RDG_TRIES","0");
            contentValues.put("MR_TYPE_CODE","");
            contentValues.put("RANGE_CODE","");
            contentValues.put("CONSTYPE_CODE","");
            contentValues.put("GPS_LATITUDE","");
            contentValues.put("GPS_LONGITUDE","");
            String where= "CRDOCNO=?";
            database.update("T_CURRENT_RDG",contentValues,where,new String[]{crdocid});
        }catch (Exception e){
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
                    "from  T_CURRENT_RDG where CRDOCNO='"+crdocno+"'";
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
            String sql_stmt = "SELECT CRDOCNO, FFCODE1,FFCODE2,READSTAT," +
                    "CSMB_TYPE_CODE,CSMB_PARENT,ACCTNUM,REMARKS  from " +
                    " T_CURRENT_RDG where CRDOCNO='"+crdocno+"'";
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


    public MeterConsumption getParentMeter(String id){
        MeterConsumption meterConsumption =null;
        try{
            open();
            String sql_stmt ="select d.DLDOCNO,d.ACCT_STATUS,d.METERNO,d.GRP_FLAG,d.BLOCK_TAG," +
                    "d.DISC_TAG,d.PREVRDGDATE,d.ACTPREVRDG,d.BILLPREVRDG," +
                    "d.BILLPREVRDG2,d.BILLPREVACTTAG,d.PRACTFLAG,d.AVECONS,d.NMINITRDG," +
                    "d.NMCONSFACTOR,d.PREVFF1,d.PREVFF2,d.NODIALS,nd.maxcap,c.FFCODE1,c.FFCODE2,c.PRESRDG," +
                    "c.BILLED_CONS,c.CONSTYPE_CODE,d.PCONSAVGFLAG,d.DREPLMTR_CODE, c.SP_COMP,c.CSMB_TYPE_CODE," +
                    "c.CSMB_PARENT,c.ACCTNUM,c.READSTAT,c.RANGE_CODE " +
                    "from T_DOWNLOAD d,R_NUM_DIALS nd ,T_CURRENT_RDG c " +
                    "where d.nodials = nd.nodials and d.DLDOCNO=c.CRDOCNO and c.ACCTNUM='"+id+"'";

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


    public MeterBill  getMeterBill(String dldocno){
        MeterBill meterBill = null;
        try {
            open();
            String sql_stmt="Select d.DLDOCNO, c.BILLED_CONS,d.BILL_CLASS,d.RATE_TYPE,u.BASIC_CHARGE,\n" +
                    "u.DISCOUNT,u.SUBTOTAL_AMT,u.TOTAL_AMT_DUE,d.BULK_FLAG,d.GT34FLAG,d.GT34FACTOR,\n" +
                    "c.PRESRDG, d.PREVRDGDATE,c.RDG_DATE,u.ACCTNUM,d.METER_SIZE, r.MSC_AMOUNT,d.VAT_EXEMPT,d.NUMUSERS,\n" +
                    "u.VAT_CHARGE, u.PREVUNPAID,u.OTHER_CHARGES, d.SPBILL_RULE,\n" +
                    "d.TARIFF_PRORATE, d.FCDA_PRORATE, d.CERA_PRORATE, d.ENV_PRORATE, d.SEW_PROATE\n" +
                    "from T_DOWNLOAD d, T_UPLOAD u,T_CURRENT_RDG c, R_MSC r \n" +
                    "where r.METER_SIZE = d.METER_SIZE and  \n" +
                    "d.DLDOCNO = u.ULDOCNO and c.CRDOCNO= u.ULDOCNO "+
                    " and d.DLDOCNO='"+dldocno+"'";

            Cursor cursor =database.rawQuery(sql_stmt,null);

            if (cursor.moveToFirst()) {
                do {
                    meterBill = new MeterBill(cursor);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close();
        }
        return meterBill;
    }

    public MeterConsumption getConsumption(String dldocno){
        MeterConsumption meterConsumption =null;
        try{
            open();
            String sql_stmt ="select d.DLDOCNO,d.ACCT_STATUS,d.METERNO,d.GRP_FLAG,d.BLOCK_TAG," +
                    "d.DISC_TAG,d.PREVRDGDATE,d.ACTPREVRDG,d.BILLPREVRDG," +
                    "d.BILLPREVRDG2,d.BILLPREVACTTAG,d.PRACTFLAG,d.AVECONS,d.NMINITRDG," +
                    "d.NMCONSFACTOR,d.PREVFF1,d.PREVFF2,d.NODIALS,nd.maxcap,c.FFCODE1,c.FFCODE2,c.PRESRDG," +
                    "c.BILLED_CONS,c.CONSTYPE_CODE,d.PCONSAVGFLAG,d.DREPLMTR_CODE, c.SP_COMP,c.CSMB_TYPE_CODE," +
                    "c.CSMB_PARENT,c.ACCTNUM,c.READSTAT,c.RANGE_CODE " +
                    "from T_DOWNLOAD d,R_NUM_DIALS nd ,T_CURRENT_RDG c " +
                    "where d.nodials = nd.nodials and d.DLDOCNO=c.CRDOCNO and d.DLDOCNO='"+dldocno+"'";
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


    public int getRTolerance(int range , int type){
       int ceilComp=0;
        try {
            open();
            String sql_stmt = "select * from R_RANGE_TOLERANCE " +
                    "where MINRANGE<"+range +" and "+range+"<MAXRANGE and DEVI_TYPE ='"+
                    type+"' limit 1";
            Cursor cursor = database.rawQuery(sql_stmt,null);
            if (cursor.moveToFirst()) {
                do {
                    ceilComp = cursor.getInt(cursor.getColumnIndexOrThrow("CEILCONSUMP"));

                } while (cursor.moveToNext());
            }
            cursor.close();
        }catch(SQLException sql){
            sql.printStackTrace();
        }finally {
            close();
        }
        return ceilComp;
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


    public boolean isParentMeterRead(String parent_id,String mru_id){
         boolean isRead = true;
        int count =0;
        try {
            open();
            String str ="Select count(*) as COUNTNUM from T_CURRENT_RDG where  ACCTNUM='"+parent_id
                    +"' and MRU = '"+mru_id+" and READSTAT !='U''";
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

        if(count==0)
            isRead =false;

        return isRead;
    }

    public int countChildUnRead(String parent_id){
        int count=0;
        try {
            open();
            String str ="Select count(*) as COUNTNUM from T_CURRENT_RDG where  CSMB_PARENT='"+parent_id
                    +"' and READSTAT = 'U'";
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


    public int countMBChildBilled(String parent_id){
        int count=0;
        try {
            open();
            String str ="select count(*) as COUNTNUM from T_CURRENT_RDG where  csmb_parent='"+
                    parent_id+"' and  ( READSTAT!='P' OR READSTAT!='Q' )";
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
            String str ="select count(*) as COUNTNUM from T_CURRENT_RDG where  csmb_parent='"+
                    parent_id+"' and  ( BILLED_CONS IS NOT NULL AND BILLED_CONS!='' )";
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

    public List<MeterConsumption> getMeterChilds(String parent_code){
        List<MeterConsumption> arry = new ArrayList<>();
        try {
            open();
            String sql_stmt ="select d.DLDOCNO,d.ACCT_STATUS,d.METERNO,d.GRP_FLAG,d.BLOCK_TAG," +
                    "d.DISC_TAG,d.PREVRDGDATE,d.ACTPREVRDG,d.BILLPREVRDG," +
                    "d.BILLPREVRDG2,d.BILLPREVACTTAG,d.PRACTFLAG,d.AVECONS,d.NMINITRDG," +
                    "d.NMCONSFACTOR,d.PREVFF1,d.PREVFF2,d.NODIALS,nd.maxcap,c.FFCODE1,c.FFCODE2,c.PRESRDG," +
                    "c.BILLED_CONS,c.CONSTYPE_CODE,d.PCONSAVGFLAG,d.DREPLMTR_CODE, c.SP_COMP,c.CSMB_TYPE_CODE," +
                    "c.CSMB_PARENT,c.ACCTNUM,c.READSTAT,c.RANGE_CODE " +
                    "from T_DOWNLOAD d,R_NUM_DIALS nd ,T_CURRENT_RDG c " +
                    "where d.nodials = nd.nodials and d.DLDOCNO=c.CRDOCNO and c.CSMB_PARENT='"+parent_code+"'";
            Cursor cursor = database.rawQuery(sql_stmt,null);
            if (cursor.moveToFirst()) {
                do {
                   MeterConsumption meter = new MeterConsumption(cursor);
                    arry.add(meter);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            close();
        }
        return arry;
    }

    public ArrayList<MeterPrint> getEODReport(){
        ArrayList<MeterPrint> arrys = new ArrayList<>();
       String sql_stmt = "Select c.ACCTNUM,c.FFCODE1,c.FFCODE2,c.PRESRDG,c.RANGE_CODE,\n" +
               "d.CUSTNAME,d.ACCTNUM,u.PRINT_COUNT,u.TOTAL_AMT_DUE \n" +
               "from T_CURRENT_RDG c, T_DOWNLOAD d, T_UPLOAD u \n" +
               "where  c.CRDOCNO = d.DLDOCNO  and u.ULDOCNO = d.DLDOCNO  \n" +
               "and  c.READSTAT!='U'";
        try{
            open();
            Cursor cursor = database.rawQuery(sql_stmt,null);
            if (cursor.moveToFirst()) {
                do {
                 MeterPrint   meterPrint = new MeterPrint(cursor);
                 arrys.add(meterPrint);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close();
        }
        return arrys;
    }


    public MeterPrint getMeterPrint(String dldocno){
        MeterPrint meterPrint = null;
        String sql_stmt="Select d.MRU ,bc.BC_DESC,bc.BC_ADDRESS,bc.BC_TIN, \n" +
                "bl.BILL_CLASS_DESC, d.SOA_NUMBER,c.RANGE_CODE,mi.DUE_DATE,\n" +
                "d.ACCTNUM,d.CUSTNAME,d.CUSTADDRESS,d.ACCT_STATUS,d.TIN,d.TENANT_NAME,d.SC_ID,\n" +
                "d.SEQNO,c.METERNO,c.BILLED_CONS,c.CONSTYPE_CODE,c.RDG_DATE,d.PREVRDGDATE,d.ACTPREVRDG,c.PRESRDG,\n" +
                "d.PREVCONSLINE1, d.PREVCONSLINE2,d.WBPAYDTLS1,d.WBPAYDTLS2,d.GDPAYDTLS,d.MISCPAYDTLS,\n" +
                "u.BASIC_CHARGE,u.FCDA,u.CERA,u.ENV_CHARGE,u.SEWER_CHARGE,u.MSC_AMOUNT,u.SC_DISCOUNT,\n" +
                "u.TOTCHRG_WO_TAX,u.VAT_CHARGE,u.TOT_CURR_CHARGE,d.SPBILL_RULE,\n" +
                "INSTALL_WATER_DUE,INSTALL_WATER_CHARGE,\n"+
                "SEPTIC_CHARGE,CHANGESIZE_CHARGE,MISC_CHARGE,INSTALL_SEWER_CHARGE,AMORTIZATION,\n" +
                "INSTALL_WTR_IND,INSTALL_SEWER_DUE,INSTALL_SEW_IND,GD_AMOUNT_DUE,INSTALL_GD_IND,AMORT_DUE,\n" +
                "INSTALL_AMORT_IND,RESTORATION_DUE,RESTORATION_IND,ILLEGALITIES_DUE,ILLEGALITIES_IND,\n" +
                "UNMIGRATED_WATER_DUE,UNMIGRATED_WATER_IND,UNMIGRATED_SEWER_DUE,UNMIGRATED_SEWER_IND,\n" +
                "PENALTIES_DUE,UNMIGRATED_AR_WATER,UNMIGRATED_AR_IC,RECOVERY,\n" +
                "d.DISCHECK_FLAG,u.TOTAL_AMT_DUE,mi.SCHED_RDG_DATE,\n"+
                "d.PREVINVOICENO,u.REOPENING_FEE,u.METER_CHARGES,u.GD_CHARGE,u.OTHER_CHARGES,\n"+
                "c.FFCODE1,c.FFCODE2,c.REMARKS,u.PREVUNPAID \n"+
                "from R_BUSINESS_CENTER bc, T_MRU_INFO mi , T_DOWNLOAD d, R_BILL_CLASS bl , T_CURRENT_RDG c,\n" +
                "T_UPLOAD u\n" +
                "where mi.BC_CODE=bc.BC_CODE and  d.MRU=mi.MRU and bl.BILL_CLASS=d.BILL_CLASS and c.CRDOCNO = d.DLDOCNO and \n" +
                "u.ULDOCNO = d.DLDOCNO and d.DLDOCNO='"+dldocno+"'";

        try{
            open();
            Cursor cursor = database.rawQuery(sql_stmt,null);
            if (cursor.moveToFirst()) {
                do {
                    meterPrint = new MeterPrint(cursor);

                } while (cursor.moveToNext());
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close();
        }
        return meterPrint;
    }


    public void deleteFMeter(NewMeterInfo meterInfo){
        try {
            open();
            String sql_stmt = "delete from T_FCONN where  FCMRU='"+meterInfo.getMru_id()+"' " +
                    "and METERNO='"+meterInfo.getMeterNo()+"'";
            Cursor cursor = database.rawQuery(sql_stmt,null);
            cursor.moveToFirst();
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close();
        }


    }

    public long addFConn(NewMeterInfo meterInfo, boolean state){

        long rowInsert =0;
        try {
            open();
            ContentValues values = new ContentValues();
            if(isExistData("T_CURRENT_RDG","METERNO",meterInfo.getMeterNo())){
                return -1 ;
            }

            if(isExistData("T_FCONN","METERNO",meterInfo.getMeterNo())){
                return -2 ;
            }
            values.put("FCMRU",meterInfo.getMru_id());
            values.put("SEQNO",meterInfo.getSeqno());
            values.put("METERNO",meterInfo.getMeterNo());
            values.put("CUSTADDRESS",meterInfo.getCustAdd());
            values.put("CUSTNAME",meterInfo.getCustName());
            values.put("PRESRDG",meterInfo.getPresRdg());
            values.put("RDG_DATE",meterInfo.getRdg_date());
            values.put("RDG_TIME",meterInfo.getRdg_time());
            if(state){
                rowInsert = database.insert("T_FCONN", null, values);
            }
              else {
                String where= "METERNO=?";
                database.update("T_FCONN",values,where,new String[]{meterInfo.getMeterNo()});
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close();
        }
        return rowInsert;
    }



}
