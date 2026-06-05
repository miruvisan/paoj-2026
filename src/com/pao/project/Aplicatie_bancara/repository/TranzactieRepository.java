package com.pao.project.Aplicatie_bancara.repository;

import com.pao.project.Aplicatie_bancara.model.Tranzactie;
import com.pao.project.Aplicatie_bancara.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TranzactieRepository implements Repository<Tranzactie, String> {
    @Override
    public void save(Tranzactie tranzactie) {
        String sql = "INSERT INTO transactions (id, suma, tip, data, account_iban) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            populateStatement(statement, tranzactie);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea tranzactiei.", e);
        }
    }

    public void save(Connection connection, Tranzactie tranzactie) throws SQLException {
        String sql = "INSERT INTO transactions (id, suma, tip, data, account_iban) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            populateStatement(statement, tranzactie);
            statement.executeUpdate();
        }
    }

    @Override
    public Optional<Tranzactie> findById(String id) {
        String sql = "SELECT * FROM transactions WHERE id = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapTranzactie(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea tranzactiei dupa id.", e);
        }
    }

    @Override
    public List<Tranzactie> findAll() {
        String sql = "SELECT * FROM transactions";
        List<Tranzactie> tranzactii = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                tranzactii.add(mapTranzactie(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea tranzactiilor.", e);
        }
        return tranzactii;
    }

    public List<Tranzactie> findByAccountIban(String iban) {
        String sql = "SELECT * FROM transactions WHERE account_iban = ? ORDER BY data DESC";
        List<Tranzactie> tranzactii = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, iban);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    tranzactii.add(mapTranzactie(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea tranzactiilor dupa IBAN.", e);
        }
        return tranzactii;
    }

    @Override
    public void update(Tranzactie tranzactie) {
        String sql = "UPDATE transactions SET suma = ?, tip = ?, data = ?, account_iban = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, tranzactie.getSuma());
            statement.setString(2, tranzactie.getTip());
            statement.setTimestamp(3, Timestamp.valueOf(tranzactie.getData()));
            statement.setString(4, tranzactie.getIbanCont());
            statement.setString(5, tranzactie.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea tranzactiei.", e);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM transactions WHERE id = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea tranzactiei.", e);
        }
    }

    private void populateStatement(PreparedStatement statement, Tranzactie tranzactie) throws SQLException {
        statement.setString(1, tranzactie.getId());
        statement.setDouble(2, tranzactie.getSuma());
        statement.setString(3, tranzactie.getTip());
        statement.setTimestamp(4, Timestamp.valueOf(tranzactie.getData()));
        statement.setString(5, tranzactie.getIbanCont());
    }

    private Tranzactie mapTranzactie(ResultSet resultSet) throws SQLException {
        return new Tranzactie(
                resultSet.getString("id"),
                resultSet.getDouble("suma"),
                resultSet.getString("tip"),
                resultSet.getTimestamp("data").toLocalDateTime(),
                resultSet.getString("account_iban"));
    }
}
