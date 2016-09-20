package com.indra.rover.mwsi.compute;

import com.indra.rover.mwsi.data.pojo.meter_reading.MeterConsumption;


public class Compute {

    MeterConsumption meterConsObj;
    enum CONST_TAG{
        ACTUAL,
        AVERAGE,
        ADJUSTED
    }

    /**
     * minimum billed for billed consumption 10 cubic meter
     */
    private final static int minimum_bill =10;

    /**
     * Default computation of consumption
     *  consumption =  present reading - bill previous reading
     * @return consumption
     */
    public int defaultCondition(){
        int present_reading=  Integer.parseInt(meterConsObj.getPresent_rdg());
        int bill_prev_reading = Integer.parseInt(meterConsObj.getBill_prev_rdg());
        return  present_reading -  bill_prev_reading;
    }


    /**
     * special condition 1
     * consumption =  number of dials - billed previous reading  + previous reading
     */
    public int scenario1(){
        int num_dials =  meterConsObj.getMax_cap();
        int present_reading=  Integer.parseInt(meterConsObj.getPresent_rdg());
        int bill_prev_reading = Integer.parseInt(meterConsObj.getBill_prev_rdg());
        return num_dials-(present_reading+bill_prev_reading);
    }


    /**
     * special condition 2
     * T_CURRENT_RDG.PRESRDG + T_DOWNLOAD.NMCONSFACTOR
     * @return consumption
     */
    public int scenario2(){
        int  nmconsfactor =  Integer.parseInt(meterConsObj.getNminconsfactor());
        int present_reading=  Integer.parseInt(meterConsObj.getPresent_rdg());
        return present_reading+ nmconsfactor;
    }

    /**
     *  (T_CURRENT_RDG.PRESRDG  - T_ DOWNLOAD.NMINITRDG) * T_DOWNLOAD.NMCONSFACTOR
     * scenario 3
     * @return computed consumption
     */
    public int scenario3(){
        int present_reading=  Integer.parseInt(meterConsObj.getPresent_rdg());
        int  nminitrdg = Integer.parseInt(meterConsObj.getNminitrdg());
        int  nmconsfactor =  Integer.parseInt(meterConsObj.getNminconsfactor());

        return (present_reading-nminitrdg)*nmconsfactor;
    }

    /** scenario 4 to compute consumption
     *  T_CURRENT_RDG.PRESRDG  - T_ DOWNLOAD.ACTPREVRDG
     * @return computed consumption
     */
    public int scenario4(){
        int present_reading=  Integer.parseInt(meterConsObj.getPresent_rdg());
        int actual_prev_reading =  Integer.parseInt(meterConsObj.getPrev_rdg());
        return present_reading - actual_prev_reading;
    }

    /**
     * scenario 5 to compute consumption
     * T_CURRENT_RDG.PRESRDG  - T_ DOWNLOAD.BILLPREVRDG2
     * @return computed consumption
     */
    public int scenario5(){
        int present_reading=  Integer.parseInt(meterConsObj.getPresent_rdg());
        int bill_prev_rdg2 =  Integer.parseInt(meterConsObj.getBill_prev_rdg2());
        return present_reading-bill_prev_rdg2;
    }

    public int scenario6(){
        int present_reading=  Integer.parseInt(meterConsObj.getPresent_rdg());
        return present_reading;
    }

    /**
     * decision A Tag Consumption as ACTUAL
     */
    public void decisionA(){
        meterConsObj.setConstype_code(String.valueOf(CONST_TAG.ACTUAL));
    }

    /**
     * decision B Tag as AVERAGE Consumption
     */
     public void decisionB(){
         //use average consumption as bill consumption
         int average_consumption=  Integer.parseInt(meterConsObj.getAve_consumption());
         meterConsObj.setBilled_cons(average_consumption);
         meterConsObj.setConstype_code(String.valueOf(CONST_TAG.AVERAGE));
     }

    /**
     * decision C Tag as ADJUSTED Consumption
     */
     public void decisionC(){
         meterConsObj.setConstype_code(String.valueOf(CONST_TAG.ADJUSTED));
     }

    /**
     * decision D
     */
      public void decisionD(){
          String prev_reading = meterConsObj.getPrev_rdg();
          if(prev_reading.isEmpty()){
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
        meterConsObj.setConstype_code(String.valueOf(CONST_TAG.AVERAGE));
    }

    /**
     * check values if not empty before using scenario 3 formula
     * @return true if values is not empty otherwise return false
     */
    public boolean checkValues(){

        return  meterConsObj.getPresent_rdg().isEmpty()&&meterConsObj.getNminitrdg().isEmpty()
                && meterConsObj.getNminconsfactor().isEmpty();
    }

    /**
     * check values if not empty before using scenario 2 formula
     * @return  ture if values is not empty otherwise return false
     */
    public boolean checkValues2(){
        return  meterConsObj.getNminconsfactor().isEmpty() && meterConsObj.getPresent_rdg().isEmpty();
    }



}
