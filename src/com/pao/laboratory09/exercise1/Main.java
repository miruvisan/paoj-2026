package com.pao.laboratory09.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex1.ser";

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        if (!scanner.hasNextInt()) {
            return;
        }

        int n = scanner.nextInt();
        List<Tranzactie> tranzactii = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int id = scanner.nextInt();
            double suma = scanner.nextDouble();
            String data = scanner.next();
            String contSursa = scanner.next();
            String contDestinatie = scanner.next();
            TipTranzactie tip = TipTranzactie.valueOf(scanner.next());

            Tranzactie t = new Tranzactie(id, suma, data, contSursa, contDestinatie, tip);
            tranzactii.add(t);
        }

        for (Tranzactie t : tranzactii) {
            t.setNote("procesat");
        }

        File file = new File(OUTPUT_FILE);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(tranzactii);
        }

        List<Tranzactie> deserializedList;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            @SuppressWarnings("unchecked")
            List<Tranzactie> listaTemporara = (List<Tranzactie>) ois.readObject();
            deserializedList = listaTemporara;
        }

        while (scanner.hasNext()) {
            String command = scanner.next();

            if ("LIST".equals(command)) {
                for (Tranzactie t : deserializedList) {
                    System.out.println(t);
                }

            } else if ("FILTER".equals(command)) {
                String prefix = scanner.next();
                boolean found = false;
                for (Tranzactie t : deserializedList) {
                    if (t.getData() != null && t.getData().startsWith(prefix)) {
                        System.out.println(t);
                        found = true;
                    }
                }
                if (!found) {
                    System.out.println("Niciun rezultat.");
                }

            } else if ("NOTE".equals(command)) {
                int id = scanner.nextInt();
                boolean found = false;
                for (Tranzactie t : deserializedList) {
                    if (t.getId() == id) {
                        System.out.println("NOTE[" + id + "]: " + t.getNote());
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.out.println("NOTE[" + id + "]: not found");
                }
            }
        }

        scanner.close();
    }
}
