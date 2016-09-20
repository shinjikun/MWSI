package com.indra.rover.mwsi.compute;

import com.indra.rover.mwsi.data.pojo.meter_reading.MeterConsumption;

/**
 * Class to compute consumption of a Block Account
 */
public class ComBlockAccn  extends  Compute{


    public ComBlockAccn(MeterConsumption meterConsumption){
        this.meterConsObj = meterConsumption;
    }

    public void compute(){
        if(meterConsObj!=null){

        }
    }


}
