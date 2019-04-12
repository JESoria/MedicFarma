package com.app.medicfarma.models;

public class DetallePedido {

    private int idSucursalProducto;
    private int idFarmacia;
    private int cantidad;
    private String producto;
    private double precio;

    public DetallePedido() {
    }

    public DetallePedido(int idSucursalProducto, int idFarmacia,int cantidad, String producto, double precio) {
        this.cantidad = cantidad;
        this.idSucursalProducto = idSucursalProducto;
        this.idFarmacia = idFarmacia;
        this.producto = producto;
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getIdSucursalProducto() {
        return idSucursalProducto;
    }

    public void setIdSucursalProducto(int idSucursalProducto) { this.idSucursalProducto = idSucursalProducto; }
    public int getIdFarmacia() {
        return idFarmacia;
    }

    public void setIdFarmacia(int idFarmacia) {
        this.idFarmacia = idFarmacia;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
