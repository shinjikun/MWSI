package com.indra.rover.mwsi.data.pojo.meter_reading.display;

import android.database.Cursor;

import com.indra.rover.mwsi.data.pojo.meter_reading.misc.CustomerInfo;
import com.indra.rover.mwsi.data.pojo.meter_reading.references.BillClass;
import com.indra.rover.mwsi.utils.Utils;

import java.io.Serializable;

public class MeterInfo implements Serializable {

    /**
     * MRU ID
     */
  private  String mru_id;

    /**
     *  Sequence Number
     */
  private String seq_number;


  private  String meter_number;
  private  String grp_flag;
  private  String block_tag;
  private  String dldocno;
  private  CustomerInfo customerInfo;

  private  BillClass billClass;

  private  String rdg_tries;
  private  String present_reading;
    //read stat
  private  String readStat;
    //bill scheme
  private  String bill_scheme;
  private  String account_num;
    //childs parent
  private  String childs_parent;

  private  String range_code;

  public static final int BILLABLE =3;
  public static final int BILLNOPRINT =2;
  public static final int NONBILLABLE =1;

    /**
     * actual reading actual consumption
     */
  public static final String MRTYPE01="01";
    /**
     *  actual reading average consumption
     */
    public static final String MRTYPE93="93";
    /**
     *  no reading average consumption
     */
    public static final String MRTYPE91="91";

   private  int printTag =0;
    public MeterInfo(Cursor cursor){
        this.mru_id =cursor.getString(cursor.getColumnIndexOrThrow("MRU"));
        this.seq_number = cursor.getString(cursor.getColumnIndexOrThrow("SEQNO"));
        this.meter_number = cursor.getString(cursor.getColumnIndexOrThrow("METERNO"));
        this.dldocno = cursor.getString(cursor.getColumnIndexOrThrow("DLDOCNO"));
        this.grp_flag = cursor.getString(cursor.getColumnIndexOrThrow("GRP_FLAG"));
        this.block_tag = cursor.getString(cursor.getColumnIndexOrThrow("BLOCK_TAG"));
        this.rdg_tries = cursor.getString(cursor.getColumnIndexOrThrow("RDG_TRIES"));
        this.present_reading = cursor.getString(cursor.getColumnIndexOrThrow("PRESRDG"));
        this.readStat = cursor.getString(cursor.getColumnIndexOrThrow("READSTAT"));
        this.bill_scheme = cursor.getString(cursor.getColumnIndexOrThrow("CSMB_TYPE_CODE"));
        this.childs_parent = cursor.getString(cursor.getColumnIndexOrThrow("CSMB_PARENT"));
        this.account_num = cursor.getString(cursor.getColumnIndexOrThrow("ACCTNUM"));
        this.range_code = cursor.getString(cursor.getColumnIndexOrThrow("RANGE_CODE"));
        this.customerInfo = new CustomerInfo(cursor);
        this.billClass = new BillClass(cursor);
        String str = cursor.getString(cursor.getColumnIndexOrThrow("PRINT_TAG"));
        if(Utils.isNotEmpty(str)){
            printTag =Integer.parseInt(str);
        }

    }


    public CustomerInfo getCustomer() {
        return customerInfo;
    }


    public String getMeter_number() {
        return meter_number;
    }

    public String getMru_id() {
        return mru_id;
    }

    public String getSeq_number() {
        return seq_number;
    }

    public BillClass getBillClass() {
        return billClass;
    }

    public String getDldocno() {
        return dldocno;
    }



    public String getBlock_tag() {
        return block_tag;
    }

    public String getGrp_flag() {
        return grp_flag;
    }



    public void setReadStat(String readStat) {
        this.readStat = readStat;
    }

    public String getReadStat() {
        return readStat;
    }

    public String getRdg_tries() {
        return rdg_tries;
    }

    /**
     *  get the present meter reading
     * @return
     */
    public String getPresRdg() {
        return present_reading;
    }

    public void setPresent_reading(String present_reading) {
        this.present_reading = present_reading;
    }

    public void setRdg_tries(String rdg_tries) {
        this.rdg_tries = rdg_tries;
    }

    public String getBill_scheme() {
        return bill_scheme;
    }

    public String getParentID() {
        return childs_parent;
    }

    public String getRange_code() {
        return range_code;
    }

    public void setRange_code(String range_code) {
        this.range_code = range_code;
    }

    public void setPrintTag(int print_tag) {
        this.printTag = print_tag;
    }

    public int getPrintTag() {
        return printTag;
    }
}
