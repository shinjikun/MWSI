package com.indra.rover.mwsi.print.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BluetoothHelper {

	private static final String TAG = "BluetoothHelper";
	private static final String[] SUPPORTED_DEVICES = { "APEX3","ZEBRA","HTML" };

	public enum BluetoothHelperEvent {
		NOT_SUPPORTED, NOT_ENABLED, CONNECTION_STABLISHED, CONNECTION_FAILED,CONNECTION_LOST;
	}

	private static BluetoothHelper instance;
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothHelperEventListener eventListener;
	private ConnectThread connection;
	private PrinterWriterThread writerThread;

	private BluetoothHelper() {
	}

	public static BluetoothHelper instance() {
		if (null == instance) {
			instance = new BluetoothHelper();
		}
		return instance;
	}

	public BluetoothAdapter getBluetoothAdapter() {
		// lazy instatiation for bluetooth adapter object
		if (mBluetoothAdapter == null) {
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		}
		return mBluetoothAdapter;
	}

	public ArrayList<BluetoothDevice> getPairedDevices() {
		ArrayList<BluetoothDevice> pairedDevices = null;
		if (getBluetoothAdapter() != null) {

			// Device does not support Bluetooth
			if (!mBluetoothAdapter.isEnabled() && eventListener != null) {
				eventListener.bluetoothEventChange(BluetoothHelperEvent.NOT_ENABLED);
				// notify in case bluetooth is not enable
			}

			// return the list of paired devices anyway
			pairedDevices = new ArrayList(getBluetoothAdapter().getBondedDevices());
		} else {
			eventListener.bluetoothEventChange(BluetoothHelperEvent.NOT_SUPPORTED);
			// notify there is not support for bluetooth
		}

		return pairedDevices;
	}

	public BluetoothHelper discover() {

		return this;
	}

	public BluetoothHelper setOnBuetoothHelperEventListener(BluetoothHelperEventListener eventListener) {
		this.eventListener = eventListener;
		return this;
	}

	public interface BluetoothHelperEventListener {
		 void bluetoothEventChange(BluetoothHelperEvent event);

		 void bluetoothConnectionStart(ConnectThread connection);

        void bluetoothSelected(BluetoothDevice bluetoothDevice);
	}

	/**
	 * Will test out wether a connection to a specific device can be stablished
	 * will not sent any data and will not keep the connection open for too
	 * long, it is only for the purposefs of ensure that the device is in there.
	 * 
	 * */
	public void connectTo(BluetoothDevice device) {
		ParcelUuid uuids[] = device.getUuids();
		if (uuids != null && uuids.length > 0) {
			UUID uuid = uuids[0].getUuid();// uuid to connect

			if (connection != null) // if there is another connection open
									// cancel that one
				connection.cancel();

			connection = new ConnectThread(device, uuid, mHandler);
			connection.start();

			mHandler.sendEmptyMessage(CONNECTION_START);

		} else {
			// we start fetching the uuids, remember to implement the proper
			// broadcast receiver to get
			// notify when new uuids were discovered
			device.fetchUuidsWithSdp();
		}
	}

	public void destroy() {
		if (connection != null) // if there is another connection open cancel
								// that one
			connection.cancel();
		instance = null; // we remove this instance
	}

	public static final int CONNECTED = 0, CONNECTION_LOST = -1, CONNECTION_FAILED = 2, CANCEL_DISCOVERY = 3,
			CONNECTION_START = 5, READ = 6, WRITE = 7;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CONNECTION_START:
				if (eventListener != null) {
					// notify that a connection started
					eventListener.bluetoothConnectionStart(connection);
				}
				break;

			case READ:
				byte[] readBuf = (byte[]) msg.obj;
				// construct a string from the valid bytes in the buffer
				String readMessage = new String(readBuf, 0, msg.arg1);
				Log.d(TAG, "Message: " + readMessage);
				break;
				
			case WRITE:

				break;

			case CONNECTED:

				BluetoothSocket socket = (BluetoothSocket) msg.obj;

				connection = null;

				startWritingThread(socket);

				eventListener.bluetoothEventChange(BluetoothHelperEvent.CONNECTION_STABLISHED);

				break;
			case CANCEL_DISCOVERY:
				if (getBluetoothAdapter().isDiscovering())
					// before doing a connect if an adapter was set to us we
					// cancel the discovery of any new devices
					getBluetoothAdapter().cancelDiscovery();
				break;
			case CONNECTION_LOST:
				Log.i(TAG,"Connection Lost before");
				if (eventListener != null) {
					Log.i(TAG,"Connection Lost");
					// notify that a connection started
					Log.e(TAG, "Notifying connection failed!");
					eventListener.bluetoothEventChange(BluetoothHelperEvent.CONNECTION_LOST);
				}
				Log.d(TAG, "Connection was lost");
				break;
			case CONNECTION_FAILED:
				Log.i(TAG,"Connection failed before");
				if (eventListener != null) {
					Log.i(TAG,"Connection Failed");
					// notify that a connection started
					Log.e(TAG, "Notifying connection failed!");
					eventListener.bluetoothEventChange(BluetoothHelperEvent.CONNECTION_FAILED);
				}
				if(connection!=null){
					connection.cancel();
					connection = null;
				}
				break;

			}
		}
	};

	public void startWritingThread(BluetoothSocket socket) {

		if (writerThread != null) {
			writerThread.cancel();
			writerThread = null;
		}

		writerThread = new PrinterWriterThread(socket, mHandler);
		writerThread.start();
	}
	
	public void sendData(byte[] data) {
		if (writerThread != null) {
			Log.d(TAG, "sent to writer Thread data");
			writerThread.write(data);
		}
	}


	public BluetoothDevice getBluetoothDevice(String macAddress){
        BluetoothDevice btSelected = null;
		 ArrayList<BluetoothDevice> arry =getPairedDevices();
		if(arry!=null){
			int size = arry.size();
			for(int  i=0;i<size;i++){
				BluetoothDevice btDevice = arry.get(i);
				if(btDevice.getAddress().equals(macAddress)){
					if(btDevice.getBondState() == BluetoothDevice.BOND_BONDED){
                        btSelected = btDevice;
						break;
					}

				}
			}
		}
		return btSelected;
	}

    public void bluetoothSelected(BluetoothDevice bluetoothDevice){
        if(eventListener != null) {
            eventListener.bluetoothSelected(bluetoothDevice);
        }
    }



    public void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unpairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
