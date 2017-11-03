package com.example.juanpablorn30.firm;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    //https://developer.android.com/guide/topics/connectivity/bluetooth.html
    //https://github.com/omaflak/Bluetooth-Terminal/blob/master/app/src/main/java/me/aflak/bluetoothterminal/Scan.java
    //https://developer.android.com/reference/android/bluetooth/BluetoothDevice.html
    //https://developer.android.com/guide/topics/ui/dialogs.html
    private String TAG = "BluetoothActivity";

    private LinearLayout linearLayout;
    private ListView list_devices;
    private ArrayAdapter<String> mArrayAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    //private final UUID MY_UUID = UUID.randomUUID();
    private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private List<BluetoothDevice> devices;


    private int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        devices = new ArrayList<>();

        list_devices = findViewById(R.id.bluetooth_list_devices);
        list_devices.setOnItemClickListener(this);

        iniciarConfiguracion();
    }

    private void iniciarConfiguracion(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null){
            Snackbar.make(list_devices, "Este dispositivo no tiene bluetooth", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }else{
            if(!mBluetoothAdapter.isEnabled()){
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }else{
                cargarInformacion();
            }
        }
    }

    private void cargarInformacion(){
        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                devices.add(device);
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
        list_devices.setAdapter(mArrayAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case RESULT_OK:
                cargarInformacion();
                Snackbar.make(list_devices, "Bluetooth Encendido", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            break;

            case RESULT_CANCELED:
                Snackbar.make(list_devices, "Bluetooth Apagado", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ConnectThread connectThread = new ConnectThread();
        connectThread.execute(devices.get(i));
    }

    private void manageConnectedSocket(BluetoothSocket mSocket){
        try{
            Log.i(TAG, "Conectado de manera bien");
            MessageThread messageThread = new MessageThread();
            messageThread.execute(mSocket);
            byte[] bytes = new byte[10];
            for(int i = 0 ; i < bytes.length ; i++)
                bytes[i] = 0;
            bytes[0] = 1;
            bytes[bytes.length-1] = 1;
            messageThread.write(bytes);
        }catch (Exception e){
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
        }

    }

    private class ConnectThread extends AsyncTask<BluetoothDevice, Void, BluetoothSocket>{

        @Override
        protected BluetoothSocket doInBackground(BluetoothDevice... bluetoothDevices) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            BluetoothDevice mmDevice = bluetoothDevices[0];
            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) { }
            return tmp;
        }

        @Override
        protected void onPostExecute(BluetoothSocket bluetoothSocket) {
            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                bluetoothSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                Snackbar.make(list_devices, "No se ha podido conectar con el dispositivo", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                try {
                    bluetoothSocket.close();
                } catch (IOException closeException) { }
                return;
            }

            // Do work to manage the connection (in a separate thread)
            manageConnectedSocket(bluetoothSocket);
        }
    }

    private class MessageThread extends AsyncTask<BluetoothSocket, Void, Void> {
        private BluetoothSocket mmSocket;
        private InputStream mmInStream;
        private OutputStream mmOutStream;

        @Override
        protected Void doInBackground(BluetoothSocket... bluetoothSockets) {
            mmSocket = bluetoothSockets[0];
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = mmSocket.getInputStream();
                tmpOut = mmSocket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
            if(mmOutStream == null)
                Log.d(TAG, "Estoy en null");
            else
                Log.d(TAG, "NO Estoy en null");
            Log.d(TAG, mmOutStream.toString());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    Log.d(TAG, String.valueOf(bytes));
                    if (bytes > 0) {
                        String str = new String(buffer, "UTF-8");
                        Log.d(TAG, str);
                        Log.d(TAG, String.valueOf(str.length()));
                    } else {
                        Log.d(TAG, "No he recibido nada");
                    }

                    // Send the obtained bytes to the UI activity
                    /*mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                            .sendToTarget();*/
                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            if(mmOutStream == null){
                Log.d(TAG, "Estoy en null");
            }
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Log.d(TAG, e.getMessage());
            }
        }
    }

}
