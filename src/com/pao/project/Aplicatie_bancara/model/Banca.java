package com.pao.project.Aplicatie_bancara.model;

import java.util.*;

public class Banca {
    private String nume;
    private Set<Client> clienti;
    private Map<String, Cont> conturi;
    private List<Card> carduri;

    public Banca(String nume) {
        this.nume = nume;
        this.clienti = new TreeSet<>();
        this.conturi = new HashMap<>();
        this.carduri = new ArrayList<>();
    }

    public String getNume() { return nume; }
    public Set<Client> getClienti() { return clienti; }
    public Map<String, Cont> getConturi() { return conturi; }
    public List<Card> getCarduri() { return carduri; }

    @Override
    public String toString() {
        return "Banca " + nume + " {" +
                "clienti=" + clienti.size() +
                ", conturi=" + conturi.size() +
                '}';
    }
}
