package com.indra.rover.mwsi.data.pojo.meter_reading.misc;

import android.database.Cursor;

/**
 * Created by Indra on 9/7/2016.
 */
public class CustomerInfo {

    /**
     *  customer account number
     */
    String accn;
    /**
     * customer name
     */
    String cname;
    /**
     *  customer Address
     */
    String address;



    public CustomerInfo(Cursor cursor){
        this.accn =cursor.getString(cursor.getColumnIndexOrThrow("ACCTNUM"));
        this.cname = cursor.getString(cursor.getColumnIndexOrThrow("CUSTNAME"));
        this.address = cursor.getString(cursor.getColumnIndexOrThrow("CUSTADDRESS"));
    }


    public String getCname() {
        return cname;
    }

    public String getAccn() {
        return accn;
    }

    public String getAddress() {
        return address;
    }

  }
