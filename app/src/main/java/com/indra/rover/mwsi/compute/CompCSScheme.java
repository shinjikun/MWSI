package com.indra.rover.mwsi.compute;

import com.indra.rover.mwsi.data.db.MeterReadingDao;
import com.indra.rover.mwsi.data.pojo.meter_reading.MeterConsumption;

import java.util.List;

/**
 * Created by Indra on 9/28/2016.
 */
public class CompCSScheme extends Compute implements Compute.ConsumptionListener {
    MeterReadingDao mtrDao;
    public CompCSScheme(ConsumptionListener listener) {
        super(listener);
    }

    public void compute(MeterConsumption mtrCons,MeterReadingDao mtrDao){
        this.mtrDao = mtrDao;
        meterConsObj = mtrCons;

        //compute for own consumption
        CompConsumption comConsumption = new CompConsumption(this);
        comConsumption.compute(meterConsObj);

    }

    @Override
    public void onPostConsResult(MeterConsumption meterConsumption) {

            String parent_code =  meterConsumption.getAcct_num();
            List<MeterConsumption> csChilds = mtrDao.getMeterChilds(parent_code);
            if(!csChilds.isEmpty()){
                int size = csChilds.size();
                int totalCons= 0;
                for(int i=0;i<size;i++){
                    MeterConsumption csChild = csChilds.get(i);
                     totalCons = csChild.getBilled_cons() +totalCons;
                }

                int parent_consumption = meterConsumption.getBilled_cons();
                int sum = parent_consumption - totalCons;
                if(sum>=0){
                    decisionA();
                }
                else {
                    decisionB();
                }
            }


    }

    @Override
    public void onPrintChildMeters(MeterConsumption meterConsumption,List<MeterConsumption> csChildMeter) {

    }
}
