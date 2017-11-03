package com.example.juanpablorn30.firm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ContactoEmergenciaDetailActivity extends AppCompatActivity {

    private String tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto_emergencia_detail);

        tipo = getIntent().getStringExtra("tipo");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contacto_emergencia, menu);
        getSupportActionBar().setTitle(tipo);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemClicked = item.getItemId();
        if(itemClicked == R.id.menu_llamar){
            //Codigo para llamar
        }
        return super.onOptionsItemSelected(item);
    }
}
