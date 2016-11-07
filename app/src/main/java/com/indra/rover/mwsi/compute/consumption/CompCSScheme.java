package com.indra.rover.mwsi.compute.consumption;

import com.indra.rover.mwsi.data.db.MeterReadingDao;
import com.indra.rover.mwsi.data.pojo.meter_reading.MeterConsumption;
import com.indra.rover.mwsi.data.pojo.meter_reading.display.MeterInfo;

import java.util.List;


public class CompCSScheme extends Compute implements Compute.ConsumptionListener {
    private MeterReadingDao mtrDao;
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
             //use special condition 5 to compute for consumption
             int bill_cosumption = scenario5();
             meterConsumption.setBilled_cons(bill_cosumption);

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
                    meterConsumption.setPrintTag(MeterInfo.BILLNOPRINT);
                    meterConsObj.setMrType(MeterInfo.MRTYPE01);
                    decisionA();
                }
                else {
                    meterConsumption.setPrintTag(MeterInfo.BILLNOPRINT);
                    meterConsObj.setMrType(MeterInfo.MRTYPE93);
                    decisionB();
                }
            }


    }

    @Override
    public void onPrintChildMeters(MeterConsumption meterConsumption,List<MeterConsumption> csChildMeter) {

    }
}
