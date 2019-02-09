package com.app.medicfarma.models;

public class Pharmacy {

    private int idFarmacia;
    private String nombreFarmacia;

    public Pharmacy() {
    }

    public Pharmacy(int idFarmacia, String nombreFarmacia) {
        this.idFarmacia = idFarmacia;
        this.nombreFarmacia = nombreFarmacia;
    }

    public int getIdFarmacia() {
        return idFarmacia;
    }

    public void setIdFarmacia(int idFarmacia) {
        this.idFarmacia = idFarmacia;
    }

    public String getNombreFarmacia() {
        return nombreFarmacia;
    }

    public void setNombreFarmacia(String nombreFarmacia) {
        this.nombreFarmacia = nombreFarmacia;
    }
}
