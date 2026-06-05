package com.pao.project.Aplicatie_bancara.service;

import com.pao.project.Aplicatie_bancara.model.Adresa;
import com.pao.project.Aplicatie_bancara.model.Client;
import com.pao.project.Aplicatie_bancara.repository.ClientRepository;
import com.pao.project.Aplicatie_bancara.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientService {
    private static ClientService instance;
    private final ClientRepository clientRepository;
    private final AuditService auditService;

    private ClientService() {
        this.clientRepository = new ClientRepository();
        this.auditService = AuditService.getInstance();
    }

    public static ClientService getInstance() {
        if (instance == null) {
            instance = new ClientService();
        }
        return instance;
    }

    public Client adaugaClient(String nume, String prenume, String cnp, String oras, String strada, int numar) {
        auditService.logAction("inregistrare_client");
        if (clientRepository.findByCnp(cnp).isPresent()) {
            throw new IllegalArgumentException("Exista deja un client cu acest CNP.");
        }
        Adresa adresa = new Adresa(oras, strada, numar);
        Client client = new Client(UUID.randomUUID().toString(), nume, prenume, cnp, adresa);
        clientRepository.save(client);
        return client;
    }

    public void stergeClient(String id) {
        auditService.logAction("stergere_client");
        clientRepository.delete(id);
    }

    public Client cautaClientDupaId(String id) {
        auditService.logAction("cautare_client_id");
        return clientRepository.findById(id).orElse(null);
    }

    public List<Client> cautaClientiDupaNume(String nume) {
        auditService.logAction("cautare_client_nume");
        return clientRepository.findAll().stream().filter(client -> client.getNume() != null && client.getNume().equalsIgnoreCase(nume)).toList();
    }

    public void afiseazaClienti() {
        List<Client> clienti = clientRepository.findAll();
        System.out.println("--- Lista Clienti ---");
        clienti.forEach(System.out::println);
    }

    public List<Client> listeazaClienti() {
        auditService.logAction("listare_clienti");
        return clientRepository.findAll();
    }

    public Client cautaClientDupaCnp(String cnp) {
        return clientRepository.findByCnp(cnp).orElse(null);
    }

    public List<String> getClientiCuNumarConturi() {
        String sql = """
                SELECT c.nume, c.prenume, COUNT(a.iban) AS nr_conturi
                FROM clients c
                LEFT JOIN accounts a ON c.id = a.client_id
                GROUP BY c.id, c.nume, c.prenume
                ORDER BY c.nume, c.prenume
                """;
        List<String> rezultate = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String linie = resultSet.getString("nume") + " " +
                        resultSet.getString("prenume") + " -> " +
                        resultSet.getInt("nr_conturi") + " conturi";
                rezultate.add(linie);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la interogarea clientilor cu numar de conturi.", e);
        }
        return rezultate;
    }
}
