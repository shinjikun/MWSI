package com.indra.rover.mwsi.adapters;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.indra.rover.mwsi.R;

import java.util.List;



public class DiscoveredPrinterAdapter extends ArrayAdapter<BluetoothDevice> {


    Context context;
    public DiscoveredPrinterAdapter(Context context, int resource, List<BluetoothDevice> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.list_item_discovered_printer, null);
        }

        BluetoothDevice btDevice = getItem(position);
        TextView txt = (TextView) convertView.findViewById(R.id.printerName);
        txt.setText(btDevice.getName());

        txt = (TextView) convertView.findViewById(R.id.printerAddress);
        txt.setText(btDevice.getAddress());

        ImageView imgView =  (ImageView)convertView.findViewById(R.id.imgBt);
        int bondState =  btDevice.getBondState();

        if(bondState == BluetoothDevice.BOND_BONDED) {
            imgView.setBackgroundResource(R.drawable.ic_bluetooth_connected);
        }
        else {
                imgView.setBackgroundResource(R.drawable.ic_bluetooth);

            }
        return convertView;
    }
}
