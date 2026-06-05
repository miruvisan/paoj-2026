package com.pao.project.Aplicatie_bancara.model;

public class ContCurent extends ContOperabil {
    private double limitaDescoperire;

    public ContCurent(String iban, double sold, String idClient, double limitaDescoperire) {
        super(iban, sold, idClient);
        this.limitaDescoperire = limitaDescoperire;
    }

    @Override
    public String getTipCont() {
        return "Curent";
    }

    public double getLimitaDescoperire() { return limitaDescoperire; }
    public void setLimitaDescoperire(double limitaDescoperire) { this.limitaDescoperire = limitaDescoperire; }
}
