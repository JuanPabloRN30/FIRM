package com.example.juanpablorn30.firm;

import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.juanpablorn30.clases.Alarma;
import com.example.juanpablorn30.clases.ListAlarmAdapter;
import com.example.juanpablorn30.clases.ListColorAdapter;
import com.example.juanpablorn30.clases.Paciente;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AlarmasActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "AlarmasActivity";

    private DatabaseReference mCurrentUserReference;
    private ValueEventListener mPacientesListener;

    private ListView listView;
    private TextView txtNoAlarmas;
    private List<Alarma> alarmas;

    private float historicX;
    private int itemSelected;
    private View viewSelected;
    private static final int DELTA = 50;

    private BluetoothService bluetoothService;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmas);
        listView = findViewById(R.id.alarmas_list);
        txtNoAlarmas = findViewById(R.id.txtAlarmasNoAlarmas);

        mCurrentUserReference = FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        alarmas = new ArrayList<>();
        findViewById(R.id.btn_alarmas_send).setOnClickListener(this);

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        historicX = motionEvent.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        if(motionEvent.getX() - historicX < -DELTA){
                            deleteRow();
                        }
                    break;
                }
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemSelected = i;
                viewSelected = view;
            }
        });
    }



    private void deleteRow(){
        Log.d(TAG, "Entre");
        /*final Animation animation = AnimationUtils.loadAnimation(getBaseContext(), android.R.anim.slide_in_left);
        viewSelected.startAnimation(animation);
        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {

            @Override
            public void run() {
                alarmas.remove(itemSelected);
                ListAlarmAdapter adapter = new ListAlarmAdapter(alarmas, getBaseContext());
                listView.setAdapter(adapter);
                animation.cancel();
            }
        },1000);*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, BluetoothService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        ValueEventListener coloresListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                alarmas.clear();
                Paciente current = dataSnapshot.getValue(Paciente.class);
                if (current.getAlarmas() != null) {
                    Set<String> keys = current.getAlarmas().keySet();
                    for (String aux : keys) {
                        alarmas.add(current.getAlarmas().get(aux));
                    }
                    cargarAlarmas();
                    txtNoAlarmas.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                } else {
                    txtNoAlarmas.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mCurrentUserReference.addValueEventListener(coloresListener);
        mPacientesListener = coloresListener;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mPacientesListener!= null) mCurrentUserReference.removeEventListener(mPacientesListener);
        if(mBound){
            unbindService(mConnection);
            mBound = false;
        }
    }

    private void cargarAlarmas(){
        ListAlarmAdapter adapter = new ListAlarmAdapter(alarmas, getBaseContext());
        listView.setAdapter(adapter);
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            BluetoothService.LocalBinder binder = (BluetoothService.LocalBinder) service;
            bluetoothService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista_alarmas, menu);
        getSupportActionBar().setTitle("Lista Alarmas");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemClicked = item.getItemId();
        if(itemClicked == R.id.add){
            if(alarmas.size() == 7){
                Snackbar.make(listView, "Ya se han agregado la máxima cantidad de alarmas.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }else{
                Intent intent = new Intent(getBaseContext(), AlarmaDetailActivity.class);
                startActivity(intent);

            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.btn_alarmas_send){
            String send_message = "1" + alarmas.size();
            for(Alarma aux : alarmas){
                for(int i = 0 ; i < 3 ; i++){
                    if(aux.getColor().get(i) == 255)
                        send_message += "1";
                    else
                        send_message += "0";
                }
            }
            for(Alarma aux : alarmas){
                Date aux_date = new Date(aux.getHora());
                SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
                send_message += sdf.format(aux_date);
            }
            if(mBound){
                bluetoothService.sendMessage(view,send_message);
            }
        }
    }
}
