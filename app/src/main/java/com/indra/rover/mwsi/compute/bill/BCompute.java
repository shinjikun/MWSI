package com.indra.rover.mwsi.compute.bill;

import android.content.Context;

import com.indra.rover.mwsi.data.db.MeterBillDao;
import com.indra.rover.mwsi.data.pojo.meter_reading.MeterBill;
import com.indra.rover.mwsi.data.pojo.meter_reading.MeterConsumption;
import com.indra.rover.mwsi.data.pojo.meter_reading.MeterPrint;
import com.indra.rover.mwsi.data.pojo.meter_reading.references.GLCharge;

import java.util.HashMap;
import java.util.List;

/**
 * MWSI Bill Computation Module
 * Created by Indra on 9/22/2016.
 */
public abstract  class BCompute {

    BillComputeListener listener;
    Context context;
    MeterBillDao billDao;
    HashMap<String,GLCharge> hashGLRates;

    final String RESB= "RESB";
    final String PATR = "PATR";
    final String RLDS = "RLDS";
    final String CERA = "CERA";
    final String FCDA = "FCDA";
    final String ENVM = "ENVM";
    final String SEWR = "SEWR";
    final String RESIDENTIAL ="0001";
    final String SEMIBIZ = "0002";
    final String COMMER = "0003";
    final String INDUSTRIAL ="0004";
    final String HBCOM ="9992";
    final String HBINDUS = "9993";

    public BCompute(BillComputeListener listener, Context context){
        this.listener = listener;
        this.context = context;
        billDao = new MeterBillDao(this.context);
        hashGLRates = billDao.getGLRates();
    }

    public GLCharge getGLRate(String code){
         return hashGLRates.get(code);
    }


   public abstract void compute(MeterBill  meterBill);


    /**
     * compute basic charge amount based on tariff schedule
     * @return
     */
  abstract void getBasicCharge(MeterBill meterBill);

    /**
     *  Special computation  for basic charge amount for BP> 34 days
     * @return computed basic charge
     */
    abstract void getBulkBasicCharge(MeterBill meterBill);

    /**
     *  Total Basic Charge for Bulk Account
     * @return
     */
  abstract     void getGT3BasicCharge(MeterBill meterBill);

    /**
     *  Total Basic Charge for HR/LT Accounts
     * @param l
     * @return
     */
  abstract    double getHRLBasicCharge(long l);

    /**
     *   Get Basic Charge based on OC only entry
     * @param l
     * @param oc oc code
     * @return
     */
   abstract   double getOCBasicCharge(long l,char oc);




    public interface BillComputeListener {
        void onPostBillResult(MeterBill mtrBill);

    }




}
