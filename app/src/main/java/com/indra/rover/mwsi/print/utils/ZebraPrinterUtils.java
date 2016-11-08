package com.indra.rover.mwsi.print.utils;

import android.os.Looper;

import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;

import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;

/**
 * Created by Indra on 11/8/2016.
 */
public class ZebraPrinterUtils {


    private Connection printerConnection;
    private ZebraPrinter printer;
    ZPrinterEventListener listener;
    String btAddress;

    private static ZebraPrinterUtils instance;


    public static ZebraPrinterUtils getInstance() {
        if(instance == null){
            instance = new ZebraPrinterUtils();
        }
        return instance;
    }

    public ZebraPrinterUtils setBtEventListener(ZPrinterEventListener eventListener) {
        this.listener = eventListener;
        return this;
    }

    public interface ZPrinterEventListener {

        void onFinishPrinting();




    }

    static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public  void sendData(final byte[] data){

        new Thread(new Runnable() {
            public void run() {

                Looper.prepare();
                doConnectionTest(data);
                Looper.loop();
                Looper.myLooper().quit();
            }
        }).start();
    }


    public ZebraPrinter connect() {

        printerConnection = null;
        printerConnection = new BluetoothConnection(getBtAddress());
        try {
            printerConnection.open();

        } catch (ConnectionException e) {

            sleep(1000);
            disconnect();
        }

        ZebraPrinter printer = null;

        if (printerConnection.isConnected()) {
            try {
                printer = ZebraPrinterFactory.getInstance(printerConnection);



            } catch (ConnectionException e) {

                printer = null;
                sleep(1000);
                disconnect();
            } catch (ZebraPrinterLanguageUnknownException e) {

                printer = null;
                sleep(1000);
                disconnect();
            }
        }

        return printer;
    }

    public void disconnect() {
        try {

            if (printerConnection != null) {
                printerConnection.close();
            }

        } catch (ConnectionException e) {
            e.printStackTrace();
        } finally {

        }
    }



    private String getMacAddressFieldText() {
        return "f";
    }


    private void doConnectionTest(byte[] data) {
        printer = connect();
        if (printer != null) {
            //  storeImage();
            write(data);
        } else {
            disconnect();
        }
    }

    private void write(byte[] data) {
        try {
            printerConnection.write(data);
            sleep(1500);
            if (printerConnection instanceof BluetoothConnection) {
                sleep(500);
            }
        } catch (ConnectionException e) {
             e.printStackTrace();
        } finally {
            disconnect();
            if(listener!=null){
                listener.onFinishPrinting();
            }
        }
    }

    public void setBtAddress(String btAddress) {
        this.btAddress = btAddress;
    }

    public String getBtAddress() {
        return btAddress;
    }
}
