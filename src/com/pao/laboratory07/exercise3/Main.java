package com.pao.laboratory07.exercise3;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) return;

        int n = Integer.parseInt(sc.nextLine());
        List<Comanda> comenzi = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String[] tokens = sc.nextLine().split(" ");
            String tip = tokens[0];
            switch (tip) {
                case "STANDARD" -> comenzi.add(new ComandaStandard(tokens[1], Double.parseDouble(tokens[2]), tokens[3]));
                case "DISCOUNTED" -> comenzi.add(new ComandaRedusa(tokens[1], Double.parseDouble(tokens[2]), Integer.parseInt(tokens[3]), tokens[4]));
                case "GIFT" -> comenzi.add(new ComandaGratuita(tokens[1], tokens[2]));
            }
        }

        comenzi.forEach(c -> System.out.println(c.descriere()));

        while (sc.hasNext()) {
            String commandLine = sc.nextLine();
            if (commandLine.isBlank()) continue;
            String[] tokens = commandLine.split(" ");
            String cmd = tokens[0];

            if (cmd.equals("QUIT")) break;

            System.out.println("\n--- " + cmd + (tokens.length > 1 ? " (" + tokens[1] + ")" : "") + " ---");

            switch (cmd) {
                case "STATS" -> {
                    var stats = comenzi.stream()
                            .collect(Collectors.groupingBy(
                                    c -> c.getClass().getSimpleName().replace("Comanda", "").toUpperCase(),
                                    Collectors.averagingDouble(Comanda::pretFinal)
                            ));
                    stats.forEach((tip, medie) -> System.out.printf("%s: medie = %.2f lei\n", tip, medie));
                }
                case "FILTER" -> {
                    double limit = Double.parseDouble(tokens[1]);
                    comenzi.stream()
                            .filter(c -> c.pretFinal() >= limit)
                            .forEach(c -> System.out.printf("%s: %s, pret: %.2f lei - client: %s\n",
                                    c.getClass().getSimpleName().replace("Comanda", "").toUpperCase(),
                                    c.nume, c.pretFinal(), c.client));
                }
                case "SORT" -> {
                    comenzi.stream()
                            .sorted(Comparator.comparing(Comanda::getClient).thenComparing(Comanda::pretFinal))
                            .forEach(c -> {
                                String info = c.pretFinal() == 0 ? "gratuit" : String.format("pret: %.2f lei", c.pretFinal());
                                System.out.printf("%s: %s, %s - client: %s\n",
                                        c.getClass().getSimpleName().replace("Comanda", "").toUpperCase(),
                                        c.nume, info, c.client);
                            });
                }
                case "SPECIAL" -> {
                    comenzi.stream()
                            .filter(c -> c instanceof ComandaRedusa r && r.getDiscountProcent() > 15)
                            .forEach(c -> System.out.println(c.descriere().replace(" [PLACED]", "")));
                }
            }
        }
    }
}
