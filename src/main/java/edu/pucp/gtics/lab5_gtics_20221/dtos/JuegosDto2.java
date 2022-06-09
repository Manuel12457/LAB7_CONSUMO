package edu.pucp.gtics.lab5_gtics_20221.dtos;

import edu.pucp.gtics.lab5_gtics_20221.entity.Juegos;

public class JuegosDto2 {

    private String estado;
    private Juegos[] juegos;
    private String msg;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Juegos[] getJuegos() {
        return juegos;
    }

    public void setJuegos(Juegos[] juegos) {
        this.juegos = juegos;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
