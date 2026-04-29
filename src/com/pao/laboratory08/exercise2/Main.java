package com.pao.laboratory08.exercise2;

import com.pao.laboratory08.exercise1.Adresa;
import com.pao.laboratory08.exercise1.Student;

import java.io.*;
import java.util.*;

public class Main {
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        List<Student> studenti = citesteStudentiDinFisier();

        Scanner scanner = new Scanner(System.in);
        int prag = scanner.hasNextInt() ? scanner.nextInt() : 0;

        List<Student> filtrati = new ArrayList<>();
        for (Student student : studenti) {
            if (student.getVarsta() >= prag) {
                filtrati.add(student);
            }
        }

        BufferedWriter fout = new BufferedWriter(new FileWriter("rezultate.txt"));
        for (Student student : filtrati) {
            fout.write(student.toString());
            fout.newLine();
        }
        fout.close();

        System.out.println("Filtru: varsta >= " + prag);
        System.out.println("Rezultate: " + filtrati.size() + " studenti");
        System.out.println();
        for (Student student : filtrati) {
            System.out.println(student);
        }
        System.out.println();
        System.out.println("Scris in: rezultate.txt");
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

            studenti.add(new Student(nume, varsta, new Adresa(oras, strada)));
        }

        fin.close();
        return studenti;
    }
}

