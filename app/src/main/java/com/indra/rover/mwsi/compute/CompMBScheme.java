package com.indra.rover.mwsi.compute;

import com.indra.rover.mwsi.data.db.MeterReadingDao;
import com.indra.rover.mwsi.data.pojo.meter_reading.MeterConsumption;

/**
 * Created by Indra on 9/28/2016.
 */
public class CompMBScheme extends Compute {
    MeterReadingDao mtrDao;
    public CompMBScheme(ConsumptionListener listener) {
        super(listener);
    }

    public void compute(MeterConsumption mtrCons,MeterReadingDao mtrDao){
        this.mtrDao = mtrDao;
        meterConsObj = mtrCons;
    }
}
