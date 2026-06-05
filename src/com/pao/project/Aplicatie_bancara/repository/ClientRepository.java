package com.pao.project.Aplicatie_bancara.repository;

import com.pao.project.Aplicatie_bancara.model.Adresa;
import com.pao.project.Aplicatie_bancara.model.Client;
import com.pao.project.Aplicatie_bancara.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientRepository implements Repository<Client, String> {
    @Override
    public void save(Client client) {
        String sql = "INSERT INTO clients (id, nume, prenume, cnp, oras, strada, numar) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, client.getId());
            statement.setString(2, client.getNume());
            statement.setString(3, client.getPrenume());
            statement.setString(4, client.getCnp());
            statement.setString(5, client.getAdresa().getOras());
            statement.setString(6, client.getAdresa().getStrada());
            statement.setInt(7, client.getAdresa().getNumar());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea clientului.", e);
        }
    }

    @Override
    public Optional<Client> findById(String id) {
        String sql = "SELECT * FROM clients WHERE id = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapClient(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea clientului dupa id.", e);
        }
    }

    public Optional<Client> findByCnp(String cnp) {
        String sql = "SELECT * FROM clients WHERE cnp = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cnp);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapClient(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea clientului dupa CNP.", e);
        }
    }

    @Override
    public List<Client> findAll() {
        String sql = "SELECT * FROM clients";
        List<Client> clienti = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                clienti.add(mapClient(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea clientilor.", e);
        }
        return clienti;
    }

    @Override
    public void update(Client client) {
        String sql = "UPDATE clients SET nume = ?, prenume = ?, cnp = ?, oras = ?, strada = ?, numar = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, client.getNume());
            statement.setString(2, client.getPrenume());
            statement.setString(3, client.getCnp());
            statement.setString(4, client.getAdresa().getOras());
            statement.setString(5, client.getAdresa().getStrada());
            statement.setInt(6, client.getAdresa().getNumar());
            statement.setString(7, client.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea clientului.", e);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM clients WHERE id = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea clientului.", e);
        }
    }

    private Client mapClient(ResultSet resultSet) throws SQLException {
        Adresa adresa = new Adresa(
                resultSet.getString("oras"),
                resultSet.getString("strada"),
                resultSet.getInt("numar"));
        return new Client(
                resultSet.getString("id"),
                resultSet.getString("nume"),
                resultSet.getString("prenume"),
                resultSet.getString("cnp"),
                adresa);
    }
}
