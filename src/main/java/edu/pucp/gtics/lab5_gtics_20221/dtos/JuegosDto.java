package edu.pucp.gtics.lab5_gtics_20221.dtos;

import edu.pucp.gtics.lab5_gtics_20221.entity.Juegos;

public class JuegosDto {
    private String result;
    private String msg;
    private Juegos juegos;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Juegos getJuegos() {
        return juegos;
    }

    public void setJuegos(Juegos juegos) {
        this.juegos = juegos;
    }
}
