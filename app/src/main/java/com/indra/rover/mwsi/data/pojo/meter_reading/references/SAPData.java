package com.indra.rover.mwsi.data.pojo.meter_reading.references;

import android.database.Cursor;

public class SAPData {
    String lineCode;
    String docNO;
    String acctNum;
    String quantity;
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
        this.quantity = cursor.getString(cursor.getColumnIndexOrThrow("QUANTITY"));
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

    public String getQuantity() {
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


    public void setAcctNum(String acctNum) {
        this.acctNum = acctNum;
    }

    public void setDocNO(String docNO) {
        this.docNO = docNO;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setLineCode(String lineCode) {
        this.lineCode = lineCode;
    }

    public void setOld_amount(double old_amount) {
        this.old_amount = old_amount;
    }

    public void setOld_price(double old_price) {
        this.old_price = old_price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }
}
