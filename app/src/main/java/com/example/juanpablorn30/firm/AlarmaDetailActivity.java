package com.example.juanpablorn30.firm;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;

import com.example.juanpablorn30.clases.Alarma;
import com.example.juanpablorn30.clases.ListColorAdapter;
import com.example.juanpablorn30.clases.Paciente;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AlarmaDetailActivity extends AppCompatActivity implements View.OnClickListener{

    private String TAG = "AlarmaDetailActivity";

    private DatabaseReference mCurrentUserReference;

    private EditText time;
    private ListView listView;
    private ArrayAdapter<String> mArrayAdapter;

    private int color_seleccionado;
    private View prev;

    private Set<List<Integer>> colores_totales;
    private List<List<Integer>> item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarma_detail);

        mCurrentUserReference = FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        color_seleccionado = -1;
        prev = null;

        findViewById(R.id.btnGuardarAlarma).setOnClickListener(this);

        time = findViewById(R.id.etTime);
        time.setOnClickListener(this);
        listView = findViewById(R.id.color_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(prev != null){
                    prev.findViewById(R.id.btnCheck).setVisibility(View.INVISIBLE);
                }
                prev = view;
                color_seleccionado = i;
                view.findViewById(R.id.btnCheck).setVisibility(View.VISIBLE);
            }
        });

        colores_totales = new HashSet<>();
        cargarColoresSeleccionados();
    }

    private void crearArregloColor(){
        try{
            Log.d(TAG, "Entre a crear la mascara");
            int lim = (1 << 3);
            for(int i = 1 ; i < lim ; i++){
                List<Integer> aux = new ArrayList<>();
                for(int j = 0 ; j < 3 ; j++){
                    if( (i & (1 << j)) != 0 ){
                        aux.add(255);
                    }else{
                        aux.add(0);
                    }
                }
                colores_totales.add(aux);
            }
        }catch (Exception e){
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    private void cargarColoresSeleccionados(){
        ValueEventListener coloresListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Paciente current = dataSnapshot.getValue(Paciente.class);
                crearArregloColor();
                if (current.getAlarmas() != null) {
                    Set<String> keys = current.getAlarmas().keySet();
                    for (String aux : keys) {
                        colores_totales.remove(current.getAlarmas().get(aux).getColor());
                    }
                }
                cargarColores();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mCurrentUserReference.addListenerForSingleValueEvent(coloresListener);
    }

    private void cargarColores(){
        try{
            Log.d(TAG, "Entre a cargar colores");
            Log.d(TAG, "EL tam es: " + colores_totales.size());
            item = new ArrayList<>();
            for(List<Integer> aux : colores_totales){
                item.add(aux);
            }
            ListColorAdapter adapter = new ListColorAdapter(item, getBaseContext());
            listView.setAdapter(adapter);
        }catch (Exception e){
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    private void guardarAlarma(View view){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date date = null;
        try {
            date = sdf.parse(time.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mCurrentUserReference.child("alarmas").push().setValue(new Alarma(date, item.get(this.color_seleccionado)));
        Snackbar.make(view, "Se ha creado la alarma exitosamente", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        Intent intent = new Intent(getBaseContext(), AlarmasActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
      int id = view.getId();
      if(id == R.id.etTime){
          Calendar mcurrentTime = Calendar.getInstance();
          int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
          int minute = mcurrentTime.get(Calendar.MINUTE);
          TimePickerDialog mTimePicker;
          mTimePicker = new TimePickerDialog(AlarmaDetailActivity.this, new TimePickerDialog.OnTimeSetListener() {
              @Override
              public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                  String texto = selectedHour + ":";
                  if(selectedMinute < 10) texto = texto + "0";
                  texto += selectedMinute;
                  time.setText(texto);
              }
          }, hour, minute, true);//Yes 24 hour time
          mTimePicker.setTitle("Seleccione la hora");
          mTimePicker.show();
      }else if(id == R.id.btnGuardarAlarma){
          if(time.getText().length() == 0 || color_seleccionado == -1){
              Snackbar.make(view, "No ha seleccionado color y/o hora", Snackbar.LENGTH_LONG).setAction("Action", null).show();
          }else {
              guardarAlarma(view);
          }
      }
    }

}
