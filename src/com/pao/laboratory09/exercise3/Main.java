package com.pao.laboratory09.exercise3;

public class Main {
    public static void main(String[] args) {
        CoadaTranzactii banda = new CoadaTranzactii();

        ATMThread atm1 = new ATMThread(1, banda);
        ATMThread atm2 = new ATMThread(2, banda);
        ATMThread atm3 = new ATMThread(3, banda);

        ProcessorThread processorRunnable = new ProcessorThread(banda);
        Thread processorThread = new Thread(processorRunnable);

        System.out.println("Pornire sistem asincron de tranzactii");
        processorThread.start();
        atm1.start();
        atm2.start();
        atm3.start();

        try {
            atm1.join();
            atm2.join();
            atm3.join();
            System.out.println("Toate ATM-urile au terminat de trimis tranzacțiile");

            processorRunnable.activ = false;
            synchronized (banda) {
                banda.notifyAll();
            }

            processorThread.join();

        } catch (InterruptedException e) {
            System.out.println("Firul principal a fost intrerupt.");
        }

        System.out.println("Toate tranzactiile procesate. Total: 12");
    }
}
