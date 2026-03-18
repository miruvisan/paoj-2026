package com.pao.laboratory03.exercise;

import java.util.Scanner;
import com.pao.laboratory03.exercise.model.Subject;
import com.pao.laboratory03.exercise.service.StudentService;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StudentService service = StudentService.getInstance();
        
        System.out.println("=== Sistem Gestiune Studenti ===");

        boolean running = true;
        while (running) {
            System.out.println("\n--- Meniu ---");
            System.out.println("1. Adauga student");
            System.out.println("2. Adauga nota");
            System.out.println("3. Afiseaza toti studentii");
            System.out.println("4. Top studenti (dupa medie)");
            System.out.println("5. Media pe materie");
            System.out.println("0. Iesire");
            System.out.print("Optiune: ");

            String option = scanner.nextLine().trim();

            try {
                switch (option) {
                    case "1":
                        System.out.print("Nume: ");
                        String name = scanner.nextLine().trim();
                        System.out.print("Varsta: ");
                        int age = Integer.parseInt(scanner.nextLine().trim());
                        service.addStudent(name, age);
                        System.out.println("Student adaugat cu succes!");
                        break;

                    case "2":
                        System.out.print("Nume student: ");
                        String studentName = scanner.nextLine().trim();
                        System.out.print("Materie (" + "PAOJ, BD, SO, RC" + "): ");
                        String subjectStr = scanner.nextLine().trim().toUpperCase();
                        System.out.print("Nota (1-10): ");
                        double grade = Double.parseDouble(scanner.nextLine().trim());
                        
                        Subject subject = Subject.valueOf(subjectStr);
                        service.addGrade(studentName, subject, grade);
                        System.out.println("Nota adaugata!");
                        break;

                    case "3":
                        service.printAllStudents();
                        break;

                    case "4":
                        service.printTopStudents();
                        break;

                    case "5":
                        Map<Subject, Double> averages = service.getAveragePerSubject();
                        averages.forEach((sub, avg) -> System.out.printf("%s: %.2f%n", sub.getFullName(), avg));
                        break;

                    case "0":
                        running = false;
                        System.out.println("La revedere!");
                        break;

                    default:
                        System.out.println("Optiune invalida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Eroare: Introdu un număr valid.");
            } catch (IllegalArgumentException e) {
                System.out.println("Eroare: " + e.getMessage());
            } catch (RuntimeException e) {
                System.out.println("Eroare: " + e.getMessage());
            }
        }

        scanner.close();
    }
}

