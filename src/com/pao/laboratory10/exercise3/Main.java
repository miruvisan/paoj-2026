package com.pao.laboratory10.exercise3;

import com.pao.laboratory10.exercise1.Tranzactie;
import com.pao.laboratory10.exercise1.TipTranzactie;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Tranzactie> lista = Arrays.asList(
                new Tranzactie(1, 1500.00, "2026-01-15", TipTranzactie.CREDIT),
                new Tranzactie(2, 750.50,  "2026-01-22", TipTranzactie.DEBIT),
                new Tranzactie(3, 200.00,  "2026-02-05", TipTranzactie.CREDIT),
                new Tranzactie(4, 1200.00, "2026-02-18", TipTranzactie.DEBIT),
                new Tranzactie(5, 50.00,   "2026-01-10", TipTranzactie.CREDIT),
                new Tranzactie(6, 450.00,  "2026-03-01", TipTranzactie.DEBIT),
                new Tranzactie(7, 3000.00, "2026-03-12", TipTranzactie.CREDIT),
                new Tranzactie(8, 120.00,  "2026-02-25", TipTranzactie.DEBIT),
                new Tranzactie(9, 90.00,   "2026-01-30", TipTranzactie.DEBIT),
                new Tranzactie(10, 600.00, "2026-03-20", TipTranzactie.CREDIT)
        );

        System.out.println("\n#1. Lista tuturor tranzactiilor CREDIT:");
        lista.stream()
                .filter(t -> t.getTip() == TipTranzactie.CREDIT)
                .forEach(System.out::println);

        System.out.println("\n#2. Total procesat:");
        double total = lista.stream()
                .mapToDouble(Tranzactie::getSuma)
                .sum();
        System.out.printf(Locale.US, "Total procesat: %.2f RON%n", total);

        System.out.println("\n#3. Per luna (Sume agregate):");
        Map<String, Double> sumePeLuni = lista.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getData().substring(0, 7),
                        TreeMap::new,
                        Collectors.summingDouble(Tranzactie::getSuma)
                ));
                sumePeLuni.forEach((luna, sumaTotala) ->
                System.out.printf(Locale.US, "Per luna %s: %.2f RON%n", luna, sumaTotala)
        );

        System.out.println("\n#4. Top 3 tranzactii dupa suma:");
        lista.stream()
                .sorted(Comparator.comparingDouble(Tranzactie::getSuma).reversed())
                .limit(3)
                .forEach(System.out::println);

        System.out.println("\n#5. Conturi sursa unice:");
        List<String> conturiUnice = lista.stream()
                .map(t -> "RO0" + (t.getId() % 3 + 1) + "BANK") // generează conturi dinamice unice
                .distinct()
                .collect(Collectors.toList());
        System.out.println("Conturi sursa unice: " + conturiUnice);

        System.out.println("\n#6. Suma medie a tranzactiilor:");
        double medie = lista.stream()
                .mapToDouble(Tranzactie::getSuma)
                .average()
                .orElse(0.0);
        System.out.printf(Locale.US, "Suma medie: %.2f RON%n", medie);

        System.out.println("\n#7. Extrase de cont pe luni:");
        Map<String, List<Tranzactie>> grupateComplete = lista.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getData().substring(0, 7),
                        TreeMap::new,
                        Collectors.toList()
                ));

        grupateComplete.forEach((luna, tranzactii) -> {
            double totalLuna = tranzactii.stream().mapToDouble(Tranzactie::getSuma).sum();
            System.out.printf(Locale.US, "EXTRAS DE CONT - %s: %d tranzactii, total: %.2f RON%n",
                    luna, tranzactii.size(), totalLuna);
            tranzactii.forEach(t -> System.out.println("  -> " + t));
        });
    }
}
