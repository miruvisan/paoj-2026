package com.pao.proiect.bank.model;

public class ContCurent extends Cont {
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
