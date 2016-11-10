package com.indra.rover.mwsi.data.pojo.meter_reading;

import android.database.Cursor;

import com.indra.rover.mwsi.utils.Utils;

import java.util.ArrayList;

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
  private String tenantName;
  private String sc_id;
  private String soa;

  private String meterNO;
  private int billCons;
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
  private String prevInvoiceNo;

  private String basicCharge;
  private String cera;
    /**
     *  total current charge
      */
  private String totcurr_charge;
    /**
     * total amount due
     */
  private String totalamt;
  private String orig_totalamt;
  private String otherCharges;


  private String disCheckFlg;

  private String disConFlg;
  private String rangeCode;
  private String dueDate;
  private String schedRdgDate;
  private String remarks;

  private double prevUnPaid;

  public double[] otherCharges0;
  public double[] otherCharges1;
  public double[] otherCharges2;
  public double[] otherCharges3;
  public double[] otherCharges4;

  public String[] otherChrgInd1;
  public String[] otherChrgInd2;

  public MeterPrint(){

  }
 public ArrayList<String> cur_charges;

  public MeterPrint(Cursor cursor,boolean type){
        this.OC1 =  cursor.getString(cursor.getColumnIndexOrThrow("FFCODE1"));
        if(!Utils.isNotEmpty(this.OC1)){
            this.OC1 = "";
        }
        this.OC2 =  cursor.getString(cursor.getColumnIndexOrThrow("FFCODE2"));
       if(!Utils.isNotEmpty(this.OC2)){
          this.OC2 = "";
       }
        this.presRdg =  cursor.getString(cursor.getColumnIndexOrThrow("PRESRDG"));
      if(!Utils.isNotEmpty(this.presRdg)){
          this.presRdg = "";
      }
      this.rangeCode = cursor.getString(cursor.getColumnIndexOrThrow("RANGE_CODE"));
      if(!Utils.isNotEmpty(this.rangeCode)){
          this.rangeCode = "";
      }
        this.custName = cursor.getString(cursor.getColumnIndexOrThrow("CUSTNAME"));
        this.acctNum = cursor.getString(cursor.getColumnIndexOrThrow("ACCTNUM"));
        this.printCount = cursor.getString(cursor.getColumnIndexOrThrow("PRINT_COUNT"));
        this.orig_totalamt = cursor.getString(cursor.getColumnIndexOrThrow("TOTAL_AMT_DUE"));
      if(!Utils.isNotEmpty(this.orig_totalamt)){
          this.orig_totalamt = "";
      }
      double d = cursor.getDouble(cursor.getColumnIndexOrThrow("TOTAL_AMT_DUE"));
      this.totalamt = Utils.formatValue(d);
        otherCharges0 = new double[4];

        otherCharges2 = new double[7];
  }


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
    this.tenantName = cursor.getString(cursor.getColumnIndexOrThrow("TENANT_NAME"));
    this.sc_id = cursor.getString(cursor.getColumnIndexOrThrow("SC_ID"));
    this.soa = cursor.getString(cursor.getColumnIndexOrThrow("SOA_NUMBER"));

    //metering info
    this.mru = cursor.getString(cursor.getColumnIndexOrThrow("MRU"));
    this.seqNo = cursor.getString(cursor.getColumnIndexOrThrow("SEQNO"));
    this.meterNO = cursor.getString(cursor.getColumnIndexOrThrow("METERNO"));
    this.billCons = cursor.getInt(cursor.getColumnIndexOrThrow("BILLED_CONS"));
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
    this.prevInvoiceNo = cursor.getString(cursor.getColumnIndexOrThrow("PREVINVOICENO"));
    this.remarks = cursor.getString(cursor.getColumnIndexOrThrow("REMARKS"));

      this.orig_totalamt = cursor.getString(cursor.getColumnIndexOrThrow("TOTAL_AMT_DUE"));


      //billing summary - current charges
      this.cera = cursor.getString(cursor.getColumnIndexOrThrow("CERA"));
      this.basicCharge = cursor.getString(cursor.getColumnIndexOrThrow("BASIC_CHARGE"));
      cur_charges = new ArrayList<>();
      double data = cursor.getDouble(cursor.getColumnIndexOrThrow("BASIC_CHARGE"));
      String str = Utils.formatValue(data);
      cur_charges.add(str);
      data =  cursor.getDouble(cursor.getColumnIndexOrThrow("FCDA"));
      str = Utils.formatValue(data);
      cur_charges.add(str);

      data  = cursor.getDouble(cursor.getColumnIndexOrThrow("ENV_CHARGE"));
      str = Utils.formatValue(data);
      cur_charges.add(str);

      data = cursor.getDouble(cursor.getColumnIndexOrThrow("SEWER_CHARGE"));
      str = Utils.formatValue(data);
      cur_charges.add(str);

      data = cursor.getDouble(cursor.getColumnIndexOrThrow("MSC_AMOUNT"));
      str = Utils.formatValue(data);
      cur_charges.add(str);

      data = cursor.getDouble(cursor.getColumnIndexOrThrow("SC_DISCOUNT"));
      str = Utils.formatValue(data);
      cur_charges.add(str);

      data = cursor.getDouble(cursor.getColumnIndexOrThrow("TOTCHRG_WO_TAX"));
      str = Utils.formatValue(data);
      cur_charges.add(str);

      data = cursor.getDouble(cursor.getColumnIndexOrThrow("VAT_CHARGE"));
      str = Utils.formatValue(data);
      cur_charges.add(str);

      data = cursor.getDouble(cursor.getColumnIndexOrThrow("TOT_CURR_CHARGE"));
      str = Utils.formatValue(data);
      this.totcurr_charge = str;

      //billing summary -  other charges
      data =  cursor.getDouble(cursor.getColumnIndexOrThrow("OTHER_CHARGES"));
      str =  Utils.formatValue(data);
      this.otherCharges =str;
      this.disCheckFlg = cursor.getString(cursor.getColumnIndexOrThrow("DISCHECK_FLAG"));
      this.rangeCode = cursor.getString(cursor.getColumnIndexOrThrow("RANGE_CODE"));
      this.dueDate = cursor.getString(cursor.getColumnIndexOrThrow("DUE_DATE"));
      this.schedRdgDate = cursor.getString(cursor.getColumnIndexOrThrow("SCHED_RDG_DATE"));
      double d = cursor.getDouble(cursor.getColumnIndexOrThrow("TOTAL_AMT_DUE"));
      this.totalamt = Utils.formatValue(d);

      prevUnPaid  = cursor.getDouble(cursor.getColumnIndexOrThrow("PREVUNPAID"));

      otherCharges0 = new double[4];
      otherCharges0[0] = cursor.getDouble(cursor.getColumnIndexOrThrow("GD_CHARGE"));
      otherCharges0[1] = cursor.getDouble(cursor.getColumnIndexOrThrow("INSTALL_WATER_CHARGE"));
      otherCharges0[2] = cursor.getDouble(cursor.getColumnIndexOrThrow("INSTALL_SEWER_CHARGE"));
      otherCharges0[3] = cursor.getDouble(cursor.getColumnIndexOrThrow("AMORTIZATION"));

      otherCharges1 = new double[6];
      otherCharges1[0] = cursor.getDouble(cursor.getColumnIndexOrThrow("GD_AMOUNT_DUE"));
      otherCharges1[1] = cursor.getDouble(cursor.getColumnIndexOrThrow("INSTALL_WATER_DUE"));
      otherCharges1[2] = cursor.getDouble(cursor.getColumnIndexOrThrow("INSTALL_WATER_DUE"));
      otherCharges1[3] = cursor.getDouble(cursor.getColumnIndexOrThrow("AMORT_DUE"));
      otherCharges1[4] = cursor.getDouble(cursor.getColumnIndexOrThrow("RESTORATION_DUE"));
      otherCharges1[5] = cursor.getDouble(cursor.getColumnIndexOrThrow("ILLEGALITIES_DUE"));

      otherChrgInd1 = new String[6];
      otherChrgInd1[0] = cursor.getString(cursor.getColumnIndexOrThrow("INSTALL_GD_IND"));
      otherChrgInd1[1] = cursor.getString(cursor.getColumnIndexOrThrow("INSTALL_WTR_IND"));
      otherChrgInd1[2] = cursor.getString(cursor.getColumnIndexOrThrow("INSTALL_SEW_IND"));
      otherChrgInd1[3] = cursor.getString(cursor.getColumnIndexOrThrow("INSTALL_AMORT_IND"));
      otherChrgInd1[4] = cursor.getString(cursor.getColumnIndexOrThrow("RESTORATION_IND"));
      otherChrgInd1[5] = cursor.getString(cursor.getColumnIndexOrThrow("ILLEGALITIES_IND"));

      otherCharges2 = new double[7];
      otherCharges2[0] = cursor.getDouble(cursor.getColumnIndexOrThrow("REOPENING_FEE"));
      otherCharges2[1] = cursor.getDouble(cursor.getColumnIndexOrThrow("METER_CHARGES"));
      otherCharges2[2] = cursor.getDouble(cursor.getColumnIndexOrThrow("SEPTIC_CHARGE"));
      otherCharges2[3] = cursor.getDouble(cursor.getColumnIndexOrThrow("CHANGESIZE_CHARGE"));
      otherCharges2[4] = cursor.getDouble(cursor.getColumnIndexOrThrow("PENALTIES_DUE"));
      otherCharges2[5] = cursor.getDouble(cursor.getColumnIndexOrThrow("UNMIGRATED_AR_WATER"));
      otherCharges2[6] = cursor.getDouble(cursor.getColumnIndexOrThrow("UNMIGRATED_AR_IC"));

      otherCharges3 = new double[2];
      otherCharges3[0] = cursor.getDouble(cursor.getColumnIndexOrThrow("UNMIGRATED_WATER_DUE"));
      otherCharges3[1] = cursor.getDouble(cursor.getColumnIndexOrThrow("UNMIGRATED_SEWER_DUE"));

      otherChrgInd2 = new String[2];
      otherChrgInd2[0] = cursor.getString(cursor.getColumnIndexOrThrow("UNMIGRATED_WATER_IND"));
      otherChrgInd2[1] = cursor.getString(cursor.getColumnIndexOrThrow("UNMIGRATED_SEWER_IND"));


      otherCharges4 = new double[2];
      otherCharges4[0] = cursor.getDouble(cursor.getColumnIndexOrThrow("RECOVERY"));
      otherCharges4[1] = cursor.getDouble(cursor.getColumnIndexOrThrow("MISC_CHARGE"));


     this.disConFlg = cursor.getString(cursor.getColumnIndexOrThrow("DISC_TAG"));
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

    public int getBillCons() {
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


    public String getPresRdg() {
        if(!Utils.isNotEmpty(presRdg))
            presRdg="";
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


    public String getTenantName() {
        return tenantName;
    }

    public String getTIN() {
        return TIN;
    }


    public String getTotcurr_charge() {
        return totcurr_charge;
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

    public String getDisCheckFlg() {
        return disCheckFlg;
    }

    public String getDisConFlg() {
        return disConFlg;
    }

    public String getRangeCode() {
        return rangeCode;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getPrevInvoiceNo() {
        return prevInvoiceNo;
    }

    public String getTotalamt() {
        return totalamt;
    }

    public String getOtherCharges() {
        return otherCharges;
    }

    public String getSchedRdgDate() {
        return schedRdgDate;
    }

    String OC1, OC2;
    String printCount;

    public String getOC1() {
        return OC1;
    }

    public String getOC2() {
        return OC2;
    }

    public String getPrintCount() {
        return printCount;
    }

    public String getRemarks() {
        return remarks;
    }

    public double getPrevUnPaid() {
        return prevUnPaid;
    }

    public String getOrig_totalamt() {
        return orig_totalamt;
    }
}
