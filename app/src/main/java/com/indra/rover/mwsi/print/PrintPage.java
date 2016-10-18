package com.indra.rover.mwsi.print;

import android.content.Context;
import android.util.Log;

import com.indra.rover.mwsi.data.pojo.meter_reading.MeterPrint;
import com.indra.rover.mwsi.print.layout.ZebraLayout;

public class PrintPage {

    PrintPageListener listener;
    Context context;
    public PrintPage(Context context,PrintPageListener listener){
        this.context =context;
        this.listener = listener;
    }

    public void execute(MeterPrint meterPrint){
        if(meterPrint!=null){
            ZebraLayout zebraLayout =new ZebraLayout(this.context);
            String str = zebraLayout.contentPrint(meterPrint);
            Log.i("Test","str"+str);
            if(listener!=null){
                listener.onPrintPageResult(str);
            }
        }

    }

    public interface  PrintPageListener{
         void onPrintPageResult(String str);
    }

}
