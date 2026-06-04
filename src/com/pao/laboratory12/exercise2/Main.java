package com.pao.laboratory12.exercise2;

import com.pao.laboratory12.model.*;
import com.pao.laboratory12.repository.*;
import com.pao.laboratory12.service.AuditService;
import com.pao.laboratory12.service.LibraryService;
import com.pao.laboratory12.util.DatabaseConnection;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== ETAPA II - REZOLVARE EXERCITIUL 2 ===\n");

        AuditService audit = AuditService.getInstance();
        LibraryService libraryService = LibraryService.getInstance();

        AuthorRepository authorRepo = new AuthorRepository();
        BookRepository bookRepo = new BookRepository();
        ReaderRepository readerRepo = new ReaderRepository();

        try {
            try (java.sql.Statement st = DatabaseConnection.getInstance().getConnection().createStatement()) {
                st.executeUpdate("DELETE FROM loan; DELETE FROM book; DELETE FROM reader; DELETE FROM author;");
            } catch (Exception ignored) {}

            Author author = new Author();
            author.setName("Stephen King");
            author.setCountry("USA");
            authorRepo.save(author);
            audit.log("add_author");
            System.out.println("1. [AUDIT] Autor salvat: " + author);

            Book book1 = new Book();
            book1.setTitle("The Shining");
            book1.setAuthorId(author.getId());
            book1.setAvailable(true);
            bookRepo.save(book1);

            Book book2 = new Book();
            book2.setTitle("It");
            book2.setAuthorId(author.getId());
            book2.setAvailable(true);
            bookRepo.save(book2);

            audit.log("add_book");
            System.out.println("2. [AUDIT] Carti adaugate: " + book1.getTitle() + ", " + book2.getTitle());

            Reader reader = new Reader();
            reader.setName("Miruna Alexandra");
            reader.setEmail("miruna.visan@s.unibuc.ro");
            readerRepo.save(reader);
            audit.log("add_reader");
            System.out.println("3. [AUDIT] Cititor adaugat: " + reader.getName());

            List<Book> books = bookRepo.findAll();
            audit.log("list_books");
            System.out.println("4. [AUDIT] Toate cartile disponibile in sistem:");
            books.forEach(b -> System.out.println("   - " + b.getTitle()));

            audit.log("find_book_by_id");
            bookRepo.findById(book1.getId()).ifPresent(b ->
                    System.out.println("5. [AUDIT] Carte gasita dupa ID: " + b.getTitle())
            );

            book2.setTitle("It - Editie Revizuita");
            bookRepo.update(book2);
            audit.log("update_book");
            System.out.println("6. [AUDIT] Carte actualizata cu succes: " + book2.getTitle());

            long loanId = libraryService.borrowBook(reader.getId(), book1.getId());
            audit.log("borrow_book");
            System.out.println("7. [AUDIT] Tranzactie finalizata! Imprumut creat cu succes, ID=" + loanId);

            System.out.println("\n--- Se testeaza mecanismul de Rollback ---");
            try {
                libraryService.borrowBook(reader.getId(), book1.getId());
            } catch (SQLException e) {
                System.out.println("[ROLLBACK DEMONSTRAT CU SUCCES] " + e.getMessage());
            }
            System.out.println("------------------------------------------\n");

            libraryService.returnBook(loanId);
            audit.log("return_book");
            System.out.println("8. [AUDIT] Carte returnata și marcata ca disponibila.");

            List<String> activeLoans = libraryService.getActiveLoansWithDetails();
            audit.log("report_active_loans");
            System.out.println("9. [AUDIT] Raport JOIN - Imprumuturi Active:");
            if (activeLoans.isEmpty()) System.out.println("   (Niciun imprumut activ in acest moment)");
            activeLoans.forEach(line -> System.out.println("   " + line));

            System.out.println("   -> Raport JOIN - Top Carti:");
            libraryService.getTopBorrowedBooksWithAuthor().forEach(line -> System.out.println("      " + line));

            try (java.sql.Statement st = DatabaseConnection.getInstance().getConnection().createStatement()) {
                st.executeUpdate("DELETE FROM loan");
            }

            readerRepo.delete(reader.getId());
            audit.log("delete_reader");
            System.out.println("10. [AUDIT] Cititor sters din sistem cu succes.");

            System.out.println("\nTOATE CELE 10 ACTIUNI DE AUDIT AU FOST SALVATE ÎN audit.csv!");

        } catch (Exception e) {
            System.err.println("Eroare în fluxul principal: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                DatabaseConnection.getInstance().close();
            } catch (Exception ignored) {}
        }
    }
}