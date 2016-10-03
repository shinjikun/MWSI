package com.indra.rover.mwsi.data.pojo.meter_reading;

import android.database.Cursor;


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
    String prev_rdg;
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
    //Has new meter info, replacement date or initial reading?
    String dreplmtr_code;
    /**
     * Special  Computation
     */
    String spComp;
    /**
     * Previous Consumption AVERAGE
     */
    String prev_con_avg;

    String  csmb_type_code;
    String  csmb_parent;
    String acct_num;
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
        this.prev_rdg = cursor.getString(cursor.getColumnIndexOrThrow("ACTPREVRDG"));
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
        this.dreplmtr_code = cursor.getString(cursor.getColumnIndexOrThrow("DREPLMTR_CODE"));
        this.prev_con_avg = cursor.getString(cursor.getColumnIndexOrThrow("PCONSAVGFLAG"));
        this.spComp = cursor.getString(cursor.getColumnIndexOrThrow("SP_COMP"));
        this.csmb_parent = cursor.getString(cursor.getColumnIndexOrThrow("CSMB_PARENT"));
        this.csmb_type_code = cursor.getString(cursor.getColumnIndexOrThrow("CSMB_TYPE_CODE"));
        this.acct_num =  cursor.getString(cursor.getColumnIndexOrThrow("ACCTNUM"));
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

    public String getPrev_rdg() {
        return prev_rdg;
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

    public String getFfcode1() {
        return ffcode1;
    }

    public String getFfcode2() {
        return ffcode2;
    }

    public void setConstype_code(String constype_code) {
        this.constype_code = constype_code;
    }

    public void setBilled_cons(int billed_cons) {
        this.billed_cons = billed_cons;
    }

    public String getAve_consumption() {
        return ave_consumption;
    }

    public String getPrevff2() {
        return prevff2;
    }

    public String getDreplmtr_code() {
        return dreplmtr_code;
    }

    public String getPrev_con_avg() {
        return prev_con_avg;
    }

    public String getPrevff1() {
        return prevff1;
    }

    public String getNum_dials() {
        return num_dials;
    }

    public String getBill_prev_act_tag() {
        return bill_prev_act_tag;
    }

    public void setPresent_rdg(String present_rdg) {
        this.present_rdg = present_rdg;
    }

    public int getBilled_cons() {
        return billed_cons;
    }

    public String getConstype_code() {
        return constype_code;
    }


    public void setSpComp(String spComp) {
        this.spComp = spComp;
    }

    public String getSpComp() {
        return spComp;
    }

    public String getCsmb_parent() {
        return csmb_parent;
    }

    public String getCsmb_type_code() {
        return csmb_type_code;
    }

    public void setAcct_num(String acct_num) {
        this.acct_num = acct_num;
    }

    public String getAcct_num() {
        return acct_num;
    }
}
