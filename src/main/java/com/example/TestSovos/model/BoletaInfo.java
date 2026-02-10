package com.example.TestSovos.model;

public class BoletaInfo {
    private String razonSocial;
    private String nombre;
    private String email;
    private double total;

    public BoletaInfo(String razonSocial, String nombre, String email, double total) {
        this.razonSocial = razonSocial;
        this.nombre = nombre;
        this.email = email;
        this.total = total;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
