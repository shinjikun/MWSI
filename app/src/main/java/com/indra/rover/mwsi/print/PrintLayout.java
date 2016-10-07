package com.indra.rover.mwsi.print;

import android.content.Context;

import com.indra.rover.mwsi.data.pojo.meter_reading.MeterPrint;

/**
 * Created by Indra on 10/5/2016.
 */
public  abstract class PrintLayout {


    Context context;
    abstract String headerConfig();
    abstract String breadCrumbsHeader();
    abstract String billHeader(MeterPrint mtrPrint);
    abstract String serviceInfo(MeterPrint mtrPrint);
    abstract String meterInfo(MeterPrint mtrPrint);
    abstract String paymentHistory(MeterPrint mtrPrint);
    abstract String billSummary(MeterPrint mtrPrint);
    abstract String billAdvisory(MeterPrint mtrPrint);
    abstract String billFooter(MeterPrint mtrPrint);
    abstract String breadCrumbsFooter();
    abstract String testfont();
   abstract String billDiscon(MeterPrint mtrPrint);
    public PrintLayout(Context context){
        this.context = context;
    }

    public String contentPrint(MeterPrint mtrPrint) {

        StringBuilder strPrint = new StringBuilder();
        //header configuration
        strPrint.append(headerConfig());
        //header breadcrumbs
       strPrint.append(breadCrumbsHeader());
        //header layout

       // strPrint.append(billHeader(mtrPrint));
        //body layout
        strPrint.append(bodyLayout(mtrPrint));
        //bill advisory
       // strPrint.append(billAdvisory(mtrPrint));
        //footer layout
       // strPrint.append(billFooter(mtrPrint));
        //add if there is a disconnection notice in the bill
       // strPrint.append(billDiscon(mtrPrint));
        //footer breadcrumbs
        strPrint.append(breadCrumbsFooter());
        return strPrint.toString();
    }

    public String bodyLayout(MeterPrint mtrPrint) {
        StringBuilder strBuild = new StringBuilder();
        //service Information
        //String str = serviceInfo(mtrPrint);
       // strBuild.append(str);
        //metering info

        strBuild.append(meterInfo(mtrPrint));
        //payment history
        //str = paymentHistory(mtrPrint);
        //strBuild.append(str);
        //bill summary
       // str = billSummary(mtrPrint);
       // strBuild.append(str);
        return strBuild.toString();
    }

}
