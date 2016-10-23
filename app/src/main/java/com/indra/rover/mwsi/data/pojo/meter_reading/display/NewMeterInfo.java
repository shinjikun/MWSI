package com.indra.rover.mwsi.data.pojo.meter_reading.display;

import android.database.Cursor;

/**
 * Created by leonardoilagan on 22/10/2016.
 */

public class NewMeterInfo {

    String mru_id;
    String seqno;
    String meterNo;
    String custAdd;
    String custName;
    String presRdg;
    String rdg_date;
    String rdg_time;


    public  NewMeterInfo(){

    }


    public NewMeterInfo(Cursor cursor) {
        this.mru_id = cursor.getString(cursor.getColumnIndexOrThrow("FCMRU"));
        this.seqno = cursor.getString(cursor.getColumnIndexOrThrow("SEQNO"));
        this.meterNo = cursor.getString(cursor.getColumnIndexOrThrow("METERNO"));
        this.custAdd = cursor.getString(cursor.getColumnIndexOrThrow("CUSTADDRESS"));
        this.custName = cursor.getString(cursor.getColumnIndexOrThrow("CUSTNAME"));
        this.presRdg = cursor.getString(cursor.getColumnIndexOrThrow("PRESRDG"));
        this.rdg_date = cursor.getString(cursor.getColumnIndexOrThrow("RDG_DATE"));
        this.rdg_time = cursor.getString(cursor.getColumnIndexOrThrow("RDG_TIME"));


    }

    public String getMeterNo() {
        return meterNo;
    }

    public String getCustAdd() {
        return custAdd;
    }

    public String getCustName() {
        return custName;
    }

    public String getMru_id() {
        return mru_id;
    }

    public String getPresRdg() {
        return presRdg;
    }

    public String getSeqno() {
        return seqno;
    }

    public String getRdg_date() {
        return rdg_date;
    }

    public String getRdg_time() {
        return rdg_time;
    }

    public void setCustAdd(String custAdd) {
        this.custAdd = custAdd;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo;
    }

    public void setMru_id(String mru_id) {
        this.mru_id = mru_id;
    }

    public void setPresRdg(String presRdg) {
        this.presRdg = presRdg;
    }

    public void setRdg_date(String rdg_date) {
        this.rdg_date = rdg_date;
    }

    public void setRdg_time(String rdg_time) {
        this.rdg_time = rdg_time;
    }

    public void setSeqno(String seqno) {
        this.seqno = seqno;
    }

}
