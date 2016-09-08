package com.indra.rover.mwsi.data.pojo;

import android.database.Cursor;

import com.indra.rover.mwsi.data.pojo.meter_reading.misc.InstallMisc;

/**
 * Created by Indra on 9/7/2016.
 */
public class MeterReading {

   public static final  int from_T_DOWNLOAD =0;
   public static final  int from_T_CURRENT_RDG =1 ;

    public MeterReading(Cursor cursor, int type){

        this.mru = cursor.getString(cursor.getColumnIndexOrThrow("MRU"));
        this.meterno = cursor.getString(cursor.getColumnIndexOrThrow("METERNO"));
        this.acctnum = cursor.getString(cursor.getColumnIndexOrThrow("ACCTNUM"));
        this.csmb_type_code =  cursor.getInt(cursor.getColumnIndexOrThrow("CSMB_TYPE_CODE"));
        this.csmb_parent = cursor.getString(cursor.getColumnIndexOrThrow("CSMB_PARENT"));
        this.mb_pref_flag = cursor.getShort(cursor.getColumnIndexOrThrow("MR_REP_FLAG"));
        if(type ==  from_T_CURRENT_RDG){
            this.crdocno =  cursor.getString(cursor.getColumnIndexOrThrow("CRDOCNO"));
            this.readstat = cursor.getString(cursor.getColumnIndexOrThrow("READSTAT"));
            this.rdg_date = cursor.getString(cursor.getColumnIndexOrThrow("RDG_DATE"));
            this.rdg_time = cursor.getString(cursor.getColumnIndexOrThrow("RDG_TIME"));
            this.recmd_seqno = cursor.getString(cursor.getColumnIndexOrThrow("RECMD_SEQNO"));
            this.new_meterno = cursor.getString(cursor.getColumnIndexOrThrow("NEW_METERNO"));
            this.ffcode1 = cursor.getString(cursor.getColumnIndexOrThrow("FFCODE1"));
            this.ffcode2 = cursor.getString(cursor.getColumnIndexOrThrow("FFCODE2"));
            this.remarks = cursor.getString(cursor.getColumnIndexOrThrow("REMARKS"));
            this.presentRDG = cursor.getInt(cursor.getColumnIndexOrThrow("PRESRDG"));
            this.range_code = cursor.getString(cursor.getColumnIndexOrThrow("RANGE_CODE"));
            this.mr_type_code  = cursor.getString(cursor.getColumnIndexOrThrow("MR_TYPE_CODE"));
            this.billed_cons = cursor.getInt(cursor.getColumnIndexOrThrow("BILLED_CONS"));
            this.csmb_orign_cons = cursor.getInt(cursor.getColumnIndexOrThrow("CSMB_ORIG_CONS"));
            this.constype_code = cursor.getInt(cursor.getColumnIndexOrThrow("CONSTYPE_CODE"));
            this.del_code = cursor.getString(cursor.getColumnIndexOrThrow("DEL_CODE"));
            this.dev_date = cursor.getString(cursor.getColumnIndexOrThrow("DELIV_DATE"));
            this.delivery_time = cursor.getString(cursor.getColumnIndexOrThrow("DELIV_TIME"));
            this.dev_remarks = cursor.getString(cursor.getColumnIndexOrThrow("DELIV_REMARKS"));
            this.rdg_tries = cursor.getInt(cursor.getColumnIndexOrThrow("RDG_TRIES"));
            this.sp_comp = cursor.getInt(cursor.getColumnIndexOrThrow("SP_COMP"));
            this.gps_latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("GPLS_LATITUDE"));
            this.gps_longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("GPS_LONGITUDE"));
            this.bill_print_date = cursor.getString(cursor.getColumnIndexOrThrow("BILLPRINT_DATE"));

        }
        else  if(type == from_T_DOWNLOAD){
            this.crdocno =  cursor.getString(cursor.getColumnIndexOrThrow("DLDOCNO"));
        }
    }

    public MeterReading(){

    }

    public MeterReading(Cursor cursor){
        this(cursor,1);

    }



    /**
     *
     */
    String crdocno;
    /**
     * MRU
     */
    String mru;
    /**
     *  user's account number
     */
    String acctnum;
    /**
     * meter number
     */
    String meterno;
    String readstat;
    /**
     * reading date
     */
    String rdg_date;
    /**
     * reading time
     */
    String rdg_time;

    String recmd_seqno;
    String new_meterno;
    /**
     *  Observation Code 1
     */
    String ffcode1;
    /**
     *  Observation Code 2
     */
    String ffcode2;
    /**
     *  remarks
     */
    String remarks;
    /**
     * current or present reading
     */
    int presentRDG;
    String range_code;
    String mr_type_code;
    int billed_cons;
    int csmb_orign_cons;
    int constype_code;
    /**
     * Delivery Code
     */
    String del_code;
    /**
     * Date of deliver of receipt to customer
     */
    String dev_date;
    /**
     *  Time of delivery of receipt to customer
     */
    String delivery_time;
    /**
     * delivery remarks
     */
    String dev_remarks;
    int csmb_type_code;
    String csmb_parent;
    short mb_pref_flag;


    /**
     * date of printing of bill
     */
    String bill_print_date;
    short mr_rep_flag;
    /**
     *  number of user tries to enter a reading
     */
    int rdg_tries;
    int sp_comp;
    /**
     * Latitude record of meter
     */
    double gps_latitude;
    /**
     *  Longitutude record to meter
     */
    double gps_longitude;

    public double getGps_latitude() {
        return gps_latitude;
    }

    public double getGps_longitude() {
        return gps_longitude;
    }

    public void setMru(String mru) {
        this.mru = mru;
    }

    public void setAcctnum(String acctnum) {
        this.acctnum = acctnum;
    }

    public void setMeterno(String meterno) {
        this.meterno = meterno;
    }

    public void setCrdocno(String crdocno) {
        this.crdocno = crdocno;
    }

    /**
     * Installation Miscellaniuos
     */
    InstallMisc installMisc;

    public void setInstallMisc(InstallMisc installMisc) {
        this.installMisc = installMisc;
    }

    public InstallMisc getInstallMisc() {
        return installMisc;
    }
}
