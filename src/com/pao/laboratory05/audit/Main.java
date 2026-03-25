package com.pao.laboratory05.audit;

import java.util.Scanner;

/**
 * Exercise 4 (Bonus) — Audit Log
 *
 * Cerințele complete se află în:
 *   src/com/pao/laboratory05/Readme.md  →  secțiunea "Exercise 4 (Bonus) — Audit"
 *
 * Extinde soluția de la Exercise 3 cu un sistem de audit bazat pe record.
 * Creează fișierele de la zero în acest pachet, apoi rulează Main.java
 * pentru a verifica output-ul așteptat din Readme.
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AngajatService service = AngajatService.getInstance();
            
        while(true){
            System.out.println("\n===== Gestionare Angajati (cu Audit) =====");
            System.out.println("1. Adauga angajat");
            System.out.println("2. Listare dupa salariu");
            System.out.println("3. Cauta dupa departament");
            System.out.println("4. Afiseaza audit log");
            System.out.println("0. Iesire");
            System.out.print("Optiune: ");

            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    System.out.print("Nume: ");
                    String nume = scanner.nextLine();
                    System.out.print("Departament (nume): ");
                    String dNume = scanner.nextLine();
                    System.out.print("Departament (locatie): ");
                    String dLoc = scanner.nextLine();
                    System.out.print("Salariu: ");
                    double sal = Double.parseDouble(scanner.nextLine());
                    service.addAngajat(new Angajat(nume, new Departament(dNume, dLoc), sal));
                    break;
                case "2":
                    service.listBySalary();
                    break;
                case "3":
                    System.out.print("Departament: ");
                    service.findByDepartament(scanner.nextLine());
                    break;
                case "4":
                    service.printAuditLog();
                    break;
                case "0":
                    System.out.println("La revedere!");
                    return;
                default:
                    System.out.println("Optiune invalida!");
            }
        }
    }
}
