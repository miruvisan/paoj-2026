package com.pao.project.Aplicatie_bancara.model;

public abstract class ContOperabil extends Cont {
    protected ContOperabil(String iban, double sold, String idClient) {
        super(iban, sold, idClient);
    }
}