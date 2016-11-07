package com.indra.rover.mwsi.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
;
import com.indra.rover.mwsi.data.pojo.meter_reading.display.MeterInfo;
import com.indra.rover.mwsi.utils.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by leonardoilagan on 08/09/2016.
 */

public class ConnectDao extends ModelDao {

    public ConnectDao(Context context){
        super(context);
    }
    final int MBMother =2 ;
    final int MBChild =5;
    final int CSMother =1;
    final int CSChild = 3;
    @Override
    public void open() {
        database = dbHelper.openDB();
    }

    @Override
    public void close() {
        database.close();
    }

    public long insertTCurrRDGData(String[] records){
        long rowInsert =0;
        try {
            open();
            ContentValues values = new ContentValues();
            if(isExistData("T_CURRENT_RDG","CRDOCNO",records[10])){
                return -1 ;
            }
            values.put("MRU",records[0]);
            values.put("ACCTNUM",records[2]);
            values.put("METERNO",records[9]);
            values.put("CRDOCNO",records[10]);
           values.put("CSMB_TYPE_CODE",records[80]);
            values.put("CSMB_PARENT",records[81]);
            values.put("MB_PREF_FLAG",records[82]);
            //default of REASTAT is U = UnRead
            values.put("READSTAT","U");
            rowInsert = database.insert("T_CURRENT_RDG", null, values);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close();
        }
        return rowInsert;
    }




    public long insertTDLData(String[] records){
         long rowInsert =0;
        try{
            open();
            if(isExistData("T_DOWNLOAD","DLDOCNO",records[10])){
               return -1 ;
            }
            ContentValues values = new ContentValues();
            values.put("MRU",records[0]);
            values.put("SEQNO",records[1]);
            values.put("ACCTNUM",records[2]);
            values.put("CUSTNAME",records[3]);
            values.put("CUSTADDRESS",records[4]);
            values.put("BILL_CLASS",records[5]);
            values.put("RATE_TYPE",records[6]);
            values.put("BULK_FLAG",records[7]);
            values.put("ACCT_STATUS",records[8]);
            values.put("METERNO",records[9]);
            values.put("DLDOCNO",records[10]);
            values.put("MATERIALNO",records[11]);
            values.put("METER_SIZE",records[12]);
            values.put("NODIALS",records[13]);
            values.put("GRP_FLAG",records[14]);
            values.put("BLOCK_TAG",records[15]);
            values.put("DISC_TAG",records[16]);
            values.put("PREVRDGDATE",records[17]);
            values.put("ACTPREVRDG",records[18]);
            values.put("BILLPREVRDG",records[19]);
            values.put("BILLPREVRDG2",records[20]);
            values.put("BILLPREVACTTAG",records[21]);
            values.put("PRACTFLAG",records[22]);
            values.put("PCONSAVGFLAG",records[23]);
            values.put("AVECONS",records[24]);
            values.put("DREPLMTR_CODE",records[25]);
            values.put("NMINITRDG",records[26]);
            values.put("NMCONSFACTOR",records[27]);
            values.put("PREVFF1",records[28]);
            values.put("PREVFF2",records[29]);
            values.put("GT34FLAG",records[30]);
            values.put("GT34FACTOR",records[31]);
            values.put("TARIFF_PRORATE",records[32]);
            values.put("FCDA_PRORATE",records[33]);
            values.put("CERA_PRORATE",records[34]);
            values.put("ENV_PRORATE",records[35]);
            values.put("SEW_PROATE",records[36]);
            values.put("PREV_REMARKS",records[37]);
            values.put("PREVINVOICENO",records[38]);
            values.put("WBPAYDTLS1",records[39]);
            values.put("WBPAYDTLS2",records[40]);
            values.put("MISCPAYDTLS",records[41]);
            values.put("GDPAYDTLS",records[42]);
            values.put("SC_ID",records[44]);
            values.put("TENANT_NAME",records[45]);
            values.put("INSTALL_WTR_IND",records[53]);
            values.put("INSTALL_SEW_IND",records[55]);
            values.put("INSTALL_GD_IND",records[57]);
            values.put("INSTALL_AMORT_IND",records[59]);
            values.put("RESTORATION_IND",records[61]);
            values.put("ILLEGALITIES_IND",records[63]);
            values.put("UNMIGRATED_WATER_IND",records[65]);
            values.put("UNMIGRATED_SEWER_IND",records[67]);
            values.put("TIN",records[76]);
            values.put("VAT_EXEMPT",records[77]);
            values.put("DISCHECK_FLAG",records[78]);
            values.put("NUMUSERS",records[79]);
            values.put("PREVCONSLINE1",records[83]);
            values.put("PREVCONSLINE2",records[84]);
            values.put("SOA_NUMBER",records[85]);
            values.put("SPBILL_RULE",records[86]);
            values.put("SP_BILL_EFF_DATE",records[87]);

            rowInsert = database.insert("T_DOWNLOAD", null, values);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close();
        }
        return rowInsert;
    }



    public long insertResourceData(String tableName, String[] headers, String[] records){
        long rowInsert =0;
        try {
            truncateTable(tableName);
            open();
            ContentValues values = new ContentValues();
            int size = headers.length;
            for(int i=0;i<size;i++){
                //check for empty string//don't allow to be inserted
                if(Utils.isNotEmpty(records[i]))
                    values.put(headers[i],records[i]);
            }
            rowInsert = database.insert(tableName, null, values);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close();
        }
        return rowInsert;
    }


    public long insertTUploadData(String[] records){
        long rowInsert =0;
        try {

            open();
            if(isExistData("T_UPLOAD","ULDOCNO",records[10])){

                return -1 ;
            }
            ContentValues values = new ContentValues();
            values.put("MRU",records[0]);
            values.put("ACCTNUM",records[2]);
            values.put("PREVUNPAID",records[43]);
            values.put("INSTALL_WATER_DUE",records[46]);
            values.put("INSTALL_WATER_CHARGE",records[47]);
            values.put("SEPTIC_CHARGE",records[48]);
            values.put("CHANGESIZE_CHARGE",records[49]);
            values.put("MISC_CHARGE",records[50]);
            values.put("INSTALL_SEWER_CHARGE",records[51]);
            values.put("AMORTIZATION",records[52]);
            values.put("INSTALL_SEWER_DUE",records[54]);

            values.put("GD_AMOUNT_DUE",records[56]);
            values.put("AMORT_DUE",records[58]);


            values.put("RESTORATION_DUE",records[60]);
            values.put("ILLEGALITIES_DUE",records[62]);
            values.put("UNMIGRATED_WATER_DUE",records[64]);
            values.put("UNMIGRATED_SEWER_DUE",records[66]);
            values.put("PENALTIES_DUE",records[68]);
            values.put("UNMIGRATED_AR_WATER",records[69]);
            values.put("UNMIGRATED_AR_IC",records[70]);
            values.put("RECOVERY",records[71]);
            values.put("REOPENING_FEE",records[72]);
            values.put("METER_CHARGES",records[73]);
            values.put("GD_CHARGE",records[74]);
            values.put("OTHER_CHARGES",records[75]);
            values.put("ULDOCNO",records[10]);
            values.put("PRINT_COUNT",0);
             if(Utils.isNotEmpty(records[14])){
                 if(records[14].equals("K")){
                     values.put("PRINT_TAG", MeterInfo.BILLNOPRINT);
                 }
             }

            if(Utils.isNotEmpty(records[80])){
                int bill_scheme =  Integer.parseInt(records[80]);
                switch (bill_scheme){
                    case CSMother:
                    case CSChild:
                    case MBMother:
                    case MBChild:
                        values.put("PRINT_TAG", MeterInfo.BILLNOPRINT);
                        break;
                }
            }

            rowInsert = database.insert("T_UPLOAD", null, values);

        }catch(Exception e){
            e.printStackTrace();
        } finally {
            close();
        }
        return rowInsert;
    }



    public long insertTMRU(String[] record){
        open();
        long rowInsert = 0;
        if(isExistData("T_MRU_INFO","MRU",record[0])){
            return -1 ;
        }

        ContentValues values = new ContentValues();
        values.put("MRU", record[0]);
        values.put("READER_CODE",record[1]);
        values.put("READER_NAME",record[2]);
        values.put("SCHED_RDG_DATE",record[3]);
        values.put("DUE_DATE",record[4]);
        values.put("BC_CODE",record[5]);
        values.put("KAM_MRU_FLAG",record[6]);
        values.put("MAX_SEQNO",record[7]);
        values.put("CUST_COUNT",record[8]);
        values.put("ACTIVE_COUNT",record[9]);
        values.put("BLOCKED_COUNT",record[10]);
        values.put("READ_METERS",record[11]);
        values.put("UNREAD_METERS",record[12]);

        rowInsert = database.insert("T_MRU_INFO", null, values);
        close();
        return rowInsert;
    }

    public List<String> fetchMRUs(){
        List<String> arry = new ArrayList<>();
        String sql_stmt = "select MRU from T_MRU_INFO";
        try {
            open();
            Cursor cursor = database.rawQuery(sql_stmt,null);
            if (cursor.moveToFirst()) {
                do {
                    String mruID = cursor.getString(0);
                    arry.add(mruID);
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



   public void truncateTable(String tablename){
         try{
             open();
             String sql_stmt ="Delete from "+tablename;
             Cursor cursor =  database.rawQuery(sql_stmt, null);
            boolean is =  cursor.moveToFirst();
            cursor.close();
         }catch (Exception e){
             e.printStackTrace();
         }finally {
             close();
         }
    }

     boolean isExistData(String tablename,String columnname,String param){
        String selectSql = String.format(
                "SELECT "+columnname+" from "+tablename+" where "+columnname+" = '\"%s\"' limit 1", param);
        Cursor cursor = database.rawQuery(selectSql, null);

        boolean result = cursor.moveToFirst();
         cursor.close();
        return result;
    }


    public List<String[]> query(String selectstmt){
        List<String[]> arry = new ArrayList<>();
        try{
            open();
            Cursor cursor = database.rawQuery(selectstmt, null);
            if (cursor.moveToFirst()) {
                do {
                    int size = cursor.getColumnCount();
                    String[] records = new String[size]  ;

                    for(int i=0;i<size;i++){
                        String data = cursor.getString(i);
                        records[i]= data;
                    }
                    arry.add(records);
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

    public List<String[]> query_sap(){
        String selectstmt ="select RECNUM, SAPDOCNO, ACCTNUM,SAP_LINE_CODE,QUANTITY," +
                "PRICE,AMOUNT,OLD_PRICE,OLD_AMOUNT,TOTAL_AMOUNT from T_SAP_DETAILS " +
                "ORDER BY RECNUM ASC";


        return query(selectstmt);
    }

    public List<String[]> query_fconn(){
        String selectstmt ="select FCMRU, METERNO,SEQNO, RDG_DATE,RDG_TIME," +
                "CUSTNAME,PRESRDG,CUSTADDRESS from T_FCONN";

        return query(selectstmt);
    }





    public List<String[]> query_upload(String mruNO, boolean isMultiBook){

        String selectstmt = " Select u.MRU as BOOKNO, u.ACCTNUM,u.ULDOCNO, c.METERNO,c.MR_TYPE_CODE as READTAG , c.RDG_DATE as RDGDATE ,c.RDG_TIME as RDGTIME ,\n" +
                "c.RECMD_SEQNO as SEQNO, c.FFCODE1 as BILLR_OC, c.FFCODE2 as FFCODE,c.REMARKS, c.PRESRDG as BILLED_RDG, c.RDG_TRIES as TRIES,\n" +
                "c.BILLED_CONS as BILLED_CONS, c.RANGE_CODE as RANGECODE, c.CONSTYPE_CODE as CONSTAG, c.MR_TYPE_CODE as NEWMTRBRAND,\n" +
                "c.NEW_METERNO as NEWMTRNUM, c.DEL_CODE as DEL_CODE,c.DELIV_DATE as DELIVERY_DATE, c.DELIV_TIME as DELIVERY_TIME, c.DELIV_REMARKS as DEL_REMARKS,\n" +
                "d.NUMUSERS as SANZPER,\n" +
                "d.TARIFF_PRORATE,d.CERA_PRORATE,d.FCDA_PRORATE,d.ENV_PRORATE,d.SEW_PROATE,\n" +
                "u.BASIC_CHARGE as BASECHRG, u.DISCOUNT,u.CERA,u.FCDA,u.ENV_CHARGE as ENVCHRG, u.SEWER_CHARGE as SEWERCHRG, 0 as PREPAYADJ,\n" +
                "u.MSC_AMOUNT as MSC, u.SC_DISCOUNT as SCDISC, u.TOTCHRG_WO_TAX as TOTCHRGWOTAX , u.VAT_CHARGE as VAT ,0 as PIA,\n" +
                "u.SUBTOTAL_AMT as TOTCURRCHRG, u.TOTAL_AMT_DUE as TOTAMT_DUE, u.PRINT_COUNT as BPRINTCNT, u.PRINT_TAG,m.READER_CODE ,c.BILLPRINT_DATE as PRINT_DATE,\n" +
                "d.SPBILL_RULE ,0 as SP_BILL_PRORATE, c.SP_COMP,c.GPS_LATITUDE,c.GPS_LONGITUDE  from T_UPLOAD u, T_CURRENT_RDG c, T_DOWNLOAD d,T_MRU_INFO m where   u.ULDOCNO = c.CRDOCNO and u.ULDOCNO = d.DLDOCNO \n" +
                "and u.MRU = m.MRU";
        Log.i("Test",selectstmt);
        StringBuilder str = new StringBuilder();
        str.append(selectstmt);
        if(!isMultiBook){
            str.append(" and  u.MRU='");
            str.append(mruNO);
            str.append('\'');
        }
        Log.i("Test",str.toString());

        return query(str.toString());

    }
    public void truncateMRUTable(){
        truncateTable("T_MRU_INFO");
    }

    public void truncateTables(){
        truncateTable("T_CURRENT_RDG");
        truncateTable("T_DOWNLOAD");
        truncateTable("T_FCONN");
        truncateTable("T_UPLOAD");
        truncateTable("T_SAP_DETAILS");
    }

}
