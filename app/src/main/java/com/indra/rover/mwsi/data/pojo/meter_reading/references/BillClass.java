package com.indra.rover.mwsi.data.pojo.meter_reading.references;

import android.database.Cursor;

import java.io.Serializable;

/**
 * Created by Indra on 9/9/2016.
 */
public class BillClass implements Serializable{

    private String bill_class;
    private String bill_class_desc;

    public BillClass(Cursor cursor){
        this.bill_class =cursor.getString(cursor.getColumnIndexOrThrow("BILL_CLASS"));
        this.bill_class_desc = cursor.getString(cursor.getColumnIndexOrThrow("BILL_CLASS_DESC"));


    }

    public String getBill_class() {
        return bill_class;
    }

    public String getDesc() {
        return bill_class_desc;
    }
}
