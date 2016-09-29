package com.indra.rover.mwsi.data.pojo.meter_reading.display;

import android.database.Cursor;

import com.indra.rover.mwsi.data.pojo.meter_reading.misc.CustomerInfo;


public class MeterRHistory {


   private CustomerInfo customerInfo;

    private String prevRemarks;
    private String prevFF1;
    private String prevFF2;
    private String actPrevReading;
    private String prevRDGDate;
    public MeterRHistory(Cursor cursor){
        this.prevRemarks =cursor.getString(cursor.getColumnIndexOrThrow("PREV_REMARKS"));
        this.prevRDGDate = cursor.getString(cursor.getColumnIndexOrThrow("PREVRDGDATE"));
        this.prevFF1 = cursor.getString(cursor.getColumnIndexOrThrow("PREVFF1"));
        this.prevFF2 =  cursor.getString(cursor.getColumnIndexOrThrow("PREVFF2"));
        this.actPrevReading = cursor.getString(cursor.getColumnIndexOrThrow("ACTPREVRDG"));
        this.customerInfo = new CustomerInfo(cursor);
    }


    public String getPrevFF1() {
        return prevFF1;
    }

    public String getActPrevReading() {
        return actPrevReading;
    }

    public String getPrevFF2() {
        return prevFF2;
    }

    public String getPrevRDGDate() {
        return prevRDGDate;
    }

    public String getPrevRemarks() {
        return prevRemarks;
    }

    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }
}
