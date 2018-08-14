package com.example.cristhianpinzon.lectorqr.Logica;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserApp {

    @SerializedName("id_usuario")
    @Expose
    private String id;

    @SerializedName("tipo")
    @Expose
    private String tipo;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("phone_user")
    @Expose
    private String phone_user;

    @SerializedName("nombres")
    @Expose
    private String nombre;

    @SerializedName("foto_perfil")
    @Expose
    private String foto_perfil;

    private int ptos_globales;
    private int ptos_fidelizados;
    private List<Placa> placas;


    public UserApp(String id, String tipo, String email, String phone_user, String nombre) {
        this.id = id;
        this.tipo = tipo;
        this.email = email;
        this.phone_user = phone_user;
        this.nombre = nombre;
    }

    public String getFoto_perfil() {
        return foto_perfil;
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

    public String getEmail() {
        return email;
    }

    public String getPhone_user() {
        return phone_user;
    }

    public String getNombre() {
        return nombre;
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

    public List<Placa> getPlacas() {
        return placas;
    }

    public void setPtos_fidelizados(int ptos_fidelizados) {
        this.ptos_fidelizados = ptos_fidelizados;
    }

    public void setPlacas(List<Placa> placas) {
        this.placas = placas;
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
