package com.indra.rover.mwsi.data.pojo.meter_reading.references;

import android.database.Cursor;



import java.io.Serializable;

/**
 *
 * Created by Indra on 8/26/2016.
 */
public class ObservationCode   implements Serializable{
    private String ff_code;
    private String ff_desc;
    private short bill_related;




    public ObservationCode(Cursor cursor){
        this.ff_code =cursor.getString(cursor.getColumnIndexOrThrow("FF_CODE"));
        this.ff_desc = cursor.getString(cursor.getColumnIndexOrThrow("FF_DESC"));
        this.bill_related = cursor.getShort(cursor.getColumnIndexOrThrow("BILL_RELATED"));
    }

    public void setFf_code(String ff_code) {
        this.ff_code = ff_code;
    }

    public String getFf_code() {
        return ff_code;
    }

    public void setFf_desc(String ff_desc) {
        this.ff_desc = ff_desc;
    }

    public short getBill_related() {
        return bill_related;
    }

    public void setBill_related(short bill_related) {
        this.bill_related = bill_related;
    }

    public String getFf_desc() {
        return ff_desc;
    }


}

