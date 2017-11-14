package com.example.juanpablorn30.firm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ContactosEmergenciaActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "ContactosEmergenciaActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos_emergencia);

        findViewById(R.id.btnMedico).setOnClickListener(this);
        findViewById(R.id.btnGuardian).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.btnMedico){
            Intent intent = new Intent(getBaseContext(), ContactoEmergenciaDetailActivity.class);
            intent.putExtra("tipo", "Médico");
            startActivity(intent);
        }else if(id == R.id.btnGuardian){
            Intent intent = new Intent(getBaseContext(), ContactoEmergenciaDetailActivity.class);
            intent.putExtra("tipo", "Guardián");
            startActivity(intent);
        }
    }
}
