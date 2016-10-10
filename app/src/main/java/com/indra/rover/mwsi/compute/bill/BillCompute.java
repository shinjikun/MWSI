package com.indra.rover.mwsi.compute.bill;

import com.indra.rover.mwsi.data.pojo.meter_reading.MeterPrint;

/**
 * Created by Indra on 10/10/2016.
 */
public class BillCompute extends Compute {



    @Override
    void compute(MeterPrint meterPrint) {

    }

    @Override
    double getBasicCharge(long consumption, char rate_type) {
        return 0;
    }

    @Override
    double getBulkBasicCharge(long l1, long l2, char rate_type) {
        return 0;
    }

    @Override
    double getGT3BasicCharge(long l, char c1, char c2) {
        return 0;
    }

    @Override
    double getHRLBasicCharge(long l) {
        return 0;
    }

    @Override
    double getOCBasicCharge(long l, char oc) {
        return 0;
    }
}
