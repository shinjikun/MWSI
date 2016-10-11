package com.indra.rover.mwsi.data.pojo.meter_reading.references;

import android.database.Cursor;

public class SAPData {
    String lineCode;
    String docNO;
    String acctNum;
    int quantity;
    double price;
    double amount;
    double total_amount;
    //old
    double old_price;
    double old_amount;


    public SAPData(){

    }

    public SAPData(Cursor cursor){
        this.lineCode =cursor.getString(cursor.getColumnIndexOrThrow("SAP_LINE_CODE"));
        this.docNO = cursor.getString(cursor.getColumnIndexOrThrow("SAPDOCNO"));
        this.acctNum = cursor.getString(cursor.getColumnIndexOrThrow("ACCTNUM"));
        this.quantity = cursor.getInt(cursor.getColumnIndexOrThrow("QUANTITY"));
        this.price = cursor.getDouble(cursor.getColumnIndexOrThrow("PRICE"));
        this.amount = cursor.getDouble(cursor.getColumnIndexOrThrow("AMOUNT"));
        this.total_amount = cursor.getDouble(cursor.getColumnIndexOrThrow("TOTAL_AMOUNT"));
        //old
        this.old_price = cursor.getDouble(cursor.getColumnIndexOrThrow("OLD_PRICE"));
        this.old_amount = cursor.getDouble(cursor.getColumnIndexOrThrow("OLD_AMOUNT"));
    }

    public String getLineCode() {
        return lineCode;
    }

    public String getDocNO() {
        return docNO;
    }

    public String getAcctNum() {
        return acctNum;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getAmount() {
        return amount;
    }

    public double getOld_amount() {
        return old_amount;
    }

    public double getOld_price() {
        return old_price;
    }

    public double getTotal_amount() {
        return total_amount;
    }

}
