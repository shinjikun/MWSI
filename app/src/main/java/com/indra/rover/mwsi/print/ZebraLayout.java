package com.indra.rover.mwsi.print;

import android.content.Context;
import android.os.Build;

import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.data.pojo.meter_reading.MeterPrint;
import com.indra.rover.mwsi.utils.Utils;


public class ZebraLayout   extends   PrintLayout{
    Utils utils;
    public ZebraLayout(Context context) {
        super(context);
    }

    @Override
    public String headerConfig() {
        //needed for the printer to know you request a line print mode
        return "! U1 setvar \"device.languages\", \"line_print\"\r\n";
    }

    @Override
    String breadCrumbsHeader() {
        return "! U1 BEGIN-PAGE\r\n";
    }

    @Override
    public String billHeader(MeterPrint mtrPrint) {
        StringBuilder str = new StringBuilder();
       // str.append("! U1 JOURNAL\r\n");
        String cpclData = "! 0 200 200 100 1\r\n"
                + "TEXT 4 0 0 0 This is a CPCL test.\r\n"

                + "PRINT\r\n";
        str.append(cpclData);
        return str.toString();
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
        StringBuilder str = new StringBuilder();
        str.append("! U1 CENTER\r\n");
        str.append("! U1 SETLP 5 0 24");
        str.append("! U1 SETSP 5\r\n");
        str.append(context.getString(R.string.print_meter_info));
        str.append("\r\n");
        return str.toString();
    }

    @Override
    public String paymentHistory(MeterPrint mtrPrint) {
        return "\n";
    }

    @Override
    public String billSummary(MeterPrint mtrPrint) {
        StringBuilder str = new StringBuilder();
        str.append("! U1 CENTER\r\n");
        str.append("! U1 SETLP 5 0 24");
        str.append("! U1 SETSP 5\r\n");
        str.append(context.getString(R.string.print_bill_summary));
        str.append("\r\n");
        str.append("! U1 SETLP 7 0 16");
        str.append("! U1 SETSP 0");
        str.append(context.getString(R.string.print_bill_period));
        str.append("\r\n");
        str.append(context.getString(R.string.print_current_charges));
        str.append("\r\n");
        String[] arry = context.getResources().getStringArray(R.array.print_arry_curcharnges);
        int size=  arry.length;
        for(int i=0;i<size;i++){
            str.append("    ");
            str.append(arry[i]);
            str.append("\r\n");
        }
        str.append(context.getString(R.string.print_other_charges));
        str.append("\r\n");
        arry = context.getResources().getStringArray(R.array.print_arry_othercharge);
        size=  arry.length;
        for(int i=0;i<size;i++){
            str.append("    ");
            str.append(arry[i]);
            str.append("\r\n");
        }
        str.append(context.getString(R.string.print_refund));
        str.append("\r\n");
        str.append(context.getString(R.string.print_previous));
        str.append("\r\n");
        return str.toString();
    }

    @Override
    public String billDiscon(MeterPrint mtrPrint) {
        StringBuilder str = new StringBuilder();
        str.append("! U1 CENTER\r\n");
        str.append("! U1 SETLP 5 0 24");
        str.append("! U1 SETSP 5\r\n");
        str.append(context.getString(R.string.print_notic_discon));
        str.append("\r\n");
        str.append("! U1 SETLP 7 0 24");
        str.append("! U1 SETSP 0");
        String str1 = context.getString(R.string.print_discon,"34.00","September 24, 2014");
        str.append(str1);
        str.append("\r\r\n");
        str.append("Respectfully Yours\r\r\n");
        str.append("MAYNILAD\r\n");
        return str.toString();
    }

    @Override
    String breadCrumbsFooter() {
        return "! U1 END-PAGE\r\n";
    }


}
