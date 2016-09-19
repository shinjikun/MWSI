package com.indra.rover.mwsi.utils;

import com.indra.rover.mwsi.data.pojo.meter_reading.MeterConsumption;

/**
 * Created by Indra on 9/19/2016.
 */
public class ComputeBlockAccount {

    MeterConsumption meterConsObj;
    public ComputeBlockAccount(MeterConsumption meterConsumption){
        this.meterConsObj = meterConsumption;
    }

    public void compute(){
        if(meterConsObj!=null){

        }
    }

    /**
     * Default computation of consumption
     *  consumption =  present reading - bill previous reading
     * @return consumption
     */
    private int defaultCondition(){
        int present_reading=  Integer.parseInt(meterConsObj.getPresent_rdg());
        int bill_prev_reading = Integer.parseInt(meterConsObj.getBill_prev_rdg());
        return  present_reading -  bill_prev_reading;
    }


    /**
     * special condition 1
     * consumption =  number of dials - billed previous reading  + previous reading
     */
    private int scenario1(){
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
    private int scenario2(){
        int  nmconsfactor =  Integer.parseInt(meterConsObj.getNminconsfactor());
        int present_reading=  Integer.parseInt(meterConsObj.getPresent_rdg());
        return present_reading+ nmconsfactor;
    }

    /**
     *  (T_CURRENT_RDG.PRESRDG  - T_ DOWNLOAD.NMINITRDG) * T_DOWNLOAD.NMCONSFACTOR
     * scenario 3
     * @return computed consumption
     */
    private int scenario3(){
        int present_reading=  Integer.parseInt(meterConsObj.getPresent_rdg());
        int  nminitrdg = Integer.parseInt(meterConsObj.getNminitrdg());
        int  nmconsfactor =  Integer.parseInt(meterConsObj.getNminconsfactor());

        return (present_reading-nminitrdg)*nmconsfactor;
    }

    /** scenario 4 to compute consumption
     *  T_CURRENT_RDG.PRESRDG  - T_ DOWNLOAD.ACTPREVRDG
     * @return computed consumption
     */
    private int scenario4(){
        int present_reading=  Integer.parseInt(meterConsObj.getPresent_rdg());
        int actual_prev_reading =  Integer.parseInt(meterConsObj.getActual_prev_rdg());
        return present_reading - actual_prev_reading;
    }

    /**
     * scenario 5 to compute consumption
     * T_CURRENT_RDG.PRESRDG  - T_ DOWNLOAD.BILLPREVRDG2
     * @return computed consumption
     */
    private int scenario5(){
        int present_reading=  Integer.parseInt(meterConsObj.getPresent_rdg());
        int bill_prev_rdg2 =  Integer.parseInt(meterConsObj.getBill_prev_rdg2());
        return present_reading-bill_prev_rdg2;
    }

    private int scenario6(){
        int present_reading=  Integer.parseInt(meterConsObj.getPresent_rdg());
        return present_reading;
    }
}
