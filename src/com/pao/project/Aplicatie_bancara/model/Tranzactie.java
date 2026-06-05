package com.pao.project.Aplicatie_bancara.model;

import java.time.LocalDateTime;
import java.util.Objects;

public final class Tranzactie {
    private final String id;
    private final double suma;
    private final String tip;
    private final LocalDateTime data;
    private final String ibanCont;

    public Tranzactie(String id, double suma, String tip, LocalDateTime data, String ibanCont) {
        this.id = id;
        this.suma = suma;
        this.tip = tip;
        this.data = data;
        this.ibanCont = ibanCont;
    }

    public String getId() {
        return id;
    }

    public double getSuma() {
        return suma;
    }

    public String getTip() {
        return tip;
    }

    public LocalDateTime getData() {
        return data;
    }

    public String getIbanCont() {
        return ibanCont;
    }

    @Override
    public String toString() {
        return "Tranzactie{" +
                "id='" + id + '\'' +
                ", suma=" + suma +
                ", tip='" + tip + '\'' +
                ", data=" + data +
                ", ibanCont='" + ibanCont + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tranzactie that = (Tranzactie) o;
        return Double.compare(that.suma, suma) == 0 && Objects.equals(id, that.id) && Objects.equals(tip, that.tip) && Objects.equals(data, that.data) && Objects.equals(ibanCont, that.ibanCont);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, suma, tip, data, ibanCont);
    }
}
