package com.example.juanpablorn30.clases;

/**
 * Created by juanpablorn30 on 5/11/17.
 */

public class Persona {

    protected String nombre;
    protected String correo;
    protected String numero_celular;

    public Persona() {
    }

    public Persona(String nombre, String correo, String numero_celular) {
        this.nombre = nombre;
        this.correo = correo;
        this.numero_celular = numero_celular;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNumero_celular() {
        return numero_celular;
    }

    public void setNumero_celular(String numero_celular) {
        this.numero_celular = numero_celular;
    }
}
