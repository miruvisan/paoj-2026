package com.pao.project.Aplicatie_bancara.service;

import com.pao.project.Aplicatie_bancara.exception.FonduriInsuficienteException;
import com.pao.project.Aplicatie_bancara.exception.ContNegasitException;
import com.pao.project.Aplicatie_bancara.model.Cont;
import com.pao.project.Aplicatie_bancara.model.ContCurent;
import com.pao.project.Aplicatie_bancara.model.ContEconomii;
import com.pao.project.Aplicatie_bancara.model.Client;
import com.pao.project.Aplicatie_bancara.model.Tranzactie;
import com.pao.project.Aplicatie_bancara.repository.ContRepository;
import com.pao.project.Aplicatie_bancara.repository.TranzactieRepository;
import com.pao.project.Aplicatie_bancara.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ContService {
    private static ContService instance;
    private final ContRepository contRepository;
    private final TranzactieRepository tranzactieRepository;
    private final AuditService auditService;

    private ContService() {
        this.contRepository = new ContRepository();
        this.tranzactieRepository = new TranzactieRepository();
        this.auditService = AuditService.getInstance();
    }

    public static ContService getInstance() {
        if (instance == null) {
            instance = new ContService();
        }
        return instance;
    }

    public Cont deschideContCurent(Client client, double soldInitial, double limita) {
        auditService.logAction("deschidere_cont_curent");
        String iban = "RO" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        ContCurent cont = new ContCurent(iban, soldInitial, client.getId(), limita);
        contRepository.save(cont);
        return cont;
    }

    public Cont deschideContEconomii(Client client, double soldInitial, double dobanda) {
        auditService.logAction("deschidere_cont_economii");
        String iban = "RO" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        ContEconomii cont = new ContEconomii(iban, soldInitial, client.getId(), dobanda);
        contRepository.save(cont);
        return cont;
    }

    public void stergeCont(String iban) {
        auditService.logAction("stergere_cont");
        contRepository.delete(iban);
    }

    public Cont cautaContDupaIban(String iban) {
        auditService.logAction("cautare_cont_iban");
        return contRepository.findById(iban).orElse(null);
    }

    public List<Cont> listeazaConturi() {
        auditService.logAction("listare_conturi");
        return contRepository.findAll();
    }

    public void depunere(String iban, double suma) throws ContNegasitException {
        auditService.logAction("depunere_numerar");
        Cont cont = contRepository.findById(iban).orElse(null);
        if (cont == null)
            throw new ContNegasitException("Contul cu IBAN-ul " + iban + " nu exista.");

        cont.setSold(cont.getSold() + suma);
        contRepository.update(cont);
        tranzactieRepository
                .save(new Tranzactie(UUID.randomUUID().toString(), suma, "DEPUNERE", LocalDateTime.now(), iban));
    }

    public void retragere(String iban, double suma) throws ContNegasitException, FonduriInsuficienteException {
        auditService.logAction("retragere_numerar");
        Cont cont = contRepository.findById(iban).orElse(null);
        if (cont == null)
            throw new ContNegasitException("Contul cu IBAN-ul " + iban + " nu exista.");

        double limita = 0;
        if (cont instanceof ContCurent) {
            limita = ((ContCurent) cont).getLimitaDescoperire();
        }

        if (cont.getSold() + limita < suma) {
            throw new FonduriInsuficienteException("Fonduri insuficiente pentru retragere.");
        }

        cont.setSold(cont.getSold() - suma);
        contRepository.update(cont);
        tranzactieRepository
                .save(new Tranzactie(UUID.randomUUID().toString(), suma, "RETRAGERE", LocalDateTime.now(), iban));
    }

    public void transfer(String ibanSursa, String ibanDestinatie, double suma)
            throws ContNegasitException, FonduriInsuficienteException {
        auditService.logAction("transfer_bancar");
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            connection.setAutoCommit(false);
            try {
                Cont sursa = contRepository.findById(connection, ibanSursa).orElse(null);
                Cont destinatie = contRepository.findById(connection, ibanDestinatie).orElse(null);
                if (sursa == null || destinatie == null) {
                    throw new ContNegasitException("Unul dintre conturi nu exista.");
                }

                double limita = 0;
                if (sursa instanceof ContCurent) {
                    limita = ((ContCurent) sursa).getLimitaDescoperire();
                }
                if (sursa.getSold() + limita < suma) {
                    throw new FonduriInsuficienteException("Fonduri insuficiente pentru transfer.");
                }

                double soldSursaNou = sursa.getSold() - suma;
                double soldDestNou = destinatie.getSold() + suma;
                contRepository.updateSold(connection, ibanSursa, soldSursaNou);
                contRepository.updateSold(connection, ibanDestinatie, soldDestNou);

                tranzactieRepository.save(connection, new Tranzactie(
                        UUID.randomUUID().toString(), suma, "TRANSFER_TRIMIS", LocalDateTime.now(), ibanSursa));
                tranzactieRepository.save(connection, new Tranzactie(
                        UUID.randomUUID().toString(), suma, "TRANSFER_PRIMIT", LocalDateTime.now(), ibanDestinatie));

                connection.commit();
            } catch (Exception e) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    throw new RuntimeException("Eroare la rollback-ul transferului.", rollbackException);
                }
                if (e instanceof ContNegasitException) {
                    throw (ContNegasitException) e;
                }
                if (e instanceof FonduriInsuficienteException) {
                    throw (FonduriInsuficienteException) e;
                }
                throw new RuntimeException("Eroare la procesarea transferului.", e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la procesarea transferului.", e);
        }
    }

    public double afiseazaSold(String iban) throws ContNegasitException {
        auditService.logAction("afisare_sold");
        Cont cont = contRepository.findById(iban).orElse(null);
        if (cont == null)
            throw new ContNegasitException("Contul nu exista.");
        return cont.getSold();
    }

    public void afiseazaExtrasCont(String iban) throws ContNegasitException {
        auditService.logAction("generare_extras_cont");
        Cont cont = contRepository.findById(iban).orElse(null);
        if (cont == null)
            throw new ContNegasitException("Contul nu exista.");

        System.out.println("--- Extras Cont " + iban + " ---");
        System.out.println("Sold curent: " + cont.getSold());
        List<Tranzactie> tranzactii = tranzactieRepository.findByAccountIban(iban);
        for (Tranzactie t : tranzactii) {
            System.out.println(t);
        }
    }

    public List<String> getTranzactiiCuClientSiCont() {
        String sql = """
                SELECT t.id, t.tip, t.suma, t.data, a.iban, c.nume, c.prenume
                FROM transactions t
                JOIN accounts a ON t.account_iban = a.iban
                JOIN clients c ON a.client_id = c.id
                ORDER BY t.data DESC
                """;
        List<String> rezultate = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String linie = resultSet.getString("nume") + " " +
                        resultSet.getString("prenume") + " | IBAN: " +
                        resultSet.getString("iban") + " | " +
                        resultSet.getString("tip") + " " +
                        resultSet.getDouble("suma") + " (" +
                        resultSet.getTimestamp("data") + ")";
                rezultate.add(linie);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la interogarea tranzactiilor cu clienti si conturi.", e);
        }
        return rezultate;
    }
}
