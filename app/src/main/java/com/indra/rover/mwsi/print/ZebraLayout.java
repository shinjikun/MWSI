package com.indra.rover.mwsi.print;

import android.content.Context;

import com.indra.rover.mwsi.data.pojo.meter_reading.MeterPrint;

/**
 * Created by Indra on 10/5/2016.
 */
public class ZebraLayout   extends   PrintLayout{

    public ZebraLayout(Context context) {
        super(context);
    }

    @Override
    public String headerConfig() {
        //needed for the printer to know you request a line print mode
        String str = "! U1 setvar \"device.languages\", \"line_print\"\r\n";
        return str;
    }

    @Override
    public String billHeader(MeterPrint mtrPrint) {
        StringBuilder strBuild = new StringBuilder();
        strBuild.append(headerConfig());
        return strBuild.toString();
    }

    @Override
    public String billFooter(MeterPrint mtrPrint) {
        return "\n";
    }




    @Override
    public String serviceInfo(MeterPrint mtrPrint) {
        return "\n";
    }

    @Override
    public String meterInfo(MeterPrint mtrPrint) {
        return "\n";
    }

    @Override
    public String paymentHistory(MeterPrint mtrPrint) {
        return "\n";
    }

    @Override
    public String billSummary(MeterPrint mtrPrint) {
        return "\n";
    }

    @Override
    public String billDiscon(MeterPrint mtrPrint) {
        return "\n";
    }
}
