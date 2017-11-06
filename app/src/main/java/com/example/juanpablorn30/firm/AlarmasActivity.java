package com.example.juanpablorn30.firm;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AlarmasActivity extends AppCompatActivity {

    private String TAG = "AlarmasActivity";

    private DatabaseReference mCurrentUserReference;
    private ValueEventListener mPacientesListener;

    private ListView listView;
    private TextView txtNoAlarmas;
    private List<Alarma> alarmas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmas);
        listView = findViewById(R.id.alarmas_list);
        txtNoAlarmas = findViewById(R.id.txtAlarmasNoAlarmas);

        mCurrentUserReference = FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        alarmas = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
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
    }

    private void cargarAlarmas(){
        ListAlarmAdapter adapter = new ListAlarmAdapter(alarmas, getBaseContext());
        listView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista_alarmas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemClicked = item.getItemId();
        if(itemClicked == R.id.add){
            Intent intent = new Intent(getBaseContext(), AlarmaDetailActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
