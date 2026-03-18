package com.pao.laboratory03.enums;

public class Main {
    public static void main(String[] args) {

        System.out.println("=== Toate prioritatile ===");
        for (Priority p : Priority.values()) {
            System.out.println(p.getEmoji() + " " + p.name() +
                    " (level=" + p.getLevel() + ", color=" + p.getColor() + ")");
        }

        System.out.println("\n=== Switch pe prioritate ===");
        Priority current = Priority.HIGH;
        switch (current) {
            case HIGH:
            case CRITICAL:
                System.out.println("⚠️ Atentie! Prioritate ridicata!");
                break;
            default:
                System.out.println("Totul este sub control.");
        }

        System.out.println("\n=== valueOf ===");
        Priority highPriority = Priority.valueOf("HIGH");
        System.out.println("Priority.valueOf(\"HIGH\") = " + highPriority);

        System.out.println("\n=== Comparare enum ===");
        System.out.println("HIGH == HIGH? " + (highPriority == Priority.HIGH));
        System.out.println("HIGH == LOW? " + (highPriority == Priority.LOW));

        System.out.println("\n=== name() și ordinal() ===");
        for (Priority p : Priority.values()) {
            System.out.println(p.name() + ": name=" + p.name() + ", ordinal=" + p.ordinal());
        }
    }
}