package com.pao.project.Aplicatie_bancara.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

public class AuditService {
    private static AuditService instance;
    private static final Path AUDIT_PATH = Path.of("audit.csv");

    private AuditService() {}

    public static synchronized AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    public synchronized void logAction(String actionName) {
        boolean exists = Files.exists(AUDIT_PATH);
        try (BufferedWriter writer = Files.newBufferedWriter(AUDIT_PATH,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            if (!exists) {
                writer.write("nume_actiune,timestamp");
                writer.newLine();
            }
            writer.write(actionName + "," + LocalDateTime.now());
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Eroare la scrierea fisierului de audit.", e);
        }
    }
}
