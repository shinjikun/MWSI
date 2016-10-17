package com.indra.rover.mwsi.print;

import android.content.Context;

public class PrintPage {

    PrintPageListener listener;
    Context context;
    public PrintPage(Context context,PrintPageListener listener){
        this.context =context;
        this.listener = listener;
    }

    public void execute(){
            if(listener!=null){
                listener.onPrintPageResult();
            }
    }

    public interface  PrintPageListener{
         void onPrintPageResult();
    }

}
