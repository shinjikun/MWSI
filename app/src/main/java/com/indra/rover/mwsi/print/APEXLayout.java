package com.indra.rover.mwsi.print;

import android.content.Context;

import com.indra.rover.mwsi.data.pojo.meter_reading.MeterPrint;

/**
 * Created by Indra on 10/5/2016.
 */
public class APEXLayout  extends   PrintLayout {

    public APEXLayout(Context context) {
        super(context);
    }

    @Override
    String headerConfig() {
        return null;
    }

    @Override
    String billHeader(MeterPrint mtrPrint) {
        return null;
    }

    @Override
    String billFooter(MeterPrint mtrPrint) {
        return null;
    }

    @Override
    String serviceInfo(MeterPrint mtrPrint) {
        return null;
    }

    @Override
    String meterInfo(MeterPrint mtrPrint) {
        return null;
    }

    @Override
    String paymentHistory(MeterPrint mtrPrint) {
        return null;
    }

    @Override
    String billSummary(MeterPrint mtrPrint) {
        return null;
    }

    @Override
    String billDiscon(MeterPrint mtrPrint) {

        return null;
    }
}
