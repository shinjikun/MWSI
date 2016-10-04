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

    //bill scheme
    String bill_scheme;
    String account_num;
    //childs parent
    String childs_parent;

    public MeterOC(Cursor cursor) {
        this.crdocno = cursor.getString(cursor.getColumnIndexOrThrow("CRDOCNO"));
        this.oc1 =  cursor.getString(cursor.getColumnIndexOrThrow("FFCODE1"));
        this.oc2 =  cursor.getString(cursor.getColumnIndexOrThrow("FFCODE2"));
        this.readstat = cursor.getString(cursor.getColumnIndexOrThrow("READSTAT"));
        this.bill_scheme = cursor.getString(cursor.getColumnIndexOrThrow("CSMB_TYPE_CODE"));
        this.childs_parent = cursor.getString(cursor.getColumnIndexOrThrow("CSMB_PARENT"));
        this.account_num = cursor.getString(cursor.getColumnIndexOrThrow("ACCTNUM"));
    }

    public String getReadstat() {
        return readstat;
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

    public String getBill_scheme() {
        return bill_scheme;
    }

    public String getAccount_num() {
        return account_num;
    }

    public String getChilds_parent() {
        return childs_parent;
    }
}
