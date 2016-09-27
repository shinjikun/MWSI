package com.indra.rover.mwsi.compute;

import com.indra.rover.mwsi.data.pojo.meter_reading.MeterConsumption;
import com.indra.rover.mwsi.utils.Utils;


public class Compute {

    MeterConsumption meterConsObj;

    ConsumptionListener listener;
    public Compute(ConsumptionListener listener){
        this.listener = listener;
    }


    /**
     * minimum billed for billed consumption 10 cubic meter
     */
    private final static int minimum_bill =10;

    /** Normal Condition - Current Lesst Billed Previous Consumption
     * Default computation of consumption
     *  consumption =  present reading - bill previous reading
     * @return consumption
     */
    public int defaultCondition(){
        int present_reading=  Integer.parseInt(meterConsObj.getPresent_rdg());
        int bill_prev_reading = Integer.parseInt(meterConsObj.getBill_prev_rdg());
        meterConsObj.setSpComp("0");
        return  present_reading -  bill_prev_reading;
    }


    /**
     * Negative Consumption-  Tumbled Meter
     * special condition 1
     * consumption =  number of dials - billed previous reading  + previous reading
     */
    public int scenario1(){
        int num_dials =  meterConsObj.getMax_cap();
        int present_reading=  Integer.parseInt(meterConsObj.getPresent_rdg());
        int bill_prev_reading = Integer.parseInt(meterConsObj.getBill_prev_rdg());
        meterConsObj.setSpComp("1");
        return num_dials-(present_reading+bill_prev_reading);
    }


    /**
     * New Meter All Information Available
     * special condition 2
     * T_CURRENT_RDG.PRESRDG + T_DOWNLOAD.NMCONSFACTOR
     * @return consumption
     */
    public int scenario2(){
        int  nmconsfactor =  Integer.parseInt(meterConsObj.getNminconsfactor());
        int present_reading=  Integer.parseInt(meterConsObj.getPresent_rdg());
        meterConsObj.setSpComp("2");
        return present_reading+ nmconsfactor;
    }

    /**
     *  New Meter  Incomplete Information available
     *  (T_CURRENT_RDG.PRESRDG  - T_ DOWNLOAD.NMINITRDG) * T_DOWNLOAD.NMCONSFACTOR
     * scenario 3
     * @return computed consumption
     */
    public int scenario3(){
        int present_reading=  Integer.parseInt(meterConsObj.getPresent_rdg());
        int  nminitrdg = Integer.parseInt(meterConsObj.getNminitrdg());
        int  nmconsfactor =  Integer.parseInt(meterConsObj.getNminconsfactor());
        meterConsObj.setSpComp("3");
        return (present_reading-nminitrdg)*nmconsfactor;
    }

    /**
     * Negative Consumption - Previous Reading Actual and Less Than Or Equal To Current Reading
     * scenario 4 to compute consumption
     *  T_CURRENT_RDG.PRESRDG  - T_ DOWNLOAD.ACTPREVRDG
     * @return computed consumption
     */
    public int scenario4(){
        int present_reading=  Integer.parseInt(meterConsObj.getPresent_rdg());
        int actual_prev_reading =  Integer.parseInt(meterConsObj.getPrev_rdg());
        meterConsObj.setSpComp("4");
        return present_reading - actual_prev_reading;

    }

    /**
     * Use BillPrevRdg2 with BillPrevActTag
     * scenario 5 to compute consumption
     * T_CURRENT_RDG.PRESRDG  - T_ DOWNLOAD.BILLPREVRDG2
     * @return computed consumption
     */
    public int scenario5(){
        int present_reading=  Integer.parseInt(meterConsObj.getPresent_rdg());
        int bill_prev_rdg2 =  Integer.parseInt(meterConsObj.getBill_prev_rdg2());
        meterConsObj.setSpComp("5");
        return present_reading-bill_prev_rdg2;
    }

    /**
     * Block Tag P use Current Reading
     * @return computed consumption
     */
    public int scenario6(){
        int present_reading=  Integer.parseInt(meterConsObj.getPresent_rdg());
        meterConsObj.setSpComp("6");
        return present_reading;
    }

    /**
     * decision A Tag Consumption as ACTUAL
     */
    public void decisionA(){

        meterConsObj.setConstype_code("0");
        if(listener!=null){
            listener.onPostConsResult(meterConsObj);
        }
    }

    /**
     * decision B Tag as AVERAGE Consumption
     */
     public void decisionB(){
         //use average consumption as bill consumption
         int average_consumption=  Integer.parseInt(meterConsObj.getAve_consumption());
         meterConsObj.setBilled_cons(average_consumption);
         meterConsObj.setConstype_code("1");
         if(listener!=null){
             listener.onPostConsResult(meterConsObj);
         }
     }

    /**
     * decision C Tag as ADJUSTED Consumption
     */
     public void decisionC(){
         meterConsObj.setConstype_code("2");
         if(listener!=null){
             listener.onPostConsResult(meterConsObj);
         }
     }

    /**
     * decision D
     */
      public void decisionD(){
          String prev_reading = meterConsObj.getPrev_rdg();
          if(Utils.isNotEmpty(prev_reading)){
            int billed_consumption =  defaultCondition();
              meterConsObj.setBilled_cons(billed_consumption);
              //tag as actual
              decisionA();
          }
          else {
              int billed_consumption = scenario4();
              meterConsObj.setBilled_cons(billed_consumption);
              //tag as adjusted
              decisionC();
          }
      }

    /**
     * decision E Tag as AVERAGE Consumption
     * use the minimum billed bill cosumption 10 cu. m as consumption
     */
    public void decisionE(){
        meterConsObj.setBilled_cons(minimum_bill);
        meterConsObj.setConstype_code("1");
        if(listener!=null){
            listener.onPostConsResult(meterConsObj);
        }
    }

    /**
     * check values if not empty before using scenario 3 formula
     * @return true if values is not empty otherwise return false
     */
    public boolean checkValues(){

        return Utils.isNotEmpty(meterConsObj.getPresent_rdg())&&
                Utils.isNotEmpty(meterConsObj.getNminitrdg())&&
                Utils.isNotEmpty(meterConsObj.getNminconsfactor());
    }

    /**
     * check values if not empty before using scenario 2 formula
     * @return  ture if values is not empty otherwise return false
     */
    public boolean checkValues2(){
        return  Utils.isNotEmpty(meterConsObj.getNminconsfactor()) &&
                Utils.isNotEmpty(meterConsObj.getPresent_rdg());
    }


    public interface ConsumptionListener {
        void onPostConsResult(MeterConsumption meterConsumption);

    }


}
