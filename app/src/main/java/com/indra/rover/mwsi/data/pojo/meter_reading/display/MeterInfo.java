package com.indra.rover.mwsi.data.pojo.meter_reading.display;

import android.database.Cursor;

import com.indra.rover.mwsi.data.pojo.meter_reading.misc.CustomerInfo;
import com.indra.rover.mwsi.data.pojo.meter_reading.references.BillClass;

import java.io.Serializable;

/**
 * Created by Indra on 9/7/2016.
 */
public class MeterInfo implements Serializable {

    /**
     * MRU ID
     */
    String mru_id;

    /**
     *  Sequence Number
     */
    String seq_number;


    String meter_number;
    String grp_flag;
    String block_tag;
    String dldocno;
    CustomerInfo customerInfo;

    BillClass billClass;

    String rdg_tries;
    String present_reading;
    //read stat
    String readStat;
    //bill scheme
    String bill_scheme;

    //childs parent
    String childs_parent;
    public MeterInfo(Cursor cursor){
        this.mru_id =cursor.getString(cursor.getColumnIndexOrThrow("MRU"));
        this.seq_number = cursor.getString(cursor.getColumnIndexOrThrow("SEQNO"));
        this.meter_number = cursor.getString(cursor.getColumnIndexOrThrow("METERNO"));
        this.dldocno = cursor.getString(cursor.getColumnIndexOrThrow("DLDOCNO"));
        this.grp_flag = cursor.getString(cursor.getColumnIndexOrThrow("GRP_FLAG"));
        this.block_tag = cursor.getString(cursor.getColumnIndexOrThrow("BLOCK_TAG"));
        this.rdg_tries = cursor.getString(cursor.getColumnIndexOrThrow("RDG_TRIES"));
        this.present_reading = cursor.getString(cursor.getColumnIndexOrThrow("PRESRDG"));
        this.readStat = cursor.getString(cursor.getColumnIndexOrThrow("READSTAT"));
        this.bill_scheme = cursor.getString(cursor.getColumnIndexOrThrow("CSMB_TYPE_CODE"));
        this.childs_parent = cursor.getString(cursor.getColumnIndexOrThrow("CSMB_PARENT"));
        this.customerInfo = new CustomerInfo(cursor);
        this.billClass = new BillClass(cursor);


    }


    public CustomerInfo getCustomer() {
        return customerInfo;
    }


    public String getMeter_number() {
        return meter_number;
    }

    public String getMru_id() {
        return mru_id;
    }

    public String getSeq_number() {
        return seq_number;
    }

    public BillClass getBillClass() {
        return billClass;
    }

    public String getDldocno() {
        return dldocno;
    }



    public String getBlock_tag() {
        return block_tag;
    }

    public String getGrp_flag() {
        return grp_flag;
    }



    public void setReadStat(String readStat) {
        this.readStat = readStat;
    }

    public String getReadStat() {
        return readStat;
    }

    public String getRdg_tries() {
        return rdg_tries;
    }

    /**
     *  get the present meter reading
     * @return
     */
    public String getPresRdg() {
        return present_reading;
    }

    public void setPresent_reading(String present_reading) {
        this.present_reading = present_reading;
    }

    public void setRdg_tries(String rdg_tries) {
        this.rdg_tries = rdg_tries;
    }

    public String getBill_scheme() {
        return bill_scheme;
    }
}
