package com.pao.proiect.bank.model;

public class Adresa {
    private String oras;
    private String strada;
    private int numar;

    public Adresa(String oras, String strada, int numar) {
        this.oras = oras;
        this.strada = strada;
        this.numar = numar;
    }

    public String getOras() { return oras; }
    public void setOras(String oras) { this.oras = oras; }

    public String getStrada() { return strada; }
    public void setStrada(String strada) { this.strada = strada; }

    public int getNumar() { return numar; }
    public void setNumar(int numar) { this.numar = numar; }

    @Override
    public String toString() {
        return oras + ", " + strada + " nr. " + numar;
    }
}
