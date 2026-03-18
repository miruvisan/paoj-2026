package com.pao.laboratory03.exceptions;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void riskyMethod() {
        String s = null;
        s.length();    }

    public static void validateAge(int age) {
        if (age < 0 || age > 150) {
            throw new InvalidAgeException("Varsta " + age + " nu este valida (0-150)");
        }
    }

    public static void addToList(List<String> list, String name) {
        if (list.contains(name)) {
            throw new DuplicateEntryException("'" + name + "' exista deja in lista");
        }
        list.add(name);
    }

    public static void process(int age) throws InvalidAgeException {
        validateAge(age);
    }
    public static void main(String[] args) {
        System.out.println("=== a) Unchecked - NullPointerException ===");
        try {
            riskyMethod();
        } catch (NullPointerException e) {
            System.out.println("Prins: " + e.getMessage());
        } finally {
            System.out.println("Finally se execută mereu!");
        }

        System.out.println("\n=== b) Custom exceptions ===");
        try {
            validateAge(-5);
        } catch (InvalidAgeException e) {
            System.out.println("InvalidAgeException: " + e.getMessage());
        }

        List<String> names = new ArrayList<>();
        names.add("Ana");
        try {
            addToList(names, "Ana");
        } catch (DuplicateEntryException e) {
            System.out.println("DuplicateEntryException: " + e.getMessage());
        }

        System.out.println("\n=== c) Multi-catch ===");
        try {
            validateAge(200);
        } catch (InvalidAgeException | DuplicateEntryException e) {
            System.out.println("Exceptie prinsa: " + e.getMessage());
        }

        System.out.println("\n=== d) Catch ordering (specific -> general) ===");
        try {
            validateAge(-1);
        } catch (InvalidAgeException e) {
            System.out.println("InvalidAgeException prinsa specific: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("General: " + e.getMessage());
        }

        System.out.println("\n=== e) Throw vs throws ===");
        try {
            process(999);
        } catch (InvalidAgeException e) {
            System.out.println("Metoda process() a aruncat: " + e.getMessage());
        }
    }
}

