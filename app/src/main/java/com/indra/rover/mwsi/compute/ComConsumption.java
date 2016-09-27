package com.indra.rover.mwsi.compute;


import android.util.Log;

import com.indra.rover.mwsi.data.pojo.meter_reading.MeterConsumption;
import com.indra.rover.mwsi.utils.Utils;

/**
 * Class to compute  the consumption
 */
public class ComConsumption extends Compute{



    public ComConsumption(ConsumptionListener listener){
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
                        ComBlockAccn blockAccount = new ComBlockAccn(this.listener);
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
                //reading plus non-bill related oc excluding oc11, 12 and 14
                if(!oc2.equals("11")||!oc2.equals("12")||!oc2.equals("14")){
                    //tag as averages
                    decisionB();
                }
                //reading plus non billed related OC
                else {
                    checkNewMeterInfo();
                }
            }
            //reading only plus billed related OC
            else if(Utils.isNotEmpty(oc1)){
                //tag as average
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
                        decisionE();
                    }
                    else {
                        //use decision B  average consumption set to billed consumption - tag as average
                        decisionB();
                    }
                }else{
                    decisionB();
                }
            }
            else {
                //or OC only excluding 11,12,14
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
                    //tag as adjusted
                    decisionC();
                }else{
                    //get default consumption
                    int bill_consumption =  defaultCondition();
                    meterConsObj.setBilled_cons(bill_consumption);
                    decisionA();
                }
            }else {
                //check values/component for compution is present
                if(checkValues()){
                    //if yes use secnario3 to get the bill consumption
                    int bill_consumption = scenario3();
                    meterConsObj.setBilled_cons(bill_consumption);
                    //tag as adjusted
                    decisionC();
                }
                else {
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
                    //tag as adjusted
                    decisionC();
                }
               int bill_consumption =   scenario2();
                meterConsObj.setBilled_cons(bill_consumption);
                //tag as adjusted
                decisionC();
            }
            else if(dreplmtr_code.equals("2")){
                //tag as average
                decisionB();

            }else if(dreplmtr_code.equals("3")){
                //check values/component for compution is present
                if(checkValues()){
                    //if yes use secnario3 to get the bill consumption
                    int bill_consumption = scenario3();
                    meterConsObj.setBilled_cons(bill_consumption);
                    //tag as adjusted
                    decisionC();
                }
                else {
                    //otherwise tag as AVERAGE
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
            if(previous_cons_avg.equals("1")){
                //if previous OC 26 or 29
                if(prev_oc1.equals("26")||prev_oc2.equals("29")){
                    //if yes
                    decisionD();
                }else{
                int bill_consumption =   defaultCondition();
                meterConsObj.setBilled_cons(bill_consumption);
                    decisionC();
                }

            }
            //otherwise get Default consumption
            else {
                int bill_consumption =   defaultCondition();
                meterConsObj.setBilled_cons(bill_consumption);
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
        //Actual rdg two months prior to current month is available and actual ?
        //Previous rdg is less than actual rdg two months prior to current month?
        String bill_previous_2 = meterConsObj.getBill_prev_act_tag();
        if(Utils.isNotEmpty(bill_previous_2)){
                //previous oc2 enter is 29
                if(prevOC2.equals("29")){
                    //use scenario 4 to compute the consumption
                    int bill_consumption = scenario4();
                    meterConsObj.setBilled_cons(bill_consumption);
                    //tag as adjusted
                    decisionC();
                }
                else {
                    //refer to formula for special condtion 5 to compute for consumption
                    int bill_consumption = scenario5();
                    //consumption computed less than 0
                    if(bill_consumption<0){
                        //tag as average
                        decisionB();
                    }
                    else {
                        meterConsObj.setBilled_cons(bill_consumption);
                        //tag as adjusted
                        decisionC();
                    }
                }

        }else {
            //use scenario 4 to compute the consumption
            int bill_consumption = scenario4();
            meterConsObj.setBilled_cons(bill_consumption);
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
                    if(oc2.equals("29")){
                        //TAG AS average
                        decisionB();
                    }
                    else {
                        meterConsObj.setBilled_cons(bill_consumption);
                        //tag as actual
                        decisionA();
                    }
                }
                else {
                    meterConsObj.setBilled_cons(bill_consumption);
                    //tag as actual
                    decisionA();
                }
            }
            else {
                //tag as average
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
                 percent = .40f;
                 break;
         }
        if(bill_consumption<(max_capacity*percent))
            isMaxed = true;
        return  isMaxed;
    }












}
