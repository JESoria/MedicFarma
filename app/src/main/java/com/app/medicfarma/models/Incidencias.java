package com.app.medicfarma.models;

public class Incidencias {

    private int idPedido;
    private String codidoPedido;
    private String sucursal;
    private String direccion;
    private String longitud;
    private String latitud;
    private String telefono;
    private double montoCompra;

    public Incidencias() {
    }

    public Incidencias(int idPedido, String codidoPedido, String sucursal, String direccion, String longitud, String latitud, String telefono, double montoCompra) {
        this.idPedido = idPedido;
        this.codidoPedido = codidoPedido;
        this.sucursal = sucursal;
        this.direccion = direccion;
        this.longitud = longitud;
        this.latitud = latitud;
        this.telefono = telefono;
        this.montoCompra = montoCompra;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public String getCodidoPedido() {
        return codidoPedido;
    }

    public void setCodidoPedido(String codidoPedido) {
        this.codidoPedido = codidoPedido;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public double getMontoCompra() {
        return montoCompra;
    }

    public void setMontoCompra(double montoCompra) {
        this.montoCompra = montoCompra;
    }

}
