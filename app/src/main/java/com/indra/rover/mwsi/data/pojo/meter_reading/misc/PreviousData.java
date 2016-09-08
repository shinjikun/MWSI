package com.indra.rover.mwsi.data.pojo.meter_reading.misc;

/**
 * Created by Indra on 9/8/2016.
 */
public class PreviousData {
    String prevconsline1;
    String prevconsline2;
    String previous_Remarks;
    String previous_invoice_num;
    int prevFF1;
    int prevFF2;
    int billprevRDG;
    int billprevRG2;
    int billprevactag;


    public PreviousData(String records[]){
        prevconsline1 = records[70];
        prevconsline2 = records[71];
        previous_Remarks = records[37];
        previous_invoice_num = records[38];
        prevFF1 = Integer.parseInt(records[28]);
        prevFF2 = Integer.parseInt(records[29]);
        //bill previous reading date
        setBillprevRDG(Integer.parseInt(records[19]));
        setBillprevRG2(Integer.parseInt(records[20]));
        setBillprevactag(Integer.parseInt(records[21]));
    }

    public void setPrevconsline1(String prevconsline1) {
        this.prevconsline1 = prevconsline1;
    }

    public void setPrevconsline2(String prevconsline2) {
        this.prevconsline2 = prevconsline2;
    }

    public void setPrevFF1(int prevFF1) {
        this.prevFF1 = prevFF1;
    }

    public void setPrevFF2(int prevFF2) {
        this.prevFF2 = prevFF2;
    }

    public void setPrevious_invoice_num(String previous_invoice_num) {
        this.previous_invoice_num = previous_invoice_num;
    }

    public void setPrevious_Remarks(String previous_Remarks) {
        this.previous_Remarks = previous_Remarks;
    }

    public int getPrevFF1() {
        return prevFF1;
    }

    public int getPrevFF2() {
        return prevFF2;
    }

    public String getPrevconsline1() {
        return prevconsline1;
    }

    public String getPrevconsline2() {
        return prevconsline2;
    }

    public String getPrevious_invoice_num() {
        return previous_invoice_num;
    }

    public String getPrevious_Remarks() {
        return previous_Remarks;
    }

    public void setBillprevRDG(int billprevRDG) {
        this.billprevRDG = billprevRDG;
    }



    public void setBillprevRG2(int billprevRG2) {
        this.billprevRG2 = billprevRG2;
    }

    public void setBillprevactag(int billprevactag) {
        this.billprevactag = billprevactag;
    }
}
