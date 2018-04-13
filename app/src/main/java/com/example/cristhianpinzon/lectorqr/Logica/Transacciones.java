package com.example.cristhianpinzon.lectorqr.Logica;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Transacciones {

    @SerializedName("puntos")
    @Expose
    private String puntos;

    @SerializedName("id_userapp")
    @Expose
    private String id_userapp;

    @SerializedName("id_estacion")
    @Expose
    private String id_estacion;

    @SerializedName("cedula_empleado")
    @Expose
    private String cedula_empleado;

    @SerializedName("nombre_empleado")
    @Expose
    private String nombre_empleado;

    @SerializedName("tipo_puntaje")
    @Expose
    private String tipo_puntaje;

    @SerializedName("fecha_puntaje")
    @Expose
    private String fecha_puntaje;

    @SerializedName("nombre_usuario")
    @Expose
    private String nombre_usuario;


    public String getPuntos() {
        return puntos;
    }

    public String getId_userapp() {
        return id_userapp;
    }

    public String getId_estacion() {
        return id_estacion;
    }

    public String getCedula_empleado() {
        return cedula_empleado;
    }

    public String getNombre_empleado() {
        return nombre_empleado;
    }

    public String getTipo_puntaje() {
        return tipo_puntaje;
    }

    public String getFecha_puntaje() {
        return fecha_puntaje;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

}

