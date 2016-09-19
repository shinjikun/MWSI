package com.indra.rover.mwsi.data.pojo.meter_reading;

import android.database.Cursor;

/**
 * Created by Indra on 9/19/2016.
 */
public class MeterConsumption {

    String  id;
    String acct_status;
    String meter_number;
    String num_dials;
    int max_cap;
    String grp_flag;
    String block_tag;
    String disc_tag;
    String prev_rdg_date;
    String actual_prev_rdg;
    String bill_prev_rdg;
    //bill prev reading 2 months ago
    String bill_prev_rdg2;
    String bill_prev_act_tag;
    //previous reading actual
    String pract_flag;
    //average consumption
    String ave_consumption;
    //nminitrdg
    String nminitrdg;
    //nminconsfactor
    String nminconsfactor;
    //previous oc Code 1
    String prevff1;
    //previous oc Code 2
    String prevff2;
    //OC code 1
    String ffcode1;
    //OC code 2
    String ffcode2;
    //present reading
    String present_rdg;
    //present billed consumption
    int billed_cons;
    String constype_code;


    public MeterConsumption(Cursor cursor){
        this.id =cursor.getString(cursor.getColumnIndexOrThrow("DLDOCNO"));
        this.acct_status = cursor.getString(cursor.getColumnIndexOrThrow("ACCT_STATUS"));
        this.meter_number = cursor.getString(cursor.getColumnIndexOrThrow("METERNO"));
        this.num_dials = cursor.getString(cursor.getColumnIndexOrThrow("NODIALS"));
        this.max_cap = cursor.getInt(cursor.getColumnIndexOrThrow("MAXCAP"));
        this.grp_flag = cursor.getString(cursor.getColumnIndexOrThrow("GRP_FLAG"));
        this.block_tag = cursor.getString(cursor.getColumnIndexOrThrow("BLOCK_TAG"));
        this.disc_tag = cursor.getString(cursor.getColumnIndexOrThrow("DISC_TAG"));
        this.prev_rdg_date = cursor.getString(cursor.getColumnIndexOrThrow("PREVRDGDATE"));
        this.actual_prev_rdg = cursor.getString(cursor.getColumnIndexOrThrow("ACTPREVRDG"));
        this.pract_flag = cursor.getString(cursor.getColumnIndexOrThrow("PRACTFLAG"));
        this.ave_consumption = cursor.getString(cursor.getColumnIndexOrThrow("AVECONS"));
        this.nminitrdg = cursor.getString(cursor.getColumnIndexOrThrow("NMINITRDG"));
        this.nminconsfactor = cursor.getString(cursor.getColumnIndexOrThrow("NMCONSFACTOR"));
        this.prevff1 = cursor.getString(cursor.getColumnIndexOrThrow("PREVFF1"));
        this.prevff2 = cursor.getString(cursor.getColumnIndexOrThrow("PREVFF2"));
        this.ffcode1 = cursor.getString(cursor.getColumnIndexOrThrow("FFCODE1"));
        this.ffcode2 = cursor.getString(cursor.getColumnIndexOrThrow("FFCODE2"));
        this.present_rdg = cursor.getString(cursor.getColumnIndexOrThrow("PRESRDG"));
        this.billed_cons = cursor.getInt(cursor.getColumnIndexOrThrow("BILLED_CONS"));
        this.constype_code = cursor.getString(cursor.getColumnIndexOrThrow("CONSTYPE_CODE"));
        this.bill_prev_rdg = cursor.getString(cursor.getColumnIndexOrThrow("BILLPREVRDG"));
        this.bill_prev_rdg2 = cursor.getString(cursor.getColumnIndexOrThrow("BILLPREVRDG2"));
        this.bill_prev_act_tag = cursor.getString(cursor.getColumnIndexOrThrow("BILLPREVACTTAG"));
    }

    /**
     *  Present Reading
     * @return returns present reading
     */
    public String getPresent_rdg() {
        return present_rdg;
    }

    /**
     * Bill previous reading
     * @return returns bill previous reading value
     */
    public String getBill_prev_rdg() {
        return bill_prev_rdg;
    }

    /**
     * number of dial corresponding value
     * @return
     */
    public int getMax_cap() {
        return max_cap;
    }

    public String getActual_prev_rdg() {
        return actual_prev_rdg;
    }


    public String getBill_prev_rdg2() {
        return bill_prev_rdg2;
    }

    public String getNminconsfactor() {
        return nminconsfactor;
    }

    public String getNminitrdg() {
        return nminitrdg;
    }

    public String getBlock_tag() {
        return block_tag;
    }
}
