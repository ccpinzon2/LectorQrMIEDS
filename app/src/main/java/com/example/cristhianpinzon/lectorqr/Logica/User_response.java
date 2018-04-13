package com.example.cristhianpinzon.lectorqr.Logica;

public class User_response {

    private String nombre;

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
