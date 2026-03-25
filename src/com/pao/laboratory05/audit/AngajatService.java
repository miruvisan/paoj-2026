package com.pao.laboratory05.audit;

import java.util.Arrays;
import java.time.LocalDateTime;

public class AngajatService {
    private Angajat[] angajati = new Angajat[0];
    private AuditEntry[] auditLog = new AuditEntry[0];

    private AngajatService() {}

    private static class Holder {
        private static final AngajatService INSTANCE = new AngajatService();
    }

    public static AngajatService getInstance() {
        return Holder.INSTANCE;
    }

    private void logAction(String action, String target) {
        AuditEntry entry = new AuditEntry(action, target, LocalDateTime.now().toString());
        AuditEntry[] tmp = new AuditEntry[auditLog.length + 1];
        System.arraycopy(auditLog, 0, tmp, 0, auditLog.length);
        tmp[tmp.length - 1] = entry;
        auditLog = tmp;
    }

    public void addAngajat(Angajat a) {
        Angajat[] tmp = new Angajat[angajati.length + 1];
        System.arraycopy(angajati, 0, tmp, 0, angajati.length);
        tmp[tmp.length - 1] = a;
        angajati = tmp;
        
        logAction("ADD", a.getNume()); // Audit aici
        System.out.println("Angajat adaugat: " + a.getNume());
    }

    public void listBySalary() {
        Angajat[] copy = angajati.clone();
        Arrays.sort(copy);
        System.out.println("--- Angajati dupa salariu (descrescător) ---");
        for (int i = 0; i < copy.length; i++) {
            System.out.println((i + 1) + ". " + copy[i]);
        }
    }

    public void findByDepartament(String numeDept) {
        logAction("FIND_BY_DEPT", numeDept); // Audit aici
        boolean found = false;
        for (Angajat a : angajati) {
            if (a.getDepartament().nume().equalsIgnoreCase(numeDept)) {
                System.out.println(a);
                found = true;
            }
        }
        if (!found) System.out.println("Niciun angajat în departamentul: " + numeDept);
    }

    public void printAuditLog() {
        System.out.println("--- Audit Log ---");
        for (AuditEntry entry : auditLog) {
            System.out.println(entry);
        }
    }
}