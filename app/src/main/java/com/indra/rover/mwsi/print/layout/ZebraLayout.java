package com.indra.rover.mwsi.print.layout;

import android.content.Context;
import android.os.Build;

import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.data.db.MeterBillDao;
import com.indra.rover.mwsi.data.pojo.meter_reading.MeterPrint;
import com.indra.rover.mwsi.utils.Utils;

import java.util.ArrayList;


public class ZebraLayout   extends   PrintLayout{

    final String TAB_SPACE=" ";
    public ZebraLayout(Context context) {
        super(context);
    }

    @Override
   public String eodReport(ArrayList<MeterPrint> mtrPrints) {
        StringBuilder strPrint = new StringBuilder();
        //header configuration
        strPrint.append(headerConfig());
        //header breadcrumbs
        strPrint.append(breadCrumbsHeader());
        //add the header
        strPrint.append(lineBreakPrint());
        strPrint.append("! U1 SETLP 7 0 24\r\n");
        strPrint.append("! U1 SETSP 0\r\n");
        strPrint.append(setBold(1));
        strPrint.append("\r\n");
        strPrint.append(centerText("EOD Report",68));
        strPrint.append(setBold(0));
        strPrint.append("\r\n");
        /*
        strPrint.append(context.getString(R.string.print_eod_reader));
        strPrint.append(' ');
        strPrint.append("\r\n");
        strPrint.append(context.getString(R.string.print_eod_mru));
        strPrint.append(' ');
        strPrint.append("\r\n");
        strPrint.append(context.getString(R.string.print_eod_num));
        strPrint.append(' ');
        strPrint.append("\r\n");
        strPrint.append(context.getString(R.string.print_eod_reading_date));
        strPrint.append(' ');
        strPrint.append("\r\n");
        */
        strPrint.append("CAN        NAME              READING OC1 OC2 RN PC  TOTAL DUE    ");
        strPrint.append("\r\n");
        //iterate all of accounts
        int size = mtrPrints.size();
        for(int i = 0;i<size;i++){
           MeterPrint mtrPrint =  mtrPrints.get(i);
            //account number/CAN
            strPrint.append(addSpace(mtrPrint.getAcctNum(),10));
            strPrint.append(' ');
            //acount name
            strPrint.append(addSpace(mtrPrint.getCustName(),17));
            strPrint.append(' ');
            //reading
            strPrint.append(addSpace(mtrPrint.getPresRdg(),7));
            strPrint.append(' ');
            strPrint.append(addSpace(mtrPrint.getOC1(),3));
            strPrint.append(' ');
            strPrint.append(addSpace(mtrPrint.getOC2(),3));
            strPrint.append(' ');
            strPrint.append(addSpace(mtrPrint.getRangeCode(),2));
            strPrint.append(' ');
            strPrint.append(addSpace(mtrPrint.getPrintCount(),2));
            strPrint.append("  ");
            //total amount
            strPrint.append(mtrPrint.getTotalamt());
            strPrint.append("\r\n");
        }
        strPrint.append("\r\r\n");
        //footer breadcrumbs
        strPrint.append(breadCrumbsFooter());
        return strPrint.toString();
    }

    @Override
    public String mrStub(MeterPrint meterPrint) {
        StringBuilder strPrint = new StringBuilder();
        //header configuration
        strPrint.append(headerConfig());
        //header breadcrumbs
        strPrint.append(breadCrumbsHeader());
        //content
        strPrint.append(lineBreakPrint());
        strPrint.append("! U1 SETLP 7 0 24\r\n");
        strPrint.append("! U1 SETSP 0\r\n");
        strPrint.append(setBold(0));
        strPrint.append("\r\n");

        String strTitle = context.getString(R.string.print_mr_title);
        strPrint.append(centerText(strTitle,68));
        strPrint.append("\r\n");
        strPrint.append(context.getString(R.string.print_mr_mru));
        strPrint.append("   : ");
        strPrint.append(meterPrint.getMru());
        strPrint.append("/");
        strPrint.append(meterPrint.getSeqNo());
        strPrint.append("\r\n");
        strPrint.append(context.getString(R.string.print_mr_contact));
        strPrint.append("   : ");
        strPrint.append(meterPrint.getAcctNum());
        strPrint.append("\r\n");
        strPrint.append(context.getString(R.string.print_mr_name));
        strPrint.append("       : ");
        strPrint.append(meterPrint.getCustName());
        strPrint.append("\r\n");
        strPrint.append(context.getString(R.string.print_mr_reading));
        strPrint.append("    : ");
        strPrint.append(meterPrint.getPresRdg());
        strPrint.append("\r\n");
        strPrint.append(context.getString(R.string.print_mr_billcons));
        strPrint.append(" : ");
        strPrint.append(meterPrint.getBillCons());
        strPrint.append("\r\n");
        strPrint.append(context.getString(R.string.print_mr_ff1));
        strPrint.append("    : ");
        String str = meterPrint.getOC1();
        if(!Utils.isNotEmpty(str)){
            str ="-";
        }
        strPrint.append(str);
        strPrint.append("\r\n");
        strPrint.append(context.getString(R.string.print_mr_ff2));
        strPrint.append("    : ");
        str = meterPrint.getOC2();
        if(!Utils.isNotEmpty(str)){
            str ="-";
        }
        strPrint.append(str);
        strPrint.append("\r\n");
        strPrint.append(context.getString(R.string.print_mr_remarks));
        strPrint.append("            : ");
        str = meterPrint.getRemarks();
        if(!Utils.isNotEmpty(str)){
            str ="-";
        }
        strPrint.append(str);
        strPrint.append("\r\n");
        strPrint.append(context.getString(R.string.print_mr_billdate));
        strPrint.append("          : ");
        strPrint.append(meterPrint.getPresRdgDate());
        strPrint.append("\r\n");
        strPrint.append(context.getString(R.string.print_total_amount_due));
        strPrint.append("       : ");
        strPrint.append(meterPrint.getTotalamt());
        strPrint.append("\r\n");
        strPrint.append(lineBreakPrint());
        strPrint.append("\r\r\n");
        //footer breadcrumbs
        strPrint.append(breadCrumbsFooter());
        return strPrint.toString();
    }

    @Override
    String mrStubEnhance(ArrayList<MeterPrint> mtrPrints,int total) {
        StringBuilder strPrint = new StringBuilder();
        //header configuration
        strPrint.append(headerConfig());
        //header breadcrumbs
        strPrint.append(breadCrumbsHeader());
        //content
        strPrint.append(lineBreakPrint());
        strPrint.append("! U1 SETLP 7 0 24\r\n");
        strPrint.append("! U1 SETSP 0\r\n");
        strPrint.append(setBold(0));
        strPrint.append("\r\n");

        String strTitle = context.getString(R.string.print_mr_title);
        strPrint.append(centerText(strTitle,68));
        strPrint.append("\r\n");

        if(!mtrPrints.isEmpty()){
            StringBuilder strItem = new StringBuilder();
            strItem.append(context.getString(R.string.print_mr_mru1));
            strItem.append(mtrPrints.get(0).getMru());
            String s = context.getString(R.string.print_mr_pdate);
            strPrint.append(strItem.toString());
            strPrint.append(addSpace(strItem.toString(),68,s+Utils.getFormattedDate()));
            strPrint.append("\r\n");
            strItem = new StringBuilder();
            strItem.append(context.getString(R.string.print_mr_total));
            strItem.append(total);
            s = context.getString(R.string.print_mr_ptime);
            strPrint.append(strItem.toString());
            strPrint.append(addSpace(strItem.toString(),68,s+Utils.getFormattedTime()));
            strPrint.append("\r\n");
            strPrint.append(context.getString(R.string.print_mr_date));
            strPrint.append(Utils.getFormattedDate());
            strPrint.append("\r\n");
            strPrint.append("\r\n");
            strPrint.append("Count    CAN      Pres Rdg   Cons      OC1  OC2   Remarks          ");
            strPrint.append("\r\n");
            strPrint.append("\r\n");
            for(int i=0;i<mtrPrints.size();i++){
                MeterPrint mtrPrint = mtrPrints.get(i);
                strPrint.append(addSpace(mtrPrint.getSeqNo(),2));
                strPrint.append(addSpace(mtrPrint.getAcctNum(),3));
                strPrint.append(addSpace(mtrPrint.getPresRdg(),3));
                strPrint.append(addSpace(String.valueOf(mtrPrint.getBillCons()),2));
                strPrint.append(addSpace(String.valueOf(mtrPrint.getOC1()),3));
                strPrint.append(addSpace(String.valueOf(mtrPrint.getOC2()),2));
                strPrint.append(mtrPrint.getRemarks());
                strPrint.append("\r\n");
            }

        }



        //footer breadcrumbs
        strPrint.append(breadCrumbsFooter());
        return strPrint.toString();
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
        str.append("! 0 200 200 210 1\r\n");
        str.append("JOURNAL\r\n");
        str.append("PCX 110 20 !<maynilad.pcx\r\n");
        str.append("T 5 0 378 6 Maynilad Water Services Inc\r\n" );
        //str.append("T 5 0 380 39 MWSS Compound\r\n" );
        ArrayList<String> arry =  stringSplit(mtrPrint.getBcAddress(), 30);
        int xpost = 39;
        for(int i=0;i<arry.size();i++){
            str.append("T 7 0 378 "+xpost);
            str.append(' ');
            str.append(arry.get(i));
            str.append("\r\n");
            xpost =xpost+24;
        }

        str.append("T 7 0 378 "+xpost+" VAT Reg TIN ");
        str.append(mtrPrint.getBcTin());
        str.append("\r\n");
        xpost = xpost+30;
        str.append("T 7 0 378 "+xpost+" Permit No. 0107-116-00006-CBA/AR\r\n" );
        xpost = xpost+30;
        str.append("T 7 0 378 "+xpost+" Machine No. "+Build.SERIAL+" \r\n" );
        str.append("PRINT\r\n");


        return str.toString();
    }


    private ArrayList<String> stringSplit(String strToSplit, int x){

        ArrayList<String> arry = new ArrayList<>();
        if(strToSplit.length()<x){
            arry.add(strToSplit);
            return arry;
        }

        int startx=x;
        char[] charArray = strToSplit.toCharArray();
        for(int i=x;i<strToSplit.length();i++){
            startx =i;
            if(charArray[i]== ' '){
                break;
            }
        }
        arry.add(strToSplit.substring(0,startx));
        arry.add(strToSplit.substring(startx,strToSplit.length()));
        return  arry;
    }


    @Override
    public String billFooter(MeterPrint mtrPrint) {
        StringBuilder str = new StringBuilder();
        str.append(lineBreakPrint());


       // str.append("! U1 SETLP 7 0 24\r\n");
        str.append("! U1 CENTER\r\n");
        str.append("! U1 B 39 1 30 78 13 1 ");

        str.append(mtrPrint.getAcctNum());
        str.append(' ');
        str.append(mtrPrint.getOrig_totalamt());
        str.append("\r\n");
        str.append("\r\n");
        str.append("! U1 SETLP 7 0 24\r\n");
        str.append("! U1 SETSP 0\r\n");
        str.append(setBold(0));


        boolean isCashOnly =false;
        if(Utils.isNotEmpty(mtrPrint.getDisCheckFlg())){
            if(mtrPrint.getDisCheckFlg().equals("1")){
                isCashOnly = true;
            }

        }


       str.append(paymentInverse(isCashOnly));

        StringBuilder str1 = new StringBuilder();


        str1.append("Contract Account No. : ");
        str1.append(mtrPrint.getAcctNum());
        str.append(str1.toString());
        str.append(addSpace(str1.toString(),68,"Amount Due "+mtrPrint.getTotalamt()));
        str.append("\r\n");
         str1 = new StringBuilder();
        str1.append("Account Name : ");
        str1.append(mtrPrint.getCustName());
        str.append(str1.toString());
        StringBuilder str2 = new StringBuilder();
        str.append("! U1 SETLP 0 2 18\r\n");
        str2.append(context.getString(R.string.print_bill_period));
        str2.append(":");
        str2.append(mtrPrint.getPrevRdgDate());
        str2.append(" TO ");
        str2.append(mtrPrint.getPresRdgDate());


        str.append(addSpace(str1.toString(),88,str2.toString()));
        /*
        str.append("! U1 SETLP 7 0 24\r\n");
        str.append("\r\n");
        str.append("! U1 CENTER\r\n");
        str.append("! U1 B 39 1 30 78 13 1 ");
        str.append(mtrPrint.getAcctNum());
        str.append(' ');
        str.append(mtrPrint.getOrig_totalamt());
        str.append("\r\n");
        */
        str.append("\r\n");
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
        str.append("\r\r\r\n");
        return str.toString();
    }

    @Override
    public String serviceInfo(MeterPrint mtrPrint) {

        StringBuilder str = new StringBuilder();
        str.append("\r\n");
        str.append("! U1 CENTER\r\n");
        str.append("! U1 SETLP 0 3 18");
        str.append("! U1 SETSP 0\r\n");
        str.append(setBold(0));
        str.append("SOA # ");
        str.append(mtrPrint.getSOA());
        str.append("\r\r\r\n");
        str.append("! U1 CENTER 383\r\n");
        str.append("! U1 SETLP 0 3 18\r\n");
        str.append(setBold(1));
        String strTitle = context.getString(R.string.print_statement);
        str.append(centerText(strTitle,52));
        str.append("\r\n");
        str.append("! U1 SETLP 0 3 18\r\n");
        str.append(setBold(1));
        String stTemp = "For the month of: "+Utils.getCurrentDate("MMMM yyyy");
        str.append(centerText(stTemp,52));
        str.append("\r\r\n");
        str.append("! U1 SETLP 0 3 18\r\n");
        str.append(setBold(1));
        strTitle = context.getString(R.string.print_service);
        str.append(centerText(strTitle,52));
        str.append("\r\r\n");
        str.append("! U1 CENTER\r\n");
        str.append("! U1 SETLP 0 3 18");
        str.append("! U1 SETSP 0\r\r\n");
        str.append(setBold(0));
        str.append(context.getString(R.string.print_contract_num));
        str.append("  : ");
        str.append("! U1 CENTER\r\n");
        str.append("! U1 SETLP 5 1 48");
        str.append("! U1 SETSP 3\r\n");
        str.append(setBold(1));
        str.append(mtrPrint.getAcctNum());
        str.append("\r\n");
        str.append("! U1 SETLP 0 3 18\r\n");
        str.append("! U1 SETSP 0\r\n");
        str.append(setBold(0));
        str.append(context.getString(R.string.print_acct_name));
        str.append("    : ");
        str.append(setBold(1));
        str.append("! U1 SETSP 1");
        str.append(mtrPrint.getCustName());
        str.append("\r\n");
        str.append("! U1 SETSP 0\r\n");
        if(Utils.isNotEmpty(mtrPrint.getTenantName())){
            str.append(setBold(0));
            str.append(context.getString(R.string.print_tenant_name));
            str.append("     : ");
            str.append(setBold(1));
            str.append(mtrPrint.getTenantName());
            str.append("\r\n");
        }


        str.append("! U1 SETLP 0 3 18\r\n");
        str.append("! U1 SETSP 0\r\n");
        str.append(setBold(0));
        str.append(context.getString(R.string.print_address));
        str.append(" : ");
        ArrayList<String> arry =  stringSplit(mtrPrint.getCustAddress(), 32);
        str.append(arry.get(0));
        if(arry.size()>1){
            str.append("\r\n");
            for(int i=1;i<arry.size();i++){
                str.append("         ");
                str.append("         ");
                str.append(arry.get(i));
                str.append("\r\n");
            }
        }
        str.append("\r\n");
        str.append("! U1 SETLP 0 3 18\r\n");
        str.append("! U1 SETSP 0\r\n");
        str.append(setBold(0));
        str.append(context.getString(R.string.print_rate));
        str.append("      : ");
        str.append(mtrPrint.getBillClass());
        str.append(" ");
        str.append("TIN : ");
        str.append(mtrPrint.getTIN());

        str.append("\r\n");
        str.append("! U1 SETLP 0 3 18\r\n");
        str.append("! U1 SETSP 0\r\n");
        str.append(setBold(0));
        str.append(context.getString(R.string.print_business_area));
        str.append("   : ");
        str.append(mtrPrint.getBcDesc());
        str.append("\r\n");

        if(Utils.isNotEmpty(mtrPrint.getSc_id())){
           str.append("Senior Citizen ID : ");
            str.append(mtrPrint.getSc_id());
            str.append("\r\n");
        }
        str.append(linePrint());
        return str.toString();
    }

    @Override
    public String meterInfo(MeterPrint mtrPrint) {
        StringBuilder str = new StringBuilder();
        str.append("! U1 CENTER\r\n");
        str.append("! U1 SETLP 0 3 18\r\n");
        str.append(setBold(1));
        String strTitle = context.getString(R.string.print_meter_info);
        str.append(centerText(strTitle,52));
        str.append("\r\r\n");
        str.append("! U1 CENTER\r\n");
        str.append("! U1 SETLP 0 3 18\r\n");
        str.append("! U1 SETSP 0\r\n");
        str.append(setBold(1));
        str.append("Meter No.              MRU No.       Seq No.");
        str.append("\r\n");
        str.append(setBold(0));
        str.append("    ");
        str.append(addSpace(mtrPrint.getMeterNO(),19));
        str.append(addSpace(mtrPrint.getMru(),14));
        str.append(mtrPrint.getSeqNo());

        str.append("\r\r\n");
        str.append(setBold(0));
        str.append(context.getString(R.string.print_reading_date));
        str.append("        : ");
        str.append(setBold(1));
        str.append(mtrPrint.getPresRdgDate());
        str.append("\r\n");
        str.append(setBold(0));
        str.append(context.getString(R.string.print_pres_rdg));
        str.append("     : ");
        str.append(setBold(1));
        str.append(mtrPrint.getPresRdg());
        str.append("\r\n");
        str.append(setBold(0));
        str.append(context.getString(R.string.print_prev_rdg));
        str.append("    : ");
        str.append(setBold(1));
        str.append(mtrPrint.getPrevRdg());
        str.append("\r\n");
        str.append(setBold(0));
        str.append(context.getString(R.string.print_cosumption));
        str.append("  : ");
        str.append(setBold(1));
        str.append(mtrPrint.getBillCons());
        if(mtrPrint.getBillCons()<=10){
            str.append(" minimum");
        }
        else {
            int constypecode = mtrPrint.getConstype();
            if(constypecode == 1){
                str.append(" AVE");
            }
            else if(constypecode == 2) {
                str.append(" ADJ");
            }
        }
        str.append("\r\r\n");
        str.append(setBold(0));
        str.append(context.getString(R.string.print_prevcons));
        str.append("      ");
        String prevConsline1 = mtrPrint.getPrevConsLine1();
        String prevConsline2 = mtrPrint.getPrevConsLine2();
        if(Utils.isNotEmpty(prevConsline1)&& Utils.isNotEmpty(prevConsline2)){
            String[] str1 = prevConsline1.split(",");
            String[] str2 = prevConsline2.split(",");

            int size = str1.length;
            for(int i=0;i<size;i++){
                if(Utils.isNotEmpty(str1[i])){
                    str.append(str1[i]);
                    str.append("      ");
                }
            }
            str.append("\r\n");
            str.append("   Consumption         ");
             size = str2.length;
            for(int i=0;i<size;i++){
                if(Utils.isNotEmpty(str2[i])){
                    str.append(str2[i]);
                    str.append("      ");
                }

            }
        }
        str.append("\r\n");
        str.append(linePrint());
        return str.toString();
    }

    @Override
    public String paymentHistory(MeterPrint mtrPrint) {
        StringBuilder str = new StringBuilder();
        str.append("! U1 CENTER\r\n");
        str.append("! U1 SETLP 0 3 18");

        str.append(setBold(1));
        String strTitle = context.getString(R.string.print_bill_payment);

        str.append(centerText(strTitle,52));
        str.append("\r\n");
        str.append("! U1 CENTER\r\n");
        str.append("! U1 SETLP 0 3 18");
        str.append("! U1 SETSP 0\r\n");

        str.append(setBold(1));
        str.append("\r\n");
        str.append("INVOICE NO.");
        str.append("\r\n");
        str.append("\r\n");
        str.append("! U1 CENTER\r\n");
        str.append("! U1 SETLP 7 0 24\r\n");
        str.append("! U1 SETSP 0\r\n");
        str.append(setBold(1));
        str.append("Desc  Net Amount VAT  Total Amount   OR#        Date      Tax Code");
        str.append("\r\n");
        str.append(setBold(0));
        str.append(payhistoryDetails(mtrPrint.getWbPaydtls1()));
        str.append(payhistoryDetails(mtrPrint.getWbPaydtls2()));
        str.append(payhistoryDetails(mtrPrint.getMiscPaydtls()));
        str.append(payhistoryDetails(mtrPrint.getGdPaydtls()));
        str.append(mtrPrint.getGdPaydtls());
        str.append("\r\n");
        str.append("! U1 CENTER\r\n");
        str.append("! U1 SETLP 0 2 18\r\n");
        str.append("! U1 SETSP 0\r\n");
        str.append(setBold(0));
        str.append("DESCRIPTION: WB-Water Bill, GD-Guarantee Deposit, MISC-Reopening Fee,Connection Fee,Metering Charge\r\n");
        str.append(linePrint());

        return str.toString();
    }

    private String payhistoryDetails(String data){
        StringBuilder strPrint =  new StringBuilder();


        if(Utils.isNotEmpty(data)){
          String datas[]  =   data.split(",");
          strPrint.append(addSpace(datas[0],4));
            strPrint.append("  ");
            strPrint.append(addSpace(datas[1],10));
            strPrint.append(' ');
            strPrint.append(addSpace(datas[2],4));
            strPrint.append(' ');
            if(datas.length>3)
            strPrint.append(addSpace(datas[3],11));
            strPrint.append(' ');
            if(datas.length>4)
            strPrint.append(addSpace(datas[4],10));
            strPrint.append(' ');
            if(datas.length>5)
            strPrint.append(datas[5]);
            strPrint.append(' ');
            if(datas.length>6)
            strPrint.append(datas[6]);
            strPrint.append("\r\n");
        }
        return strPrint.toString();
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


    private String rightJustify(int max,String value){
        int val_lenght =  value.length();
        StringBuilder str = new StringBuilder();
        int x = max - val_lenght;
        for(int i=0;i<x;i++){
            str.append(' ');
        }
        str.append(value);
        return str.toString();
    }


    private String addSpace(String str,int maxSpace){
        StringBuilder strOr = new StringBuilder();
        int curLinex = str.length();
        strOr.append(str);
        if(curLinex<maxSpace){
            int s = maxSpace - curLinex;
            for(int i=0;i<s;i++){
                strOr.append(' ');
            }
        }
        return strOr.toString();
    }

    @Override
    public String billSummary(MeterPrint mtrPrint) {
        StringBuilder str = new StringBuilder();
        str.append("! U1 CENTER\r\n");
        str.append("! U1 SETLP 0 3 18\r\n");
        str.append(setBold(1));
        String strTitle = context.getString(R.string.print_bill_summary);
        str.append(centerText(strTitle,52));
        str.append("\r\n");
        str.append("\r\n");
        str.append("! U1 SETLP 0 3 18\r\n");
        str.append("! U1 SETSP 0\r\n");
        str.append(setBold(1));
        str.append(context.getString(R.string.print_bill_period));
        str.append("   ");
        str.append(mtrPrint.getPrevRdgDate());
        str.append("-");
        str.append(mtrPrint.getPresRdgDate());

        str.append("\r\n");
        str.append("! U1 SETLP 0 3 18\r\n");
        str.append("! U1 SETSP 0\r\n");
        str.append(setBold(1));
        String str1 = context.getString(R.string.print_current_charges);
        String value = mtrPrint.getTotcurr_charge();
        str.append(str1);
        str.append(addSpace(str1,50,value));
        str.append("\r\n");
        str.append(setBold(0));
        str.append("! U1 SETLP 7 0 24\r\n");
        str.append("! U1 SETSP 0\r\n");

        String[] arry = context.getResources().getStringArray(R.array.print_arry_curcharnges);
        int size=  arry.length;
        for(int i=0;i<size;i++){
            value = mtrPrint.cur_charges.get(i);
            if(value.equals("0.00")){
                continue;
            }
            str.append(TAB_SPACE);
            str.append(arry[i]);
            String temp =  arry[i];



            str.append(addSpace(temp,63-TAB_SPACE.length(),value));
            str.append("\r\n");
        }

        str.append("\r\n");
        str.append("! U1 SETLP 0 3 18\r\n");
        str.append("! U1 SETSP 0\r\n");
        str.append(setBold(1));
        str1 = context.getString(R.string.print_other_charges);
        value = mtrPrint.getOtherCharges();
        if(!value.equals("0.00")){
            str.append(str1);
            str.append(addSpace(str1,50,value));
            str.append("\r\n");

            str.append("! U1 SETLP 7 0 24\r\n");
            str.append("! U1 SETSP 0\r\n");
            str.append(setBold(0));

            String result =  othrChgContent(R.array.print_arry_othercharge0,mtrPrint.otherCharges0,63);
            str.append(result);
            result =  othrChgContent(R.array.print_arry_othercharge1,
                    mtrPrint.otherCharges1,mtrPrint.otherChrgInd1,69);
            str.append(result);
            result =  othrChgContent(R.array.print_arry_othercharge2,mtrPrint.otherCharges2,63);
            str.append(result);
            result =  othrChgContent(R.array.print_arry_othercharge3,
                    mtrPrint.otherCharges3,mtrPrint.otherChrgInd2,69);
            str.append(result);

            result =  othrChgContent(R.array.print_arry_othercharge4,mtrPrint.otherCharges4,63);
            str.append(result);
        }

        str.append("\r\n");
        double prevUnpaid =  mtrPrint.getPrevUnPaid();
        if(prevUnpaid!=0.00){
            str.append("! U1 SETLP 0 3 18\r\n");
            str.append("! U1 SETSP 0\r\n");
            str.append(setBold(1));
            String unpaidStr =  Utils.formatValue(prevUnpaid);
            if(prevUnpaid>0.00){
                str1 = context.getString(R.string.print_previous);
                str.append(str1);
                str.append(addSpace(str1,50,unpaidStr));
                str.append("\r\n");
                str.append("     ");
                str.append("(Please pay immediately)");
            }
            else if(prevUnpaid<0.00) {
                str1 = context.getString(R.string.print_refund);
                str.append(str1);
                String str2 = Utils.formatValue(Math.abs(prevUnpaid));
                str.append(addSpace(str1,50,str2));
            }


            str.append("\r\r\n");
        }

        str.append(lineBreakPrint());
        str.append("! U1 SETLP 7 1 48\r\n");
        str.append("! U1 SETSP 3\r\n");
        str.append(setBold(1));
        str.append(context.getString(R.string.print_total_amount_due));
        String temp =  context.getString(R.string.print_total_amount_due);
        value = "PHP   "+mtrPrint.getTotalamt();
        str.append(addSpace(temp,53,value));

        str.append("\r\n");
        temp =  context.getString(R.string.print_payment_date);
        str.append(temp);
         value = mtrPrint.getDueDate();
        str.append(addSpace(temp,53,value));

        str.append("\r\n");
        str.append(lineBreakPrint());
        str.append("! U1 SETLP 5 0 24\r\n");
        str.append("! U1 SETSP 0\r\n");
        str.append(setBold(0));
        arry = context.getResources().getStringArray(R.array.arry_instruction);
        for (String string: arry
             ) {
            str.append(string);
            str.append("\r\n");
        }

        return str.toString();
    }

    @Override
    String promoMsg(MeterPrint mtrPrint) {
        MeterBillDao mtrDao = new MeterBillDao(context);
        ArrayList<String> arry = mtrDao.getPromoMessages();
        if(arry.size()!=0){
            StringBuilder str = new StringBuilder();
            str.append(lineBreakPrint());
            str.append("! U1 CENTER\r\n");
            str.append("! U1 SETLP 0 3 18\r\n");

            str.append(setBold(1));
            String strTitle = context.getString(R.string.print_promo);
            str.append(centerText(strTitle,52));
            str.append("\r\n");
            str.append("! U1 SETLP 7 0 24\r\n");
            str.append("! U1 SETSP 0");
            str.append(setBold(0));
            for(int i=0;i<arry.size();i++){
                str.append("\r\n");
                str.append(centerText(arry.get(i),68));
                str.append("\r\n");
                str.append("\r\n");
            }
            return str.toString();
        }




        return "";
    }

    @Override
    String billAdvisory(MeterPrint mtrPrint,String range) {
        StringBuilder str = new StringBuilder();
        str.append(lineBreakPrint());
        str.append("! U1 CENTER\r\n");
        str.append("! U1 SETLP 0 3 18\r\n");

        str.append(setBold(1));
        String strTitle = context.getString(R.string.print_advisory);
        str.append(centerText(strTitle,52));
        str.append("\r\n");
        str.append("! U1 SETLP 7 0 24\r\n");
        str.append("! U1 SETSP 0");
        str.append(setBold(0));

        str.append(context.getResources().getString(R.string.print_advisory1,range));
        str.append("\r\n");
        str.append(context.getResources().getString(R.string.print_advisory2,mtrPrint.getBillCons()));
        str.append("\r\n");
        str.append(context.getResources().getString(R.string.print_advisory3,range,mtrPrint.getBillCons()));
        str.append("\r\n");

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
        str.append("! 0 200 200 210 1\r\n");
        str.append("JOURNAL\r\n");
        str.append("PCX 110 20 !<maynilad.pcx\r\n");
        str.append("T 5 0 378 6 Maynilad Water Services Inc\r\n" );
        //str.append("T 5 0 380 39 MWSS Compound\r\n" );
        ArrayList<String> arry =  stringSplit(mtrPrint.getBcAddress(), 30);
        int xpost = 39;
        for(int i=0;i<arry.size();i++){
            str.append("T 7 0 378 "+xpost);
            str.append(' ');
            str.append(arry.get(i));
            str.append("\r\n");
            xpost =xpost+24;
        }

        str.append("T 7 0 378 "+xpost+" VAT Reg TIN ");
        str.append(mtrPrint.getBcTin());
        str.append("\r\n");
        xpost = xpost+30;
        str.append("T 7 0 378 "+xpost+" Permit No. 0107-116-00006-CBA/AR\r\n" );

        str.append("PRINT\r\n");

        str.append("! U1 CENTER\r\n");
        str.append("! U1 SETLP 0 3 18");
        str.append(setBold(1));
        String strTitle = centerText(context.getString(R.string.print_notic_discon),52);
        str.append(strTitle);
        str.append("\r\n");
        str.append("! U1 CENTER\r\n");
        str.append("! U1 SETLP 5 1 48");
        str.append("! U1 SETSP 3\r\n");
        str.append(setBold(1));
        str.append(mtrPrint.getCustName());
        str.append("\r\n");
        str.append("! U1 CENTER\r\n");
        str.append("! U1 SETLP 0 3 18");
        str.append("! U1 SETSP 0\r\n");
        str.append(setBold(0));
        str.append(context.getString(R.string.print_contract_num));
        str.append("   ");
        str.append(setBold(1));
        str.append(mtrPrint.getAcctNum());
        str.append("\r\n");
        str.append("! U1 CENTER\r\n");
        str.append("! U1 SETLP 0 3 18");
        str.append("! U1 SETSP 0\r\n");
        str.append(setBold(0));
        str.append(mtrPrint.getCustAddress());
        str.append("\r\n");

        str.append("! U1 SETLP 7 0 24\r\n");
        str.append("! U1 SETSP 0\r\n");
        str.append(setBold(0));
        str.append(context.getResources().getString(R.string.print_arry_discon1,mtrPrint.getTotalamt()));
        str.append("\r\n");
        str.append(context.getResources().getString(R.string.print_arry_discon2));
        str.append("\r\n");
        str.append(context.getResources().getString(R.string.print_arry_discon3,mtrPrint.getDueDate()));
        str.append("\r\n");
        str.append(context.getResources().getString(R.string.print_arry_discon4));
        str.append("\r\n");
        str.append(context.getResources().getString(R.string.print_arry_discon5));
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
    public String testfont() {
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


        /*
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

        */
        str.append("! U1 SETLP 7 0 24\r\n");
        str.append("Font 7,0 Height 24\r\n");
        str.append(randomString());
        str.append("! U1 SETLP 7 1 48\r\n");
        str.append("Font 7,1 Height 48\r\n");
        str.append(randomString());
        str.append("\r\n");
        return str.toString();
    }

    @Override
    String billReminder(MeterPrint mtrPrint) {
        StringBuilder str = new StringBuilder();
        str.append(lineBreakPrint());
        str.append("! U1 CENTER\r\n");
        str.append("! U1 SETLP 0 3 18\r\n");

        str.append(setBold(1));
        String strTitle = context.getString(R.string.print_reminder_title);
        str.append(centerText(strTitle,52));
        str.append("\r\n");
        str.append("! U1 SETLP 7 0 24\r\n");
        str.append("! U1 SETSP 0");
        str.append(setBold(0));


        String[]   arry = context.getResources().getStringArray(R.array.print_reminder);
        int size=  arry.length;
        for(int i=0;i<size;i++){
            str.append(centerText(arry[i],68));
            str.append("\r\n");
        }
        str.append("\r\r\r\n");
        return str.toString();
    }

    String linePrint(){
        /*
        String cpclData = "! 0 200 200 30 1\r\n"
                + "LINE 1 0 811 0 2\r\n"
                + "PRINT\r\n";
        return cpclData;
        *
        */
        StringBuilder str = new StringBuilder();
        str.append("\r\n");
        str.append("! U1 SETLP 7 0 24\r\n");
        str.append("! U1 SETSP 0\r\n");
        for(int i=0;i<68;i++){
            str.append('_');
        }
        str.append("\r\n");
        return str.toString();
    }

    String paymentInverse(boolean isInverse){
        StringBuilder strPrint = new StringBuilder();
        strPrint.append("! 0 200 200 30 1\r\n");
        if(isInverse){
            strPrint.append("T 7 0 2 2 CASH PAYMENT ONLY\r\n");
            strPrint.append("IL 2 2 223 2 24\r\n");
        }
        strPrint.append("T 7 0 514 2 PAYMENT CENTER/BANK COPY\r\n");
        strPrint.append("PRINT\r\n");


        return strPrint.toString();
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

    String centerText(String str,int maxlength){
        StringBuilder stringBuilder = new StringBuilder();
        int length = str.length();
        int t_space =maxlength -length;
        int left_space = t_space/2;
        int right_space = t_space - left_space;
        for(int i= 0;i<left_space;i++){
            stringBuilder.append(' ');
        }
        stringBuilder.append(str);
        for(int i= 0;i<right_space;i++){
            stringBuilder.append(' ');
        }
        return stringBuilder.toString();

    }

    private String othrChgContent(int arryID, double[] otherCharges, int max){
        StringBuilder str = new StringBuilder();
        String[]  arry = context.getResources().getStringArray(arryID);

        int size=  arry.length;
        for(int i=0;i<size;i++){
            double val = otherCharges[i];
            if(val ==0.00){
                continue;
            }
            str.append(TAB_SPACE);
            str.append(arry[i]);
            String temp =  arry[i];
            String value = Utils.formatValue(val);

            str.append(addSpace(temp,max-TAB_SPACE.length(),value));
            str.append("\r\n");
        }
        return str.toString();
    }

    private String othrChgContent(int arryID, double[] otherCharges,String[] indicators, int max){
        StringBuilder str = new StringBuilder();
        String[]  arry = context.getResources().getStringArray(arryID);

        int size=  arry.length;
        for(int i=0;i<size;i++){
            double val =  otherCharges[i];
            if(val ==0.00){
                continue;
            }
            str.append(TAB_SPACE);
            str.append(arry[i]);
            str.append(' ');
            str.append(indicators[i]);
            String temp =  arry[i]+' '+indicators[i];

            String value = Utils.formatValue(val);
            if(value.length()<7){
                max =63;
            }

            str.append(addSpace(temp,max-TAB_SPACE.length(),value));
            str.append("\r\n");
        }
        return str.toString();
    }

    

}
