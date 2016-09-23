package com.indra.rover.mwsi.ui.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;


import com.indra.rover.mwsi.R;
import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.comm.TcpConnection;
import com.zebra.sdk.printer.PrinterLanguage;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;

public class ConnectivityDemo extends Activity {

    private Connection printerConnection;
    private RadioButton btRadioButton;
    private ZebraPrinter printer;
    private TextView statusField;
    private EditText macAddress, ipDNSAddress, portNumber;
    private Button testButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection_screen_with_status);

        ipDNSAddress = (EditText) this.findViewById(R.id.ipAddressInput);


        portNumber = (EditText) this.findViewById(R.id.portInput);


        macAddress = (EditText) this.findViewById(R.id.macInput);


        statusField = (TextView) this.findViewById(R.id.statusText);
        btRadioButton = (RadioButton) this.findViewById(R.id.bluetoothRadio);

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

        RadioGroup radioGroup = (RadioGroup) this.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.bluetoothRadio) {
                    toggleEditField(macAddress, true);
                    toggleEditField(portNumber, false);
                    toggleEditField(ipDNSAddress, false);
                } else {
                    toggleEditField(portNumber, true);
                    toggleEditField(ipDNSAddress, true);
                    toggleEditField(macAddress, false);
                }
            }
        });
    }

    private void toggleEditField(EditText editText, boolean set) {
        /*
         * Note: Disabled EditText fields may still get focus by some other means, and allow text input.
         *       See http://code.google.com/p/android/issues/detail?id=2771
         */
        editText.setEnabled(set);
        editText.setFocusable(set);
        editText.setFocusableInTouchMode(set);
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

    private boolean isBluetoothSelected() {
        return btRadioButton.isChecked();
    }

    public ZebraPrinter connect() {
        setStatus("Connecting...", Color.YELLOW);
        printerConnection = null;
        if (isBluetoothSelected()) {
            printerConnection = new BluetoothConnection(getMacAddressFieldText());
          //  SettingsHelper.saveBluetoothAddress(this, getMacAddressFieldText());
        } else {
            try {
                int port = Integer.parseInt(getTcpPortNumber());
                printerConnection = new TcpConnection(getTcpAddress(), port);
            //    SettingsHelper.saveIp(this, getTcpAddress());
              //  SettingsHelper.savePort(this, getTcpPortNumber());
            } catch (NumberFormatException e) {
                setStatus("Port Number Is Invalid", Color.RED);
                return null;
            }
        }

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

    private String getTcpAddress() {
        return ipDNSAddress.getText().toString();
    }

    private String getTcpPortNumber() {
        return portNumber.getText().toString();
    }

    private void doConnectionTest() {
        printer = connect();
        if (printer != null) {
            sendTestLabel();
        } else {
            disconnect();
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
        PrinterLanguage printerLanguage = printer.getPrinterControlLanguage();

        byte[] configLabel = null;
        if (printerLanguage == PrinterLanguage.ZPL) {
            configLabel = "^XA^FO17,16^GB379,371,8^FS^FT65,255^A0N,135,134^FDTEST^FS^XZ".getBytes();
        } else if (printerLanguage == PrinterLanguage.CPCL) {
            String cpclConfigLabel = "! 0 200 200 2436 1\n" +
                    "PW 830\n" +
                    "TONE 0\n" +
                    "SPEED 0\n" +
                    "ON-FEED IGNORE\n" +
                    "NO-PACE\n" +
                    "BAR-SENSE\n" +
                    "T 5 1 487 24 Maynilad Water Services Inc\n" +
                    "T 5 1 487 72 MWSS Compound\n" +
                    "T 5 1 487 120 Katipunan Road, Balara Q C\n" +
                    "T 4 0 68 112 Zebra Hlv(4-0) 10.0 Pt\n" +
                    "T 4 1 61 192 Zebra Hlv (4-1) 16.7 Pt\n" +
                    "T 7 0 140 450 Zebra Monaco (7-0) 8.5 Pt\n" +
                    "T 0 2 192 336 Zebra Standard 0-2 pt\n" +
                    "T 5 2 91 533 Zebra Time 5-2 16.3 Pt\n" +
                    "T 0 5 163 725 Zebra Standard 0-5 18.5 Pt\n" +
                    "T 7 1 182 626 Zebra Monaco 7-1 17.0 Pt\n" +
                    "T 0 6 44 1298 Zebra Standard 0-6 18.5\n" +
                    "T 0 5 38 1201 Zebra Standard 0-5 18.5\n" +
                    "T 0 4 53 910 Zebra Standard 0-4 18.5\n" +
                    "T 0 3 24 1118 Zebra Standard 0-3 18.5\n" +
                    "T 0 2 38 998 Zebra Standard 0-2 18.5\n" +
                    "PRINT\n";
            configLabel = cpclConfigLabel.getBytes();
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
