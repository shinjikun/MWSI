package com.indra.rover.mwsi.data.pojo.meter_reading.display;

import android.database.Cursor;

public class MeterDelivery {

    /**
     *  Delivery Remarks
     */
    private String dev_remarks;
    /**
     * Delivery Description
     */
    private String dev_code;
    /**
     * id
     */
    private String crdodcno;
    /**
     * read status
     */
    private String readstat;
    /**
     * delivery time
     */
    private String deliv_time;
    /**
     * delivery date
     */
    private String deliv_date;


    public MeterDelivery(Cursor cursor){
        this.crdodcno = cursor.getString(cursor.getColumnIndexOrThrow("CRDOCNO"));
        this.readstat = cursor.getString(cursor.getColumnIndexOrThrow("READSTAT"));
        this.dev_code = cursor.getString(cursor.getColumnIndexOrThrow("DEL_CODE"));
        this.dev_remarks = cursor.getString(cursor.getColumnIndexOrThrow("DELIV_REMARKS"));
        this.deliv_date = cursor.getString(cursor.getColumnIndexOrThrow("DELIV_DATE"));
        this.deliv_time = cursor.getString(cursor.getColumnIndexOrThrow("DELIV_TIME"));

    }



    public String getDev_remarks() {
        return dev_remarks;
    }

    public String getDev_code() {
        return dev_code;
    }

    public void setDev_remarks(String dev_remarks) {
        this.dev_remarks = dev_remarks;
    }

    public String getDeliv_date() {
        return deliv_date;
    }

    public String getDeliv_time() {
        return deliv_time;
    }

    public String getReadstat() {
        return readstat;
    }

    public void setReadstat(String readstat) {
        this.readstat = readstat;
    }
}
