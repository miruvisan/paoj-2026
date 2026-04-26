package com.pao.proiect.bank.model;

import java.time.LocalDate;

public class CardDebit extends Card {
    public CardDebit(String numarCard, String pin, LocalDate dataExpirare, String ibanContAtasat) {
        super(numarCard, pin, dataExpirare, ibanContAtasat);
    }

    @Override
    public String getTipCard() {
        return "Debit";
    }
}
