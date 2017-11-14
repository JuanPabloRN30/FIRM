package com.example.juanpablorn30.firm;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    //https://developer.android.com/guide/topics/connectivity/bluetooth.html
    private String TAG = "BluetoothActivity";

    private ListView list_devices;
    private ArrayAdapter<String> mArrayAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private List<BluetoothDevice> devices;
    private int REQUEST_ENABLE_BT = 1;
    private BluetoothDevice bluetoothDevice;

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
                finish();
            break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("device", bluetoothDevice);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent2 = new Intent(getBaseContext(), BluetoothService.class);
        intent2.putExtra("device" , devices.get(i));
        startService(intent2);
        bluetoothDevice = devices.get(i);
    }
}
