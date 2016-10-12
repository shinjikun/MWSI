package com.indra.rover.mwsi.compute.bill;

import android.content.Context;

import com.indra.rover.mwsi.data.pojo.meter_reading.MeterBill;
import com.indra.rover.mwsi.data.pojo.meter_reading.references.GLCharge;
import com.indra.rover.mwsi.data.pojo.meter_reading.references.SAPData;
import com.indra.rover.mwsi.utils.Utils;

/**
 * Created by Indra on 10/10/2016.
 */
public class BillCompute extends BCompute {


    public BillCompute(BillComputeListener listener, Context context) {
        super(listener, context);
    }

    @Override
    public void compute(MeterBill meterBill) {
     String gt34flg = meterBill.getGt34_flg();
     String  bulkflg = meterBill.getBulk_flg();
        if(Utils.isNotEmpty(gt34flg)){
            if(gt34flg.equals("1")){
              getGT3BasicCharge(meterBill);
                return;
            }
        }
         if(Utils.isNotEmpty(bulkflg)){
            if(bulkflg.equals("1")){
                getBulkBasicCharge(meterBill);
                return;
            }
        }
        getBasicCharge(meterBill);

    }

    @Override
    void getBasicCharge(MeterBill meterBill) {
        int consumption = meterBill.getConsumption();
        //if consumption is minum
        if(consumption<=10){
            minimumCharge(meterBill);
        }

        if(listener!=null){
            listener.onPostBillResult(meterBill);
        }
    }

    void minimumCharge(MeterBill  meterBill){
        double basic_Charge;
        double discount;
            billDao.getGLRate(RESB);
        if(meterBill.getBillClass().equals(RESIDENTIAL)){
            GLCharge glres = getGLRate(RESB);
            GLCharge glpatr = getGLRate("PATR");
            GLCharge glrdls = getGLRate("RLDS");
            basic_Charge = glres.getGl_rate();
            meterBill.setBasicCharge(basic_Charge);
            double zdiscn = basic_Charge *  glrdls.getGl_rate();
            double zdispa = basic_Charge * glpatr.getGl_rate();
            discount = zdiscn+ zdispa;
            billDao.updateBasicCharge(basic_Charge,discount,meterBill.getId());
            insertSAPData(meterBill,"ZBASIC",basic_Charge,basic_Charge);
            insertSAPData(meterBill,"ZDISCN",0.0,zdiscn);
            insertSAPData(meterBill,"ZDISPA",0.0,zdispa);
        }

    }

    @Override
    void getBulkBasicCharge(MeterBill meterBill) {

    }

    @Override
    void getGT3BasicCharge(MeterBill meterBill) {

    }


    @Override
    double getHRLBasicCharge(long l) {
        return 0;
    }

    @Override
    double getOCBasicCharge(long l, char oc) {
        return 0;
    }


    public void insertSAPData(MeterBill meterBill,String sapline,double price,double amount){
        insertSAPData( meterBill, sapline, price, amount,0,0);
    }

    public void insertSAPData(MeterBill meterBill,String sapline,
                              double price,double amount,double oldprice,double oldamout){
        SAPData sapdata = new SAPData();
        sapdata.setDocNO(meterBill.getId());
        sapdata.setPrice(price);
        sapdata.setAmount(amount);
        sapdata.setLineCode(sapline);
        sapdata.setOld_amount(oldamout);
        sapdata.setOld_price(oldprice);
        double total = price+amount+oldamout+oldprice;
        sapdata.setTotal_amount(total);
        billDao.insertSAPData(sapdata);
    }
}
