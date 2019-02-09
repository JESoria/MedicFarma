package com.app.medicfarma.models;

public class Product {

    private String producto;
    private double precio;
    private int idSucursalProducto;
    private int idSucursal;
    private String sucursal;
    private String latitud;
    private String longitud;
    private String direccion;

    public Product() {
    }

    public Product(String producto, double precio, int idSucursalProducto, int idSucursal, String sucursal, String latitud, String longitud, String direccion) {
        this.producto = producto;
        this.precio = precio;
        this.idSucursalProducto = idSucursalProducto;
        this.idSucursal = idSucursal;
        this.sucursal = sucursal;
        this.latitud = latitud;
        this.longitud = longitud;
        this.direccion = direccion;
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

    public int getIdSucursalProducto() {
        return idSucursalProducto;
    }

    public void setIdSucursalProducto(int idSucursalProducto) {
        this.idSucursalProducto = idSucursalProducto;
    }

    public int getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(int idSucursal) {
        this.idSucursal = idSucursal;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
}
