package com.example.cristhianpinzon.lectorqr.Logica;

/**
 * Created by Cristhian Pinzon on 24/08/2017.
 */

public class UserApp {
    private String id;
    private String tipo;
    private String email;
    private String phone_user;
    private String nombre;
    private String foto_perfil;
    private int ptos_globales;
    private int ptos_fidelizados;


    public UserApp(String id, String tipo, String email, String phone_user, String nombre, int ptos_globales, int ptos_fidelizados) {
        this.id = id;
        this.tipo = tipo;
        this.email = email;
        this.phone_user = phone_user;
        this.nombre = nombre;
        this.ptos_globales = ptos_globales;
        this.ptos_fidelizados = ptos_fidelizados;
    }

    public String getFoto_perfil() {
        return foto_perfil;
    }

    public void setFoto_perfil(String foto_perfil) {
        this.foto_perfil = foto_perfil;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_user() {
        return phone_user;
    }

    public void setPhone_user(String phone_user) {
        this.phone_user = phone_user;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPtos_globales() {
        return ptos_globales;
    }

    public void setPtos_globales(int ptos_globales) {
        this.ptos_globales = ptos_globales;
    }

    public int getPtos_fidelizados() {
        return ptos_fidelizados;
    }

    public void setPtos_fidelizados(int ptos_fidelizados) {
        this.ptos_fidelizados = ptos_fidelizados;
    }

    @Override
    public String toString() {
        return "UserApp{" +
                "id='" + id + '\'' +
                ", tipo='" + tipo + '\'' +
                ", email='" + email + '\'' +
                ", phone_user='" + phone_user + '\'' +
                ", nombre='" + nombre + '\'' +
                ", ptos_globales=" + ptos_globales +
                ", ptos_fidelizados=" + ptos_fidelizados +
                '}';
    }
}
