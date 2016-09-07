package com.indra.rover.mwsi.data.pojo;

import android.database.Cursor;

/**
 * Created by Indra on 9/7/2016.
 */
public class Customer {

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


    public Customer(Cursor cursor){
        this.accn =cursor.getString(cursor.getColumnIndexOrThrow("ACCTNUM"));
        this.cname = cursor.getString(cursor.getColumnIndexOrThrow("CUSTNAME"));
        this.address = cursor.getString(cursor.getColumnIndexOrThrow("CUSTADDRESS"));
    }

    public void setCname(String name) {
        this.cname = name;
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

    public void setAccn(String accn) {
        this.accn = accn;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
