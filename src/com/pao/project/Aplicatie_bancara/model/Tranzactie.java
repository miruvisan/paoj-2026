package com.pao.proiect.bank.model;

import java.time.LocalDateTime;

public final class Tranzactie {
    private final String id;
    private final double suma;
    private final String tip;
    private final LocalDateTime data;

    public Tranzactie(String id, double suma, String tip, LocalDateTime data) {
        this.id = id;
        this.suma = suma;
        this.tip = tip;
        this.data = data;
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

    @Override
    public String toString() {
        return "Tranzactie{" +
                "id='" + id + '\'' +
                ", suma=" + suma +
                ", tip='" + tip + '\'' +
                ", data=" + data +
                '}';
    }
}
