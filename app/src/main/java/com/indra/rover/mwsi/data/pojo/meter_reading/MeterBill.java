package com.indra.rover.mwsi.data.pojo.meter_reading;

import android.database.Cursor;

/**
 * Created by Indra on 9/27/2016.
 */
public class MeterBill {

    String id;
    int consumption;
    String billClass;
    String ratetype;
    double basicCharge;
    double discount;
    double subtotal;
    double totalAmt;
    String bulk_flg;
    String gt34_flg;
    /**
     * Present Reading Date
     */
    String presRdgDate;
    /**
     * Previous Reading Date
     */
    String prevRdgDate;

    public MeterBill(Cursor cursor){
        this.id =cursor.getString(cursor.getColumnIndexOrThrow("DLDOCNO"));
        this.consumption = cursor.getInt(cursor.getColumnIndexOrThrow("BILLED_CONS"));
        this.billClass = cursor.getString(cursor.getColumnIndexOrThrow("BILL_CLASS"));
        this.ratetype = cursor.getString(cursor.getColumnIndexOrThrow("RATE_TYPE"));
        this.basicCharge = cursor.getDouble(cursor.getColumnIndexOrThrow("BASIC_CHARGE"));
        this.discount = cursor.getDouble(cursor.getColumnIndexOrThrow("DISCOUNT"));
        this.subtotal = cursor.getDouble(cursor.getColumnIndexOrThrow("SUBTOTAL_AMT"));
        this.totalAmt = cursor.getDouble(cursor.getColumnIndexOrThrow("TOTAL_AMT_DUE"));
        this.bulk_flg = cursor.getString(cursor.getColumnIndexOrThrow("BULK_FLAG"));
        this.gt34_flg = cursor.getString(cursor.getColumnIndexOrThrow("GT34FLAG"));
        this.presRdgDate =  cursor.getString(cursor.getColumnIndexOrThrow("PRESRDG"));
        this.prevRdgDate =  cursor.getString(cursor.getColumnIndexOrThrow("PREVRDGDATE"));
    }

    public String getGt34_flg() {
        return gt34_flg;
    }

    public String getBulk_flg() {
        return bulk_flg;
    }

    public int getConsumption() {
        return consumption;
    }

    public String getBillClass() {
        return billClass;
    }

    public void setBasicCharge(double basicCharge) {
        this.basicCharge = basicCharge;
    }

    public String getId() {
        return id;
    }
}
