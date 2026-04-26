### 1.1 - Lista cu actiuni si interogari
1. **Inregistrare client**: Adaugarea unui client nou in baza de date a bancii.
2. **Deschidere cont curent**: Crearea unui cont de tip curent pentru un client existent.
3. **Deschidere cont economii**: Crearea unui cont de economii (cu dobanda) pentru un client.
4. **Emitere card**: Atasarea unui card (debit sau credit) unui cont existent.
5. **Depunere numerar**: Adaugarea unei sume de bani intr-un cont.
6. **Retragere numerar**: Extragerea unei sume de bani folosind un card sau direct din cont.
7. **Transfer bancar**: Transferul unei sume intre doua conturi (IBAN sursa -> IBAN destinatie).
8. **Blocare card**: Schimbarea statusului unui card in "blocat" pentru securitate.
9. **Afisare sold**: Interogarea soldului curent al unui cont.
10. **Generare extras de cont**: Listarea tuturor tranzactiilor efectuate pe un anumit cont.

### 1.2 - Tipuri de obiecte (clase)
1. `Client` - contine datele personale ale utilizatorului.
2. `Adresa` - clasa auxiliara pentru adresa clientului.
3. `Cont` (Abstracta) - clasa de baza pentru conturi bancare.
4. `ContCurent` - tip de cont pentru operatiuni zilnice.
5. `ContEconomii` - tip de cont purtator de dobanda.
6. `Card` (Abstracta) - clasa de baza pentru carduri.
7. `CardDebit` - card atasat soldului real.
8. `CardCredit` - card cu limita de credit.
9. `Tranzactie` (Imutabila) - inregistrarea unei operatiuni financiare.
10. `Banca` - clasa centrala care gestioneaza clientii si conturile.
