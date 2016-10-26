package com.indra.rover.mwsi.data.pojo.meter_reading.references;

import android.database.Cursor;


/**
 * Created by Indra on 10/24/2016.
 */
public class SPBillRule {

    private String id;
    //String desc;
    String glratecomp;
    private double spl_rate;
    private double spl_oldrate;


    public SPBillRule(Cursor cursor){

        this.id =cursor.getString(cursor.getColumnIndexOrThrow("SPBILL_RULE"));
        //this.desc = cursor.getString(cursor.getColumnIndexOrThrow("SPBILL_DESC"));
        this.glratecomp = cursor.getString(cursor.getColumnIndexOrThrow("GLRATECOMP"));
        this.spl_rate = cursor.getDouble(cursor.getColumnIndexOrThrow("SPL_RATE"));
        this.spl_oldrate = cursor.getDouble(cursor.getColumnIndexOrThrow("OLD_SPECIAL_RATE"));

    }

    public String getId() {
        return id;
    }

    public double getSpl_rate() {
        return spl_rate;
    }

    public double getSpl_oldrate() {
        return spl_oldrate;
    }


    public String getGlratecomp() {
        return glratecomp;
    }
}
