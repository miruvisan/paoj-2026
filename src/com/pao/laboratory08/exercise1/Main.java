package com.pao.laboratory08.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        List<Student> studenti = citesteStudentiDinFisier();

        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) {
            return;
        }

        String comanda = scanner.nextLine().trim();
        if (comanda.isEmpty()) {
            return;
        }

        String[] parti = comanda.split(" ", 2);
        String tipComanda = parti[0].trim().toUpperCase(Locale.ROOT);

        if ("PRINT".equals(tipComanda)) {
            for (Student student : studenti) {
                System.out.println(student);
            }
            return;
        }

        if (parti.length < 2) {
            return;
        }

        String numeCautat = parti[1].trim();
        Student original = gasesteStudentDupaNume(studenti, numeCautat);
        if (original == null) {
            return;
        }

        Student clona;
        if ("SHALLOW".equals(tipComanda)) {
            clona = original.clone();
        } else if ("DEEP".equals(tipComanda)) {
            clona = original.deepClone();
        } else {
            return;
        }

        clona.getAdresa().setOras("MODIFICAT");
        System.out.println("Original: " + original);
        System.out.println("Clona: " + clona);
    }

    private static List<Student> citesteStudentiDinFisier() throws IOException {
        List<Student> studenti = new ArrayList<>();
        BufferedReader fin = new BufferedReader(new FileReader(FILE_PATH));

        String linie;
        while ((linie = fin.readLine()) != null) {
            if (linie.isBlank()) {
                continue;
            }

            String[] parti = linie.split(",", 4);
            String nume = parti[0].trim();
            int varsta = Integer.parseInt(parti[1].trim());
            String oras = parti[2].trim();
            String strada = parti[3].trim();

            Adresa adresa = new Adresa(oras, strada);
            Student student = new Student(nume, varsta, adresa);
            studenti.add(student);
        }

        fin.close();
        return studenti;
    }

    private static Student gasesteStudentDupaNume(List<Student> studenti, String nume) {
        for (Student student : studenti) {
            if (student.getNume().equals(nume)) {
                return student;
            }
        }
        return null;
    }
}
