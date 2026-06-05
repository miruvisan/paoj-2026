package com.pao.project.Aplicatie_bancara.model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Adresa adresa = (Adresa) o;
        return numar == adresa.numar && Objects.equals(oras, adresa.oras) && Objects.equals(strada, adresa.strada);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oras, strada, numar);
    }
}
