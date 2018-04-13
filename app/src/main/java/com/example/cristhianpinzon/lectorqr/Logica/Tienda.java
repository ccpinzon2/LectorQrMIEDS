package com.example.cristhianpinzon.lectorqr.Logica;

import java.util.List;

public class Tienda {

    private String nombre;
    private String id;
    private String direccion;
    private String tipoEstab;
    private String tipo;
    private String telefono;
    private String marca;
    private String logo_image;
    private List<Empleado> employees;

    public String getNombre () {
        return nombre;
    }

    public void setNombre (String nombre) {
        this.nombre = nombre;
    }

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public String getDireccion () {
        return direccion;
    }

    public void setDireccion (String direccion) {
        this.direccion = direccion;
    }

    public String getTipoEstab () {
        return tipoEstab;
    }

    public void setTipoEstab (String tipoEstab) {
        this.tipoEstab = tipoEstab;
    }

    public String getTipo () {
        return tipo;
    }

    public void setTipo (String tipo) {
        this.tipo = tipo;
    }

    public String getTelefono () {
        return telefono;
    }

    public void setTelefono (String telefono) {
        this.telefono = telefono;
    }

    public String getMarca () {
        return marca;
    }

    public void setMarca (String marca) {
        this.marca = marca;
    }

    public String getLogo_image () {
        return logo_image;
    }

    public void setLogo_image (String logo_image) {
        this.logo_image = logo_image;
    }

    public List<Empleado> getEmployees () {
        return employees;
    }

    public void setEmployees (List<Empleado> employees) {
        this.employees = employees;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [nombre = "+nombre+", id = "+id+", direccion = "+direccion+", tipoEstab = "+tipoEstab+", tipo = "+tipo+", telefono = "+telefono+", marca = "+marca+", logo_image = "+logo_image+", employees = "+employees+"]";
    }
}
