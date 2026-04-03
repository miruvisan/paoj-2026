package com.pao.laboratory06.exercise2;

import java.util.*;

interface PersoanaFizica {}
interface PersoanaJuridica {}

interface IOperatiiCitireScriere {
    void citeste(Scanner in);
    void afiseaza();
    String tipContract();
    default boolean areBonus() { return false; }
}

enum TipColaborator { CIM, PFA, SRL }

abstract class Colaborator implements IOperatiiCitireScriere {
    String nume, prenume;
    double venit_brut_lunar;
    static final double SALARIU_MINIM_BRUT = 48600.0;

    public abstract TipColaborator getTip();
    public abstract double calculeazaVenitNetAnual();

    @Override
    public void afiseaza() {
        System.out.printf("%s: %s %s, venit net anual: %.2f lei\n",
                tipContract(), nume, prenume, calculeazaVenitNetAnual());
    }
}

class CIMColaborator extends Colaborator implements PersoanaFizica {
    private boolean bonus = false;

    @Override
    public void citeste(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        this.venit_brut_lunar = in.nextDouble();
        if (in.hasNext()) {
            String b = in.next();
            this.bonus = b.equalsIgnoreCase("DA");
        }
    }

    @Override
    public String tipContract() { return "CIM"; }
    @Override
    public TipColaborator getTip() { return TipColaborator.CIM; }
    @Override
    public boolean areBonus() { return bonus; }

    @Override
    public double calculeazaVenitNetAnual() {
        double net = venit_brut_lunar * 12 * 0.55;
        return bonus ? net * 1.1 : net;
    }
}

class PFAColaborator extends Colaborator implements PersoanaFizica {
    double cheltuieliLunare;

    @Override
    public void citeste(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        this.venit_brut_lunar = in.nextDouble();
        this.cheltuieliLunare = in.nextDouble();
    }

    @Override
    public String tipContract() { return "PFA"; }
    @Override
    public TipColaborator getTip() { return TipColaborator.PFA; }

    @Override
    public double calculeazaVenitNetAnual() {
        double venitNet = (venit_brut_lunar - cheltuieliLunare) * 12;

        double impozit = venitNet * 0.10;

        double cass;
        double plafon6 = 6 * SALARIU_MINIM_BRUT;
        double plafon72 = 72 * SALARIU_MINIM_BRUT;

        if (venitNet < plafon6) {
            cass = 0.10 * plafon6;
        } else if (venitNet <= plafon72) {
            cass = 0.10 * venitNet;
        } else {
            cass = 0.10 * plafon72;
        }

        double cas = 0;
        double plafon12 = 12 * SALARIU_MINIM_BRUT;
        double plafon24 = 24 * SALARIU_MINIM_BRUT;

        if (venitNet >= plafon24) {
            cas = 0.25 * plafon24;
        } else if (venitNet >= plafon12) {
            cas = 0.25 * plafon12;
        }
        return venitNet - impozit - cass - cas;
    }
}

class SRLColaborator extends Colaborator implements PersoanaJuridica {
    double cheltuieliLunare;

    @Override
    public void citeste(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        this.venit_brut_lunar = in.nextDouble();
        this.cheltuieliLunare = in.nextDouble();
    }

    @Override
    public String tipContract() { return "SRL"; }
    @Override
    public TipColaborator getTip() { return TipColaborator.SRL; }

    @Override
    public double calculeazaVenitNetAnual() {
        return (venit_brut_lunar - cheltuieliLunare) * 12 * 0.84;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        List<Colaborator> colaboratori = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String tip = in.next();
            Colaborator c = switch (tip) {
                case "CIM" -> {
                    CIMColaborator obj = new CIMColaborator();
                    obj.citeste(in);
                    yield obj;
                }
                case "PFA" -> {
                    PFAColaborator obj = new PFAColaborator();
                    obj.citeste(in);
                    yield obj;
                }
                case "SRL" -> {
                    SRLColaborator obj = new SRLColaborator();
                    obj.citeste(in);
                    yield obj;
                }
                default -> throw new IllegalArgumentException("Tip necunoscut: " + tip);
            };
            colaboratori.add(c);
        }
        // Sortează și afișează pe tip, fiecare descrescător după venit net anual
        for (TipColaborator tipColab : TipColaborator.values()) {
            colaboratori.stream()
                    .filter(c -> c.getTip() == tipColab)
                    .sorted((a, b) -> Double.compare(b.calculeazaVenitNetAnual(), a.calculeazaVenitNetAnual()))
                    .forEach(Colaborator::afiseaza);
        }
        // Colaborator cu venit net maxim
        Colaborator max = colaboratori.stream().max(Comparator.comparingDouble(Colaborator::calculeazaVenitNetAnual)).orElse(null);
        System.out.printf("\nColaborator cu venit net maxim: ");
        if (max != null) max.afiseaza();
        // Colaboratori persoane juridice (SRL)
        System.out.println("\nColaboratori persoane juridice:");
        colaboratori.stream()
                .filter(c -> c instanceof PersoanaJuridica)
                .sorted((a, b) -> Double.compare(b.calculeazaVenitNetAnual(), a.calculeazaVenitNetAnual()))
                .forEach(Colaborator::afiseaza);
        // Sume și număr colaboratori pe tip
        System.out.println("\nSume și număr colaboratori pe tip:");
        Map<TipColaborator, Double> suma = new EnumMap<>(TipColaborator.class);
        Map<TipColaborator, Integer> numar = new EnumMap<>(TipColaborator.class);
        var typesOfCollaborators = new HashSet<TipColaborator>();
        for (Colaborator c : colaboratori) {
            typesOfCollaborators.add(c.getTip());
        }
        for (TipColaborator t : typesOfCollaborators) {
            suma.put(t, 0.0);
            numar.put(t, 0);
        }
        for (Colaborator c : colaboratori) {
            TipColaborator t = c.getTip();
            suma.put(t, suma.get(t) + c.calculeazaVenitNetAnual());
            numar.put(t, numar.get(t) + 1);
        }
        for (TipColaborator t : TipColaborator.values()) {
            System.out.printf("%s: suma = %.2f lei, număr = %d\n", t, suma.get(t), numar.get(t));
        }
    }
}