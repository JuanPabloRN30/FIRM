package com.example.juanpablorn30.clases;

/**
 * Created by juanpablorn30 on 5/11/17.
 */

public class Medico extends Persona {

    public Medico() {
    }

    public Medico(String nombre, String correo, String numero_celular) {
        super(nombre, correo, numero_celular);
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
}
