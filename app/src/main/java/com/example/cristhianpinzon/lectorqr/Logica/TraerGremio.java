package com.example.cristhianpinzon.lectorqr.Logica;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TraerGremio {

    @SerializedName("id_estacion")
    @Expose
    private String id_estacion;

    @SerializedName("nombre_estacion")
    @Expose
    private String nombre_estacion;

    @SerializedName("id_gremio")
    @Expose
    private String id_gremio;

    @SerializedName("nombre_gremio")
    @Expose
    private String nombre_gremio;

    @SerializedName("nombres")
    @Expose
    private String nombre_usuario;

    public String getId_estacion() {
        return id_estacion;
    }

    public String getNombre_estacion() {
        return nombre_estacion;
    }

    public String getId_gremio() {
        return id_gremio;
    }

    public String getNombre_gremio() {
        return nombre_gremio;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }
}
