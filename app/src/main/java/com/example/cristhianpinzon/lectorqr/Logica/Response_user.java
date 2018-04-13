package com.example.cristhianpinzon.lectorqr.Logica;

import java.util.List;

public class Response_user {

    private String status;
    private UserApp usuario;
    private String puntosRegalo;
    private int puntosFidelizados;
    private List<Placa> puntosPlacas;

    public String getStatus() {
        return status;
    }

    public UserApp getUsuario() {
        return usuario;
    }

    public String getPuntosRegalo() {
        return puntosRegalo;
    }

    public int getPuntosFidelizados() {
        return puntosFidelizados;
    }

    public List<Placa> getPuntosPlacas() {
        return puntosPlacas;
    }
}
