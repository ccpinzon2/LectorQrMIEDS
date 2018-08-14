package com.example.cristhianpinzon.lectorqr.Logica;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Tienda {

    @SerializedName("nombre")
    @Expose
    private String nombre;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("direccion")
    @Expose
    private String direccion;

    @SerializedName("EstablishmentType")
    @Expose
    private String tipoEstab;

    @SerializedName("tipo")
    @Expose
    private String tipo;

    @SerializedName("telefono")
    @Expose
    private String telefono;

    @SerializedName("marca")
    @Expose
    private String marca;

    @SerializedName("logo_image")
    @Expose
    private String logo_image;

    @SerializedName("list_empleados")
    @Expose
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
