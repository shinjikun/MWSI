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


}
