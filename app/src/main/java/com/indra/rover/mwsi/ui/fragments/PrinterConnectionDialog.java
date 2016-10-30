package com.indra.rover.mwsi.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.indra.rover.mwsi.R;
import com.indra.rover.mwsi.adapters.DiscoveredPrinterAdapter;
import com.indra.rover.mwsi.print.utils.BluetoothHelper;


import java.util.ArrayList;

public class PrinterConnectionDialog extends DialogFragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "Test";

     TextView emptyView;
    DiscoveredPrinterAdapter adapter;
    private ArrayList<BluetoothDevice> discoveredPrinters;
    AlertDialog  dialog;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.i(TAG, "onCreateDialog()");

        View view = View.inflate(getActivity(), R.layout.dialog_printer_connect, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setView(view);


        BluetoothHelper helper = BluetoothHelper.instance();
        discoveredPrinters = helper.getPairedDevices();
        adapter = new DiscoveredPrinterAdapter(getActivity(), R.layout.list_item_discovered_printer, discoveredPrinters);

        emptyView = (TextView) view.findViewById(R.id.discoveredPrintersEmptyView);

        ListView discoveredPrintersListView = (ListView) view.findViewById(R.id.discoveredPrintersListView);
        discoveredPrintersListView.setEmptyView(emptyView);
        discoveredPrintersListView.setAdapter(adapter);

        dialog = builder.create();

        discoveredPrintersListView.setOnItemClickListener(this);

        startSearching();
        return dialog;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        BluetoothHelper btHelper =  BluetoothHelper.instance();

        BluetoothHelper.instance().connectTo(discoveredPrinters.get(i));
        btHelper.bluetoothSelected(discoveredPrinters.get(i));


        dialog.dismiss();
    }


    private void startSearching() {
        Log.i("Log", "in the start searching method");
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getActivity().registerReceiver(myReceiver, intentFilter);
        BluetoothHelper.instance().getBluetoothAdapter().startDiscovery();
    }


    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
           // Message msg = Message.obtain();
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if(discoveredPrinters.size()<1) // this checks if the size of bluetooth device is 0,then add the
                {                                           // device to the arraylist.
                 //   adapter.add(device);
                    discoveredPrinters.add(device);
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    boolean flag = true;    // flag to indicate that particular device is already in the arlist or not
                    for(int i = 0; i<discoveredPrinters.size();i++)
                    {
                        if(device.getAddress().equals(discoveredPrinters.get(i).getAddress()))
                        {
                            flag = false;
                        }
                    }
                    if(flag)
                    {
                       // adapter.add(device);
                        discoveredPrinters.add(device);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
            /*else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {

            }
            */
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                 if(discoveredPrinters.isEmpty()){
                     Toast.makeText(context, "No Bluetooth Device Found!", Toast.LENGTH_LONG).show();
                     dialog.dismiss();
                 }
            }
        }
    };

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(myReceiver);
        super.onDestroy();
    }
}
