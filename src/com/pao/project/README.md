# Proiect Individual - Programare Avansata pe Obiecte in Java (2026)

## Actiuni posibile
1. Inregistrare client
2. Deschidere cont curent
3. Deschidere cont economii
4. Emitere card
5. Depunere numerar
6. Retragere numerar
7. Transfer bancar
8. Blocare card
9. Afisare sold
10. Generare extras de cont

## Tipuri de obiecte
1. Client
2. Adresa
3. Cont
4. ContOperabil
5. ContCurent
6. ContEconomii
7. Card
8. CardDebit
9. CardCredit
10. Tranzactie

## Cerinte etapa I
- 8+ clase de domeniu
- atribute private sau protected cu getteri si setteri unde este necesar
- toString, equals si hashCode in cel putin 2 clase
- mostenire cu minim 2 niveluri
- clasa abstracta sau interfata in ierarhie
- clasa imutabila
- 2 exceptii custom
- 2 tipuri de colectii, una sortata, si un Map
- 2 servicii Singleton
- Main care demonstreaza cele 10 actiuni

## Cerinte etapa II
- schema.sql la radacina proiectului
- db.properties in resources
- DatabaseConnection Singleton
- Repository generic si 4 repository-uri concrete
- PreparedStatement si try-with-resources
- tranzactie JDBC cu commit si rollback
- 3 interogari JOIN
- AuditService thread-safe si apelat din actiuni
