package com.pao.proiect.bank;

import com.pao.proiect.bank.model.*;
import com.pao.proiect.bank.service.*;
import com.pao.proiect.bank.exception.*;

public class Main {
    public static void main(String[] args) {
        Banca banca = new Banca("MIRUNA Bank");
        ClientService clientService = ClientService.getInstance();
        ContService contService = ContService.getInstance();
        CardService cardService = CardService.getInstance();

        try {
            System.out.println("SISTEM BANCAR");

            System.out.println("\n1. Inregistrare clienti...");
            Client c1 = clientService.adaugaClient(banca, "Popescu", "Ion", "1234567890123", "Bucuresti", "Str. Academiei", 14);
            Client c2 = clientService.adaugaClient(banca, "Ionescu", "Maria", "2234567890123", "Cluj", "Str. Observatorului", 2);
            clientService.afiseazaClienti(banca);

            // Scuze ca e hardcodat, la etapa 2 nu se repeta
            System.out.println("\n2. Deschidere cont curent pentru Popescu Ion...");
            Cont contC1 = contService.deschideContCurent(banca, c1, 500.0, 1000.0);
            System.out.println("Cont creat: " + contC1.getIban());

            System.out.println("\n3. Deschidere cont economii pentru Ionescu Maria...");
            Cont contE2 = contService.deschideContEconomii(banca, c2, 10000.0, 0.05);
            System.out.println("Cont creat: " + contE2.getIban());

            System.out.println("\n4. Emitere card debit pentru Popescu Ion...");
            Card card1 = cardService.emiteCardDebit(banca, contC1.getIban(), "1234");
            System.out.println("Card emis: " + card1.getNumarCard());

            System.out.println("\n5. Depunere 200 RON in contul lui Ion...");
            contService.depunere(banca, contC1.getIban(), 200.0);
            System.out.println("Sold nou Ion: " + contC1.getSold());

            System.out.println("\n6. Retragere 100 RON din contul Mariei...");
            contService.retragere(banca, contE2.getIban(), 100.0);
            System.out.println("Sold nou Maria: " + contE2.getSold());

            System.out.println("\n7. Transfer 300 RON de la Maria la Ion...");
            contService.transfer(banca, contE2.getIban(), contC1.getIban(), 300.0);
            System.out.println("Sold Ion: " + contC1.getSold());
            System.out.println("Sold Maria: " + contE2.getSold());

            System.out.println("\n8. Blocare card Ion...");
            cardService.blocheazaCard(banca, card1.getNumarCard());
            System.out.println("Status card: " + (card1.isBlocat() ? "Blocat" : "Activ"));

            System.out.println("\n9. Interogare sold final Popescu Ion...");
            Cont c = banca.getConturi().get(contC1.getIban());
            System.out.println("IBAN: " + c.getIban() + " | Sold: " + c.getSold());

            System.out.println("\n10. Generare extras de cont pentru Maria...");
            contService.afiseazaExtrasCont(banca, contE2.getIban());

            System.out.println("\n- Testare exceptie FonduriInsuficiente");
            contService.retragere(banca, contC1.getIban(), 5000.0);

        } catch (ContNegasitException | FonduriInsuficienteException e) {
            System.err.println("EROARE: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
