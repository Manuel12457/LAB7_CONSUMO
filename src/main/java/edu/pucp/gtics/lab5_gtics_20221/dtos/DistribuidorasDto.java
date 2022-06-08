package edu.pucp.gtics.lab5_gtics_20221.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import edu.pucp.gtics.lab5_gtics_20221.entity.Distribuidoras;

import java.util.List;

public class DistribuidorasDto {

    private String estado;
    private Distribuidoras[] distribuidoras;
    private String msg;


    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Distribuidoras[] getDistribuidoras() {
        return distribuidoras;
    }

    public void setDistribuidoras(Distribuidoras[] distribuidoras) {
        this.distribuidoras = distribuidoras;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
