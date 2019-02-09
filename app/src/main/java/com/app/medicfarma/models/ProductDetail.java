package com.app.medicfarma.models;

public class ProductDetail {

    private String producto;
    private String Presentacion;
    private String fechavencimiento;
    private String laboratorio;
    private String principalActivos;
    private String categoria;
    private double precio;

    public ProductDetail() {
    }

    public ProductDetail(String producto, String presentacion, String fechavencimiento, String laboratorio, String principalActivos, String categoria, double precio) {
        this.producto = producto;
        Presentacion = presentacion;
        this.fechavencimiento = fechavencimiento;
        this.laboratorio = laboratorio;
        this.principalActivos = principalActivos;
        this.categoria = categoria;
        this.precio = precio;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getPresentacion() {
        return Presentacion;
    }

    public void setPresentacion(String presentacion) {
        Presentacion = presentacion;
    }

    public String getFechavencimiento() {
        return fechavencimiento;
    }

    public void setFechavencimiento(String fechavencimiento) {
        this.fechavencimiento = fechavencimiento;
    }

    public String getLaboratorio() {
        return laboratorio;
    }

    public void setLaboratorio(String laboratorio) {
        this.laboratorio = laboratorio;
    }

    public String getPrincipalActivos() {
        return principalActivos;
    }

    public void setPrincipalActivos(String principalActivos) {
        this.principalActivos = principalActivos;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

}
