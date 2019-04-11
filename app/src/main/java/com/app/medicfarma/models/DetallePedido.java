package com.app.medicfarma.models;

public class DetallePedido {

    private int idproducto;
    private int cantidad;

    public DetallePedido() {
    }

    public DetallePedido(int idproducto, int cantidad) {
        this.idproducto = idproducto;
        this.cantidad = cantidad;
    }

    public int getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(int idproducto) {
        this.idproducto = idproducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

}
