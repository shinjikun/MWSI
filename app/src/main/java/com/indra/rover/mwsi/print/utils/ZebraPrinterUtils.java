package com.indra.rover.mwsi.print.utils;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import com.indra.rover.mwsi.utils.DialogUtils;
import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;


public class ZebraPrinterUtils implements DialogUtils.DialogListener {


    private Connection printerConnection;
    private ZebraPrinter printer;
    private Context context;
    private DialogUtils dlgUtils;
    ZebraPrintListener listener;
    String btAddress;

    public final static int PRINT_ONLY=0;
    public final static int PRINT_READING =1;
    public final static int PRINT_W_MRSTUB=2;

    private static ZebraPrinterUtils instance;

    public ZebraPrinterUtils(Context context){
        this.context =context;
        dlgUtils = new DialogUtils(this.context);
        dlgUtils.setListener(this);

    }

    public ZebraPrinterUtils setListener(ZebraPrintListener listener){
        this.listener = listener;
        return this;
    }



    private void setStatus(final String statusMessage, final int color) {
        Log.i("Test",statusMessage);

    //    sleep(1000);
    }


    public static ZebraPrinterUtils getInstance(Context context) {
        if(instance == null){
            instance = new ZebraPrinterUtils(context);
        }

        return instance;
    }

    static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dialog_confirm(int dialog_id, Bundle params) {

    }

    @Override
    public void dialog_cancel(int dialog_id, Bundle params) {

    }

    public interface ZebraPrintListener{
        void onFinishPrinting(int type);
        void onErrorPrinting();
        void onStartPrinting();

    }

    public void printMeterReading(byte[] data){
        sendData(data,PRINT_READING);
    }


    public void sendData(final byte[] data,final  int type){
        if(listener!=null){
            listener.onStartPrinting();
        }
        new Thread(new Runnable() {
            public void run() {

                Looper.prepare();
                doConnectionTest(data, type);
                Looper.loop();
                Looper.myLooper().quit();
            }
        }).start();
    }


    public void sendData(final byte[] data){
        sendData(data,PRINT_ONLY);
    }


    private void doConnectionTest(byte[] data, int type) {
        printer = connect();
        if (printer != null) {
            //  storeImage();
            sendTestLabel(data, type);
        } else {
            disconnect(true, 0);
        }
    }



    private void sendTestLabel(byte[] data, int type) {
        try {

            printerConnection.write(data);
            setStatus("Sending Data", Color.BLUE);
            sleep(1000);
            /*
            if (printerConnection instanceof BluetoothConnection) {
                String friendlyName = ((BluetoothConnection) printerConnection).getFriendlyName();
                setStatus(friendlyName, Color.MAGENTA);
                sleep(500);
            }
            */
        } catch (ConnectionException e) {
            setStatus(e.getMessage(), Color.RED);
        } finally {
            disconnect(false, type);
        }
    }


    public ZebraPrinter connect() {
        setStatus("Connecting...", Color.YELLOW);

        printerConnection = null;
        printerConnection = new BluetoothConnection(getBtAddress());
        try {
            printerConnection.open();
            setStatus("Connected", Color.GREEN);
        } catch (ConnectionException e) {
            if(listener!=null){
                listener.onErrorPrinting();
            }

            setStatus("Comm Error! Disconnecting", Color.RED);
            sleep(500);
            disconnect(true, 0);
        }

        ZebraPrinter printer = null;

        if (printerConnection.isConnected()) {
            try {
                printer = ZebraPrinterFactory.getInstance(printerConnection);
            } catch (ConnectionException e) {
                setStatus("Unknown Printer Language", Color.RED);
                printer = null;
                sleep(500);
                disconnect(true, 0);
            } catch (ZebraPrinterLanguageUnknownException e) {
                setStatus("Unknown Printer Language", Color.RED);
                printer = null;
                sleep(500);
                disconnect(true, 0);
            }
        }

        return printer;
    }

    public void disconnect(boolean isError, int type) {
        try {
            setStatus("Disconnecting", Color.RED);
            if (printerConnection != null) {
                printerConnection.close();
            }
            setStatus("Not Connected", Color.RED);
        } catch (ConnectionException e) {
            if(listener!=null){
                listener.onErrorPrinting();
            }

            setStatus("COMM Error! Disconnected", Color.RED);
        } finally {

            if(!isError){
                if(listener!=null){
                    listener.onFinishPrinting(type);
                }

            }

        }
    }

    public void setBtAddress(String btAddress) {
        this.btAddress = btAddress;
    }

    private String getBtAddress() {
        return btAddress;
    }
}