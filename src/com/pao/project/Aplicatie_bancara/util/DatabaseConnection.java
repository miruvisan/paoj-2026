package com.pao.project.Aplicatie_bancara.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private final String url;
    private final String user;
    private final String password;

    private DatabaseConnection() {
        Properties properties = new Properties();
        try (InputStream input = openResource("/com/pao/project/Aplicatie_bancara/resources/db.properties",
                "src/com/pao/project/Aplicatie_bancara/resources/db.properties")) {
            if (input == null) {
                throw new IllegalStateException("Nu pot gasi fisierul db.properties in resources.");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Nu pot citi configuratia bazei de date.", e);
        }

        this.url = properties.getProperty("db.url");
        this.user = properties.getProperty("db.user");
        this.password = properties.getProperty("db.password");

        initializeSchemaIfNeeded();
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        Connection connection;
        if (user == null || user.isBlank()) {
            connection = DriverManager.getConnection(url);
        } else {
            connection = DriverManager.getConnection(url, user, password);
        }
        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        }
        return connection;
    }

    private void initializeSchemaIfNeeded() {
        if (!isSqlite()) {
            return;
        }

        try (Connection connection = DriverManager.getConnection(url);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement
                        .executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='clients'")) {
            if (!resultSet.next()) {
                runSchemaScript(connection);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Nu pot initializa schema bazei de date.", e);
        }
    }

    private boolean isSqlite() {
        return url != null && url.startsWith("jdbc:sqlite:");
    }

    private void runSchemaScript(Connection connection) throws SQLException {
        try (InputStream input = openResource("/com/pao/project/Aplicatie_bancara/resources/schema.sql",
                "src/com/pao/project/Aplicatie_bancara/resources/schema.sql")) {
            if (input == null) {
                throw new IllegalStateException("Nu pot gasi fisierul schema.sql in resources.");
            }

            String script = new String(input.readAllBytes(), StandardCharsets.UTF_8);
            String[] statements = script.split(";");

            try (Statement statement = connection.createStatement()) {
                for (String rawStatement : statements) {
                    String sql = rawStatement.trim();
                    if (sql.isEmpty() || sql.startsWith("--")) {
                        continue;
                    }
                    statement.execute(sql);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Nu pot citi schema bazei de date.", e);
        }
    }

    private InputStream openResource(String resourcePath, String fileSystemPath) throws IOException {
        InputStream resourceStream = DatabaseConnection.class.getResourceAsStream(resourcePath);
        if (resourceStream != null) {
            return resourceStream;
        }

        Path path = Paths.get(fileSystemPath);
        if (Files.exists(path)) {
            return Files.newInputStream(path);
        }

        return null;
    }
}
