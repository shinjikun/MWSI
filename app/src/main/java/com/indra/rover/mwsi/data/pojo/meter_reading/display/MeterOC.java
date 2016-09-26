package com.indra.rover.mwsi.data.pojo.meter_reading.display;

import android.database.Cursor;

/**
 * Created by leonardoilagan on 17/09/2016.
 */

public class MeterOC {
    /**
     * Billed Related OC Code
     */
    String oc1;
    /**
     * Non-Billed Related OC Code
     */
    String oc2;
    /**
     * id
     */
    private String crdocno;
    /**
     * read status
     */
    private String readstat;
    /**
     * remarks
     */
    private String remarks;

    public MeterOC(Cursor cursor) {
        this.crdocno = cursor.getString(cursor.getColumnIndexOrThrow("CRDOCNO"));
        this.oc1 =  cursor.getString(cursor.getColumnIndexOrThrow("FFCODE1"));
        this.oc2 =  cursor.getString(cursor.getColumnIndexOrThrow("FFCODE2"));
        this.readstat = cursor.getString(cursor.getColumnIndexOrThrow("READSTAT"));
        this.remarks = cursor.getString(cursor.getColumnIndexOrThrow("REMARKS"));
    }


    public void setReadstat(String readstat) {
        this.readstat = readstat;
    }
}
