package com.indra.rover.mwsi.data.pojo.meter_reading;

import android.database.Cursor;

public class MeterBill {

    private  String id;
    private int consumption;
    private String billClass;
    private String ratetype;
    private double basicCharge;
    private double discount;
    private double subtotal;
    private double totalAmt;
    private String bulk_flg;
    private String gt34_flg;
    private String acctNum;
    private String vatExempt;
    private String numusers;
    /**
     * Present Reading Date
     */
    String presRdgDate;
    /**
     * Previous Reading Date
     */
    String prevRdgDate;

    double msc_amount;

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
        this.acctNum = cursor.getString(cursor.getColumnIndexOrThrow("ACCTNUM"));
        this.msc_amount = cursor.getDouble(cursor.getColumnIndexOrThrow("MSC_AMOUNT"));
        this.vatExempt = cursor.getString(cursor.getColumnIndexOrThrow("VAT_EXEMPT"));
        this.numusers = cursor.getString(cursor.getColumnIndexOrThrow("NUMUSERS"));
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

    public String getAcctNum() {
        return acctNum;
    }

    public double getBasicCharge() {
        return basicCharge;
    }

    public String getRatetype() {
        return ratetype;
    }



    public double getMsc_amount() {
        return msc_amount;
    }


    double cera;
    double fcda;
    double env_charge;
    double sewer_charge;
    double sc_discount;
    /**
     * Total current charges before tax
     */
    private double totcurb4tax;

    private double totcurcharge;
    private double vat;


    public void setCera(double cera) {
        this.cera = cera;
    }

    public void setFcda(double fcda) {
        this.fcda = fcda;
    }

    public void setEnv_charge(double env_charge) {
        this.env_charge = env_charge;
    }

    public double getEnv_charge() {
        return env_charge;
    }

    public void setSewer_charge(double sewer_charge) {
        this.sewer_charge = sewer_charge;
    }

    public double getSewer_charge() {
        return sewer_charge;
    }

    public void setSc_discount(double sc_discount) {
        this.sc_discount = sc_discount;
    }

    public double getSc_discount() {
        return sc_discount;
    }

    public void setMsc_amount(double msc_amount) {
        this.msc_amount = msc_amount;
    }


    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getDiscount() {
        return discount;
    }

    public double getFcda() {
        return fcda;
    }

    public double getCera() {
        return cera;
    }

    public void setTotcurb4tax(double totcurb4tax) {
        this.totcurb4tax = totcurb4tax;
    }

    public double getTotcurb4tax() {
        return totcurb4tax;
    }

    public String getVatExempt() {
        return vatExempt;
    }

    public void setVat(double vat) {
        this.vat = vat;
    }


    public double getVat() {
        return vat;
    }


    public String getNumusers() {
        return numusers;
    }

    public void setTotcurcharge(double totcurcharge) {
        this.totcurcharge = totcurcharge;
    }

    public double getTotcurcharge() {
        return totcurcharge;
    }
}
