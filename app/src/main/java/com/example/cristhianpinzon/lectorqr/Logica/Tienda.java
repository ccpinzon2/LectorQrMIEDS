package com.example.cristhianpinzon.lectorqr.Logica;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristhian Pinzon on 09/08/2017.
 */

public class Tienda {
    private String id;
    private String nombre;
    private String direccion;
    private String telefono;
    private String tipo;
    private String marca;
    private String logo_image;
    private List<Empleado> list_empleado;

    public List<Empleado> getList_empleado() {
        return list_empleado;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getTipo() {
        return tipo;
    }

    public String getMarca() {
        return marca;
    }

    public String getLogo_image() {
        return logo_image;
    }
}
