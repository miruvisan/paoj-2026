package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

final class Transaction {
    private final int id;
    private final BigDecimal amount;
    private final LocalDate date;
    private final String country;
    private final String channel;

    public Transaction(int id, BigDecimal amount, LocalDate date, String country, String channel) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.country = country;
        this.channel = channel;
    }

    public int getId() { return id; }
    public BigDecimal getAmount() { return amount; }
    public LocalDate getDate() { return date; }
    public String getCountry() { return country; }
    public String getChannel() { return channel; }

    @Override
    public String toString() {
        return String.format(Locale.US, "[ID: %d] %s | %s | %s | %s RON", id, date, country, channel, amount);
    }
}

final class Snapshot {
    private final Map<String, Long> countByCountry;
    private final Map<String, Long> countByChannel;
    private final BigDecimal totalAmount;
    private final List<Transaction> topTransactions;

    public Snapshot(Map<String, Long> byCountry, Map<String, Long> byChannel, BigDecimal total, List<Transaction> top) {
        this.countByCountry = Collections.unmodifiableMap(new HashMap<>(byCountry));
        this.countByChannel = Collections.unmodifiableMap(new HashMap<>(byChannel));
        this.totalAmount = total;
        this.topTransactions = List.copyOf(top);
    }

    public Map<String, Long> getCountByCountry() { return countByCountry; }
    public Map<String, Long> getCountByChannel() { return countByChannel; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public List<Transaction> getTopTransactions() { return topTransactions; }
}

class CustomCollectors {
    public static Collector<Transaction, ?, Snapshot> toSnapshot(int topN) {
        class Agg {
            final Map<String, Long> countByCountry = new HashMap<>();
            final Map<String, Long> countByChannel = new HashMap<>();
            BigDecimal totalAmount = BigDecimal.ZERO;
            final List<Transaction> allTransactions = new ArrayList<>();

            void accumulate(Transaction tx) {
                countByCountry.put(tx.getCountry(), countByCountry.getOrDefault(tx.getCountry(), 0L) + 1);
                countByChannel.put(tx.getChannel(), countByChannel.getOrDefault(tx.getChannel(), 0L) + 1);
                totalAmount = totalAmount.add(tx.getAmount());
                allTransactions.add(tx);
            }

            Agg combine(Agg other) {
                other.countByCountry.forEach((k, v) -> this.countByCountry.merge(k, v, Long::sum));
                other.countByChannel.forEach((k, v) -> this.countByChannel.merge(k, v, Long::sum));
                this.totalAmount = this.totalAmount.add(other.totalAmount);
                this.allTransactions.addAll(other.allTransactions);
                return this;
            }

            Snapshot finisher() {
                List<Transaction> top = allTransactions.stream()
                        .sorted(Comparator.comparing(Transaction::getAmount).reversed()
                                .thenComparingInt(Transaction::getId))
                        .limit(topN)
                        .collect(Collectors.toList());

                return new Snapshot(countByCountry, countByChannel, totalAmount, top);
            }
        }

        return Collector.of(
                Agg::new,
                Agg::accumulate,
                Agg::combine,
                Agg::finisher
        );
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("..................................................");

        List<Transaction> data = Arrays.asList(
                new Transaction(1, new BigDecimal("1500.00"), LocalDate.of(2026, 5, 1), "RO", "WEB"),
                new Transaction(2, new BigDecimal("750.50"),  LocalDate.of(2026, 5, 1), "RU", "ATM"),
                new Transaction(3, new BigDecimal("1200.00"), LocalDate.of(2026, 5, 2), "RO", "CRYPTO"),
                new Transaction(4, new BigDecimal("1200.00"), LocalDate.of(2026, 5, 2), "NL", "WEB"), // Tie with ID 3
                new Transaction(5, new BigDecimal("50.00"),   LocalDate.of(2026, 5, 3), "RO", "POS"),
                new Transaction(6, new BigDecimal("450.00"),  LocalDate.of(2026, 5, 4), "RU", "WEB"),
                new Transaction(7, new BigDecimal("3000.00"), LocalDate.of(2026, 5, 5), "NG", "APP")
        );

        Snapshot snap = data.stream().collect(CustomCollectors.toSnapshot(3));

        System.out.println("\nValoare totala procesata in Snapshot: " + snap.getTotalAmount() + " RON");

        System.out.println("\n..................................................");
        System.out.println("Interogare 1: Top 3 Tranzactii active (din Snapshot):");
        snap.getTopTransactions().forEach(System.out::println);

        System.out.println("\n..................................................");
        System.out.println("Interogare 2: Clasament Tari dupa numarul de tranzactii:");
        snap.getCountByCountry().entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .forEach(entry -> System.out.println("  Tara: " + entry.getKey() + " -> " + entry.getValue() + " tranzactii"));

        System.out.println("\n..................................................");
        System.out.println("Interogare 3: Clasament Canale (Descrescator):");
        snap.getCountByChannel().entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .forEach(entry -> System.out.println("  Canal: " + entry.getKey() + " -> " + entry.getValue() + " utilizari"));

        System.out.println("\n..................................................");
        System.out.println("Test de imutabilitate (Ar trebui sa arunce exceptie la modificare):");
        try {
            snap.getCountByCountry().put("FR", 10L);
        } catch (UnsupportedOperationException e) {
            System.out.println("Succes! Modificarea a fost blocata: Colectia din Snapshot este Read-Only.");
        }
    }
}
