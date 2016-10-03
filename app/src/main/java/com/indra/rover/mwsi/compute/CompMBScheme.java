package com.indra.rover.mwsi.compute;

import com.indra.rover.mwsi.data.db.MeterReadingDao;
import com.indra.rover.mwsi.data.pojo.meter_reading.MeterConsumption;

import java.util.List;

/**
 * Created by Indra on 9/28/2016.
 */
public class CompMBScheme extends Compute implements Compute.ConsumptionListener {
    MeterReadingDao mtrDao;
    public CompMBScheme(ConsumptionListener listener) {
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
        List<MeterConsumption> mbChilds = mtrDao.getCSChilds(parent_code);
        if(!mbChilds.isEmpty()){
            int size = mbChilds.size();
            int totalCons= 0;
            for(int i=0;i<size;i++){
                MeterConsumption mbChild = mbChilds.get(i);
                totalCons = mbChild.getBilled_cons() +totalCons;
            }

            int parent_consumption = meterConsumption.getBilled_cons();
            int sum = parent_consumption - totalCons;
            if(sum>0){
                //share consumption between its child
              //  decisionA();
            }
            else {
                //print child meters
               if(listener!=null){
                   listener.onPrintChildMeters(meterConsumption,mbChilds);
               }
            }
        }
    }

    @Override
    public void onPrintChildMeters(MeterConsumption meterConsumption, List<MeterConsumption> csChildMeter) {

    }
}
