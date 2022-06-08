package edu.pucp.gtics.lab5_gtics_20221.entity;

import javax.naming.InterruptedNamingException;
import javax.persistence.*;
import javax.validation.Valid;

public class Distribuidoras {

    private Integer id;


    private String nombre;


    private String descripcion;


    private String web;


    private Integer fundacion;

    private Paises idsede;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public Integer getFundacion() {
        return fundacion;
    }

    public void setFundacion(Integer fundacion) {
        this.fundacion = fundacion;
    }

    public Paises getIdsede() {
        return idsede;
    }

    public void setIdsede(Paises idsede) {
        this.idsede = idsede;
    }
}
