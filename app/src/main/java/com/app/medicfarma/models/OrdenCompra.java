package com.app.medicfarma.models;

import java.util.ArrayList;

public class OrdenCompra {

    private Pedido pedidos;
    private ArrayList<DetallePedido> detallePedido;

    public OrdenCompra() {
        detallePedido = new ArrayList<>();
    }

    public Pedido getPedidos() {
        return pedidos;
    }

    public void setPedidos(Pedido pedidos) {
        this.pedidos = pedidos;
    }

    public ArrayList<DetallePedido> getDetallePedido() {
        return detallePedido;
    }

    public void setDetallePedido(ArrayList<DetallePedido> detallePedido) {
        this.detallePedido = detallePedido;
    }

}
