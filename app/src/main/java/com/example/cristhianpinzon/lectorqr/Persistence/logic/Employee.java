package com.example.cristhianpinzon.lectorqr.Persistence.logic;

/**
 * Created by Cristhian Pinzon on 22/08/2017.
 */

public class Employee {
    private String cedula;
    private String nombre_empleado;
    private String apellido_empleado;
    private int    logueado;

    public Employee(String cedula, String nombre_empleado, String apellido_empleado, int logueado) {
        this.cedula = cedula;
        this.nombre_empleado = nombre_empleado;
        this.apellido_empleado = apellido_empleado;
        this.logueado = logueado;
    }

    public int getLogueado() {
        return logueado;
    }

    public void setLogueado(int logueado) {
        this.logueado = logueado;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre_empleado() {
        return nombre_empleado;
    }

    public void setNombre_empleado(String nombre_empleado) {
        this.nombre_empleado = nombre_empleado;
    }

    public String getApellido_empleado() {
        return apellido_empleado;
    }

    public void setApellido_empleado(String apellido_empleado) {
        this.apellido_empleado = apellido_empleado;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "cedula='" + cedula + '\'' +
                ", nombre_empleado='" + nombre_empleado + '\'' +
                ", apellido_empleado='" + apellido_empleado + '\'' +
                ", logueado=" + logueado +
                '}';
    }
}
