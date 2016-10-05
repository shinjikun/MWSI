package com.indra.rover.mwsi.print;

import com.indra.rover.mwsi.data.pojo.meter_reading.MeterPrint;

/**
 * Created by Indra on 10/5/2016.
 */
public  interface PrintLayout {

    String headerConfig();
    String billHeader(MeterPrint mtrPrint);
    String billFooter(MeterPrint mtrPrint);
    String bodyLayout(MeterPrint mtrPrint);
    String contentPrint(MeterPrint mtrPrint);
    String serviceInfo(MeterPrint mtrPrint);
    String meterInfo(MeterPrint mtrPrint);
    String paymentHistory(MeterPrint mtrPrint);
    String billSummary(MeterPrint mtrPrint);
    String billDiscon(MeterPrint mtrPrint);
}
