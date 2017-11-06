package com.example.juanpablorn30.clases;

import java.util.Date;
import java.util.List;

/**
 * Created by juanpablorn30 on 5/11/17.
 */

public class Alarma {

    private Date hora;
    private List<Integer> color;

    public Alarma() {
    }

    public Alarma(Date hora, List<Integer> color) {
        this.hora = hora;
        this.color = color;
    }

    public Long getHora() {
        return hora.getTime();
    }

    public void setHora(Long hora) {
        this.hora = new Date(hora);
    }

    public List<Integer> getColor() {
        return color;
    }

    public void setColor(List<Integer> color) {
        this.color = color;
    }
}
