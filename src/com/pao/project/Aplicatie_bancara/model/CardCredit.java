package com.pao.proiect.bank.model;

import java.time.LocalDate;

public class CardCredit extends Card {
    private double limitaCredit;

    public CardCredit(String numarCard, String pin, LocalDate dataExpirare, String ibanContAtasat, double limitaCredit) {
        super(numarCard, pin, dataExpirare, ibanContAtasat);
        this.limitaCredit = limitaCredit;
    }

    @Override
    public String getTipCard() {
        return "Credit";
    }

    public double getLimitaCredit() { return limitaCredit; }
    public void setLimitaCredit(double limitaCredit) { this.limitaCredit = limitaCredit; }
}
