package com.example.juanpablorn30.firm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MenuUsuarioActivity extends AppCompatActivity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_usuario);

        findViewById(R.id.btnSincronizarBluetooth).setOnClickListener(this);
        findViewById(R.id.btnAlarmas).setOnClickListener(this);
        findViewById(R.id.btnContactosEmergencia).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_general_menu_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemClicked = item.getItemId();
        if(itemClicked == R.id.it_exit_menu){
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if(i == R.id.btnSincronizarBluetooth){
            Intent intent = new Intent(getBaseContext(), BluetoothActivity.class);
            startActivity(intent);
        }else if(i == R.id.btnAlarmas){
            Intent intent = new Intent(getBaseContext(), AlarmasActivity.class);
            startActivity(intent);
        }else if(i == R.id.btnContactosEmergencia){
            Intent intent = new Intent(getBaseContext(), ContactosEmergenciaActivity.class);
            startActivity(intent);
        }
    }
}
