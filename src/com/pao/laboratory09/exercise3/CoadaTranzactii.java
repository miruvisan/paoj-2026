package com.pao.laboratory09.exercise3;

import com.pao.laboratory09.exercise1.Tranzactie;
import java.util.LinkedList;
import java.util.Queue;

public class CoadaTranzactii {
    private final int capacitateMaxima = 5;
    private final Queue<Tranzactie> coada = new LinkedList<>();

    public synchronized void adauga(Tranzactie t, String atmName) throws InterruptedException {
        // Cât timp banda este plină, producătorul (ATM-ul) așteaptă
        while (coada.size() == capacitateMaxima) {
            System.out.println("[" + atmName + "] astept loc...");
            wait();
        }

        coada.add(t);
        // Trezim consumatorul (Processor) care poate aștepta într-o coadă goală
        notifyAll();
    }

    public synchronized Tranzactie extrage() throws InterruptedException {
        while (coada.isEmpty()) {
            wait();
        }

        Tranzactie t = coada.poll();
        notifyAll();
        return t;
    }

    public synchronized boolean isEmpty() {
        return coada.isEmpty();
    }
}