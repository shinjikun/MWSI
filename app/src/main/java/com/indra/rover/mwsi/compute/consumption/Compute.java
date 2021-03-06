package com.indra.rover.mwsi.compute.consumption;

import com.indra.rover.mwsi.data.pojo.meter_reading.MeterConsumption;
import com.indra.rover.mwsi.data.pojo.meter_reading.display.MeterInfo;
import com.indra.rover.mwsi.utils.Utils;

import java.util.List;


public class Compute {

    MeterConsumption meterConsObj;

   final String AVERAGE="1";
   final String ACTUAL = "0";
   final String ADJUSTED="2";
   final String NEW_METER="29";
   final String INTERCHANGEMR = "26";
   ConsumptionListener listener;
   //SP Comp values Special Condition values
    /**
     *  Normal Condition - Current Less Billed Previous  Consumption
     */
   private final String SP0 = "0";
    /**
     * Negative Consumption - Tumbled Meter
     */
   private  final String SP1 = "1";
    /**
     * New Meter - All Information Available
     */
    final String SP2 = "2";
    /**
     * New Meter - Incomplete Information
     */
    final String SP3 = "3";
    /**
     *  Negative Consumption - Previous Reading actual and less than or equal
     *  to Current Reading
     */
    final String SP4 = "4";
    /**
     *  Use BillPRevRdg2 with BillPrevActTag
     */
    final String SP5 = "5";
    /**
     *  Block Tag P use Current Reading
     */
    final String SP6 = "6";


    public Compute(ConsumptionListener listener){
        this.listener = listener;
    }




    /**
     * minimum billed for billed consumption 10 cubic meter
     */
    private final static int minimum_bill =10;

    /** Normal Condition - Current Lest Billed Previous Consumption
     * Default computation of consumption
     *  consumption =  present reading - bill previous reading
     * @return consumption
     */
     int defaultCondition(){
        int present_reading=  Integer.parseInt(meterConsObj.getPresent_rdg());
        int bill_prev_reading = Integer.parseInt(meterConsObj.getBill_prev_rdg());
        meterConsObj.setSpComp(SP0);
        return  present_reading -  bill_prev_reading;
    }


    /**
     * Negative Consumption-  Tumbled Meter
     * special condition 1
     * consumption =  number of dials - billed previous reading  + previous reading
     */
     int scenario1(){
        int num_dials =  meterConsObj.getMax_cap();
        int present_reading=  Integer.parseInt(meterConsObj.getPresent_rdg());
        int bill_prev_reading = Integer.parseInt(meterConsObj.getBill_prev_rdg());
        meterConsObj.setSpComp(SP1);
        return (num_dials - bill_prev_reading) +present_reading;
    }


    /**
     * New Meter All Information Available
     * special condition 2
     * T_CURRENT_RDG.PRESRDG + T_DOWNLOAD.NMCONSFACTOR
     * @return consumption
     */
     int scenario2(){
        int  nmconsfactor =  Integer.parseInt(meterConsObj.getNminconsfactor());
        int present_reading=  Integer.parseInt(meterConsObj.getPresent_rdg());
        meterConsObj.setSpComp(SP2);
        return present_reading+ nmconsfactor;
    }

    /**
     *  New Meter  Incomplete Information available
     *  (T_CURRENT_RDG.PRESRDG  - T_ DOWNLOAD.NMINITRDG) * T_DOWNLOAD.NMCONSFACTOR
     * scenario 3
     * @return computed consumption
     */
     int scenario3(){
        int present_reading=  Integer.parseInt(meterConsObj.getPresent_rdg());
        int  nminitrdg = Integer.parseInt(meterConsObj.getNminitrdg());
        double  nmconsfactor =  Double.parseDouble(meterConsObj.getNminconsfactor());
        meterConsObj.setSpComp(SP3);
        return (int)((present_reading-nminitrdg)*nmconsfactor);
    }

    /**
     * Negative Consumption - Previous Reading Actual and Less Than Or Equal To Current Reading
     * scenario 4 to compute consumption
     *  T_CURRENT_RDG.PRESRDG  - T_ DOWNLOAD.ACTPREVRDG
     * @return computed consumption
     */
     int scenario4(){
        int present_reading=  Integer.parseInt(meterConsObj.getPresent_rdg());
        int actual_prev_reading =  Integer.parseInt(meterConsObj.getPrev_rdg());
        meterConsObj.setSpComp(SP0);
        return present_reading - actual_prev_reading;

    }

    /**
     * Use BillPrevRdg2 with BillPrevActTag
     * scenario 5 to compute consumption
     * T_CURRENT_RDG.PRESRDG  - T_ DOWNLOAD.BILLPREVRDG2
     * @return computed consumption
     */
     int scenario5(){
        int present_reading=  Integer.parseInt(meterConsObj.getPresent_rdg());
        int bill_prev_rdg2 =  Integer.parseInt(meterConsObj.getBill_prev_rdg2());
        meterConsObj.setSpComp(SP5);
        return present_reading-bill_prev_rdg2;
    }

    /**
     * Block Tag P use Current Reading
     * @return computed consumption
     */
     int scenario6(){
        int present_reading=  Integer.parseInt(meterConsObj.getPresent_rdg());
        meterConsObj.setSpComp(SP6);
        return present_reading;
    }

    /**
     * decision A Tag Consumption as ACTUAL
     */
     void decisionA(){

        meterConsObj.setConstype_code(ACTUAL);
        if(listener!=null){
            listener.onPostConsResult(meterConsObj);
        }
    }

    /**
     * decision B Tag as AVERAGE Consumption
     */
      void decisionB(){
         //use average consumption as bill consumption
         int average_consumption=  Integer.parseInt(meterConsObj.getAve_consumption());
         meterConsObj.setBilled_cons(average_consumption);
         meterConsObj.setConstype_code(AVERAGE);
          if(listener!=null){
             listener.onPostConsResult(meterConsObj);
         }
     }

    /**
     * decision C Tag as ADJUSTED Consumption
     */
      void decisionC(){
         meterConsObj.setConstype_code(ADJUSTED);
          if(listener!=null){
             listener.onPostConsResult(meterConsObj);
         }
     }

    /**
     * decision D
     */
       void decisionD(){
          String prev_reading = meterConsObj.getPrev_rdg();
          if(Utils.isNotEmpty(prev_reading)){
            int billed_consumption =  defaultCondition();
              meterConsObj.setBilled_cons(billed_consumption);
              meterConsObj.setMrType(MeterInfo.MRTYPE01);
              //tag as actual
              decisionA();
          }
          else {
              int billed_consumption = scenario4();
              meterConsObj.setBilled_cons(billed_consumption);
              meterConsObj.setMrType(MeterInfo.MRTYPE93);
              //tag as adjusted
              decisionC();
          }
      }

    /**
     * decision E Tag as AVERAGE Consumption
     * use the minimum billed bill cosumption 10 cu. m as consumption
     */
     void decisionE(){
        meterConsObj.setSpComp(SP0);
         meterConsObj.setPrintTag(MeterInfo.BILLABLE);
         meterConsObj.setBilled_cons(minimum_bill);
        meterConsObj.setConstype_code(AVERAGE);
        if(listener!=null){
            listener.onPostConsResult(meterConsObj);
        }
    }

     void noBill(){
         meterConsObj.setMrType(MeterInfo.MRTYPE01);
         meterConsObj.setPrintTag(MeterInfo.NONBILLABLE);
        if(listener!=null){
            listener.onPostConsResult(meterConsObj);
        }
    }


    /**
     * check values if not empty before using scenario 3 formula
     * @return true if values is not empty otherwise return false
     */
     boolean checkValues(){

        return Utils.isNotEmpty(meterConsObj.getPresent_rdg())&&
                Utils.isNotEmpty(meterConsObj.getNminitrdg())&&
                Utils.isNotEmpty(meterConsObj.getNminconsfactor());
    }

    /**
     * check values if not empty before using scenario 2 formula
     * @return  ture if values is not empty otherwise return false
     */
     boolean checkValues2(){
        return  Utils.isNotEmpty(meterConsObj.getNminconsfactor()) &&
                Utils.isNotEmpty(meterConsObj.getPresent_rdg());
    }


    public interface ConsumptionListener {
        void onPostConsResult(MeterConsumption meterConsumption);
        void onPrintChildMeters(MeterConsumption meterConsumption,List<MeterConsumption> csChildMeter);

    }


}
