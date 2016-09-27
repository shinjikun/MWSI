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


    public MeterOC(Cursor cursor) {
        this.crdocno = cursor.getString(cursor.getColumnIndexOrThrow("CRDOCNO"));
        this.oc1 =  cursor.getString(cursor.getColumnIndexOrThrow("FFCODE1"));
        this.oc2 =  cursor.getString(cursor.getColumnIndexOrThrow("FFCODE2"));
        this.readstat = cursor.getString(cursor.getColumnIndexOrThrow("READSTAT"));

    }


    public void setReadstat(String readstat) {
        this.readstat = readstat;
    }

    public String getOc1() {
        return oc1;
    }

    public String getOc2() {
        return oc2;
    }
}
