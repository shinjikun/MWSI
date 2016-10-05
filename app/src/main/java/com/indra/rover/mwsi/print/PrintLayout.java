package com.indra.rover.mwsi.print;

import android.content.Context;

import com.indra.rover.mwsi.data.pojo.meter_reading.MeterPrint;

/**
 * Created by Indra on 10/5/2016.
 */
public  abstract class PrintLayout {


    Context context;
   abstract String headerConfig();
   abstract String billHeader(MeterPrint mtrPrint);
   abstract String billFooter(MeterPrint mtrPrint);
   abstract String serviceInfo(MeterPrint mtrPrint);
   abstract String meterInfo(MeterPrint mtrPrint);
   abstract String paymentHistory(MeterPrint mtrPrint);
   abstract String billSummary(MeterPrint mtrPrint);
   abstract String billDiscon(MeterPrint mtrPrint);

    public PrintLayout(Context context){
        this.context = context;
    }

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

}
