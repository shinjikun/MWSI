package com.indra.rover.mwsi.compute.consumption;

import com.indra.rover.mwsi.data.db.MeterReadingDao;
import com.indra.rover.mwsi.data.pojo.meter_reading.MeterConsumption;
import com.indra.rover.mwsi.data.pojo.meter_reading.display.MeterInfo;

import java.util.List;

public class CompMBScheme extends Compute implements Compute.ConsumptionListener {
    private MeterReadingDao mtrDao;
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
        List<MeterConsumption> mbChilds = mtrDao.getMeterChilds(parent_code);
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
               computeShareCons(mbChilds,totalCons,sum, meterConsumption);
            }
            else {
                //print child meters
               if(listener!=null){
                   meterConsObj.setPrintTag(MeterInfo.BILLNOPRINT);
                   listener.onPrintChildMeters(meterConsumption,mbChilds);
               }
            }
        }
    }

    @Override
    public void onPrintChildMeters(MeterConsumption meterConsumption, List<MeterConsumption> childMeters) {

    }

    /**
     *  @param childMeters list of child meters
     * @param totalCons total consumption of all child meters
     * @param x difference of mother meter consumption and total consumption of child
     * @param parentMeter parent meter
     */
    private void computeShareCons(List<MeterConsumption> childMeters, int totalCons, int x, MeterConsumption parentMeter){
        int size = childMeters.size();
        for(int i=0; i<size;i++){
            MeterConsumption childMeter = childMeters.get(i);
            int a = childMeter.getBilled_cons();
            int sharedcons = x * a /totalCons;
            childMeter.setBilled_cons(a+sharedcons);
            //if child meter is tagged as actual
            if(childMeter.getConstype_code().equals("0")){
                //tag parent as adjusted
                parentMeter.setConstype_code(ADJUSTED);
            }
            childMeters.set(i,childMeter);
        }

        if(listener!=null){
            meterConsObj.setPrintTag(MeterInfo.BILLNOPRINT);
            listener.onPrintChildMeters(parentMeter,childMeters);
        }
    }
}
