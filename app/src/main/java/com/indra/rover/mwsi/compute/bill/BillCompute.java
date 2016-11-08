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

        if(Utils.isNotEmpty(bulkflg)){
            if(bulkflg.equals("1")){
                getBulkBasicCharge(meterBill);
                return;
            }
        }

        if(Utils.isNotEmpty(gt34flg)){
            if(gt34flg.equals("1")){
              getGT3BasicCharge(meterBill);
                return;
            }
        }

        getBasicCharge(meterBill);

    }

    @Override
    void getBasicCharge(MeterBill meterBill) {

        int consumption = meterBill.getConsumption();
        double discount =0.00;
        String strpro = meterBill.getTariffPro();
        if(Utils.isNotEmpty(meterBill.getSpbillrule())){
            String spid = meterBill.getSpbillrule();
            if(spid.equals("1")){
                spBillRule = getSPBillRule(spid);
            }
        }


        char proratetype = NOPRORATE;
        if(Utils.isNotEmpty(strpro)){
             proratetype =  strpro.charAt(0);

        }

        //if consumption is above minimum
        if(consumption>10){
            ArrayList<Tariff> arryTariff = billDao.getTariffs(meterBill.getBillClass());
            double basic_charge=0.0;
            int size = arryTariff.size();
            for(int i=0;i<size;i++){
                Tariff tariff = arryTariff.get(i);
                //old days -  effective date - previous reading date +1
                int OD = Utils.dateDiff(tariff.getEffectDate(),meterBill.getPrevRdgDate()) +1;
                //new days = present reading date - effectivity  date +1
                int ND = Utils.dateDiff(meterBill.getPresRdgDate(),tariff.getEffectDate())+1;
                //billing period present reading date - previous reading date
                int DBP = Utils.dateDiff(meterBill.getPresRdgDate(),meterBill.getPrevRdgDate());

                int low_limit = tariff.getLowLimit();
                int high_limit = tariff.getHighLimit();
                double amount = tariff.getBaseAmount();
                double old_amount = 0.0;
                double old_price = 0.0;
                int quantity = tariff.getCons_band();
                double price = tariff.getPrice();
                double totalAmount;


                if(i==0){
                    price =amount;
                    switch (proratetype){
                        case WITHPRORATE:
                        case PRO_TYPE1:
                            old_amount =  tariff.getOld_baseAmount();
                            old_price = old_amount;
                            break;
                    }

                }
                else {
                    switch (proratetype){
                        case NOPRORATE:
                            amount = price * quantity;
                            break;
                        case PRO_TYPE1:
                            amount = price * quantity;
                            old_price = tariff.getOld_price();
                            old_amount =  old_price * quantity;
                            break;
                        case WITHPRORATE:
                            old_price = tariff.getOld_price();
                            amount = price*quantity *  ((double)ND/(double)DBP);
                            old_amount = old_price*quantity *  ((double)OD/(double)DBP);
                            break;
                    }
                }

               if(low_limit<=consumption && consumption<=high_limit){
                    quantity = consumption - low_limit +1;
                   switch (proratetype){
                       case NOPRORATE:
                           amount = price * quantity;
                           break;
                       case PRO_TYPE1:
                           amount = price * quantity;
                           old_price = tariff.getOld_price();
                           old_amount =  old_price * quantity;
                           break;
                       case WITHPRORATE:
                           old_price = tariff.getOld_price();
                           amount = price*quantity *  ((double)ND/(double)DBP);
                           old_amount = old_price*quantity *   ((double)OD/(double)DBP);
                           break;
                   }
                   old_amount = Utils.roundDouble(old_amount);
                   amount = Utils.roundDouble(amount);
                    totalAmount = amount +old_amount;
                    basic_charge += totalAmount;
                    meterBill.newbasic+=amount;
                    meterBill.oldbasic+=old_amount;
                    insertSAPData(meterBill,"ZBASIC",price,amount,old_price,
                            old_amount,String.valueOf(quantity));
                        break;
                }
                old_amount = Utils.roundDouble(old_amount);
                amount = Utils.roundDouble(amount);
                totalAmount = amount+ old_amount;
                basic_charge += totalAmount;
                meterBill.newbasic+=amount;
                meterBill.oldbasic+=old_amount;
                insertSAPData(meterBill,"ZBASIC",price,amount,old_price,old_amount,
                        String.valueOf(quantity));

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
        meterBill.setTotcurcharge(totalAmount);
    }

    private void computeCERA(MeterBill meterBill){
        String strpro = meterBill.getCeraPro();
        int consumption = meterBill.getConsumption();
        GLCharge glRateObj =  getGLCharge(CERA);
        //old days -  effective date - previous reading date +1
        int OD = Utils.dateDiff(glRateObj.getEffectivity_date(),meterBill.getPrevRdgDate()) +1;
        //new days = present reading date - effectivity  date +1
        int ND = Utils.dateDiff(meterBill.getPresRdgDate(),glRateObj.getEffectivity_date())+1;
        //billing period present reading date - previous reading date
        int DBP = Utils.dateDiff(meterBill.getPresRdgDate(),meterBill.getPrevRdgDate());

        double  gl_rate =  glRateObj.getGl_rate();
        double  gl_rate_old =  glRateObj.getGl_rate_old();

        //check if special bill rule is enable if yes then apply
        // billing rule to compute cera
        //otherwise use the default computation using glrate of cera in the tariff table


        double zcera = consumption * gl_rate;
        double oldamount = 0.0;

        if(Utils.isNotEmpty(strpro)){
            char proratetype =  strpro.charAt(0);
            switch (proratetype){
                case PRO_TYPE1 :
                    zcera = consumption * gl_rate;
                    oldamount = gl_rate_old;
                    break;
                case WITHPRORATE:
                    oldamount = consumption * (gl_rate_old*((double) OD/(double) DBP));
                    zcera = consumption * (gl_rate*((double)ND/(double)DBP));
                    break;

            }
        }


        if(zcera>0){
            zcera = Utils.roundDouble(zcera);
            insertSAPData(meterBill,"ZCERA",zcera,oldamount );
            meterBill.newcera = zcera;
            meterBill.oldcera = oldamount;
            meterBill.setCera(zcera+oldamount);
        }

    }

    private void computeFCDA(MeterBill meterBill){
        String strpro = meterBill.getFcdaPro();
        double basicCharge = meterBill.getBasicCharge();
        GLCharge glRateObj =  getGLCharge(FCDA);
        //old days -  effective date - previous reading date
        int OD = Utils.dateDiff(glRateObj.getEffectivity_date(),meterBill.getPrevRdgDate()) +1;
        //new days = present reading date - effectivity  date +1
        int ND = Utils.dateDiff(meterBill.getPresRdgDate(),glRateObj.getEffectivity_date())+1;
        //billing period present reading date - previous reading date
        int DBP = Utils.dateDiff(meterBill.getPresRdgDate(),meterBill.getPrevRdgDate());

        double  gl_rate =  glRateObj.getGl_rate();
        double  gl_rate_old =  glRateObj.getGl_rate_old();

        //new amount
        double zfcda = basicCharge * gl_rate;
        //old amount
        double oldamount=0.0;

        if(Utils.isNotEmpty(strpro)){
            char proratetype =  strpro.charAt(0);
            switch (proratetype){
                case PRO_TYPE1 :
                    zfcda = basicCharge * gl_rate;
                    oldamount = gl_rate_old;
                    break;
                case WITHPRORATE:
                    oldamount = basicCharge * (gl_rate_old*((double) OD/(double) DBP));
                    zfcda = basicCharge * (gl_rate*((double)ND/(double)DBP));
                    break;
            }
        }

        if(zfcda>0.0){
            zfcda = Utils.roundDouble(zfcda);
            oldamount = Utils.roundDouble(oldamount);
            insertSAPData(meterBill,"ZFCDA",zfcda,oldamount );
            meterBill.newfcda = zfcda;
            meterBill.oldfcda = oldamount;
            meterBill.setFcda(zfcda+oldamount);
        }
    }

    private void  computeENVM(MeterBill meterBill){
        double  zdiscn = meterBill.getDiscount();
        double  zcera =  meterBill.getCera();
        double  zfcda = meterBill.getFcda();
        String strpro = meterBill.getEnvPro();
        GLCharge glRateObj = getGLCharge(ENVM);
        //old days -  effective date - previous reading date
        int OD = Utils.dateDiff(glRateObj.getEffectivity_date(),meterBill.getPrevRdgDate()) +1;
        //new days = present reading date - effectivity  date +1
        int ND = Utils.dateDiff(meterBill.getPresRdgDate(),glRateObj.getEffectivity_date())+1;
        //billing period present reading date - previous reading date
        int DBP = Utils.dateDiff(meterBill.getPresRdgDate(),meterBill.getPrevRdgDate());


        //new water charge
        double  tot_water_chrg = meterBill.getBasicCharge() +zcera+zfcda+zdiscn;
        //old water charge;
        double old_total_water_chrg = 0.0;


        double gl_rate = glRateObj.getGl_rate();
        double gl_rate_old =  glRateObj.getGl_rate_old();

        if(Utils.isNotEmpty(strpro)){
            char proratetype =  strpro.charAt(0);
            switch (proratetype){
                case PRO_TYPE1 :
                case WITHPRORATE:
                    old_total_water_chrg = (meterBill.oldbasic+
                            meterBill.oldcera+meterBill.oldfcda) * gl_rate_old;
                    break;
            }
        }

        double  zenv = tot_water_chrg * gl_rate;
        if(zenv>0){
            zenv = Utils.roundDouble(zenv);
            old_total_water_chrg = Utils.roundDouble(old_total_water_chrg);
            insertSAPData(meterBill,"ZENV",zenv,old_total_water_chrg);
            meterBill.setEnv_charge(zenv+old_total_water_chrg);
        }

    }


    private void computeMSCCharge(MeterBill meterBill){
        double msc_amout =  meterBill.getMsc_amount();
        String gt34flg = meterBill.getGt34_flg();
        String gt34factor = meterBill.getGt34factor();
        if(Utils.isNotEmpty(gt34flg)){

            if(gt34flg.equals("1")){
                double gtfactor = Double.parseDouble(gt34factor);
                gtfactor = Utils.roundDouble6(gtfactor);
                double months_factor =  gtfactor;
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
       String strpro = meterBill.getSewPro();
       double  total_water_charges = meterBill.getBasicCharge() +zcera+zfcda+zdiscn;
       double  old_total_water_charges = meterBill.oldbasic +meterBill.oldcera+
               meterBill.oldfcda+zdiscn;
        String billClass = meterBill.getBillClass();
        String rate_type = meterBill.getRatetype();
        GLCharge glRateObj = getGLCharge(SEWR);


       //old days -  effective date - previous reading date
       int OD = Utils.dateDiff(glRateObj.getEffectivity_date(),meterBill.getPrevRdgDate()) +1;
       //new days = present reading date - effectivity  date +1
       int ND = Utils.dateDiff(meterBill.getPresRdgDate(),glRateObj.getEffectivity_date())+1;
       //billing period present reading date - previous reading date
       int DBP = Utils.dateDiff(meterBill.getPresRdgDate(),meterBill.getPrevRdgDate());

       double gl_rate =  glRateObj.getGl_rate();
       double gl_rate_old = glRateObj.getGl_rate_old();
       double oldamount=0.0;


        if(billClass.equals(COMMERCIAL)||billClass.equals(INDUSTRIAL)){
            if(rate_type.equals("S")||rate_type.equals("A")||
                    rate_type.equals("SA")||rate_type.equals("HA")){

                double zsewer = total_water_charges * gl_rate;

                if(Utils.isNotEmpty(strpro)){
                    char proratetype =  strpro.charAt(0);
                    switch (proratetype){
                        case PRO_TYPE1 :

                         //   oldamount = gl_rate_old;
                            break;
                        case WITHPRORATE:
                            oldamount = old_total_water_charges * (gl_rate_old*((double) OD/(double) DBP));
                            zsewer = total_water_charges * (gl_rate*((double) ND/(double) DBP));
                            break;
                    }
                }

                if(zsewer>0){
                    zsewer = Utils.roundDouble(zsewer);
                    insertSAPData(meterBill,"ZSEWER",0.00,zsewer,0 );
                    meterBill.setSewer_charge(zsewer);
                    //Since Sewer Only rate type, remove all records related to
                    // Basic Charge from the table, i.e. set Basic Charge = 0.00

                    if(rate_type.equals("S")){
                       meterBill.setBasicCharge(0.0);
                        billDao.deleteZBasic(meterBill.getId());
                        meterBill.oldbasic =0.0;
                        meterBill.newbasic =0.0;
                    }
                }
            }
        }
    }

    private void computeSCDiscount(MeterBill meterBill){
        String rate_type = meterBill.getRatetype();

        int consumption = meterBill.getConsumption();

        //check for Rate type
        //senior citizen living in residential area
        if(rate_type.equals("SW")||rate_type.equals("SA")){
            //only applies if consumption is less than 30 cu mm
            if(consumption <=30){
                GLCharge glscdiscn = getGLCharge(SCDS);
                double discount= meterBill.getBasicCharge() * -(glscdiscn.getGl_rate());

                //round to the nearest centavos
                discount = Utils.roundDouble(discount);
                insertSAPData(meterBill,"ZDISSC",0.00,discount,0);
                meterBill.setSc_discount(discount);
            }
        }
        else if(rate_type.equals("HW")||rate_type.equals("HA")){
            GLCharge glscdiscn = getGLCharge(HFTA);
            double discount= meterBill.getBasicCharge() * -(glscdiscn.getGl_rate());
            //round to the nearest centavos
            discount = Utils.roundDouble(discount);
            insertSAPData(meterBill,"ZDISSC",0.00,discount,0);
            meterBill.setSc_discount(discount);
        }

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
            if(vatExempt.equals("0")){
                double  totChargeB4Tax = meterBill.getTotcurb4tax();
                GLCharge glVAT =  getGLCharge(VATX);
                double vat =  totChargeB4Tax * glVAT.getGl_rate();
                vat = Utils.roundDouble(vat);
                meterBill.setVat(vat);
            }
        }
    }

   private void minimumCharge(MeterBill  meterBill){
        double basic_Charge;
       String strpro = meterBill.getTariffPro();
        double discount=0.0;
        double old_price =0.0;
        double old_amount=0.0;



       if(meterBill.getBillClass().equals(RESIDENTIAL)){
            GLCharge glres = getGLCharge(RESB);
            GLCharge glpatr = getGLCharge(PATR);
            GLCharge glrdls = getGLCharge(RLDS);
            basic_Charge = glres.getGl_rate();



           //old days -  effective date - previous reading date
           int OD = Utils.dateDiff(glres.getEffectivity_date(),meterBill.getPrevRdgDate()) +1;
           //new days = present reading date - effectivity  date +1
           int ND = Utils.dateDiff(meterBill.getPresRdgDate(),glres.getEffectivity_date())+1;
           //billing period present reading date - previous reading date
           int DBP = Utils.dateDiff(meterBill.getPresRdgDate(),meterBill.getPrevRdgDate());


           double  gl_rate =  glres.getGl_rate();
           double  gl_rate_old =  glres.getGl_rate_old();

           if(Utils.isNotEmpty(strpro)){
               char proratetype =  strpro.charAt(0);
               switch (proratetype){
                   case PRO_TYPE1 :
                       old_price = gl_rate_old;
                       old_amount = gl_rate_old;
                       break;
                   case WITHPRORATE:
                       old_price = glres.getGl_rate_old();
                       old_amount = old_price *((double) OD/(double) DBP);
                       basic_Charge = basic_Charge * ((double)ND/(double) DBP);
                       old_amount = Utils.roundDouble(old_amount);

                       break;


               }
           }



           double zdiscn = basic_Charge * -(glrdls.getGl_rate());
            double zdispa = basic_Charge * -(glpatr.getGl_rate());
            //round values
            zdiscn = Utils.roundDouble(zdiscn);
            zdispa = Utils.roundDouble(zdispa);
            discount = Utils.roundDouble(zdiscn+ zdispa);
            basic_Charge = Utils.roundDouble(basic_Charge);
            meterBill.setDiscount(discount);
            updateBasicCharge(meterBill,basic_Charge,discount);
            insertSAPData(meterBill,"ZBASIC",basic_Charge,basic_Charge,old_price,
                    old_amount,String.valueOf(meterBill.getConsumption()));
            insertSAPData(meterBill,"ZDISCN",0.00,zdiscn,0 );
            insertSAPData(meterBill,"ZDISPA",0.00,zdispa,0 );
            meterBill.setBasicCharge(basic_Charge+zdiscn+zdispa);
        }
        else {
            ArrayList<Tariff> arryTariff = billDao.getTariffs(meterBill.getBillClass());
            Tariff tariff = arryTariff.get(0);
            basic_Charge =  tariff.getTierAmount();



           //old days -  effective date - previous reading date +1
           int OD = Utils.dateDiff(tariff.getEffectDate(),meterBill.getPrevRdgDate()) +1;
           //new days = present reading date - effectivity  date +1
           int ND = Utils.dateDiff(meterBill.getPresRdgDate(),tariff.getEffectDate())+1;
           //billing period present reading date - previous reading date
           int DBP = Utils.dateDiff(meterBill.getPresRdgDate(),meterBill.getPrevRdgDate());


           if(Utils.isNotEmpty(strpro)){
               char proratetype =  strpro.charAt(0);
               switch (proratetype){
                   case PRO_TYPE1 :
                       old_price = tariff.getOld_price();
                       old_amount = tariff.getOld_tierAmount();
                       break;
                   case WITHPRORATE:
                        old_price = tariff.getOld_tierAmount();
                       old_amount = old_price *((double) OD/(double) DBP);
                       basic_Charge = basic_Charge * ((double)ND/(double) DBP);
                       old_amount = Utils.roundDouble(old_amount);
                       basic_Charge = Utils.roundDouble(basic_Charge);
                       break;


               }
           }

           basic_Charge = Utils.roundDouble(basic_Charge);
           updateBasicCharge(meterBill,basic_Charge,0.0);
           insertSAPData(meterBill,"ZBASIC",basic_Charge,basic_Charge,old_price,
                   old_amount,String.valueOf(meterBill.getConsumption()));
            meterBill.setBasicCharge(basic_Charge);
        }


    }

    private void updateBasicCharge(MeterBill meterBill,double basic_charge,double discount){
        billDao.updateBasicCharge(basic_charge,discount,meterBill.getId());
        meterBill.setBasicCharge(basic_charge);
    }

    @Override
    void getBulkBasicCharge(MeterBill meterBill) {
        int consumption =  meterBill.getConsumption();
        String strusers = meterBill.getNumusers();
        double discount =0.0;

        String strpro = meterBill.getTariffPro();
        if(Utils.isNotEmpty(meterBill.getSpbillrule())){
            String spid = meterBill.getSpbillrule();
            if(spid.equals("1")){
                spBillRule = getSPBillRule(spid);
            }
        }
        char proratetype = NOPRORATE;
        if(Utils.isNotEmpty(strpro)){
            proratetype =  strpro.charAt(0);

        }

        if(Utils.isNotEmpty(strusers)){
            int numUser = Integer.parseInt(strusers);
            double ave_cons =  consumption/numUser;
            ave_cons = Utils.roundDouble(ave_cons);
            if(ave_cons>10.0){
                ArrayList<Tariff> arryTariff = billDao.getTariffs(meterBill.getBillClass());
                double basic_charge=0.0;
                int size = arryTariff.size();
                for(int i=0;i<size;i++){
                    Tariff tariff = arryTariff.get(i);

                    //old days -  effective date - previous reading date +1
                    int OD = Utils.dateDiff(tariff.getEffectDate(),meterBill.getPrevRdgDate()) +1;
                    //new days = present reading date - effectivity  date +1
                    int ND = Utils.dateDiff(meterBill.getPresRdgDate(),tariff.getEffectDate())+1;
                    //billing period present reading date - previous reading date
                    int DBP = Utils.dateDiff(meterBill.getPresRdgDate(),meterBill.getPrevRdgDate());

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
                        switch (proratetype){
                            case WITHPRORATE:
                            case PRO_TYPE1:
                                old_amount =  tariff.getOld_baseAmount();
                                old_price = old_amount;
                                break;
                        }

                    }
                    else {
                        switch (proratetype){
                            case NOPRORATE:
                                amount = price * quantity;
                                break;
                            case PRO_TYPE1:
                                amount = price * quantity;
                                old_price = tariff.getOld_price();
                                old_amount =  old_price * quantity;
                                break;
                            case WITHPRORATE:
                                old_price = tariff.getOld_price();
                                amount = price*quantity *  ((double)ND/(double)DBP);
                                old_amount = old_price*quantity *  ((double)OD/(double)DBP);
                                break;
                        }
                    }
                    if(low_limit<=ave_cons && ave_cons<=high_limit){
                        double quantity1 = ave_cons - low_limit +1;
                        amount = price * quantity1;
                        totalAmount = amount +old_amount;
                        basic_charge += totalAmount;
                         insertSAPData(meterBill,"ZAVPRC",price,amount,old_price,old_amount,
                                 String.valueOf(quantity1));
                        break;
                    }
                    totalAmount = amount+ old_amount;
                    basic_charge += totalAmount;
                    meterBill.newbasic+=amount;
                    meterBill.oldbasic+=old_amount;
                    insertSAPData(meterBill,"ZAVPRC",price,amount,old_price,
                            old_amount,String.valueOf(quantity));

                }
                double final_basic_charge =  basic_charge * numUser;
                final_basic_charge = Utils.roundDouble(final_basic_charge);
                updateBasicCharge(meterBill,final_basic_charge,discount);
                insertSAPData(meterBill,"ZBASIC",0.00,final_basic_charge,0 );
            }
            else {
                minimumCharge(meterBill);
            }

        }

        otherCharges(meterBill);
        totalAmount(meterBill);
        if(listener!=null){
            listener.onPostBillResult(meterBill);
        }
    }

    @Override
    void getGT3BasicCharge(MeterBill meterBill) {
        int consumption =  meterBill.getConsumption();
        String gt34factor = meterBill.getGt34factor();
        double discount =0.00;
        String strpro = meterBill.getTariffPro();
        if(Utils.isNotEmpty(meterBill.getSpbillrule())){
            String spid = meterBill.getSpbillrule();
            if(spid.equals("1")){
                spBillRule = getSPBillRule(spid);
            }
        }
        char proratetype = NOPRORATE;
        if(Utils.isNotEmpty(strpro)){
            proratetype =  strpro.charAt(0);

        }
        if(Utils.isNotEmpty(gt34factor)){
            double gtfactor = Double.parseDouble(gt34factor);
            gtfactor = Utils.roundDouble6(gtfactor);

            Double d = Double.valueOf(consumption/gtfactor);
            int ave_cons =d.intValue();
            if(ave_cons>10){
                ArrayList<Tariff> arryTariff = billDao.getTariffs(meterBill.getBillClass());
                double basic_charge=0.0;
                int size = arryTariff.size();
                for(int i=0;i<size;i++){
                    Tariff tariff = arryTariff.get(i);


                    //old days -  effective date - previous reading date +1
                    int OD = Utils.dateDiff(tariff.getEffectDate(),meterBill.getPrevRdgDate()) +1;
                    //new days = present reading date - effectivity  date +1
                    int ND = Utils.dateDiff(meterBill.getPresRdgDate(),tariff.getEffectDate())+1;
                    //billing period present reading date - previous reading date
                    int DBP = Utils.dateDiff(meterBill.getPresRdgDate(),meterBill.getPrevRdgDate());


                    int low_limit = tariff.getLowLimit();
                    int high_limit = tariff.getHighLimit();
                    double amount = tariff.getBaseAmount();
                    double old_amount = 0.0;//tariff.getOld_baseAmount();
                    double old_price = 0.0;//tariff.getOld_price();
                    int quantity = tariff.getCons_band();
                    quantity = quantity * (int)gtfactor;
                    double price = tariff.getPrice();
                    double totalAmount;
                    if(i==0){
                        price =amount;
                        switch (proratetype){
                            case WITHPRORATE:
                            case PRO_TYPE1:
                                old_amount =  tariff.getOld_baseAmount();
                                old_price = old_amount;
                                break;
                        }

                    }
                    else {
                        switch (proratetype){
                            case NOPRORATE:
                                amount = price * gtfactor;
                                break;
                            case PRO_TYPE1:
                                amount = price * gtfactor;
                                old_price = tariff.getOld_price();
                                old_amount =  old_price * gtfactor;
                                break;
                            case WITHPRORATE:
                                old_price = tariff.getOld_price();
                                amount = price*gtfactor *  ((double)ND/(double)DBP);
                                old_amount = old_price*gtfactor *  ((double)OD/(double)DBP);
                                break;
                        }
                    }

                    if(low_limit<=consumption && consumption<=high_limit){
                        quantity = consumption - low_limit +1;
                        quantity = quantity *(int)gtfactor;

                        switch (proratetype){
                            case NOPRORATE:
                                amount = price * gtfactor;
                                break;
                            case PRO_TYPE1:
                                amount = price * gtfactor;
                                old_price = tariff.getOld_price();
                                old_amount =  old_price * gtfactor;
                                break;
                            case WITHPRORATE:
                                old_price = tariff.getOld_price();
                                amount = price*gtfactor *  ((double)ND/(double)DBP);
                                old_amount = old_price*gtfactor *   ((double)OD/(double)DBP);
                                break;
                        }


                        amount = Utils.roundDouble(amount);
                        old_amount = Utils.roundDouble(old_amount);
                        totalAmount = amount +old_amount;
                        basic_charge += totalAmount;
                        meterBill.newbasic+=amount;
                        meterBill.oldbasic+=old_amount;
                        insertSAPData(meterBill,"ZBASIC",price,amount,old_price,
                                old_amount,String.valueOf(quantity));
                        break;
                    }

                    amount = Utils.roundDouble(amount);
                    old_amount = Utils.roundDouble(old_amount);
                    totalAmount = amount+ old_amount;
                    basic_charge += totalAmount;
                    meterBill.newbasic+=amount;
                    meterBill.oldbasic+=old_amount;
                    insertSAPData(meterBill,"ZBASIC",price,amount,old_price,old_amount,
                            String.valueOf(quantity));

                }
                updateBasicCharge(meterBill,basic_charge,discount);

            }
            else {
                minimumCharge(meterBill);
            }
        }

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
        insertSAPData( meterBill, sapline, price, amount,0,0,String.valueOf(quantity ));
    }

    private void insertSAPData(MeterBill meterBill,String sapline,double amount,double oldAmount){
        double price =0;
        double oldprice =0;
        insertSAPData( meterBill, sapline, price, amount,oldprice,oldAmount,"0");
    }

    private void insertSAPData(MeterBill meterBill, String sapline,
                              double price, double amount, double oldprice, double oldamout, String quantity){
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
