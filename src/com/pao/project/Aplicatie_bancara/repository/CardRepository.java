package com.pao.project.Aplicatie_bancara.repository;

import com.pao.project.Aplicatie_bancara.model.Card;
import com.pao.project.Aplicatie_bancara.model.CardCredit;
import com.pao.project.Aplicatie_bancara.model.CardDebit;
import com.pao.project.Aplicatie_bancara.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardRepository implements Repository<Card, String> {
    @Override
    public void save(Card card) {
        String sql = "INSERT INTO cards (numar, pin, data_expirare, blocat, account_iban, type, limita_credit) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            populateStatement(statement, card);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea cardului.", e);
        }
    }

    @Override
    public Optional<Card> findById(String numarCard) {
        String sql = "SELECT * FROM cards WHERE numar = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, numarCard);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapCard(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea cardului dupa numar.", e);
        }
    }

    @Override
    public List<Card> findAll() {
        String sql = "SELECT * FROM cards";
        List<Card> carduri = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                carduri.add(mapCard(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea cardurilor.", e);
        }
        return carduri;
    }

    @Override
    public void update(Card card) {
        String sql = "UPDATE cards SET pin = ?, data_expirare = ?, blocat = ?, account_iban = ?, type = ?, limita_credit = ? WHERE numar = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, card.getPin());
            statement.setDate(2, Date.valueOf(card.getDataExpirare()));
            statement.setBoolean(3, card.isBlocat());
            statement.setString(4, card.getIbanContAtasat());
            statement.setString(5, getTipCard(card));
            if (card instanceof CardCredit) {
                statement.setDouble(6, ((CardCredit) card).getLimitaCredit());
            } else {
                statement.setNull(6, java.sql.Types.DOUBLE);
            }
            statement.setString(7, card.getNumarCard());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea cardului.", e);
        }
    }

    @Override
    public void delete(String numarCard) {
        String sql = "DELETE FROM cards WHERE numar = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, numarCard);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea cardului.", e);
        }
    }

    private void populateStatement(PreparedStatement statement, Card card) throws SQLException {
        statement.setString(1, card.getNumarCard());
        statement.setString(2, card.getPin());
        statement.setDate(3, Date.valueOf(card.getDataExpirare()));
        statement.setBoolean(4, card.isBlocat());
        statement.setString(5, card.getIbanContAtasat());
        statement.setString(6, getTipCard(card));
        if (card instanceof CardCredit) {
            statement.setDouble(7, ((CardCredit) card).getLimitaCredit());
        } else {
            statement.setNull(7, java.sql.Types.DOUBLE);
        }
    }

    private Card mapCard(ResultSet resultSet) throws SQLException {
        String numar = resultSet.getString("numar");
        String pin = resultSet.getString("pin");
        String iban = resultSet.getString("account_iban");
        String tip = resultSet.getString("type");
        Card card;
        if ("CREDIT".equalsIgnoreCase(tip)) {
            double limita = resultSet.getDouble("limita_credit");
            card = new CardCredit(numar, pin, resultSet.getDate("data_expirare").toLocalDate(), iban, limita);
        } else {
            card = new CardDebit(numar, pin, resultSet.getDate("data_expirare").toLocalDate(), iban);
        }
        card.setBlocat(resultSet.getBoolean("blocat"));
        return card;
    }

    private String getTipCard(Card card) {
        if (card instanceof CardCredit) {
            return "CREDIT";
        }
        return "DEBIT";
    }
}
