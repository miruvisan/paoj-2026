package com.pao.project.Aplicatie_bancara.repository;

import com.pao.project.Aplicatie_bancara.model.Cont;
import com.pao.project.Aplicatie_bancara.model.ContCurent;
import com.pao.project.Aplicatie_bancara.model.ContEconomii;
import com.pao.project.Aplicatie_bancara.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContRepository implements Repository<Cont, String> {
    private static final String SELECT_BY_IBAN = "SELECT * FROM accounts WHERE iban = ?";

    @Override
    public void save(Cont cont) {
        String sql = "INSERT INTO accounts (iban, sold, client_id, type, limita_descoperire, rata_dobanda) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            populateStatement(statement, cont);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea contului.", e);
        }
    }

    @Override
    public Optional<Cont> findById(String iban) {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            return findById(connection, iban);
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea contului dupa IBAN.", e);
        }
    }

    public Optional<Cont> findById(Connection connection, String iban) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_IBAN)) {
            statement.setString(1, iban);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapCont(resultSet));
                }
            }
            return Optional.empty();
        }
    }

    @Override
    public List<Cont> findAll() {
        String sql = "SELECT * FROM accounts";
        List<Cont> conturi = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                conturi.add(mapCont(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea conturilor.", e);
        }
        return conturi;
    }

    @Override
    public void update(Cont cont) {
        String sql = "UPDATE accounts SET sold = ?, client_id = ?, type = ?, limita_descoperire = ?, rata_dobanda = ? WHERE iban = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, cont.getSold());
            statement.setString(2, cont.getIdClient());
            statement.setString(3, getTipCont(cont));
            if (cont instanceof ContCurent) {
                statement.setDouble(4, ((ContCurent) cont).getLimitaDescoperire());
            } else {
                statement.setNull(4, java.sql.Types.DOUBLE);
            }
            if (cont instanceof ContEconomii) {
                statement.setDouble(5, ((ContEconomii) cont).getRataDobanda());
            } else {
                statement.setNull(5, java.sql.Types.DOUBLE);
            }
            statement.setString(6, cont.getIban());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea contului.", e);
        }
    }

    public void updateSold(Connection connection, String iban, double soldNou) throws SQLException {
        String sql = "UPDATE accounts SET sold = ? WHERE iban = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, soldNou);
            statement.setString(2, iban);
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(String iban) {
        String sql = "DELETE FROM accounts WHERE iban = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, iban);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea contului.", e);
        }
    }

    private void populateStatement(PreparedStatement statement, Cont cont) throws SQLException {
        statement.setString(1, cont.getIban());
        statement.setDouble(2, cont.getSold());
        statement.setString(3, cont.getIdClient());
        statement.setString(4, getTipCont(cont));
        if (cont instanceof ContCurent) {
            statement.setDouble(5, ((ContCurent) cont).getLimitaDescoperire());
        } else {
            statement.setNull(5, java.sql.Types.DOUBLE);
        }
        if (cont instanceof ContEconomii) {
            statement.setDouble(6, ((ContEconomii) cont).getRataDobanda());
        } else {
            statement.setNull(6, java.sql.Types.DOUBLE);
        }
    }

    private Cont mapCont(ResultSet resultSet) throws SQLException {
        String iban = resultSet.getString("iban");
        double sold = resultSet.getDouble("sold");
        String clientId = resultSet.getString("client_id");
        String tip = resultSet.getString("type");

        if ("CUR".equalsIgnoreCase(tip)) {
            double limita = resultSet.getDouble("limita_descoperire");
            return new ContCurent(iban, sold, clientId, limita);
        }

        double rata = resultSet.getDouble("rata_dobanda");
        return new ContEconomii(iban, sold, clientId, rata);
    }

    private String getTipCont(Cont cont) {
        if (cont instanceof ContCurent) {
            return "CUR";
        }
        return "ECO";
    }
}
