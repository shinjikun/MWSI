package com.indra.rover.mwsi.data.pojo.meter_reading;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class MeterPrint {
  //business class
  private String billClass;
  //business center
  private String bcDesc;
  private String bcAddress;
  private String bcTin;

  private String mru;
  private String seqNo;

  //customer account Info
  private String acctNum;
  private String custName;
  private String custAddress;
  private String acct_status;
  private String TIN;
  private String tenant_name;
  private String sc_id;
  private String soa;

  private String meterNO;
  private String billCons;
  private String presRdgDate;
  private String prevRdgDate;
  private String prevRdg;
  private String presRdg;

  private String prevConsLine1;
  private String prevConsLine2;

  private String wbPaydtls1;
  private String wbPaydtls2;
  private String  miscPaydtls;
  private String  gdPaydtls;

  private String basicCharge;
  private String fcda;
  private String cera;
  private String env_charge;
  private String sewer_charge;
  private String msc_amount;
    /**
     * total charge without tax
      */
  private String totcharg_wotax;
  private String vat_charge;
    /**
     *  total current charge
      */
  private String totcurr_charge;

  public MeterPrint(){

  }
 public ArrayList<String> cur_charges;
  public MeterPrint(Cursor cursor){
      this.billClass = cursor.getString(cursor.getColumnIndexOrThrow("BILL_CLASS_DESC"));
      this.bcDesc = cursor.getString(cursor.getColumnIndexOrThrow("BC_DESC"));
      this.bcAddress = cursor.getString(cursor.getColumnIndexOrThrow("BC_ADDRESS"));
      this.bcTin = cursor.getString(cursor.getColumnIndexOrThrow("BC_TIN"));
    //service info
    this.acctNum = cursor.getString(cursor.getColumnIndexOrThrow("ACCTNUM"));
    this.custName = cursor.getString(cursor.getColumnIndexOrThrow("CUSTNAME"));
    this.custAddress = cursor.getString(cursor.getColumnIndexOrThrow("CUSTADDRESS"));
    this.acct_status =  cursor.getString(cursor.getColumnIndexOrThrow("ACCT_STATUS"));
    this.TIN =  cursor.getString(cursor.getColumnIndexOrThrow("TIN"));
    this.tenant_name = cursor.getString(cursor.getColumnIndexOrThrow("TENANT_NAME"));
    this.sc_id = cursor.getString(cursor.getColumnIndexOrThrow("SC_ID"));
    this.soa = cursor.getString(cursor.getColumnIndexOrThrow("SOA_NUMBER"));
    //metering info
    this.mru = cursor.getString(cursor.getColumnIndexOrThrow("MRU"));
    this.seqNo = cursor.getString(cursor.getColumnIndexOrThrow("SEQNO"));
    this.meterNO = cursor.getString(cursor.getColumnIndexOrThrow("METERNO"));
    this.billCons = cursor.getString(cursor.getColumnIndexOrThrow("BILLED_CONS"));
    this.presRdg = cursor.getString(cursor.getColumnIndexOrThrow("PRESRDG"));
      this.presRdgDate = cursor.getString(cursor.getColumnIndexOrThrow("RDG_DATE"));
      this.prevRdg = cursor.getString(cursor.getColumnIndexOrThrow("ACTPREVRDG"));
    this.prevRdgDate = cursor.getString(cursor.getColumnIndexOrThrow("PREVRDGDATE"));
      this.prevConsLine1 = cursor.getString(cursor.getColumnIndexOrThrow("PREVCONSLINE1"));
      this.prevConsLine2 = cursor.getString(cursor.getColumnIndexOrThrow("PREVCONSLINE2"));
      //bill and payment history
        this.wbPaydtls1 = cursor.getString(cursor.getColumnIndexOrThrow("WBPAYDTLS1"));
        this.wbPaydtls2 = cursor.getString(cursor.getColumnIndexOrThrow("WBPAYDTLS2"));
        this.gdPaydtls = cursor.getString(cursor.getColumnIndexOrThrow("GDPAYDTLS"));
        this.miscPaydtls = cursor.getString(cursor.getColumnIndexOrThrow("MISCPAYDTLS"));
      //billing summary - current charge
      this.cera = cursor.getString(cursor.getColumnIndexOrThrow("CERA"));
      this.basicCharge = cursor.getString(cursor.getColumnIndexOrThrow("BASIC_CHARGE"));
      cur_charges = new ArrayList<>();
      String str = cursor.getString(cursor.getColumnIndexOrThrow("BASIC_CHARGE"));
      cur_charges.add(str);
      str =  cursor.getString(cursor.getColumnIndexOrThrow("FCDA"));
      cur_charges.add(str);
      str  = cursor.getString(cursor.getColumnIndexOrThrow("ENV_CHARGE"));
      cur_charges.add(str);
      str = cursor.getString(cursor.getColumnIndexOrThrow("SEWER_CHARGE"));
      cur_charges.add(str);
      str = cursor.getString(cursor.getColumnIndexOrThrow("MSC_AMOUNT"));
      cur_charges.add(str);
      str = cursor.getString(cursor.getColumnIndexOrThrow("TOTCHRG_WO_TAX"));
      cur_charges.add(str);
      str = cursor.getString(cursor.getColumnIndexOrThrow("VAT_CHARGE"));
      cur_charges.add(str);
      this.totcurr_charge = cursor.getString(cursor.getColumnIndexOrThrow("TOT_CURR_CHARGE"));

  }

    public String getBillClass() {
        return billClass;
    }

    public String getAcct_status() {
        return acct_status;
    }

    public String getSoa() {
        return soa;
    }

    public String getAcctNum() {
        return acctNum;
    }

    public String getBasicCharge() {
        return basicCharge;
    }

    public String getBcAddress() {
        return bcAddress;
    }

    public String getBcDesc() {
        return bcDesc;
    }

    public String getBcTin() {
        return bcTin;
    }

    public String getBillCons() {
        return billCons;
    }

    public String getCera() {
        return cera;
    }

    public String getCustAddress() {
        return custAddress;
    }

    public String getCustName() {
        return custName;
    }

    public String getEnv_charge() {
        return env_charge;
    }

    public String getFcda() {
        return fcda;
    }

    public String getGdPaydtls() {
        return gdPaydtls;
    }

    public String getMeterNO() {
        return meterNO;
    }

    public String getMiscPaydtls() {
        return miscPaydtls;
    }

    public String getMru() {
        return mru;
    }

    public String getMsc_amount() {
        return msc_amount;
    }

    public String getPresRdg() {
        return presRdg;
    }

    public String getPresRdgDate() {
        return presRdgDate;
    }

    public String getPrevConsLine1() {
        return prevConsLine1;
    }

    public String getPrevConsLine2() {
        return prevConsLine2;
    }

    public String getPrevRdg() {
        return prevRdg;
    }

    public String getPrevRdgDate() {
        return prevRdgDate;
    }

    public String getSc_id() {
        return sc_id;
    }

    public String getSeqNo() {
        return seqNo;
    }

    public String getSewer_charge() {
        return sewer_charge;
    }

    public String getTenant_name() {
        return tenant_name;
    }

    public String getTIN() {
        return TIN;
    }

    public String getTotcharg_wotax() {
        return totcharg_wotax;
    }

    public String getTotcurr_charge() {
        return totcurr_charge;
    }

    public String getVat_charge() {
        return vat_charge;
    }

    public String getWbPaydtls1() {
        return wbPaydtls1;
    }

    public String getWbPaydtls2() {
        return wbPaydtls2;
    }

    public String getSOA() {
        return soa;
    }
}
