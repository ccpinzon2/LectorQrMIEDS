package com.example.cristhianpinzon.lectorqr.Logica;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User_response {

    @SerializedName("nombre")
    @Expose
    private String nombre;

    @SerializedName("id")
    @Expose
    private String id;

    public String getNombre ()
    {
        return nombre;
    }

    public void setNombre (String nombre)
    {
        this.nombre = nombre;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [nombre = "+nombre+", id = "+id+"]";
    }

}
