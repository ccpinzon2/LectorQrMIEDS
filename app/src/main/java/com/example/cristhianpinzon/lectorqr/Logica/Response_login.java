package com.example.cristhianpinzon.lectorqr.Logica;

public class Response_login {

    private String status;
    private User_response user;
    private Tienda establecimiento;

    public String getStatus() {
        return status;
    }

    public User_response getUser() {
        return user;
    }

    public Tienda getTienda() {
        return establecimiento;
    }
}
