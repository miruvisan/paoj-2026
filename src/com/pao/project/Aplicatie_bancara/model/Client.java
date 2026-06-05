package com.pao.project.Aplicatie_bancara.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Client implements Comparable<Client> {
    private String id;
    private String nume;
    private String prenume;
    private String cnp;
    private Adresa adresa;
    private List<Cont> conturi;

    public Client(String id, String nume, String prenume, String cnp, Adresa adresa) {
        this.id = id;
        this.nume = nume;
        this.prenume = prenume;
        this.cnp = cnp;
        this.adresa = adresa;
        this.conturi = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getNume() { return nume; }
    public String getPrenume() { return prenume; }
    public String getCnp() { return cnp; }
    public Adresa getAdresa() { return adresa; }
    public List<Cont> getConturi() { return conturi; }

    public void setNume(String nume) { this.nume = nume; }
    public void setPrenume(String prenume) { this.prenume = prenume; }
    public void setAdresa(Adresa adresa) { this.adresa = adresa; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(cnp, client.cnp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cnp);
    }

    @Override
    public String toString() {
        return "Client{" +
                "nume='" + nume + '\'' +
                ", prenume='" + prenume + '\'' +
                ", cnp='" + cnp + '\'' +
                ", adresa=" + adresa +
                '}';
    }

    @Override
    public int compareTo(Client o) {
        return (this.nume + this.prenume).compareTo(o.nume + o.prenume);
    }
}
