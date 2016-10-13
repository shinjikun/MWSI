package com.indra.rover.mwsi.compute.bill;

import android.content.Context;

import com.indra.rover.mwsi.data.pojo.meter_reading.MeterBill;
import com.indra.rover.mwsi.data.pojo.meter_reading.references.GLCharge;
import com.indra.rover.mwsi.data.pojo.meter_reading.references.SAPData;
import com.indra.rover.mwsi.data.pojo.meter_reading.references.Tariff;
import com.indra.rover.mwsi.utils.Utils;

import java.util.ArrayList;

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
        double discount =0.00;
        //if consumption is above minimum
        if(consumption>10){
            ArrayList<Tariff> arryTariff = billDao.getTariffs(meterBill.getBillClass());
            double basic_charge=0.0;
            int size = arryTariff.size();
            for(int i=0;i<size;i++){
                Tariff tariff = arryTariff.get(i);
                int low_limit = tariff.getLowLimit();
                int high_limit = tariff.getHighLimit();
                double amount = tariff.getBaseAmount();
                double old_amount = 0.0;//tariff.getOld_baseAmount();
                double old_price = 0.0;//tariff.getOld_price();
                int quantity = tariff.getCons_band();
                double price = tariff.getPrice();
                double totalAmount;

                if(i==0){
                    price =amount;
                }
                else {
                    amount = price * quantity;
                }

                if(low_limit<=consumption && consumption<=high_limit){
                    quantity = consumption - low_limit +1;
                    amount = price * quantity;
                    totalAmount = amount +old_amount;
                    basic_charge += totalAmount;
                    insertSAPData(meterBill,"ZBASIC",price,amount,old_price,old_amount,quantity);
                        break;
                }
                totalAmount = amount+ old_amount;
                basic_charge += totalAmount;

                insertSAPData(meterBill,"ZBASIC",price,amount,old_price,old_amount,quantity);

            }
            updateBasicCharge(meterBill,basic_charge,discount);

        }
        else {
            minimumCharge(meterBill);
        }

        otherCharges(meterBill);
        totalAmount(meterBill);
        if(listener!=null){
            listener.onPostBillResult(meterBill);
        }
    }


    private void otherCharges(MeterBill meterBill){
     computeCERA(meterBill);
     computeFCDA(meterBill);
     computeENVM(meterBill);
     computeSewerCharge(meterBill);
     computeMSCCharge(meterBill);
     computeSCDiscount(meterBill);
     computeTotChargeB4Tax(meterBill);
     computeForVAT(meterBill);

    }

    private void totalAmount(MeterBill meterBill){
        double totCurB4Tax = meterBill.getTotcurb4tax();
        double vat =  meterBill.getVat();
        double totalAmount = totCurB4Tax + vat;
        totalAmount = Utils.roundDouble(totalAmount);
    }

    private void computeCERA(MeterBill meterBill){
        int consumption = meterBill.getConsumption();
        GLCharge glCERA =  getGLRate(CERA);
        double zcera = consumption * glCERA.getGl_rate();
        if(zcera>0){
            zcera = Utils.roundDouble(zcera);
            insertSAPData(meterBill,"ZCERA",0.00,zcera,0 );
            meterBill.setCera(zcera);
        }

    }

    private void computeFCDA(MeterBill meterBill){
        int consumption = meterBill.getConsumption();
        GLCharge glFCDA =  getGLRate(FCDA);
        double zfcda = consumption * glFCDA.getGl_rate();
        if(zfcda>0){
            zfcda = Utils.roundDouble(zfcda);
            insertSAPData(meterBill,"ZFCDA",0.00,zfcda,0 );
            meterBill.setFcda(zfcda);
        }
    }

    private void  computeENVM(MeterBill meterBill){
        double  zdiscn = meterBill.getDiscount();
        double  zcera =  meterBill.getCera();
        double  zfcda = meterBill.getFcda();
        double  total_water_charge = meterBill.getBasicCharge() +zcera+zfcda+zdiscn;
        GLCharge glevm = getGLRate(ENVM);
        double  zenv = total_water_charge * glevm.getGl_rate();

        if(zenv>0){
            zenv = Utils.roundDouble(zenv);
            insertSAPData(meterBill,"ZENV",0.00,zenv,0 );
            meterBill.setEnv_charge(zenv);
        }

    }


    private void computeMSCCharge(MeterBill meterBill){
        double msc_amout =  meterBill.getMsc_amount();
        String gt34flg = meterBill.getGt34_flg();
        if(Utils.isNotEmpty(gt34flg)){

            if(gt34flg.equals("1")){
                double months_factor =  1.323;
                msc_amout =    msc_amout * months_factor;
            }
        }

        insertSAPData(meterBill,"ZMSC",0.00, msc_amout,0);
        meterBill.setMsc_amount(msc_amout);

    }


   private void computeSewerCharge(MeterBill meterBill){
       double  zdiscn = meterBill.getDiscount();
       double  zcera =  meterBill.getCera();
       double  zfcda = meterBill.getFcda();
       double  total_water_charges = meterBill.getBasicCharge() +zcera+zfcda+zdiscn;
        String billClass = meterBill.getBillClass();
        String rate_type = meterBill.getRatetype();
        if(billClass.equals(COMMERCIAL)||billClass.equals(INDUSTRIAL)){
            if(rate_type.equals("S")||rate_type.equals("A")||
                    rate_type.equals("SA")||rate_type.equals("HA")){
                GLCharge glevm = getGLRate(SEWR);
                double zsewer = total_water_charges * glevm.getGl_rate();
                if(zsewer>0){
                    zsewer = Utils.roundDouble(zsewer);
                    insertSAPData(meterBill,"ZSEWER",0.00,zsewer,0 );
                    meterBill.setSewer_charge(zsewer);
                }
            }
        }
    }

    private void computeSCDiscount(MeterBill meterBill){
        meterBill.setSc_discount(0.0);
    }

    /**
     *  compute for current charges before tax
     * @param meterBill
     */
    private void computeTotChargeB4Tax(MeterBill meterBill){
      double totb4Tax =  billDao.getTotalAmount(meterBill.getId());
        meterBill.setTotcurb4tax(totb4Tax);

    }

    private void computeForVAT(MeterBill meterBill){
        String vatExempt = meterBill.getVatExempt();
        if(Utils.isNotEmpty(vatExempt)){
            if(vatExempt.equals("1")){
                double  totChargeB4Tax = meterBill.getTotcurb4tax();
                GLCharge glVAT =  getGLRate(VATX);
                double vat =  totChargeB4Tax * glVAT.getGl_rate();
                vat = Utils.roundDouble(vat);
                meterBill.setVat(vat);
            }
        }
    }

   private void minimumCharge(MeterBill  meterBill){
        double basic_Charge;
        double discount=0.0;
        if(meterBill.getBillClass().equals(RESIDENTIAL)){
            GLCharge glres = getGLRate(RESB);
            GLCharge glpatr = getGLRate(PATR);
            GLCharge glrdls = getGLRate(RLDS);
            basic_Charge = glres.getGl_rate();
            meterBill.setBasicCharge(basic_Charge);
            double zdiscn = basic_Charge * -(glrdls.getGl_rate());
            double zdispa = basic_Charge * -(glpatr.getGl_rate());
            //round values
            zdiscn = Utils.roundDouble(zdiscn);
            zdispa = Utils.roundDouble(zdispa);
            discount = Utils.roundDouble(zdiscn+ zdispa);
            basic_Charge = Utils.roundDouble(basic_Charge);
            meterBill.setDiscount(discount);
            updateBasicCharge(meterBill,basic_Charge,discount);
            insertSAPData(meterBill,"ZBASIC",basic_Charge,basic_Charge,meterBill.getConsumption() );
            insertSAPData(meterBill,"ZDISCN",0.00,zdiscn,0 );
            insertSAPData(meterBill,"ZDISPA",0.00,zdispa,0 );
        }
        else {
        ArrayList<Tariff> arryTariff = billDao.getTariffs(meterBill.getBillClass());
        Tariff tariff = arryTariff.get(0);
          basic_Charge =  tariff.getTierAmount();
            basic_Charge = Utils.roundDouble(basic_Charge);
           updateBasicCharge(meterBill,basic_Charge,0.0);
          insertSAPData(meterBill,"ZBASIC",basic_Charge,basic_Charge,meterBill.getConsumption() );
        }
        meterBill.setBasicCharge(basic_Charge);

    }

    private void updateBasicCharge(MeterBill meterBill,double basic_charge,double discount){
        billDao.updateBasicCharge(basic_charge,discount,meterBill.getId());
        meterBill.setBasicCharge(basic_charge);
    }

    @Override
    void getBulkBasicCharge(MeterBill meterBill) {

        otherCharges(meterBill);
        totalAmount(meterBill);
        if(listener!=null){
            listener.onPostBillResult(meterBill);
        }
    }

    @Override
    void getGT3BasicCharge(MeterBill meterBill) {
        otherCharges(meterBill);
        totalAmount(meterBill);
        if(listener!=null){
            listener.onPostBillResult(meterBill);
        }
    }


    @Override
    double getHRLBasicCharge(long l) {
        return 0;
    }

    @Override
    double getOCBasicCharge(long l, char oc) {
        return 0;
    }

    private void insertSAPData(MeterBill meterBill, String sapline, double price, double amount, int quantity){
        insertSAPData( meterBill, sapline, price, amount,0,0,quantity );
    }

    private void insertSAPData(MeterBill meterBill, String sapline,
                              double price, double amount, double oldprice, double oldamout, int quantity){
        SAPData sapdata = new SAPData();
        sapdata.setDocNO(meterBill.getId());
        sapdata.setPrice(price);
        sapdata.setAmount(amount);
        sapdata.setLineCode(sapline);
        sapdata.setAcctNum(meterBill.getAcctNum());
        sapdata.setOld_amount(oldamout);
        sapdata.setOld_price(oldprice);
        sapdata.setQuantity(quantity);
        double total = amount+oldamout;
        sapdata.setTotal_amount(total);
        billDao.insertSAPData(sapdata);
    }
}
