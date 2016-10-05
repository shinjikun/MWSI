package com.indra.rover.mwsi.print;

import com.indra.rover.mwsi.data.pojo.meter_reading.MeterPrint;

/**
 * Created by Indra on 10/5/2016.
 */
public class ZebraLayout   implements  PrintLayout{

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
    public String bodyLayout(MeterPrint mtrPrint) {
        StringBuilder strBuild = new StringBuilder();
        //service Information
        String str = serviceInfo(mtrPrint);
        strBuild.append(str);
        //metering info
        str = meterInfo(mtrPrint);
        strBuild.append(str);
        //payment history
        str = paymentHistory(mtrPrint);
        strBuild.append(str);
        //bill summary
        str = billSummary(mtrPrint);
        strBuild.append(str);
        return strBuild.toString();
    }

    @Override
    public String contentPrint(MeterPrint mtrPrint) {

        StringBuilder strPrint = new StringBuilder();
        //header layout
        String headstr = billHeader(mtrPrint);
        strPrint.append(headstr);
        //body layout
        String bodystr = bodyLayout(mtrPrint);
        strPrint.append(bodystr);
        //footer layout
         String footer = billFooter(mtrPrint);
        strPrint.append(footer);

        String strDisCon =  billDiscon(mtrPrint);
        strPrint.append(strDisCon);
        return strPrint.toString();
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
