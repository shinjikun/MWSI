package com.indra.rover.mwsi.data.pojo.meter_reading.references;

import android.database.Cursor;

/**
 * Created by Indra on 10/11/2016.
 */
public class Tariff {

    String billClass;
    int cons_band;
    int lowLimit;
    int highLimit;
    double baseAmount;
    double price;
    double tierAmount;
    double old_baseAmount;
    double old_price;
    double old_tierAmount;
    String effectDate;

    public Tariff(Cursor cursor){
        this.billClass =cursor.getString(cursor.getColumnIndexOrThrow("BILL_CLASS"));
        this.cons_band = cursor.getInt(cursor.getColumnIndexOrThrow("CONS_BAND"));
        this.lowLimit = cursor.getInt(cursor.getColumnIndexOrThrow("LOW_LIMIT"));
        this.highLimit = cursor.getInt(cursor.getColumnIndexOrThrow("HIGH_LIMIT"));
        this.baseAmount = cursor.getDouble(cursor.getColumnIndexOrThrow("BASEAMOUNT"));
        this.price = cursor.getDouble(cursor.getColumnIndexOrThrow("PRICE"));
        this.tierAmount = cursor.getDouble(cursor.getColumnIndexOrThrow("TIER_AMOUNT"));
        //old
        this.old_baseAmount = cursor.getDouble(cursor.getColumnIndexOrThrow("OLD_BASEAMOUNT"));
        this.old_price = cursor.getDouble(cursor.getColumnIndexOrThrow("OLD_PRICE"));
        this.old_tierAmount = cursor.getDouble(cursor.getColumnIndexOrThrow("OLD_TIER_AMOUNT"));
        this.effectDate = cursor.getString(cursor.getColumnIndexOrThrow("EFFECTIVITY_DATE"));
    }
}
