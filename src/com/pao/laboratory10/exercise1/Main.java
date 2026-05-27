package com.pao.laboratory10.exercise1;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        LinkedList<Tranzactie> coada = new LinkedList<>();

        while (scanner.hasNext()) {
            String command = scanner.next();

            if ("ENQUEUE".equals(command)) {
                int id = scanner.nextInt();
                double suma = scanner.nextDouble();
                String data = scanner.next();
                TipTranzactie tip = TipTranzactie.valueOf(scanner.next());

                coada.addLast(new Tranzactie(id, suma, data, tip));

            } else if ("DEQUEUE".equals(command)) {
                if (coada.isEmpty()) {
                    System.out.println("Coada goala.");
                } else {
                    Tranzactie t = coada.removeFirst();
                    System.out.println("Procesat: " + t);
                }

            } else if ("PUSH".equals(command)) {
                int id = scanner.nextInt();
                double suma = scanner.nextDouble();
                String data = scanner.next();
                TipTranzactie tip = TipTranzactie.valueOf(scanner.next());

                coada.addFirst(new Tranzactie(id, suma, data, tip));

            } else if ("POP".equals(command)) {
                if (coada.isEmpty()) {
                    System.out.println("Coada goala.");
                } else {
                    Tranzactie t = coada.removeFirst();
                    System.out.println("Extras: " + t);
                }

            } else if ("REMOVE_DEBIT".equals(command)) {
                int count = 0;
                Iterator<Tranzactie> itr = coada.iterator();
                while (itr.hasNext()) {
                    Tranzactie t = itr.next();
                    if (t.getTip() == TipTranzactie.DEBIT) {
                        itr.remove();
                        count++;
                    }
                }
                System.out.println("Eliminat " + count + " tranzactii DEBIT.");

            } else if ("REMOVE_BELOW".equals(command)) {
                double threshold = scanner.nextDouble();
                int count = 0;
                Iterator<Tranzactie> itr = coada.iterator();
                while (itr.hasNext()) {
                    Tranzactie t = itr.next();
                    if (t.getSuma() < threshold) {
                        itr.remove();
                        count++;
                    }
                }
                System.out.printf(Locale.US, "Eliminat %d tranzactii sub %.2f RON.%n", count, threshold);

            } else if ("PRINT".equals(command)) {
                for (Tranzactie t : coada) {
                    System.out.println(t);
                }

            } else if ("SIZE".equals(command)) {
                System.out.println("Dimensiune coada: " + coada.size());
            }
        }

        scanner.close();
    }
}
