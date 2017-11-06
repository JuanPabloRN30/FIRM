package com.example.juanpablorn30.clases;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by juanpablorn30 on 5/11/17.
 */

public class Paciente {

    private String correo;
    private String nombre;
    private String numero_celular;
    private Map<String, Alarma> alarmas;
    private Medico medico;
    private Guardian guardian;

    public Paciente() {
    }

    public Paciente(String name, String email, Date date_birth, String cellphone) {
        this.nombre = name;
        this.correo = email;
        this.numero_celular = cellphone;
        alarmas = new HashMap<>();
        medico = new Medico();
        guardian = new Guardian();
    }

    public String getNombre() {
        return nombre == null ? "" : nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo == null ? "" : correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNumero_celular() {
        return numero_celular == null ? "" : numero_celular;
    }

    public void setNumero_celular(String numero_celular) {
        this.numero_celular = numero_celular;
    }

    public Map<String, Alarma> getAlarmas() {
        return alarmas;
    }

    public void setAlarmas(Map<String, Alarma> alarmas) {
        this.alarmas = alarmas;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Guardian getGuardian() {
        return guardian;
    }

    public void setGuardian(Guardian guardian) {
        this.guardian = guardian;
    }

}
