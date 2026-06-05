package com.pao.project.Aplicatie_bancara.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Cont {
    protected String iban;
    protected double sold;
    protected String idClient;
    protected List<Tranzactie> tranzactii;

    public Cont(String iban, double sold, String idClient) {
        this.iban = iban;
        this.sold = sold;
        this.idClient = idClient;
        this.tranzactii = new ArrayList<>();
    }

    public abstract String getTipCont();

    public String getIban() { return iban; }
    public double getSold() { return sold; }
    public String getIdClient() { return idClient; }
    public List<Tranzactie> getTranzactii() { return tranzactii; }

    public void setSold(double sold) { this.sold = sold; }

    public void adaugaTranzactie(Tranzactie t) {
        this.tranzactii.add(t);
    }

    @Override
    public String toString() {
        return "Cont " + getTipCont() + "{" +
                "iban='" + iban + '\'' +
                ", sold=" + sold +
                '}';
    }
}
