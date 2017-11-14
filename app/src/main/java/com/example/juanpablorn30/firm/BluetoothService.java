package com.example.juanpablorn30.firm;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class BluetoothService extends Service {

    private String TAG = "BluetoothService";
    private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private InputStream inputStream;
    private OutputStream outputStream;
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder{
        BluetoothService getService(){
            return BluetoothService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void sendMessage(View parentView, String message){
        if(outputStream == null){
            Snackbar.make(parentView, "Aún no se ha conectado por bluetooth", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }else{
            try {
                outputStream.write(message.getBytes());
                Snackbar.make(parentView, "Se han enviado los datos.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            } catch (IOException e) {
                //Snackbar.make(parentView, "NO pude: " + message, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Log.d(TAG, "No se logro enviar, ocurrio un problema.");
                e.printStackTrace();
            }
        }
    }

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    bytes = inputStream.read(buffer);
                    if (bytes > 0) {
                        String str = new String(buffer, "UTF-8");
                        String real_str = "";
                        for(int i = 0 ; i < bytes ; i++)
                            real_str += str.charAt(i);
                        Log.d(TAG, "Recibi: " + real_str);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);
        }
    }

    public BluetoothService() {
    }

    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                10);
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.getExtras().getParcelable("device") == null){
            Log.d(TAG, "Entre aca");
        }else{
            try {
                BluetoothDevice device = intent.getExtras().getParcelable("device");
                BluetoothSocket bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                bluetoothSocket.connect();
                Toast.makeText(getBaseContext(), "Conectado", Toast.LENGTH_SHORT).show();
                inputStream = bluetoothSocket.getInputStream();
                outputStream = bluetoothSocket.getOutputStream();
                Message msg = mServiceHandler.obtainMessage();
                Date now = new Date(System.currentTimeMillis());
                SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
                String send_message = "2" + sdf.format(now);
                outputStream.write(send_message.getBytes());
                msg.arg1 = startId;
                mServiceHandler.sendMessage(msg);

            } catch (IOException e) {
                Toast.makeText(getBaseContext(), "No se puede conectar!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        return START_STICKY;
    }
}
