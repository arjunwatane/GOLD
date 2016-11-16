package com.glucose.arjunwatane.gold_v1;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class BluetoothFragment extends Fragment {

    // Victor Code
    private static final String TAG = "BluetoothFragment";
    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter btAdapter = null;
    Set<BluetoothDevice> pairedDevices = null;
    ArrayList<String> deviceList;
    ArrayList<String> deviceAddress;
    // ArrayAdapter<String> deviceList;
    ConnectThread connectThread;
    Handler mHandler;
    private Boolean btConnected = false;

    Button  btnCT, btnDC, btnSA;
    // ListView lvDeviceList;
    TextView tvMsg, tvDev;

    private static final UUID MY_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    private static final int MESSAGE_READ = 0;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        deviceList =  new ArrayList<>();
        deviceAddress = new ArrayList<>();
        // deviceList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, devices);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_bluetooth, container, false);
        btnCT = (Button) rootView.findViewById(R.id.btnCT);
        btnDC = (Button) rootView.findViewById(R.id.btnDC);
        btnSA = (Button) rootView.findViewById(R.id.btnSA);
        tvDev = (TextView) rootView.findViewById(R.id.tvDev);
        tvMsg = (TextView) rootView.findViewById(R.id.tvMsg);
        btnDC.setEnabled(false);
        btnSA.setEnabled(false);

        mHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                if(msg.what == MESSAGE_READ)
                {
                    String readMessage = msg.obj.toString();
                    tvMsg.setText(readMessage);
                    tvMsg.refreshDrawableState();
                }
            }
        };

        btnCT.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                connectBluetooth();
            }
        });

        btnDC.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                disconnectBluetooth();
                Toast.makeText(getActivity(), "Bluetooth Disconnected", Toast.LENGTH_SHORT).show();
            }
        });

        btnSA.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendData("A");
                Toast.makeText(getActivity(), "Data Sent", Toast.LENGTH_SHORT).show();

            }
        });



        return rootView;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if(btConnected) // added this clause to disconnect BT on pause
        {
            disconnectBluetooth();
        }

    }

    private void sendData(String a)
    {
        connectThread.connectedThread.write("a".getBytes());
    }

    private void disconnectBluetooth()
    {
        connectThread.connectedThread.cancel();
        connectThread.cancel();
        btnDC.setEnabled(false);
        btnSA.setEnabled(false);
        btnCT.setEnabled(true);
    }

    private void connectBluetooth()
    {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if(btAdapter == null)
        {
            Toast.makeText(getActivity(), "Bluetooth is not supported on this device.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(!btAdapter.isEnabled())
            {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                btnCT.setEnabled(false);
            }
            else
            {
                pairedDevices = btAdapter.getBondedDevices();
                if(pairedDevices.size() > 0)
                {
                    for(BluetoothDevice device : pairedDevices)
                    {
                        deviceList.add(device.getName() + "\n" + device.getAddress());
                        deviceAddress.add(device.getAddress());
                        tvDev.setText(deviceList.get(0));
                        tvDev.refreshDrawableState();
                        // deviceList.notifyDataSetChanged();
                        // lvDeviceList.setAdapter(deviceList);
                    }
                }
                // need to move this inside the if loop
                BluetoothDevice remoteDevice = btAdapter.getRemoteDevice(deviceAddress.get(0));
                connectThread = new ConnectThread(remoteDevice);
                connectThread.start();
                btnDC.setEnabled(true);
                btnSA.setEnabled(true);
                btnCT.setEnabled(false);
                btConnected = true;
                Toast.makeText(getActivity(), "Bluetooth is now enabled.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == REQUEST_ENABLE_BT)
        {
            if(resultCode == RESULT_OK)
            {
                pairedDevices = btAdapter.getBondedDevices();
                if(pairedDevices.size() > 0)
                {
                    for(BluetoothDevice device : pairedDevices)
                    {
                        deviceList.add(device.getName() + "\n" + device.getAddress());
                        deviceAddress.add(device.getAddress());
                        tvDev.setText(deviceList.get(0));
                        tvDev.refreshDrawableState();
                    }
                    BluetoothDevice remoteDevice = btAdapter.getRemoteDevice(deviceAddress.get(0)); // make this more elegant, if the user has more than one device paired, this could fail for our purposes
                    connectThread = new ConnectThread(remoteDevice);
                    connectThread.start();
                    // need to introduce some delay for BT to connect here
                    btnDC.setEnabled(true);
                    btnSA.setEnabled(true);
                    btnCT.setEnabled(false);
                    btConnected = true;
                    Toast.makeText(getActivity(), "Bluetooth is now enabled.", Toast.LENGTH_SHORT).show();
                }

            }
            if(resultCode == RESULT_CANCELED)
            {
                Toast.makeText(getActivity(), "Bluetooth needs to be enabled to continue.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        ConnectedThread connectedThread;

        public ConnectThread(BluetoothDevice device) {
            currentThread().setName("ConnectedThread");
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) { }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            btAdapter.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }

            // Do work to manage the connection (in a separate thread)
            connectedThread = new ConnectedThread(mmSocket);
            connectedThread.start();

        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }

    }

    public class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {

            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public synchronized void run() {
            currentThread().setName("ConnectedThread");
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()
            int availableBytes = 0;

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    availableBytes = mmInStream.available();
                    if(availableBytes > 0)
                    {

                        buffer = new byte[availableBytes];
                        Log.d("Before buffer read;", new String(buffer));
                        bytes = mmInStream.read(buffer);
                        Log.d("After buffer read;", new String(buffer));
                        // Wrap the byte array as a float number ordering the bytes using little endian.
                        float test = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getFloat();
                        String s = Float.toString(test);

                        // Send the obtained bytes to the UI activity
                        mHandler.obtainMessage(MESSAGE_READ, bytes, -1, s).sendToTarget();
                    }
                    else
                    {
                        Thread.sleep(1000);
                    }


                } catch (IOException e) {
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }
}
