package com.pao.project.Aplicatie_bancara.model;

import java.time.LocalDate;

public abstract class Card {
    protected String numarCard;
    protected String pin;
    protected LocalDate dataExpirare;
    protected boolean blocat;
    protected String ibanContAtasat;

    public Card(String numarCard, String pin, LocalDate dataExpirare, String ibanContAtasat) {
        this.numarCard = numarCard;
        this.pin = pin;
        this.dataExpirare = dataExpirare;
        this.ibanContAtasat = ibanContAtasat;
        this.blocat = false;
    }

    public abstract String getTipCard();

    public String getNumarCard() { return numarCard; }
    public String getPin() { return pin; }
    public LocalDate getDataExpirare() { return dataExpirare; }
    public boolean isBlocat() { return blocat; }
    public String getIbanContAtasat() { return ibanContAtasat; }

    public void setBlocat(boolean blocat) { this.blocat = blocat; }
    public void setPin(String pin) { this.pin = pin; }

    @Override
    public String toString() {
        return "Card " + getTipCard() + "{" +
                "numar='" + numarCard + '\'' +
                ", expira=" + dataExpirare +
                ", blocat=" + blocat +
                '}';
    }
}
