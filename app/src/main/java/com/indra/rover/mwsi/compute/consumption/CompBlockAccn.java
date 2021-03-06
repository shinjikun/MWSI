package com.indra.rover.mwsi.compute.consumption;

import com.indra.rover.mwsi.data.pojo.meter_reading.MeterConsumption;
import com.indra.rover.mwsi.data.pojo.meter_reading.display.MeterInfo;
import com.indra.rover.mwsi.utils.Utils;

/**
 * Class to compute consumption of a Block Account
 */
public class CompBlockAccn extends  Compute{


    public CompBlockAccn(ConsumptionListener listener){
        super(listener);
    }

    public void compute(MeterConsumption meterConsumption){
        this.meterConsObj = meterConsumption;
        if(meterConsObj!=null){

            String has_newmeterInfo =  meterConsObj.getDreplmtr_code();
            //has new meter info,replacement date and initial reading
            //no new meter info,replacement date or initial reading
            if(Utils.isNotEmpty(has_newmeterInfo)){
                if(has_newmeterInfo.equals("0")){
                    clusterA();
                } else {
                    //is previous consumption is average
                    String prev_consumption = meterConsObj.getPrev_con_avg();
                    //if yes value is 1
                    if(prev_consumption.equals("1")){
                        clusterB();
                    }
                    //otherwise its actual
                    else {
                        clusterC();
                    }
                }
            } else {
               clusterA();
            }

        }
    }


    private void clusterC(){
        String dreplmtr_code =  meterConsObj.getDreplmtr_code();
        if(dreplmtr_code.equals("1")){
            if(checkValues2()){
                //if yes use secnario3 to get the bill consumption
                int bill_consumption = scenario2();

                boolean isLess = isLessMaxCapacity(bill_consumption);
                if(isLess){
                    int average_consumption=  Integer.parseInt(meterConsObj.getAve_consumption());
                    meterConsObj.setBilled_cons(average_consumption);

                    meterConsObj.setPrintTag(MeterInfo.BILLABLE);
                    meterConsObj.setMrType(MeterInfo.MRTYPE93);
                    decisionC();
                }
                else {
                    //if yes use secnario3 to get the bill consumption
                     bill_consumption = scenario3();
                    meterConsObj.setBilled_cons(bill_consumption);
                    meterConsObj.setPrintTag(MeterInfo.BILLABLE);
                    //tag as adjusted
                    meterConsObj.setMrType(MeterInfo.MRTYPE93);
                    decisionC();
                }


                /*
                meterConsObj.setBilled_cons(bill_consumption);
                meterConsObj.setPrintTag(MeterInfo.BILLABLE);
                meterConsObj.setMrType(MeterInfo.MRTYPE93);
                //tag as adjusted
                decisionC();
                */
            }
            else {
                //otherwise NO BILL
                noBill();
            }

        }
        else if(dreplmtr_code.equals("2")){
            //tag as adjusted

            if(checkValues()){
                int average_consumption=  Integer.parseInt(meterConsObj.getAve_consumption());
                meterConsObj.setBilled_cons(average_consumption);
                meterConsObj.setConstype_code(AVERAGE);
                meterConsObj.setPrintTag(MeterInfo.BILLABLE);
                meterConsObj.setMrType(MeterInfo.MRTYPE93);
                decisionC();
            }
            else {
                //otherwise NO BILL
                noBill();
            }


        }else if(dreplmtr_code.equals("3")){
            //check values/component for compution is present
            if(checkValues()){
                //if yes use secnario3 to get the bill consumption
                int bill_consumption = scenario3();
                meterConsObj.setBilled_cons(bill_consumption);
                meterConsObj.setPrintTag(MeterInfo.BILLABLE);
                //tag as adjusted
                meterConsObj.setMrType(MeterInfo.MRTYPE93);
                decisionC();
            }
            else {
                //otherwise NO BILL
                noBill();
            }
        }
    }

    private void clusterB(){
        int present_reading=  Integer.parseInt(meterConsObj.getPresent_rdg());
        int bill_prev_reading = Integer.parseInt(meterConsObj.getBill_prev_rdg());
        //is present reading greater than the bill previous reading
        if(present_reading>bill_prev_reading){
            String previous_reading = meterConsObj.getPrev_rdg();
            // is previous reading is actual or not empty
            if(Utils.isNotEmpty(previous_reading)){
                int bill_consumption = scenario4();
                meterConsObj.setBilled_cons(bill_consumption);
                meterConsObj.setPrintTag(MeterInfo.BILLABLE);
                meterConsObj.setMrType(MeterInfo.MRTYPE01);
                //tag as adjusted
                decisionC();
            }
            else {
                //get consumption by default
                int bill_cosumption =defaultCondition();
                meterConsObj.setBilled_cons(bill_cosumption);
                meterConsObj.setPrintTag(MeterInfo.BILLABLE);
                meterConsObj.setMrType(MeterInfo.MRTYPE01);
                //tag as actual
                decisionA();
            }
        }else {
            if(checkValues()){
                int bill_cosumption = scenario3();
                meterConsObj.setBilled_cons(bill_cosumption);
                //tag as adjusted
                meterConsObj.setPrintTag(MeterInfo.BILLABLE);
                meterConsObj.setMrType(MeterInfo.MRTYPE93);
                decisionC();
            }
            else {
                noBill();
            }
        }
    }

    private void clusterA(){
        String presRdg= meterConsObj.getPresent_rdg();
        String prevRdg = meterConsObj.getPrev_rdg();
        //present reading actual?
        //yes
        if(Utils.isNotEmpty(presRdg)){
            //previous reading actual
            //yes
            if(Utils.isNotEmpty(prevRdg)){
              int pres_rdg =  Integer.parseInt(presRdg);
              int prev_rdg = Integer.parseInt(prevRdg);
                //if present reading is greater than previous reading
                if(pres_rdg>prev_rdg){
                    int bill_consumption = scenario4();
                    meterConsObj.setBilled_cons(bill_consumption);
                    meterConsObj.setPrintTag(MeterInfo.BILLABLE);
                    meterConsObj.setMrType(MeterInfo.MRTYPE01);
                    //tag as actual
                    decisionA();
                }
                else {
                    //account was not billed in previous month and has prev oc 29
                    // BLOCK_TAG values must be P
                    String block_tag = meterConsObj.getBlock_tag();
                    if(block_tag.equals("P")){
                        int bill_consumption = scenario6();
                        meterConsObj.setBilled_cons(bill_consumption);
                        meterConsObj.setPrintTag(MeterInfo.BILLABLE);
                        meterConsObj.setMrType(MeterInfo.MRTYPE93);
                        decisionC();
                        //tag as adjusted
                    }
                    else  {
                        meterConsObj.setBilled_cons(0);
                        meterConsObj.setSpComp("0");

                        meterConsObj.setConstype_code(ACTUAL);
                        noBill();
                    }
                }
            }
            //otherwise no bill
            else {
                //NO BILL
                meterConsObj.setBilled_cons(0);
                meterConsObj.setSpComp("0");
                meterConsObj.setConstype_code(ACTUAL);
                noBill();
            }
        }
        else {
            meterConsObj.setBilled_cons(0);
            meterConsObj.setSpComp("0");
            meterConsObj.setConstype_code(ACTUAL);
            //NO BILL
            noBill();
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
        percent = .50f;
        if(bill_consumption<(max_capacity*percent))
            isMaxed = true;
        return  isMaxed;
    }


}
