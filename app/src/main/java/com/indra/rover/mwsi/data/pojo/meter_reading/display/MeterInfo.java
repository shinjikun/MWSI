package com.indra.rover.mwsi.data.pojo.meter_reading.display;

import android.database.Cursor;

import com.indra.rover.mwsi.data.pojo.meter_reading.misc.CustomerInfo;
import com.indra.rover.mwsi.data.pojo.meter_reading.references.BillClass;
import com.indra.rover.mwsi.data.pojo.meter_reading.misc.InstallMisc;
import com.indra.rover.mwsi.data.pojo.meter_reading.misc.PreviousData;
import com.indra.rover.mwsi.data.pojo.meter_reading.misc.ProRate;

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
    PreviousData previousData;
    public MeterInfo(Cursor cursor){
        this.mru_id =cursor.getString(cursor.getColumnIndexOrThrow("MRU"));
        this.seq_number = cursor.getString(cursor.getColumnIndexOrThrow("SEQNO"));
        this.meter_number = cursor.getString(cursor.getColumnIndexOrThrow("METERNO"));
        this.dldocno = cursor.getString(cursor.getColumnIndexOrThrow("DLDOCNO"));
        this.grp_flag = cursor.getString(cursor.getColumnIndexOrThrow("GRP_FLAG"));
        this.block_tag = cursor.getString(cursor.getColumnIndexOrThrow("BLOCK_TAG"));
        this.customerInfo = new CustomerInfo(cursor);
        this.billClass = new BillClass(cursor);
        this.previousData = new PreviousData(cursor);
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

    public PreviousData getPreviousData() {
        return previousData;
    }


    public String getBlock_tag() {
        return block_tag;
    }

    public String getGrp_flag() {
        return grp_flag;
    }

    //read stat
    String readStat;

    public void setReadStat(String readStat) {
        this.readStat = readStat;
    }

    public String getReadStat() {
        return readStat;
    }
}
