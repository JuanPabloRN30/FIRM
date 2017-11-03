package com.example.juanpablorn30.firm;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;

public class AlarmaDetailActivity extends AppCompatActivity implements View.OnClickListener{

    private String TAG = "AlarmaDetailActivity";
    private EditText time;
    private ListView listView;
    private ArrayAdapter<String> mArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarma_detail);

        time = findViewById(R.id.etTime);
        time.setOnClickListener(this);
        listView = findViewById(R.id.color_list);
        cargarColores();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    private void cargarColores(){
        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
        mArrayAdapter.add(new String("blanco"));
        mArrayAdapter.add(new String("azul"));
        mArrayAdapter.add(new String("verde"));
        mArrayAdapter.add(new String("morado"));
        mArrayAdapter.add(new String("amarillo"));
        mArrayAdapter.add(new String("rojo"));
        mArrayAdapter.add(new String("celeste"));
        listView.setAdapter(mArrayAdapter);
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
                  time.setText(selectedHour + ":" + selectedMinute);
              }
          }, hour, minute, true);//Yes 24 hour time
          mTimePicker.setTitle("Select Time");
          mTimePicker.show();
      }else if(id == R.id.btnGuardarAlarma){
          Intent intent = new Intent(getBaseContext(), AlarmasActivity.class);
          startActivity(intent);
      }
    }

}
