package com.pao.proiect.bank.service;

import com.pao.proiect.bank.model.Adresa;
import com.pao.proiect.bank.model.Banca;
import com.pao.proiect.bank.model.Client;

import java.util.UUID;

public class ClientService {
    private static ClientService instance;

    private ClientService() {}

    public static ClientService getInstance() {
        if (instance == null) {
            instance = new ClientService();
        }
        return instance;
    }

    public Client adaugaClient(Banca banca, String nume, String prenume, String cnp, String oras, String strada, int numar) {
        Adresa adresa = new Adresa(oras, strada, numar);
        Client client = new Client(UUID.randomUUID().toString(), nume, prenume, cnp, adresa);
        banca.getClienti().add(client);
        return client;
    }

    public void afiseazaClienti(Banca banca) {
        System.out.println("--- Lista Clienti " + banca.getNume() + " ---");
        for (Client c : banca.getClienti()) {
            System.out.println(c);
        }
    }

    public Client cautaClientDupaCnp(Banca banca, String cnp) {
        for (Client c : banca.getClienti()) {
            if (c.getCnp().equals(cnp)) {
                return c;
            }
        }
        return null;
    }
}
