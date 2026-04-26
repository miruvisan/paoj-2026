package com.pao.proiect.bank.service;

import com.pao.proiect.bank.model.*;

import java.time.LocalDate;
import java.util.UUID;

public class CardService {
    private static CardService instance;

    private CardService() {}

    public static CardService getInstance() {
        if (instance == null) {
            instance = new CardService();
        }
        return instance;
    }

    public Card emiteCardDebit(Banca banca, String iban, String pin) {
        String numarCard = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        Card card = new CardDebit(numarCard, pin, LocalDate.now().plusYears(4), iban);
        banca.getCarduri().add(card);
        return card;
    }

    public Card emiteCardCredit(Banca banca, String iban, String pin, double limita) {
        String numarCard = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        Card card = new CardCredit(numarCard, pin, LocalDate.now().plusYears(4), iban, limita);
        banca.getCarduri().add(card);
        return card;
    }

    public void blocheazaCard(Banca banca, String numarCard) {
        for (Card c : banca.getCarduri()) {
            if (c.getNumarCard().equals(numarCard)) {
                c.setBlocat(true);
                System.out.println("Cardul " + numarCard + " a fost blocat.");
                return;
            }
        }
        System.out.println("Cardul nu a fost gasit.");
    }
}
