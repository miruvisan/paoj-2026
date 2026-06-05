package com.pao.project.Aplicatie_bancara.service;

import com.pao.project.Aplicatie_bancara.model.Card;
import com.pao.project.Aplicatie_bancara.model.CardCredit;
import com.pao.project.Aplicatie_bancara.model.CardDebit;
import com.pao.project.Aplicatie_bancara.model.Cont;
import com.pao.project.Aplicatie_bancara.repository.CardRepository;
import com.pao.project.Aplicatie_bancara.repository.ContRepository;
import com.pao.project.Aplicatie_bancara.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CardService {
    private static CardService instance;
    private final CardRepository cardRepository;
    private final ContRepository contRepository;
    private final AuditService auditService;

    private CardService() {
        this.cardRepository = new CardRepository();
        this.contRepository = new ContRepository();
        this.auditService = AuditService.getInstance();
    }

    public static CardService getInstance() {
        if (instance == null) {
            instance = new CardService();
        }
        return instance;
    }

    public Card emiteCardDebit(String iban, String pin) {
        auditService.logAction("emitere_card");
        Cont cont = contRepository.findById(iban).orElse(null);
        if (cont == null) {
            throw new IllegalArgumentException("Contul pentru emiterea cardului nu exista.");
        }
        String numarCard = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        Card card = new CardDebit(numarCard, pin, LocalDate.now().plusYears(4), iban);
        cardRepository.save(card);
        return card;
    }

    public Card emiteCardCredit(String iban, String pin, double limita) {
        auditService.logAction("emitere_card");
        Cont cont = contRepository.findById(iban).orElse(null);
        if (cont == null) {
            throw new IllegalArgumentException("Contul pentru emiterea cardului nu exista.");
        }
        String numarCard = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        Card card = new CardCredit(numarCard, pin, LocalDate.now().plusYears(4), iban, limita);
        cardRepository.save(card);
        return card;
    }

    public void stergeCard(String numarCard) {
        auditService.logAction("stergere_card");
        cardRepository.delete(numarCard);
    }

    public Card cautaCardDupaNumar(String numarCard) {
        auditService.logAction("cautare_card_numar");
        return cardRepository.findById(numarCard).orElse(null);
    }

    public List<Card> listeazaCarduri() {
        auditService.logAction("listare_carduri");
        return cardRepository.findAll();
    }

    public void blocheazaCard(String numarCard) {
        auditService.logAction("blocare_card");
        Card card = cardRepository.findById(numarCard).orElse(null);
        if (card == null) {
            System.out.println("Cardul nu a fost gasit.");
            return;
        }
        card.setBlocat(true);
        cardRepository.update(card);
        System.out.println("Cardul " + numarCard + " a fost blocat.");
    }

    public List<String> getCarduriCuClientSiCont() {
        String sql = """
                SELECT ca.numar, ca.type, a.iban, c.nume, c.prenume
                FROM cards ca
                JOIN accounts a ON ca.account_iban = a.iban
                JOIN clients c ON a.client_id = c.id
                ORDER BY c.nume, c.prenume
                """;
        List<String> rezultate = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String linie = resultSet.getString("nume") + " " +
                        resultSet.getString("prenume") + " | IBAN: " +
                        resultSet.getString("iban") + " | Card: " +
                        resultSet.getString("numar") + " (" +
                        resultSet.getString("type") + ")";
                rezultate.add(linie);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la interogarea cardurilor cu clienti si conturi.", e);
        }
        return rezultate;
    }
}
