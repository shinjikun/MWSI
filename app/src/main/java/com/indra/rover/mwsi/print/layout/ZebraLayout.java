package com.indra.rover.mwsi.print.layout;

import android.content.Context;
import android.os.Build;

import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.data.pojo.meter_reading.MeterPrint;
import com.indra.rover.mwsi.utils.Utils;


public class ZebraLayout   extends   PrintLayout{
    Utils utils;
    final String TAB_SPACE=" ";
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
        String cpclData = "! 0 200 200 175 1\r\n" +
                "JOURNAL\r\n" +
                "PCX 42 10 !<maynilad.pcx\r\n"+
                //"BOX 12 10 222 147 2\r\n" +
                "T 5 0 400 6 Maynilad Water Services Inc\r\n" +
                "T 5 0 400 39 MWSS Compound\r\n" +
                "T 7 0 400 62 Katipunan Road, Balara, QC\r\n" +
                "T 7 0 400 86 VAT Reg TIN 005-393-442-000\r\n" +
                "T 7 0 400 117 Permit No. 0107-116-00006-CBA/AR\r\n" +
                "T 7 0 400 150 Machine No. "+Build.SERIAL+" \r\n" +
                "PRINT\r\n";
        str.append(cpclData);
        return str.toString();
    }



    @Override
    public String billFooter(MeterPrint mtrPrint) {
        StringBuilder str = new StringBuilder();
        str.append(lineBreakPrint());
        str.append("\r\n");
        str.append("! U1 CENTER\r\n");
        str.append("! U1 B 128 1 2 100 0 0 59285691 ST 187.10 T 2.60\r\n");
        str.append("\r\r\n");
        return str.toString();
    }

    @Override
    String billValidity(MeterPrint mtrPrint) {
        StringBuilder str = new StringBuilder();
        str.append(lineBreakPrint());
        str.append("! U1 SETLP 7 0 24\r\n");
        str.append("! U1 SETSP 0\r\n");
        str.append(setBold(0));
        str.append(context.getString(R.string.print_valid_vendor));
        str.append("\r\n");
        str.append(context.getString(R.string.print_valid_acc));
        str.append("\r\n");
        str.append(context.getString(R.string.print_valid_date));
        str.append("\r\n");
        str.append(context.getString(R.string.print_valid_until));
        str.append("\r\n");
        str.append(context.getString(R.string.print_ptu_number));
        str.append("\r\n");
        str.append("! U1 SETLP 5 0 24\r\n");
        str.append("! U1 SETSP 0\r\n");
        str.append(setBold(1));
        str.append(context.getString(R.string.print_validity));
        str.append("\r\n");
        str.append(context.getString(R.string.print_validity1));
        str.append(setBold(0));
        str.append("\r\n");
        return str.toString();
    }

    @Override
    public String serviceInfo(MeterPrint mtrPrint) {
        StringBuilder str = new StringBuilder();
        str.append("! U1 CENTER\r\n");
        str.append("! U1 SETLP 5 0 24");
        str.append("! U1 SETSP 0\r\n");
        str.append(setBold(0));
        str.append("SOA # 04000000000000695051");
        str.append("\r\n");
        str.append("! U1 CENTER 383\r\n");
        str.append("! U1 SETLP 5 0 24\r\n");
        str.append("! U1 SETSP 5\r\n");
        str.append(setBold(1));
        str.append(context.getString(R.string.print_statement));
        str.append("\r\n");
        str.append("! U1 SETLP 5 0 24\r\n");
        str.append("! U1 SETSP 5\r\n");
        str.append(setBold(0));
        str.append("For the month of: ");
        str.append(Utils.getCurrentDate("MMMM yyyy"));
        str.append("\r\n");
        str.append("! U1 SETLP 5 0 24\r\n");
        str.append("! U1 SETSP 5\r\n");
        str.append(setBold(0));
        str.append(context.getString(R.string.print_service));
        str.append("\r\r\n");
        str.append("! U1 CENTER\r\n");
        str.append("! U1 SETLP 5 0 24");
        str.append("! U1 SETSP 0\r\r\n");
        str.append(setBold(0));
        str.append(context.getString(R.string.print_contract_num));
        str.append("  :");
        str.append("! U1 CENTER\r\n");
        str.append("! U1 SETLP 5 1 48");
        str.append("! U1 SETSP 3\r\n");
        str.append(setBold(1));
        str.append(" 61483179");
        str.append("\r\n");
        str.append("! U1 SETLP 5 0 24\r\n");
        str.append("! U1 SETSP 0\r\n");
        str.append(setBold(0));
        str.append(context.getString(R.string.print_acct_name));
        str.append("          :");
        str.append(" Juanito Baquiran");
        str.append("\r\n");
        str.append("! U1 SETLP 5 0 24\r\n");
        str.append("! U1 SETSP 0\r\n");
        str.append(setBold(0));
        str.append(context.getString(R.string.print_address));
        str.append("       :");
        str.append(" B10 L9 RD 13 GSIS HILLS SUBD CALOOCAN");
        str.append("\r\r\n");
        str.append("! U1 SETLP 5 0 24\r\n");
        str.append("! U1 SETSP 0\r\n");
        str.append(setBold(0));
        str.append(context.getString(R.string.print_rate));
        str.append("              :");
        str.append(" Residential");
        str.append("\r\n");
        str.append("! U1 SETLP 5 0 24\r\n");
        str.append("! U1 SETSP 0\r\n");
        str.append(setBold(0));
        str.append(context.getString(R.string.print_business_area));
        str.append("         :");
        str.append(" Quirino-Roosevelt");
        str.append("\r\n");
        str.append(linePrint());
        return str.toString();
    }

    @Override
    public String meterInfo(MeterPrint mtrPrint) {
        StringBuilder str = new StringBuilder();
        str.append("! U1 CENTER\r\n");
        str.append("! U1 SETLP 5 0 24\r\n");
        str.append("! U1 SETSP 5\r\n");
        str.append(setBold(0));
        str.append(context.getString(R.string.print_meter_info));
        str.append("\r\n");
        str.append("! U1 CENTER\r\n");
        str.append("! U1 SETLP 5 0 24\r\n");
        str.append("! U1 SETSP 0\r\r\n");
        str.append(setBold(0));
        str.append("Meter No.       MRU No.     Seq No.");
        str.append("\r\n");
        str.append("    ");
        str.append(context.getString(R.string.print_meter_value));
        str.append("\r\n");
        str.append(context.getString(R.string.print_reading_date));
        str.append("      :");
        str.append(" 08/10/2016");
        str.append("\r\n");
        str.append(context.getString(R.string.print_pres_rdg));
        str.append("        :");
        str.append(" 33333");
        str.append("\r\n");
        str.append(context.getString(R.string.print_prev_rdg));
        str.append("       :");
        str.append(" 33333");
        str.append("\r\n");
        str.append(context.getString(R.string.print_cosumption));
        str.append("  :");
        str.append(" 33333");
        str.append("\r\n");
        str.append(context.getString(R.string.print_prevcons));
        str.append("\r\n");
        str.append(linePrint());
        return str.toString();
    }

    @Override
    public String paymentHistory(MeterPrint mtrPrint) {
        StringBuilder str = new StringBuilder();
        str.append("! U1 CENTER\r\n");
        str.append("! U1 SETLP 5 0 24");
        str.append("! U1 SETSP 5\r\n");
        str.append(setBold(0));
        str.append(context.getString(R.string.print_bill_payment));
        str.append("\r\n");
        str.append("! U1 CENTER\r\n");
        str.append("! U1 SETLP 5 0 24");
        str.append("! U1 SETSP 0\r\n");

        str.append(setBold(0));
        str.append("\r\n");
        str.append("INVOICE NO.");
        str.append("\r\n");
        str.append("\r\n");
        str.append("! U1 CENTER\r\n");
        str.append("! U1 SETLP 5 0 24\r\n");
        str.append("! U1 SETSP 0\r\n");
        str.append(setBold(0));
        str.append("Desc  Net Amount  VAT  Total Amount OR# Date Tax Code\r\n");
        str.append("\r\n");
        str.append("\r\n");
        str.append("! U1 CENTER\r\n");
        str.append("! U1 SETLP 0 2 18\r\n");
        str.append("! U1 SETSP 0\r\n");
        str.append(setBold(0));
        str.append("DESCRIPTION: WB-Water Bill, GD-Guarantee Deposit, MISC-Reopening Fee,Connection Fee,Metering Charge\r\n");
        str.append(linePrint());

        return str.toString();
    }
    private String addSpace(String str,int maxnum,String figure){
        StringBuilder strOr = new StringBuilder();
        int curLinex = str.length();

       int space =  maxnum-curLinex-figure.length();
        if(space>0){
            for(int i=0;i<space;i++){
                strOr.append(' ');
            }
        }
        strOr.append(figure);
        return strOr.toString();
    }

    @Override
    public String billSummary(MeterPrint mtrPrint) {
        StringBuilder str = new StringBuilder();
        str.append("! U1 CENTER\r\n");
        str.append("! U1 SETLP 5 0 24\r\n");
        str.append("! U1 SETSP 5\r\n");
        str.append(setBold(0));
        str.append(context.getString(R.string.print_bill_summary));
        str.append("\r\n");
        str.append("! U1 SETLP 5 0 24\r\n");
        str.append("! U1 SETSP 0\r\n");
        str.append(setBold(1));
        str.append(context.getString(R.string.print_bill_period));
        str.append("\r\n");
        str.append("! U1 SETLP 5 0 24\r\n");
        str.append("! U1 SETSP 0\r\n");
        str.append(setBold(0));
        str.append(context.getString(R.string.print_current_charges));
        str.append("\r\n");
        str.append("! U1 SETLP 7 0 24\r\n");
        str.append("! U1 SETSP 0\r\n");

        String[] arry = context.getResources().getStringArray(R.array.print_arry_curcharnges);
        int size=  arry.length;
        for(int i=0;i<size;i++){
            str.append(TAB_SPACE);
            str.append(arry[i]);
            str.append("\r\n");
        }
        str.append("\r\n");
        str.append("! U1 SETLP 5 0 24\r\n");
        str.append("! U1 SETSP 0\r\n");
        str.append(context.getString(R.string.print_other_charges));
        str.append("\r\n");
        str.append(setBold(0));
        str.append("! U1 SETLP 7 0 24\r\n");
        str.append("! U1 SETSP 0\r\n");
        arry = context.getResources().getStringArray(R.array.print_arry_othercharge);
        size=  arry.length;
        for(int i=0;i<size;i++){
            str.append(TAB_SPACE);
            str.append(arry[i]);
            String temp =  arry[i];
            str.append(addSpace(temp,69-TAB_SPACE.length(),"999,999,999.99"));

            str.append("\r\n");
        }
        str.append("\r\n");
        str.append("! U1 SETLP 5 0 24\r\n");
        str.append("! U1 SETSP 0\r\n");
        str.append(setBold(0));
        str.append(context.getString(R.string.print_refund));
        str.append("\r\r\n");
        str.append("! U1 SETLP 5 0 24\r\n");
        str.append("! U1 SETSP 0\r\n");
        str.append(setBold(0));
        str.append(context.getString(R.string.print_previous));
        str.append("\r\r\n");
        str.append(lineBreakPrint());
        str.append("! U1 SETLP 7 1 48\r\n");
        str.append("! U1 SETSP 3\r\n");
        str.append(setBold(0));
        str.append(context.getString(R.string.print_total_amount_due));
        str.append("\r\n");
        str.append(context.getString(R.string.print_payment_date));
        str.append("\r\n");
        str.append(lineBreakPrint());
        str.append("! U1 SETLP 5 0 24\r\n");
        str.append("! U1 SETSP 0\r\n");
        str.append(setBold(0));
        str.append(context.getString(R.string.print_instruction));
        str.append("\r\n");
        return str.toString();
    }

    @Override
    String billAdvisory(MeterPrint mtrPrint) {
        StringBuilder str = new StringBuilder();
        str.append(lineBreakPrint());
        str.append("! U1 CENTER\r\n");
        str.append("! U1 SETLP 5 0 24\r\n");
        str.append("! U1 SETSP 5\r\n");
        str.append(setBold(1));
        str.append(context.getString(R.string.print_advisory));
        str.append("\r\n");
        str.append("! U1 SETLP 7 0 24\r\n");
        str.append("! U1 SETSP 0");
        str.append(setBold(0));
        String[]   arry = context.getResources().getStringArray(R.array.print_arry_advisory);
        int size=  arry.length;
        for(int i=0;i<size;i++){
            str.append(arry[i]);
            str.append("\r\n");
        }
        return str.toString();
    }

    @Override
    public String billDiscon(MeterPrint mtrPrint) {
        StringBuilder str = new StringBuilder();

        String cpclData = "! 0 200 200 160 1\r\n" +
                "JOURNAL\r\n" +
                "PCX 12 10 !<maynilad.pcx\n"+
                //"BOX 12 10 222 147 2\r\n" +
                "T 5 0 470 6 Maynilad Water Services Inc\r\n" +
                "T 5 0 470 39 MWSS Compound\r\n" +
                "T 7 0 470 62 Katipunan Road, Balara, QC\r\n" +
                "T 7 0 470 86 VAT Reg TIN 005-393-442-000\r\n" +
                "T 0 2 470 117 Permit No. 0107-116-00006-CBA/AR\r\n" +
                "PRINT\r\n";
        str.append(lineBreakPrint());
        str.append(cpclData);
        str.append("! U1 CENTER\r\n");
        str.append("! U1 SETLP 5 0 24");
        str.append("! U1 SETSP 5\r\n");
        str.append(setBold(1));
        str.append(context.getString(R.string.print_notic_discon));
        str.append("\r\n");
        str.append("! U1 CENTER\r\n");
        str.append("! U1 SETLP 5 1 48");
        str.append("! U1 SETSP 3\r\n");
        str.append(setBold(1));
        str.append("Juanito Baquiran");
        str.append("\r\n");
        str.append("! U1 CENTER\r\n");
        str.append("! U1 SETLP 5 0 24");
        str.append("! U1 SETSP 0\r\n");
        str.append(setBold(0));
        str.append(context.getString(R.string.print_contract_num));
        str.append("\r\n");
        str.append("! U1 CENTER\r\n");
        str.append("! U1 SETLP 5 0 24");
        str.append("! U1 SETSP 0\r\n");
        str.append(setBold(0));
        str.append("Customer Address");
        str.append("\r\n");

        str.append("! U1 SETLP 7 0 24\r\n");
        str.append("! U1 SETSP 0\r\n");
        str.append(setBold(0));

        String[]   arry = context.getResources().getStringArray(R.array.print_arry_discon);
         int size=  arry.length;
        for(int i=0;i<size;i++){
            if(i==0){

            }
            str.append(arry[i]);
            str.append("\r\n");
        }
        str.append("\r\n");
        str.append("Respectfully yours,\r\n");
        str.append(setBold(1));
        str.append("MAYNILAD\r\r\r\n");
        str.append(setBold(0));
        return str.toString();
    }

    @Override
    String breadCrumbsFooter() {
        return "! U1 END-PAGE\r\r\n";
    }


    String randomString(){
        StringBuilder str = new StringBuilder();
        for(int i=0;i<10;i++)
            for(int j=0; j<10;j++){
                str.append(j);
            }

        str.append("\r\n");
        return str.toString();
    }

    @Override
    String testfont() {
        StringBuilder str = new StringBuilder();
        str.append("! U1 SETLP 0 9 9\r\n");
        str.append("Font 0,9 Height 9\r\n");
        str.append(randomString());
        str.append("! U1 SETLP 0 1 9\r\n");
        str.append("Font 0,1 Height 9\r\n");
        str.append(randomString());
        str.append("! U1 SETLP 0 2 18\r\n");
        str.append("Font 0,2 Height 18\r\n");
        str.append(randomString());
        str.append("! U1 SETLP 0 3 18\r\n");
        str.append("Font 0,3 Height 18\r\n");
        str.append(randomString());
        str.append("! U1 SETLP 0 4 18\r\n");
        str.append("Font 0,4 Height 18\r\n");
        str.append(randomString());
        str.append("! U1 SETLP 0 5 36\r\n");
        str.append("Font 0,5 Height 36\r\n");
        str.append(randomString());
        str.append("! U1 SETLP 0 6 36\r\n");
        str.append("Font 0,6 Height 36\r\n");
        str.append(randomString());
        str.append("! U1 SETLP 1 0 48\r\n");
        str.append("Font 1,0 Height 48\r\n");
        str.append(randomString());
        str.append("! U1 SETLP 2 0 12\r\n");
        str.append("Font 2,0 Height 12\r\n");
        str.append(randomString());
        str.append("! U1 SETLP 2 1 24\r\n");
        str.append("Font 2,1 Height 24\r\n");
        str.append(randomString());
        str.append("! U1 SETLP 4 0 47\r\n");
        str.append("Font 4,0 Height 47\r\n");
        str.append(randomString());
        str.append("! U1 SETLP 4 1 94\r\n");
        str.append("Font 4,1 Height 94\r\n");
        str.append(randomString());
        str.append("! U1 SETLP 5 0 24\r\n");
        str.append("Font 5,0 Height 24\r\n");
        str.append(randomString());
        str.append("! U1 SETLP 5 1 48\r\n");
        str.append("Font 5,1 Height 48\r\n");
        str.append(randomString());
        str.append("! U1 SETLP 5 2 46\r\n");
        str.append("Font 5,2 Height 46\r\n");
        str.append(randomString());
        str.append("! U1 SETLP 5 3 92\r\n");
        str.append("Font 5,3 Height 92\r\n");
        str.append(randomString());
        str.append("! U1 SETLP 6 0 27\r\n");
        str.append("Font 6,0 Height 27\r\n");
        str.append(randomString());
        str.append("! U1 SETLP 7 0 24\r\n");
        str.append("Font 7,0 Height 24\r\n");
        str.append(randomString());
        str.append("! U1 SETLP 7 1 48\r\n");
        str.append("Font 7,1 Height 48\r\n");
        str.append(randomString());
        str.append("\r\n");
        return str.toString();
    }

    String linePrint(){
        String cpclData = "! 0 200 200 30 1\r\n"
                + "LINE 1 0 811 0 2\r\n"
                + "PRINT\r\n";
        return cpclData;
    }

    String lineBreakPrint(){
        StringBuilder str = new StringBuilder();
        str.append("! U1 SETLP 7 0 24\r\n");
        str.append("! U1 SETSP 0\r\n");
        for(int i=0;i<68;i++){
            str.append('=');
        }
        str.append("\r\n");
        return str.toString();
    }

    String setBold(int v){
        return "! U1 SETBOLD "+v+"\r\n";

    }


}
