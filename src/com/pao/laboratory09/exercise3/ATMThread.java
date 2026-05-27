package com.pao.laboratory09.exercise3;

import com.pao.laboratory09.exercise1.TipTranzactie;
import com.pao.laboratory09.exercise1.Tranzactie;

public class ATMThread extends Thread {
    private final int atmId;
    private final CoadaTranzactii banda;
    private static int idGlobalTranzactie = 1;

    public ATMThread(int atmId, CoadaTranzactii banda) {
        this.atmId = atmId;
        this.banda = banda;
    }

    private static synchronized int generareIdUnic() {
        return idGlobalTranzactie++;
    }

    @Override
    public void run() {
        String atmName = "ATM-" + atmId;
        try {
            for (int i = 0; i < 4; i++) {
                int idTx = generareIdUnic();
                double suma = 100 + (Math.random() * 900); // sumă aleatorie

                Tranzactie t = new Tranzactie(idTx, suma, "2026-05-27", "RO_ATM_" + atmId, "RO_BANK", TipTranzactie.CREDIT);

                banda.adauga(t, atmName);
                System.out.printf("[%s] trimite: Tranzactie #%d suma %.2f RON%n", atmName, idTx, suma);

                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            System.out.println("[" + atmName + "] a fost intrerupt.");
        }
    }
}