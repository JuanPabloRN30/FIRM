package com.example.juanpablorn30.firm;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.juanpablorn30.clases.Guardian;
import com.example.juanpablorn30.clases.Medico;
import com.example.juanpablorn30.clases.Paciente;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ContactoEmergenciaDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "ContactoEmergenciaDetailActivity";


    private final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private String tipo;
    private String nombre_prev, email_prev, numero_celular_prev, parentezco_prev;

    private DatabaseReference mCurrentUserReference;

    private View parentLayout;
    private EditText nombre;
    private EditText email;
    private EditText numero_celular;
    private EditText parentezco;
    private Button save;
    private Button cancel;
    private Button edit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto_emergencia_detail);

        mCurrentUserReference = FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        tipo = getIntent().getStringExtra("tipo");

        parentLayout = findViewById(R.id.parentLinear_contactoEmergenciaDetail);
        nombre = findViewById(R.id.et_contactoEmergenciaDetail_name);
        email = findViewById(R.id.et_contactoEmergenciaDetail_email);
        numero_celular = findViewById(R.id.et_contactoEmergenciaDetail_cell);
        parentezco = findViewById(R.id.et_contactoEmergenciaDetail_parentezco);

        save = findViewById(R.id.btn_contactoEmergenciaDetail_save);
        cancel = findViewById(R.id.btn_contactoEmergenciaDetail_cancel);
        edit = findViewById(R.id.btn_contactoEmergenciaDetail_edit);

        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
        edit.setOnClickListener(this);

        if (tipo.equals("Médico")) {
            findViewById(R.id.linear_contactoEmergenciaDetail_parentezco).setVisibility(View.GONE);
            cargarDatosMedico();
        } else {
            cargarDatosGuardian();
        }

        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
            askPermissionCall();
    }

    private void askPermissionCall(){
        if(ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE))
            {
                Snackbar.make(parentLayout, "Se necesita el permiso para llamar!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch(requestCode)
        {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE:
            {
                if(!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    Snackbar.make(parentLayout, "No se puede llamar", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
            break;
        }
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_CALL_PHONE:
                if(resultCode != RESULT_OK) {
                    Snackbar.make(parentLayout, "Sin acceso a llamadas", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                break;
        }
    }

    private void cargarDatosMedico() {
        ValueEventListener medicoListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Paciente current = dataSnapshot.getValue(Paciente.class);
                Medico medico_paciente = current.getMedico();
                if (medico_paciente != null) {
                    if (!medico_paciente.getNombre().isEmpty()) {
                        nombre_prev = medico_paciente.getNombre();
                        nombre.setText(medico_paciente.getNombre());
                    }
                    if (!medico_paciente.getCorreo().isEmpty()) {
                        email_prev = medico_paciente.getCorreo();
                        email.setText(medico_paciente.getCorreo());
                    }
                    if (!medico_paciente.getNumero_celular().isEmpty()) {
                        numero_celular_prev = medico_paciente.getNumero_celular();
                        numero_celular.setText(medico_paciente.getNumero_celular());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mCurrentUserReference.addListenerForSingleValueEvent(medicoListener);
    }

    private void cargarDatosGuardian() {
        ValueEventListener guardianListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Paciente current = dataSnapshot.getValue(Paciente.class);
                Guardian guardian_paciente = current.getGuardian();
                if (guardian_paciente != null) {
                    if (!guardian_paciente.getNombre().isEmpty()) {
                        nombre_prev = guardian_paciente.getNombre();
                        nombre.setText(guardian_paciente.getNombre());
                    }
                    if (!guardian_paciente.getCorreo().isEmpty()) {
                        email_prev = guardian_paciente.getCorreo();
                        email.setText(guardian_paciente.getCorreo());
                    }
                    if (!guardian_paciente.getNumero_celular().isEmpty()) {
                        numero_celular_prev = guardian_paciente.getNumero_celular();
                        numero_celular.setText(guardian_paciente.getNumero_celular());
                    }
                    if (!guardian_paciente.getParentezco().isEmpty()) {
                        parentezco_prev = guardian_paciente.getParentezco();
                        parentezco.setText(guardian_paciente.getParentezco());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mCurrentUserReference.addListenerForSingleValueEvent(guardianListener);
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
        if (itemClicked == R.id.menu_llamar) {
            if (numero_celular.getText().length() == 0) {
                Snackbar.make(parentLayout, "No se ha definido el telefono", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            } else {
                Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                phoneIntent.setData(Uri.parse("tel:" + numero_celular.getText().toString()));
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(parentLayout, "No se han dado los permisos para llamar", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    return super.onOptionsItemSelected(item);
                }
                startActivity(phoneIntent);
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_contactoEmergenciaDetail_edit) {
            cancel.setEnabled(true);
            save.setEnabled(true);
            edit.setEnabled(true);
            nombre.setEnabled(true);
            email.setEnabled(true);
            numero_celular.setEnabled(true);
            parentezco.setEnabled(true);
        } else if (i == R.id.btn_contactoEmergenciaDetail_cancel) {
            cancel.setEnabled(true);
            save.setEnabled(false);
            edit.setEnabled(true);
            nombre.setEnabled(false);
            email.setEnabled(false);
            numero_celular.setEnabled(false);
            parentezco.setEnabled(false);
            nombre.setText(nombre_prev);
            email.setText(email_prev);
            numero_celular.setText(numero_celular_prev);
            parentezco.setText(parentezco_prev);
        } else if (i == R.id.btn_contactoEmergenciaDetail_save) {
            Map<String, Object> updates = new HashMap<>();
            if (!nombre.getText().toString().isEmpty()){
                nombre_prev = nombre.getText().toString();
                updates.put("nombre", nombre.getText().toString());
            }
            if (!email.getText().toString().isEmpty()){
                email_prev = email.getText().toString();
                updates.put("correo", email.getText().toString());
            }
            if (!numero_celular.getText().toString().isEmpty()){
                numero_celular_prev = numero_celular.getText().toString();
                updates.put("numero_celular", numero_celular.getText().toString());
            }
            if (!parentezco.getText().toString().isEmpty()) {
                parentezco_prev = parentezco.getText().toString();
                updates.put("parentezco", parentezco.getText().toString());
            }
            String child;
            if(tipo.equals("Médico")){
                child = "medico";
            }else{
                child = "guardian";
            }
            if (!updates.isEmpty()) {
                FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(child).updateChildren(updates);
                Snackbar.make(view, "¡Se han guardado los cambios!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
            cancel.setEnabled(true);
            save.setEnabled(false);
            edit.setEnabled(true);
            nombre.setEnabled(false);
            email.setEnabled(false);
            numero_celular.setEnabled(false);
            parentezco.setEnabled(false);
        }
    }
}
