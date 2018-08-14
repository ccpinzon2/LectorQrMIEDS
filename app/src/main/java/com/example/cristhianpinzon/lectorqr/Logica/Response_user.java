package com.example.cristhianpinzon.lectorqr.Logica;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response_user {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("user_app")
    @Expose
    private UserApp usuario;

    @SerializedName("puntosRegalo")
    @Expose
    private String puntosRegalo;

    @SerializedName("")
    @Expose
    private int puntosFidelizados;

    @SerializedName("puntosFidelizados")
    @Expose
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
