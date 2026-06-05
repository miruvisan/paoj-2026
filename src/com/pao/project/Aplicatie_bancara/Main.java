package com.pao.project.Aplicatie_bancara;

import com.pao.project.Aplicatie_bancara.model.*;
import com.pao.project.Aplicatie_bancara.service.*;
import com.pao.project.Aplicatie_bancara.exception.*;

public class Main {
    public static void main(String[] args) {
        ClientService clientService = ClientService.getInstance();
        ContService contService = ContService.getInstance();
        CardService cardService = CardService.getInstance();

        try {
            System.out.println("--- Miruna BANK ---");

            System.out.println("\n1. Inregistrare clienti...");
            Client c1 = clientService.adaugaClient("Popescu", "Ion", "1234567890123", "Bucuresti", "Str. Academiei",
                    14);
            Client c2 = clientService.adaugaClient("Ionescu", "Maria", "2234567890123", "Cluj", "Str. Observatorului",
                    2);
            clientService.afiseazaClienti();

            System.out.println("\n2. Deschidere cont curent pentru Popescu Ion...");
            Cont contC1 = contService.deschideContCurent(c1, 500.0, 1000.0);
            System.out.println("Cont creat: " + contC1.getIban());

            System.out.println("\n3. Deschidere cont economii pentru Ionescu Maria...");
            Cont contE2 = contService.deschideContEconomii(c2, 10000.0, 0.05);
            System.out.println("Cont creat: " + contE2.getIban());

            System.out.println("\n4. Emitere card debit pentru Popescu Ion...");
            Card card1 = cardService.emiteCardDebit(contC1.getIban(), "1234");
            System.out.println("Card emis: " + card1.getNumarCard());

            System.out.println("\n5. Depunere 200 RON in contul lui Ion...");
            contService.depunere(contC1.getIban(), 200.0);
            System.out.println("Sold nou Ion: " + contService.afiseazaSold(contC1.getIban()));

            System.out.println("\n6. Retragere 100 RON din contul Mariei...");
            contService.retragere(contE2.getIban(), 100.0);
            System.out.println("Sold nou Maria: " + contService.afiseazaSold(contE2.getIban()));

            System.out.println("\n7. Transfer 300 RON de la Maria la Ion...");
            contService.transfer(contE2.getIban(), contC1.getIban(), 300.0);
            System.out.println("Sold Ion: " + contService.afiseazaSold(contC1.getIban()));
            System.out.println("Sold Maria: " + contService.afiseazaSold(contE2.getIban()));

            System.out.println("\n8. Blocare card Ion...");
            cardService.blocheazaCard(card1.getNumarCard());

            System.out.println("\n9. Interogare sold final Popescu Ion...");
            System.out.println("IBAN: " + contC1.getIban() + " | Sold: " + contService.afiseazaSold(contC1.getIban()));

            System.out.println("\n10. Generare extras de cont pentru Maria...");
            contService.afiseazaExtrasCont(contE2.getIban());

            System.out.println("\n--- Interogari JOIN ---");
            System.out.println("Clienti cu numar de conturi:");
            clientService.getClientiCuNumarConturi().forEach(System.out::println);

            System.out.println("\nCarduri cu clienti si conturi:");
            cardService.getCarduriCuClientSiCont().forEach(System.out::println);

            System.out.println("\nTranzactii cu clienti si conturi:");
            contService.getTranzactiiCuClientSiCont().forEach(System.out::println);

            System.out.println("\n- Testare exceptie FonduriInsuficiente");
            contService.retragere(contC1.getIban(), 5000.0);

        } catch (ContNegasitException | FonduriInsuficienteException e) {
            System.err.println("EROARE: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
