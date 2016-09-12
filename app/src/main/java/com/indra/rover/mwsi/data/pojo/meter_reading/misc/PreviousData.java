package com.indra.rover.mwsi.data.pojo.meter_reading.misc;

import android.database.Cursor;

import java.io.Serializable;

/**
 * Created by Indra on 9/8/2016.
 */
public class PreviousData implements Serializable {
   private String prevRemarks;
   private int prevFF1;
   private int prevFF2;
   private int actPrevReading;
   private String prevRDGDate;


    public PreviousData(Cursor cursor){
        this.prevRemarks =cursor.getString(cursor.getColumnIndexOrThrow("PREV_REMARKS"));
        this.prevRDGDate = cursor.getString(cursor.getColumnIndexOrThrow("PREVRDGDATE"));
        this.prevFF1 = cursor.getInt(cursor.getColumnIndexOrThrow("PREVFF1"));
        this.prevFF2 =  cursor.getInt(cursor.getColumnIndexOrThrow("PREVFF2"));
        this.actPrevReading = cursor.getInt(cursor.getColumnIndexOrThrow("ACTPREVRDG"));



    }

    public int getPrevFF1() {
        return prevFF1;
    }

    public int getActPrevReading() {
        return actPrevReading;
    }

    public int getPrevFF2() {
        return prevFF2;
    }

    public String getPrevRDGDate() {
        return prevRDGDate;
    }

    public String getPrevRemarks() {
        return prevRemarks;
    }
}
