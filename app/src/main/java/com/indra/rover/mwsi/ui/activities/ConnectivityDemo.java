package com.indra.rover.mwsi.ui.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.data.pojo.meter_reading.MeterPrint;
import com.indra.rover.mwsi.print.layout.PrintLayout;
import com.indra.rover.mwsi.print.layout.ZebraLayout;
import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.device.ZebraIllegalArgumentException;
import com.zebra.sdk.graphics.ZebraImageFactory;
import com.zebra.sdk.graphics.ZebraImageI;
import com.zebra.sdk.printer.PrinterLanguage;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ConnectivityDemo extends Activity {

    private Connection printerConnection;
    private ZebraPrinter printer;
    private TextView statusField;
    private EditText macAddress;
    private Button testButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection_screen_with_status);
        macAddress = (EditText) this.findViewById(R.id.macInput);
        statusField = (TextView) this.findViewById(R.id.statusText);

        testButton = (Button) this.findViewById(R.id.testButton);
        testButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        enableTestButton(false);
                        Looper.prepare();
                        doConnectionTest();
                        Looper.loop();
                        Looper.myLooper().quit();
                    }
                }).start();
            }
        });


    }


    @Override
    protected void onStop() {
        super.onStop();
        if (printerConnection != null && printerConnection.isConnected()) {
            disconnect();
        }
    }

    private void enableTestButton(final boolean enabled) {
        runOnUiThread(new Runnable() {
            public void run() {
                testButton.setEnabled(enabled);
            }
        });
    }



    public ZebraPrinter connect() {
        setStatus("Connecting...", Color.YELLOW);
        printerConnection = null;
            printerConnection = new BluetoothConnection(getMacAddressFieldText());
        try {
            printerConnection.open();
            setStatus("Connected", Color.GREEN);
        } catch (ConnectionException e) {
            setStatus("Comm Error! Disconnecting", Color.RED);
            sleep(1000);
            disconnect();
        }

        ZebraPrinter printer = null;

        if (printerConnection.isConnected()) {
            try {
                printer = ZebraPrinterFactory.getInstance(printerConnection);
                setStatus("Determining Printer Language", Color.YELLOW);
                PrinterLanguage pl = printer.getPrinterControlLanguage();
                setStatus("Printer Language " + pl, Color.BLUE);
            } catch (ConnectionException e) {
                setStatus("Unknown Printer Language", Color.RED);
                printer = null;
                sleep(1000);
                disconnect();
            } catch (ZebraPrinterLanguageUnknownException e) {
                setStatus("Unknown Printer Language", Color.RED);
                printer = null;
                sleep(1000);
                disconnect();
            }
        }

        return printer;
    }

    public void disconnect() {
        try {
            setStatus("Disconnecting", Color.RED);
            if (printerConnection != null) {
                printerConnection.close();
            }
            setStatus("Not Connected", Color.RED);
        } catch (ConnectionException e) {
            setStatus("COMM Error! Disconnected", Color.RED);
        } finally {
            enableTestButton(true);
        }
    }

    private void setStatus(final String statusMessage, final int color) {
        runOnUiThread(new Runnable() {
            public void run() {
                statusField.setBackgroundColor(color);
                statusField.setText(statusMessage);
            }
        });
        sleep(1000);
    }

    private String getMacAddressFieldText() {
        return macAddress.getText().toString();
    }


    private void doConnectionTest() {
        printer = connect();
        if (printer != null) {
          //  storeImage();
            sendTestLabel();
        } else {
            disconnect();
        }
    }

    private void storeImage(){

        try {
            ZebraImageI zebraImageI = ZebraImageFactory.getImage(getAssets().open("maynilad_big.png"));
           // ZebraImageI zebraImageI2 = ZebraImageFactory.getImage(getAssets().open("maynilad_m.png"));

            printer.storeImage("e:maynilad_big.png",zebraImageI,-1,-1);
         //   printer.storeImage("e:maynilad_m.png",zebraImageI,-1,-1);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ConnectionException e) {
            e.printStackTrace();
        } catch (ZebraIllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private void sendTestLabel() {
        try {

            byte[] configLabel = getConfigLabel();

            printerConnection.write(configLabel);
            setStatus("Sending Data", Color.BLUE);
            sleep(1500);
            if (printerConnection instanceof BluetoothConnection) {
                String friendlyName = ((BluetoothConnection) printerConnection).getFriendlyName();
                setStatus(friendlyName, Color.MAGENTA);
                sleep(500);
            }
        } catch (ConnectionException e) {
            setStatus(e.getMessage(), Color.RED);
        } finally {
            disconnect();
        }
    }

    /*
    * Returns the command for a test label depending on the printer control language
    * The test label is a box with the word "TEST" inside of it
    *
    * _________________________
    * |                       |
    * |                       |
    * |        TEST           |
    * |                       |
    * |                       |
    * |_______________________|
    *
    *
    */
    private byte[] getConfigLabel() {
        byte[] configLabel = null;
            String cpclConfigLabel = "! U1 setvar \"device.languages\", \"line_print\"\r\n"
                    +"U"
                    + "! U1 PCX 0 30 !<maynilad.pcx\r\n"
                    +"PRINT";
        StringBuilder str = new StringBuilder();
        str.append("! U1 setvar \"device.languages\", \"line_print\"\r\n");
        str.append("! U1 BEGIN-PAGE\r\n");
        str.append("! 0 200 200 175 1\r\n");
        str.append("JOURNAL\r\n");
        str.append("B 39 1 30 78 1 1 12345678 1335446\r\n");
        str.append("PRINT\r\n");

       /* str.append("PCX 42 10 !<maynilad.pcx\r\n");
        str.append("C Maynilad Water Services Inc\r\n" );
        str.append("T 5 0 400 39 MWSS Compound\r\n" );
        str.append("T 7 0 400 62 ");

        str.append("\r\n");
        str.append("T 7 0 400 86 VAT Reg TIN ");
        str.append("\r\n");
        str.append("T 7 0 400 117 Permit No. 0107-116-00006-CBA/AR\r\n" );
        str.append("T 7 0 400 150 Machine No. "+ Build.SERIAL+" \r\n" );
        */


            String toPrint = str.toString();// s.contentPrint(new MeterPrint());
        try {
            configLabel = toPrint.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return configLabel;
    }

     static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
