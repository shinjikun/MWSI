package com.indra.rover.mwsi.data.pojo;

import android.database.Cursor;

import com.indra.rover.mwsi.data.pojo.meter_reading.references.BillClass;
import com.indra.rover.mwsi.data.pojo.meter_reading.misc.InstallMisc;
import com.indra.rover.mwsi.data.pojo.meter_reading.misc.PreviousData;
import com.indra.rover.mwsi.data.pojo.meter_reading.misc.ProRate;

import java.io.Serializable;

/**
 * Created by Indra on 9/7/2016.
 */
public class T_Download_Info implements Serializable {

    /**
     * MRU ID
     */
    String mru_id;

    /**
     *  Sequence Number
     */
    String seq_number;



    String rate_type;
    String bulk_flag;
    int acct_status;
    String meter_number;
    String mterial_number;
    int meter_size;
    int num_dials;
    String grp_flag;
    String block_tag;
    String disc_tag;
    short pracflag;
    short pconsavgflag;
    int ave_consumpstions;
    int dpreplmtr_code;

    ProRate prorates;
    PreviousData prevReading;
    String wbpaydtls1;
    String wbpaydtls2;
    String miscpaydtls;
    String gdpaydtls;
    InstallMisc installMisc;
    String tin;
    short vat_exempt;
    short djscheck_flg;
    int numuser;

    int nmintRDG;

    int nmConsfactor;
    int gt34flag;
    int gt34factor;

    String soa_number;
    int spbill_rule;
    String spbill_eff_date;
    String dldocno;
    CustomerInfo customerInfo;

    BillClass billClass;
    PreviousData previousData;
    public T_Download_Info(Cursor cursor){
        this.mru_id =cursor.getString(cursor.getColumnIndexOrThrow("MRU"));
        this.seq_number = cursor.getString(cursor.getColumnIndexOrThrow("SEQNO"));
        this.meter_number = cursor.getString(cursor.getColumnIndexOrThrow("METERNO"));
        this.dldocno = cursor.getString(cursor.getColumnIndexOrThrow("DLDOCNO"));
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
}
