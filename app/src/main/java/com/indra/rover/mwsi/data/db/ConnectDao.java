package com.indra.rover.mwsi.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.indra.rover.mwsi.data.pojo.MRU;
import com.indra.rover.mwsi.data.pojo.T_Download_Info;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by leonardoilagan on 08/09/2016.
 */

public class ConnectDao extends ModelDao {

    public ConnectDao(Context context){
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

    public long insertTCurrRDGData(String[] records){
        long rowInsert =0;
        try {
            open();
            ContentValues values = new ContentValues();
            if(isExistData("T_CURRENT_RDG","CRDOCNO",records[10])){
                return -1 ;
            }
            values.put("ACCTNUM",records[2]);
            values.put("METERNO",records[9]);
            values.put("CRDOCNO",records[10]);
            values.put("CSMB_TYPE_CODE",records[67]);
            values.put("CSMB_PARENT",records[68]);
            values.put("MB_PREF_FLAG",records[69]);
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
            values.put("INSTALL_WTR_IND",records[50]);
            values.put("INSTALL_SEW_IND",records[52]);
            values.put("INSTALL_AR_IND",records[54]);
            values.put("INSTALL_ADV_IND",records[56]);
            values.put("TIN",records[63]);
            values.put("VAT_EXEMPT",records[64]);
            values.put("DISCHECK_FLAG",records[65]);
            values.put("NUMUSERS",records[66]);
            values.put("PREVCONSLINE1",records[70]);
            values.put("PREVCONSLINE2",records[71]);
            values.put("SOA_NUMBER",records[72]);
            values.put("SPBILL_RULE",records[73]);
            values.put("SP_BILL_EFF_DATE",records[74]);
            rowInsert = database.insert("T_DOWNLOAD", null, values);
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
            values.put("SEPTIC_CHARGE",records[44]);
            values.put("CHANGESIZE_CHARGE",records[45]);
            values.put("RESTORATION_CHARGE",records[46]);
            values.put("MISC_CHARGE",records[47]);
            values.put("INSTALL_SEWER_CHARGE",records[48]);
            values.put("ADVANCES",records[49]);
            values.put("INSTALL_SEWER_DUE",records[51]);
            values.put("INSTALL_AR_DUE",records[53]);
            values.put("INSTALL_ADV_DUE",records[55]);
            values.put("INSTALL_WATER_DUE",records[57]);
            values.put("INSTALL_WATER_CHARGE",records[58]);
            values.put("REOPENING_FEE",records[59]);
            values.put("METER_CHARGES",records[60]);
            values.put("GD_CHARGE",records[61]);
            values.put("OTHER_CHARGES",records[62]);
            values.put("ULDOCNO",records[10]);
            rowInsert = database.insert("T_UPLOAD", null, values);

        }catch(Exception e){
            e.printStackTrace();
        } finally {
            close();
        }
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




     boolean isExistData(String tablename,String columnname,String param){
        String selectSql = String.format(
                "SELECT "+columnname+" from "+tablename+" where "+columnname+" = \"%s\" limit 1", param);
        Cursor cursor = database.rawQuery(selectSql, null);

        boolean result = cursor.moveToFirst();
         cursor.close();
        return result;
    }


    public List<String[]> query_upload(String mruNO){
        List<String[]> arry = new ArrayList<>();
        String selectstmt = "Select u.MRU as BOOKNO, u.ACCTNUM,u.ULDOCNO, c.METERNO,c.MR_TYPE_CODE as READTAG , c.RDG_DATE as RDGDATE ,c.RDG_TIME as RDGTIME ," +
                "c.RECMD_SEQNO as SEQNO, c.FFCODE1 as BILLR_OC, c.FFCODE2 as FFCODE,c.REMARKS, c.PRESRDG as BILLED_RDG, c.RDG_TRIES as TRIES," +
                "c.BILLED_CONS as BILLED_CONS, c.RANGE_CODE as RANDECODE, c.CONSTYPE_CODE as CONSTAG, c.MR_TYPE_CODE as NEWMTRBRAND," +
                "c.NEW_METERNO as NEWMTRNUM, c.DEL_CODE as DEL_CODE,c.DELIV_DATE as DELIVERY_DATE, c.DELIV_TIME as DELIVERY_TIME, c.DELIV_REMARKS as DEL_REMARKS," +
                "1 as SANZPER,u.BASIC_CHARGE as BASECHRG, u.DISCOUNT,u.CERA,u.FCDA,u.ENV_CHARGE as ENVCHRG, u.SEWER_CHARGE as SEWERCHRG, 1 as PREPAYADJ," +
                "u.METER_CHARGES as MSC, u.SC_DISCOUNT as SCDISC, u.TOTCHRG_WO_TAX as TOTCHRGWOTAX , u.VAT_CHARGE as VAT ,1 as PIA," +
                "u.SUBTOTAL_AMT as TOTCURRCHRG, u.TOTAL_AMT_DUE as TOTAMT_DUE, u.PRINT_COUNT as BPRINTCNT, u.PRINT_TAG ,c.BILLPRINT_DATE as PRINT_DATE," +
                "1 as SP_BILL_RULE , c.SP_COMP from T_UPLOAD u, T_CURRENT_RDG c where u.ULDOCNO = c.CRDOCNO and  u.MRU="+mruNO;
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



}
