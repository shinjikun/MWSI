package com.indra.rover.mwsi.data.pojo.meter_reading.display;

import android.database.Cursor;

public class MeterRemarks {

    private String remarks;
    private String crdodcno;
    private String readstat;


    public MeterRemarks(Cursor cursor){
        this.remarks = cursor.getString(cursor.getColumnIndexOrThrow("REMARKS"));
        this.crdodcno = cursor.getString(cursor.getColumnIndexOrThrow("CRDOCNO"));
        this.readstat = cursor.getString(cursor.getColumnIndexOrThrow("READSTAT"));
//        String meterNo = cursor.getString(cursor.getColumnIndexOrThrow("METERNO"));
//        String acctnum = cursor.getString(cursor.getColumnIndexOrThrow("ACCTNUM"));

    }



    public String getCrdodcno() {
        return crdodcno;
    }


    public String getRemarks() {
        return remarks;
    }


    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getReadstat() {
        return readstat;
    }

    public void setReadstat(String readstat) {
        this.readstat = readstat;
    }
}
