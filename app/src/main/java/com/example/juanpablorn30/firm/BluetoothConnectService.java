package com.example.juanpablorn30.firm;

import android.app.IntentService;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class BluetoothConnectService extends IntentService {

    private String TAG = "BluetoothConnectService";
    private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public BluetoothConnectService() {
        super("BluetoothConnectService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        BluetoothDevice device = intent.getExtras().getParcelable("device");
        Log.d(TAG, device.getName());
        String message = intent.getStringExtra("send_message");
        Log.d(TAG, message);
        BluetoothSocket bluetoothSocket = null;
        try {
            bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
            bluetoothSocket.connect();
            OutputStream outputStream = bluetoothSocket.getOutputStream();
            outputStream.write(message.getBytes());
            Log.d(TAG, "No tuve problemas");
        } catch (IOException connectException) {
            Log.d(TAG, "Tuve problemas");
            Log.d(TAG, connectException.getMessage());
            connectException.printStackTrace();
            try {
                bluetoothSocket.close();
            } catch (IOException closeException) { }
            return;
        }
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
