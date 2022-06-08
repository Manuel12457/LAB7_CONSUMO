package edu.pucp.gtics.lab5_gtics_20221.dtos;

import edu.pucp.gtics.lab5_gtics_20221.entity.Distribuidoras;

public class DistrDto2 {

    private String estado;
    private Distribuidoras distribuidora;
    private String msg;


    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Distribuidoras getDistribuidora() {
        return distribuidora;
    }

    public void setDistribuidora(Distribuidoras distribuidora) {
        this.distribuidora = distribuidora;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
