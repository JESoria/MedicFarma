package com.app.medicfarma.models;

public class Pedido {

    private String codigoPedido;
    private int idusuario;
    private int idsucursal;
    private String direccion;
    private String telefono;
    private double montoCompra;
    private String estadoPago;

    public Pedido() {
    }

    public Pedido(String codigoPedido, int idusuario, int idsucursal, String direccion, String telefono, double montoCompra, String estadoPago) {
        this.codigoPedido = codigoPedido;
        this.idusuario = idusuario;
        this.idsucursal = idsucursal;
        this.direccion = direccion;
        this.telefono = telefono;
        this.montoCompra = montoCompra;
        this.estadoPago = estadoPago;
    }

    public String getCodigoPedido() {
        return codigoPedido;
    }

    public void setCodigoPedido(String codigoPedido) {
        this.codigoPedido = codigoPedido;
    }

    public int getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }

    public int getIdsucursal() {
        return idsucursal;
    }

    public void setIdsucursal(int idsucursal) {
        this.idsucursal = idsucursal;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }
}
