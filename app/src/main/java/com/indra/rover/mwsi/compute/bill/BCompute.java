package com.indra.rover.mwsi.compute.bill;

import android.content.Context;

import com.indra.rover.mwsi.data.db.MeterBillDao;
import com.indra.rover.mwsi.data.pojo.meter_reading.MeterBill;
import com.indra.rover.mwsi.data.pojo.meter_reading.MeterConsumption;
import com.indra.rover.mwsi.data.pojo.meter_reading.MeterPrint;
import com.indra.rover.mwsi.data.pojo.meter_reading.references.GLCharge;
import com.indra.rover.mwsi.data.pojo.meter_reading.references.SPBillRule;

import java.util.HashMap;
import java.util.List;

/**
 * MWSI Bill Computation Module
 * Created by Indra on 9/22/2016.
 */
public abstract  class BCompute {

    BillComputeListener listener;
    private Context context;
    MeterBillDao billDao;
    private HashMap<String,GLCharge> hashGLRates;
    private HashMap<String,SPBillRule> hashSPBill;
    final String RESB= "RESB";
    final String PATR = "PATR";
    final String RLDS = "RLDS";
    final String CERA = "CERA";
    final String FCDA = "FCDA";
    final String ENVM = "ENVM";
    final String SEWR = "SEWR";
    final String VATX = "VATX";
    final String SCDS = "SCDS";
    final String HFTA = "HFTA";
    final String RESIDENTIAL ="0001";
    final String SEMIBIZ = "0002";
    final String COMMERCIAL = "0003";
    final String INDUSTRIAL ="0004";
    final String HBCOM ="9992";
    final String HBINDUS = "9993";

    public BCompute(BillComputeListener listener, Context context){
        this.listener = listener;
        this.context = context;
        billDao = new MeterBillDao(this.context);
        hashGLRates = billDao.getGLRates();
        hashSPBill = billDao.getSPBill();
    }

     GLCharge getGLRate(String code){
         return hashGLRates.get(code);
    }


    SPBillRule getSPBillRule(String id){
        return hashSPBill.get(id);
    }

   public abstract void compute(MeterBill  meterBill);


    /**
     * compute basic charge amount based on tariff schedule
     *
     */
  abstract void getBasicCharge(MeterBill meterBill);

    /**
     *  Special computation  for basic charge amount for BP> 34 days
     *
     */
    abstract void getBulkBasicCharge(MeterBill meterBill);

    /**
     *  Total Basic Charge for Bulk Account
     *
     */
  abstract     void getGT3BasicCharge(MeterBill meterBill);

    /**
     *  Total Basic Charge for HR/LT Accounts
     * @param l
     *
     */
  abstract    double getHRLBasicCharge(long l);

    /**
     *   Get Basic Charge based on OC only entry
     * @param l
     * @param oc oc code
     *
     */
   abstract   double getOCBasicCharge(long l,char oc);




    public interface BillComputeListener {
        void onPostBillResult(MeterBill mtrBill);

    }




}
