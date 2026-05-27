package com.pao.laboratory10.exercise2;

import com.pao.laboratory10.exercise1.Tranzactie;
import com.pao.laboratory10.exercise1.TipTranzactie;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        if (!scanner.hasNextInt()) {
            return;
        }

        int n = scanner.nextInt();
        ArrayList<Tranzactie> lista = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int id = scanner.nextInt();
            double suma = scanner.nextDouble();
            String data = scanner.next();
            TipTranzactie tip = TipTranzactie.valueOf(scanner.next());

            lista.add(new Tranzactie(id, suma, data, tip));
        }

        while (scanner.hasNext()) {
            String command = scanner.next();

            if ("UNIQUE_IDS".equals(command)) {
                LinkedHashSet<Integer> uniqueIds = new LinkedHashSet<>();
                for (Tranzactie t : lista) {
                    uniqueIds.add(t.getId());
                }
                System.out.println("IDs unice (" + uniqueIds.size() + "): " + uniqueIds);

            } else if ("MONTHLY_REPORT".equals(command)) {
                TreeMap<String, double[]> raport = new TreeMap<>();

                for (Tranzactie t : lista) {
                    String luna = t.getData().substring(0, 7);

                    if (!raport.containsKey(luna)) {
                        raport.put(luna, new double[2]);
                    }

                    double[] sume = raport.get(luna);
                    if (t.getTip() == TipTranzactie.CREDIT) {
                        sume[0] += t.getSuma();
                    } else {
                        sume[1] += t.getSuma();
                    }
                }

                for (Map.Entry<String, double[]> entry : raport.entrySet()) {
                    System.out.printf(Locale.US, "%s: CREDIT %.2f RON, DEBIT %.2f RON%n",
                            entry.getKey(), entry.getValue()[0], entry.getValue()[1]);
                }

            } else if ("TOP".equals(command)) {
                int topN = scanner.nextInt();

                ArrayList<Tranzactie> copie = new ArrayList<>(lista);

                Collections.sort(copie, new Comparator<Tranzactie>() {
                    @Override
                    public int compare(Tranzactie t1, Tranzactie t2) {
                        return Double.compare(t2.getSuma(), t1.getSuma());
                    }
                });

                System.out.println("Top " + topN + ":");
                int limita = Math.min(topN, copie.size());
                for (int i = 0; i < limita; i++) {
                    System.out.println(copie.get(i));
                }

            } else if ("SORT_ASC".equals(command)) {
                Collections.sort(lista, new Comparator<Tranzactie>() {
                    @Override
                    public int compare(Tranzactie t1, Tranzactie t2) {
                        return Double.compare(t1.getSuma(), t2.getSuma());
                    }
                });
                for (Tranzactie t : lista) {
                    System.out.println(t);
                }

            } else if ("SORT_DESC".equals(command)) {
                Collections.sort(lista, new Comparator<Tranzactie>() {
                    @Override
                    public int compare(Tranzactie t1, Tranzactie t2) {
                        return Double.compare(t2.getSuma(), t1.getSuma());
                    }
                });
                for (Tranzactie t : lista) {
                    System.out.println(t);
                }

            } else if ("REVERSE".equals(command)) {
                Collections.reverse(lista);
                for (Tranzactie t : lista) {
                    System.out.println(t);
                }

            } else if ("MIN_MAX".equals(command)) {
                if (!lista.isEmpty()) {
                    Comparator<Tranzactie> comparatorSuma = new Comparator<Tranzactie>() {
                        @Override
                        public int compare(Tranzactie t1, Tranzactie t2) {
                            return Double.compare(t1.getSuma(), t2.getSuma());
                        }
                    };
                    Tranzactie min = Collections.min(lista, comparatorSuma);
                    Tranzactie max = Collections.max(lista, comparatorSuma);

                    System.out.println("MIN: " + min);
                    System.out.println("MAX: " + max);
                }

            } else if ("CME_DEMO".equals(command)) {
                try {
                    for (Tranzactie t : lista) {
                        lista.remove(t);
                    }
                } catch (ConcurrentModificationException e) {
                    System.out.println("ConcurrentModificationException prins: modificare in iteratie detectata.");
                }
            }
        }

        scanner.close();
    }
}
