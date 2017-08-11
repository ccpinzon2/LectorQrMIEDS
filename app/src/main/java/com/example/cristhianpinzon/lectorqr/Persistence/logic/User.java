package com.example.cristhianpinzon.lectorqr.Persistence.logic;

/**
 * Created by Cristhian Pinzon on 09/08/2017.
 */

public class User {
    private String id_user;
    private String name_user ;
    private String id_tienda ;
    private String nombre_tienda ;
    private String direccion_tienda ;
    private String telefono_tienda ;
    private String tipo_tienda ;
    private String marca_tienda ;
    private String logo_tienda;

    public User(String id_user, String name_user, String id_tienda, String nombre_tienda, String direccion_tienda, String telefono_tienda, String tipo_tienda, String marca_tienda, String logo_tienda) {
        this.id_user = id_user;
        this.name_user = name_user;
        this.id_tienda = id_tienda;
        this.nombre_tienda = nombre_tienda;
        this.direccion_tienda = direccion_tienda;
        this.telefono_tienda = telefono_tienda;
        this.tipo_tienda = tipo_tienda;
        this.marca_tienda = marca_tienda;
        this.logo_tienda = logo_tienda;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public String getId_tienda() {
        return id_tienda;
    }

    public void setId_tienda(String id_tienda) {
        this.id_tienda = id_tienda;
    }

    public String getNombre_tienda() {
        return nombre_tienda;
    }

    public void setNombre_tienda(String nombre_tienda) {
        this.nombre_tienda = nombre_tienda;
    }

    public String getDireccion_tienda() {
        return direccion_tienda;
    }

    public void setDireccion_tienda(String direccion_tienda) {
        this.direccion_tienda = direccion_tienda;
    }

    public String getTelefono_tienda() {
        return telefono_tienda;
    }

    public void setTelefono_tienda(String telefono_tienda) {
        this.telefono_tienda = telefono_tienda;
    }

    public String getTipo_tienda() {
        return tipo_tienda;
    }

    public void setTipo_tienda(String tipo_tienda) {
        this.tipo_tienda = tipo_tienda;
    }

    public String getMarca_tienda() {
        return marca_tienda;
    }

    public void setMarca_tienda(String marca_tienda) {
        this.marca_tienda = marca_tienda;
    }

    public String getLogo_tienda() {
        return logo_tienda;
    }

    public void setLogo_tienda(String logo_tienda) {
        this.logo_tienda = logo_tienda;
    }

    @Override
    public String toString() {
        return "User{" +
                "id_user='" + id_user + '\'' +
                ", name_user='" + name_user + '\'' +
                ", id_tienda='" + id_tienda + '\'' +
                ", nombre_tienda='" + nombre_tienda + '\'' +
                ", direccion_tienda='" + direccion_tienda + '\'' +
                ", telefono_tienda='" + telefono_tienda + '\'' +
                ", tipo_tienda='" + tipo_tienda + '\'' +
                ", marca_tienda='" + marca_tienda + '\'' +
                ", logo_tienda='" + logo_tienda + '\'' +
                '}';
    }
}
