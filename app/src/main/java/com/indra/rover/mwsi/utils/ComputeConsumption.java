package com.indra.rover.mwsi.utils;

import android.content.Context;

import com.indra.rover.mwsi.data.db.MeterReadingDao;
import com.indra.rover.mwsi.data.pojo.meter_reading.MeterConsumption;

/**
 * Created by Indra on 9/19/2016.
 */
public class ComputeConsumption {

    MeterReadingDao meterReadingDao;
    MeterConsumption meterConsObj;
    public ComputeConsumption(Context context,String dldocno){
        this.meterReadingDao = new MeterReadingDao(context);
        this.meterConsObj =  meterReadingDao.getConsumption(dldocno);
    }

    public ComputeConsumption(MeterConsumption meterConsumption){
        this.meterConsObj =meterConsumption;
    }

    public void compute(){
        //check first if account is block or not
        if(meterConsObj!=null){
            String block_tag = meterConsObj.getBlock_tag();
               if(!block_tag.isEmpty()){
                   //account is block then proceed to compute BlockAccount
                   if(block_tag.equals("B")||block_tag.equals("P")){
                        ComputeBlockAccount blockAccount = new ComputeBlockAccount(meterConsObj);
                        blockAccount.compute();
                   }
               }
                else {
                   computeReadBill();
               }
        }
   }

    private void computeReadBill(){

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

    /**
     * check if the specified oc is billed related OC
     * @return return true is OC is billed related false when not
     */
    private boolean isBillRelatedOC(String OCcode){
        return true;
    }










}
