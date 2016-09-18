package com.indra.rover.mwsi.data.pojo.meter_reading;

import android.database.Cursor;

public class MeterRemarks {

    private String remarks;
    private String crdodcno;


    public MeterRemarks(Cursor cursor){
        this.remarks = cursor.getString(cursor.getColumnIndexOrThrow("REMARKS"));
        this.crdodcno = cursor.getString(cursor.getColumnIndexOrThrow("CRDOCNO"));
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
}
