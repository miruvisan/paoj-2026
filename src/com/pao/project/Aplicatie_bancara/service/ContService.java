package com.pao.proiect.bank.service;

import com.pao.proiect.bank.exception.FonduriInsuficienteException;
import com.pao.proiect.bank.exception.ContNegasitException;
import com.pao.proiect.bank.model.*;

import java.time.LocalDateTime;
import java.util.UUID;

public class ContService {
    private static ContService instance;

    private ContService() {}

    public static ContService getInstance() {
        if (instance == null) {
            instance = new ContService();
        }
        return instance;
    }

    public Cont deschideContCurent(Banca banca, Client client, double soldInitial, double limita) {
        String iban = "RO" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        ContCurent cont = new ContCurent(iban, soldInitial, client.getId(), limita);
        banca.getConturi().put(iban, cont);
        client.getConturi().add(cont);
        return cont;
    }

    public Cont deschideContEconomii(Banca banca, Client client, double soldInitial, double dobanda) {
        String iban = "RO" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        ContEconomii cont = new ContEconomii(iban, soldInitial, client.getId(), dobanda);
        banca.getConturi().put(iban, cont);
        client.getConturi().add(cont);
        return cont;
    }

    public void depunere(Banca banca, String iban, double suma) throws ContNegasitException {
        Cont cont = banca.getConturi().get(iban);
        if (cont == null) throw new ContNegasitException("Contul cu IBAN-ul " + iban + " nu exista.");
        
        cont.setSold(cont.getSold() + suma);
        cont.adaugaTranzactie(new Tranzactie(UUID.randomUUID().toString(), suma, "DEPUNERE", LocalDateTime.now()));
    }

    public void retragere(Banca banca, String iban, double suma) throws ContNegasitException, FonduriInsuficienteException {
        Cont cont = banca.getConturi().get(iban);
        if (cont == null) throw new ContNegasitException("Contul cu IBAN-ul " + iban + " nu exista.");
        
        double limita = 0;
        if (cont instanceof ContCurent) {
            limita = ((ContCurent) cont).getLimitaDescoperire();
        }

        if (cont.getSold() + limita < suma) {
            throw new FonduriInsuficienteException("Fonduri insuficiente pentru retragere.");
        }

        cont.setSold(cont.getSold() - suma);
        cont.adaugaTranzactie(new Tranzactie(UUID.randomUUID().toString(), suma, "RETRAGERE", LocalDateTime.now()));
    }

    public void transfer(Banca banca, String ibanSursa, String ibanDestinatie, double suma) throws ContNegasitException, FonduriInsuficienteException {
        Cont sursa = banca.getConturi().get(ibanSursa);
        Cont destinatie = banca.getConturi().get(ibanDestinatie);

        if (sursa == null || destinatie == null) throw new ContNegasitException("Unul dintre conturi nu exista.");

        retragere(banca, ibanSursa, suma);
        destinatie.setSold(destinatie.getSold() + suma);
        destinatie.adaugaTranzactie(new Tranzactie(UUID.randomUUID().toString(), suma, "TRANSFER_PRIMITE", LocalDateTime.now()));
    }

    public void afiseazaExtrasCont(Banca banca, String iban) throws ContNegasitException {
        Cont cont = banca.getConturi().get(iban);
        if (cont == null) throw new ContNegasitException("Contul nu exista.");

        System.out.println("--- Extras Cont " + iban + " ---");
        System.out.println("Sold curent: " + cont.getSold());
        for (Tranzactie t : cont.getTranzactii()) {
            System.out.println(t);
        }
    }
}
