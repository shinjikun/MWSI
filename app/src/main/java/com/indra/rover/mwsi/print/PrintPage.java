package com.indra.rover.mwsi.print;

import android.content.Context;
import android.util.Log;

import com.indra.rover.mwsi.data.pojo.meter_reading.MeterPrint;
import com.indra.rover.mwsi.print.layout.ZebraLayout;
import com.indra.rover.mwsi.utils.Constants;
import com.indra.rover.mwsi.utils.PreferenceKeys;
import com.indra.rover.mwsi.utils.Utils;

import java.util.ArrayList;

public class PrintPage {

   private PrintPageListener listener;
   private Context context;
   private PreferenceKeys prefs;
    public PrintPage(Context context,PrintPageListener listener){
        this.context =context;
        this.listener = listener;
        prefs = PreferenceKeys.getInstance(this.context);
    }



    public void printEOD(ArrayList<MeterPrint> mtrPrints){
            ZebraLayout zebraLayout = new ZebraLayout(this.context);
            String str =zebraLayout.eodReport(mtrPrints);
            if(listener!=null){
                listener.onPrintPageResult(str, false);
            }
    }




    public void execute(MeterPrint meterPrint){
        if(meterPrint!=null){
            //determine the print device via bluetooth name
            String btName = prefs.getData("bname","");
            if(Utils.isNotEmpty(btName)){
                btName = btName.toLowerCase();
            //    if(btName.contains("zebra")){
                    ZebraLayout zebraLayout =new ZebraLayout(this.context);
                    String str = zebraLayout.contentPrint(meterPrint);
                    if(listener!=null){

                    boolean isMRStubToPrint =    prefs.getData(Constants.PRINT_STUB_ENABLED,false);
                        if(isMRStubToPrint){
                            String mrStubPage = zebraLayout.mrStub(meterPrint);
                            Log.i("Test",mrStubPage);
                            listener.onPrintPageAndMRStub(str,mrStubPage);
                        }
                        else {
                            listener.onPrintPageResult(str, true);
                        }

                    }
             //   }
            }
        }
   }

    public interface  PrintPageListener{
         void onPrintPageResult(String meterPrintPage, boolean isMeterprint);
         void onPrintPageAndMRStub(String meterPrintPage,String mrStubPage);
    }

}
