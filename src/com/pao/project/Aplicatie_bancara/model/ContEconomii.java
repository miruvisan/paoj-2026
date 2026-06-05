package com.pao.project.Aplicatie_bancara.model;

public class ContEconomii extends ContOperabil {
    private double rataDobanda;

    public ContEconomii(String iban, double sold, String idClient, double rataDobanda) {
        super(iban, sold, idClient);
        this.rataDobanda = rataDobanda;
    }

    @Override
    public String getTipCont() {
        return "Economii";
    }

    public double getRataDobanda() { return rataDobanda; }
    public void setRataDobanda(double rataDobanda) { this.rataDobanda = rataDobanda; }
}
