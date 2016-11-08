package com.indra.rover.mwsi.compute.consumption;


import android.util.Log;

import com.indra.rover.mwsi.data.pojo.meter_reading.MeterConsumption;
import com.indra.rover.mwsi.data.pojo.meter_reading.display.MeterInfo;
import com.indra.rover.mwsi.utils.Utils;

import java.util.List;

/**
 * Class to compute  the consumption
 */
public class CompConsumption extends Compute implements Compute.ConsumptionListener{



    public CompConsumption(ConsumptionListener listener){
        super(listener);

    }

    /**
     * action to start the computation of the account
     */
    public void compute(MeterConsumption meterConsumption){
        this.meterConsObj = meterConsumption;
        //check first if account is block or not
        if(meterConsObj!=null){
            String block_tag = meterConsObj.getBlock_tag();
               if(Utils.isNotEmpty(block_tag)){
                   //account is block then proceed to compute BlockAccount
                   if(block_tag.equals("B")||block_tag.equals("P")){
                        CompBlockAccn blockAccount = new CompBlockAccn(this);
                        blockAccount.compute(meterConsObj);
                   }
                   else {
                       computeReadBill();
                   }
               }
                else {
                   computeReadBill();
               }
        }
   }

    private void computeReadBill(){
        //is data available?
        //check the conditions
        //current reading
        String reading =  meterConsObj.getPresent_rdg();
        //bill related observation code
        String oc1 = meterConsObj.getFfcode1();
        //non bill related observation code
        String oc2 = meterConsObj.getFfcode2();

        if(Utils.isNotEmpty(reading)){
            Log.i("Test","present reading not empty="+reading);
            //reading plus non-bill related oc
            if(Utils.isNotEmpty(oc2)){
                checkNewMeterInfo();
            }
            //reading only plus billed related OC
            else if(Utils.isNotEmpty(oc1)){
                meterConsObj.setPrintTag(MeterInfo.BILLABLE);
                //tag as average
                meterConsObj.setMrType(MeterInfo.MRTYPE91);
                decisionB();
            }
            else {
                //for if there is new meter info in the account
                checkNewMeterInfo();
            }
        } else {
            Log.i("Test","present reading is empty="+reading);
            //no reading but entered only OC11, OC12 or OC14
            if(Utils.isNotEmpty(oc2)){
                //entered only oc11 , oc12 or oc14
                if(oc2.equals("11")||oc2.equals("12")||oc2.equals("14")){
                    String prev_oc2 = meterConsObj.getPrevff2();
                    //is previous oc1 is OC11, OC12, OC14
                    if(prev_oc2.equals("11")||prev_oc2.equals("12")||prev_oc2.equals("14")){
                        //use decision D - bill consumption set to minimum - tag as average
                        meterConsObj.setPrintTag(MeterInfo.BILLABLE);
                        decisionE();
                    }
                    else {
                        meterConsObj.setPrintTag(MeterInfo.BILLABLE);
                        meterConsObj.setSpComp("0");
                        //use decision B  average consumption set to billed consumption - tag as average
                        meterConsObj.setMrType(MeterInfo.MRTYPE91);
                        decisionB();
                    }
                }else{
                    meterConsObj.setPrintTag(MeterInfo.BILLABLE);
                    meterConsObj.setSpComp("0");
                    meterConsObj.setMrType(MeterInfo.MRTYPE91);
                    decisionB();
                }
            }
            else {
                meterConsObj.setPrintTag(MeterInfo.BILLABLE);
                meterConsObj.setSpComp("0");
                //or OC only excluding 11,12,14
                meterConsObj.setMrType(MeterInfo.MRTYPE91);
                decisionB();
            }
        }
    }


    private void checkNewMeterInfo(){
        String has_newmeterInfo =  meterConsObj.getDreplmtr_code();
        if(Utils.isNotEmpty(has_newmeterInfo)){
            //if hasnewmeterInfo is not to zero
            if(!has_newmeterInfo.equals("0")){
                computeWithNewMeter();
            }
            else {
                computeWithPresentReading();
            }
        }else {
            computeWithPresentReading();
        }
    }

    private void computeWithNewMeter(){
        int present_reading=  Integer.parseInt(meterConsObj.getPresent_rdg());
        String previous_reading = meterConsObj.getPrev_rdg();
        //is previous consumption is average
        String prev_cons_avg = meterConsObj.getPrev_con_avg();
        int bill_prev_reading = Integer.parseInt(meterConsObj.getBill_prev_rdg());
        //if yes
        if(prev_cons_avg.equals("1")){
            //is present reading greater than actual billed previous reading
            if(present_reading>bill_prev_reading){
                //if previous reading is actual
                if(Utils.isNotEmpty(previous_reading)){
                    int bill_consumption = scenario4();
                    meterConsObj.setBilled_cons(bill_consumption);
                    meterConsObj.setPrintTag(MeterInfo.BILLABLE);
                    meterConsObj.setMrType(MeterInfo.MRTYPE93);
                    //tag as adjusted
                    decisionC();
                }else{
                    //get default consumption
                    int bill_consumption =  defaultCondition();
                    meterConsObj.setBilled_cons(bill_consumption);
                    meterConsObj.setPrintTag(MeterInfo.BILLABLE);
                    meterConsObj.setMrType(MeterInfo.MRTYPE01);
                    decisionA();
                }
            }else {
                //check values/component for compution is present
                if(checkValues()){
                    //if yes use secnario3 to get the bill consumption
                    int bill_consumption = scenario3();
                    meterConsObj.setBilled_cons(bill_consumption);
                    meterConsObj.setPrintTag(MeterInfo.BILLABLE);
                    //tag as adjusted
                    decisionC();
                }
                else {
                    meterConsObj.setPrintTag(MeterInfo.BILLABLE);
                    meterConsObj.setMrType(MeterInfo.MRTYPE93);
                    //otherwise tag as AVERAGE
                    decisionB();
                }

            }
        }else {
           String dreplmtr_code  =    meterConsObj.getDreplmtr_code();
            if(dreplmtr_code.equals("1")){
                if(checkValues2()){
                    //if yes use secnario3 to get the bill consumption
                    int bill_consumption = scenario2();
                    meterConsObj.setBilled_cons(bill_consumption);
                    meterConsObj.setPrintTag(MeterInfo.BILLABLE);
                    meterConsObj.setMrType(MeterInfo.MRTYPE93);
                    //tag as adjusted
                    decisionC();
                }
               int bill_consumption =   scenario2();
                meterConsObj.setBilled_cons(bill_consumption);
                meterConsObj.setPrintTag(MeterInfo.BILLABLE);
                meterConsObj.setMrType(MeterInfo.MRTYPE93);
                //tag as adjusted
                decisionC();
            }
            else if(dreplmtr_code.equals("2")){
                meterConsObj.setPrintTag(MeterInfo.BILLABLE);
                meterConsObj.setMrType(MeterInfo.MRTYPE93);
                //tag as average
                decisionB();

            }else if(dreplmtr_code.equals("3")){
                //check values/component for compution is present
                if(checkValues()){
                    //if yes use secnario3 to get the bill consumption
                    int bill_consumption = scenario3();
                    meterConsObj.setBilled_cons(bill_consumption);
                    //tag as adjusted
                    meterConsObj.setPrintTag(MeterInfo.BILLABLE);
                    decisionC();
                }
                else {
                    //otherwise tag as AVERAGE
                    meterConsObj.setPrintTag(MeterInfo.BILLABLE);
                    meterConsObj.setMrType(MeterInfo.MRTYPE93);
                    decisionB();
                }
            }
        }
    }

    private void computeWithPresentReading(){
        int present_reading=  Integer.parseInt(meterConsObj.getPresent_rdg());
        String previous_reading = meterConsObj.getPrev_rdg();
        int bill_prev_reading = Integer.parseInt(meterConsObj.getBill_prev_rdg());
        String prev_oc1 = meterConsObj.getPrevff1();
        String prev_oc2 = meterConsObj.getPrevff2();
        String previous_cons_avg = meterConsObj.getPrev_con_avg();
        //if present reading is greater than equal to bill previous reading
        if(present_reading>=bill_prev_reading){
            //is previous consumption average?
            //if yes check previous OC
            if(previous_cons_avg.equals(AVERAGE)){
                //if previous OC 26 or 29
                if(prev_oc1.equals(INTERCHANGEMR)||prev_oc2.equals(NEW_METER)){
                    //if yes
                    meterConsObj.setPrintTag(MeterInfo.BILLABLE);

                    decisionD();
                }else{
                int bill_consumption =   defaultCondition();
                meterConsObj.setBilled_cons(bill_consumption);
                    meterConsObj.setPrintTag(MeterInfo.BILLABLE);
                    meterConsObj.setMrType(MeterInfo.MRTYPE93);
                    decisionC();
                }

            }
            //otherwise get Default consumption
            else {
                int bill_consumption =   defaultCondition();
                meterConsObj.setBilled_cons(bill_consumption);
                meterConsObj.setPrintTag(MeterInfo.BILLABLE);
                meterConsObj.setMrType(MeterInfo.MRTYPE01);
                decisionA();
            }
        }else {
            //if previous reading is actual and previous is less than the present reading
            if(Utils.isNotEmpty(previous_reading)){
                int prev_reading = Integer.parseInt(previous_reading);
                //if previous reading is less than to present reading
                if(prev_reading<present_reading){
                    computeAgainstPrevRecord();
                } else {

                    computeAgainstNumDials();
                }
            }
            else {
                computeAgainstNumDials();
            }
        }
    }

    /**
     * do the computation against the previous reading records
     */
    public void computeAgainstPrevRecord(){
        String prevOC2 = meterConsObj.getPrevff2();


        String bill_prev_tag2 = meterConsObj.getBill_prev_act_tag();
        int billPrevRdg2 =  Integer.parseInt(meterConsObj.getBill_prev_rdg2());
        int billPrevRdg =  Integer.parseInt(meterConsObj.getBill_prev_rdg());
        if(Utils.isNotEmpty(bill_prev_tag2)){
            //Actual rdg two months prior to current month is available and actual ?
            if(bill_prev_tag2.equals("1")){
                //Previous rdg is less than actual rdg two months prior to current month?
                if(billPrevRdg>billPrevRdg2){
                    //OC 29 entered the previous month
                    if(prevOC2.equals(NEW_METER)){
                        //use scenario 4 to compute the consumption
                        int bill_consumption = scenario4();
                        meterConsObj.setBilled_cons(bill_consumption);
                        meterConsObj.setPrintTag(MeterInfo.BILLABLE);
                        //tag as adjusted
                        meterConsObj.setMrType(MeterInfo.MRTYPE93);
                        decisionC();
                    }
                    else {
                        //refer to formula for special condition5 to compute consumption
                        int bill_cosumption = scenario5();
                        //consumption computed less than zero
                        if(bill_cosumption<0){
                            meterConsObj.setPrintTag(MeterInfo.BILLABLE);
                            meterConsObj.setMrType(MeterInfo.MRTYPE93);
                            decisionB();
                        }else {
                            //use consumption computed as consumption
                            meterConsObj.setBilled_cons(bill_cosumption);
                            meterConsObj.setPrintTag(MeterInfo.BILLABLE);
                            meterConsObj.setMrType(MeterInfo.MRTYPE93);
                            //tag as adjusted
                            decisionC();
                        }
                    }

                }else {
                    //use scenario 4 to compute the consumption
                    int bill_consumption = scenario4();
                    meterConsObj.setBilled_cons(bill_consumption);
                    meterConsObj.setPrintTag(MeterInfo.BILLABLE);
                    meterConsObj.setMrType(MeterInfo.MRTYPE93);
                    //tag as adjusted
                    decisionC();
                }
            }
            else {
                //use scenario 4 to compute the consumption
                int bill_consumption = scenario4();
                meterConsObj.setBilled_cons(bill_consumption);
                meterConsObj.setPrintTag(MeterInfo.BILLABLE);
                meterConsObj.setMrType(MeterInfo.MRTYPE93);
                //tag as adjusted
                decisionC();
            }

        }else {
            //use scenario 4 to compute the consumption
            int bill_consumption = scenario4();
            meterConsObj.setBilled_cons(bill_consumption);
            meterConsObj.setPrintTag(MeterInfo.BILLABLE);
            meterConsObj.setMrType(MeterInfo.MRTYPE93);
            //tag as adjusted
            decisionC();
        }
    }

    public void computeAgainstNumDials(){
        //use formula scenario 1 to compute the scenario
       int bill_consumption =   scenario1();
        String oc2 = meterConsObj.getFfcode2();
        if(Utils.isNotEmpty(meterConsObj.getNum_dials())){

            boolean isLess = isLessMaxCapacity(bill_consumption);
            if(isLess){
                if(Utils.isNotEmpty(oc2)){
                    if(oc2.equals(NEW_METER)){
                        meterConsObj.setPrintTag(MeterInfo.BILLABLE);
                        meterConsObj.setMrType(MeterInfo.MRTYPE93);
                        //TAG AS average
                        decisionB();
                    }
                    else {
                        meterConsObj.setBilled_cons(bill_consumption);
                        meterConsObj.setPrintTag(MeterInfo.BILLABLE);
                        meterConsObj.setMrType(MeterInfo.MRTYPE01);
                        //tag as actual
                        decisionA();
                    }
                }
                else {
                    meterConsObj.setBilled_cons(bill_consumption);
                    meterConsObj.setPrintTag(MeterInfo.BILLABLE);
                    meterConsObj.setMrType(MeterInfo.MRTYPE01);
                    //tag as actual
                    decisionA();
                }
            }
            else {
                //tag as average
                meterConsObj.setPrintTag(MeterInfo.BILLABLE);
                meterConsObj.setMrType(MeterInfo.MRTYPE93);
                decisionB();
            }
        }
    }


    private boolean isLessMaxCapacity(int bill_consumption){
        //get num dials
        int num_dials = Integer.parseInt(meterConsObj.getNum_dials());
        int max_capacity = meterConsObj.getMax_cap();
        boolean isMaxed = false;
        float percent = 0.0f;
         switch(num_dials){
             case 4:
                 percent = .10f;
                 break;
             case 5:
                 percent = .20f;
                 break;
             case 6:
             case 7:
             case 8:
                 percent = .40f;
                 break;
         }
        if(bill_consumption<(max_capacity*percent))
            isMaxed = true;
        return  isMaxed;
    }


    @Override
    public void onPostConsResult(MeterConsumption meterConsumption) {
         listener.onPostConsResult(meterConsumption);
    }

    @Override
    public void onPrintChildMeters(MeterConsumption meterConsumption, List<MeterConsumption> csChildMeter) {

    }
}
