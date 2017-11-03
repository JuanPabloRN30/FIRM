package com.example.juanpablorn30.firm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class AlarmasActivity extends AppCompatActivity {

    private ListView listView;
    private List<String> colores;
    private List<String> colores_seleccionados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmas);
        listView = findViewById(R.id.alarmas_list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_general_menu_drawer, menu);
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
