package com.indra.rover.mwsi.print.layout;

import android.content.Context;
import android.util.Log;

import com.indra.rover.mwsi.data.pojo.meter_reading.MeterPrint;
import com.indra.rover.mwsi.utils.Utils;

import java.util.ArrayList;

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
    abstract String billAdvisory(MeterPrint mtrPrint,String range);
    abstract String billFooter(MeterPrint mtrPrint);
    abstract String billValidity(MeterPrint mtrPrint);
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

        strPrint.append(billHeader(mtrPrint));
        /*
        //body layout
        strPrint.append(bodyLayout(mtrPrint));


        //bill advisory
        //only add the advisory section is the consumption result is very high or very low
        if(Utils.isNotEmpty(mtrPrint.getRangeCode())){
            String range =  mtrPrint.getRangeCode();
            if(range.equals("3")){
                strPrint.append(billAdvisory(mtrPrint,"decreased"));
            }
           else if(range.equals("4")){
                strPrint.append(billAdvisory(mtrPrint,"increased"));
            }
        }
        //footer layout
        strPrint.append(billFooter(mtrPrint));
        strPrint.append(billValidity(mtrPrint));
        //add if there is a disconnection notice in the bill
        if(mtrPrint.getDisConStatus().equals("1")){

            strPrint.append(billDiscon(mtrPrint));

        }
        */

        //footer breadcrumbs
        strPrint.append(breadCrumbsFooter());
        return strPrint.toString();
    }

    public String bodyLayout(MeterPrint mtrPrint) {
        StringBuilder strBuild = new StringBuilder();
        //service Information

        strBuild.append(serviceInfo(mtrPrint));
        //metering info

        strBuild.append(meterInfo(mtrPrint));
        //payment history
        //str =
        strBuild.append(paymentHistory(mtrPrint));
        //bill summary

        strBuild.append(billSummary(mtrPrint));
        return strBuild.toString();
    }


    /**
     *  End of the Report Print
     *  Details Report consisting of info about the meter reading such as the ff
     *  CAN, Name, Reading, OC1, OC2, Remarks, Range Code, Print Count and Total Due
     * @param mtrPrints
     * @return
     */
    abstract String eodReport(ArrayList<MeterPrint> mtrPrints);

    abstract  String mrStub(MeterPrint meterPrint);


}
