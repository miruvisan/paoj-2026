package com.pao.laboratory12.exercise1;

import java.io.*;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.pao.laboratory12.model.Author;
import com.pao.laboratory12.model.Book;
import com.pao.laboratory12.model.Loan;
import com.pao.laboratory12.model.Reader;
import com.pao.laboratory12.repository.AuthorRepository;
import com.pao.laboratory12.repository.BookRepository;
import com.pao.laboratory12.repository.LoanRepository;
import com.pao.laboratory12.repository.ReaderRepository;
import com.pao.laboratory12.util.DatabaseConnection;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== CONFIGURATIE SQLITE ===");

        try {
            initializeSchemaFromFile();

            AuthorRepository authorRepo = new AuthorRepository();
            BookRepository bookRepo = new BookRepository();
            ReaderRepository readerRepo = new ReaderRepository();
            LoanRepository loanRepo = new LoanRepository();

            System.out.println("\n--- CREATE ---");

            Author author = new Author("Stephen King", "USA");
            authorRepo.save(author);
            System.out.println("Autor salvat cu ID: " + author.getId());

            Book book = new Book("The Shining", author.getId());
            bookRepo.save(book);
            System.out.println("Carte salvata cu ID: " + book.getId());

            Reader reader = new Reader("Miruna Alexandra", "miruna@unibuc.ro");
            readerRepo.save(reader);
            System.out.println("Cititor salvat cu ID: " + reader.getId());

            Loan loan = new Loan(book.getId(), reader.getId(), "2026-06-04");
            loanRepo.save(loan);
            System.out.println("Imprumut inregistrat cu ID: " + loan.getId());

            System.out.println("\n--- READ ---");

            Optional<Author> foundAuthor = authorRepo.findById(author.getId());
            foundAuthor.ifPresent(a -> System.out.println("findById a gasit: " + a));

            List<Book> allBooks = bookRepo.findAll();
            System.out.println("findAll carti: " + allBooks);

            System.out.println("\n--- UPDATE ---");

            reader.setEmail("miruna.visan@s.unibuc.ro");
            readerRepo.update(reader);
            System.out.println("Cititor actualizat în DB: " + readerRepo.findById(reader.getId()).orElseThrow());

            System.out.println("\n--- DELETE ---");

            System.out.println("Stergem imprumutul cu ID: " + loan.getId());
            loanRepo.delete(loan.getId());

            if (loanRepo.findById(loan.getId()).isEmpty()) {
                System.out.println("Confirmare: Imprumutul a fost sters cu succes.");
            }

            System.out.println("\nTOATE TESTELE PE COLOANE AU TRECUT!");

        } catch (Exception e) {
            System.err.println("\nEroare la executie: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void initializeSchemaFromFile() throws Exception {
        Connection conn = DatabaseConnection.getInstance().getConnection();

        File sqlFile = new File("src/com/pao/laboratory12/resources/schema-sqlite.sql");

        if (!sqlFile.exists()) {
            throw new java.io.FileNotFoundException("Nu am gasit schema-sqlite.sql la calea: " + sqlFile.getAbsolutePath());
        }

        String sqlScript;
        try (FileInputStream fis = new FileInputStream(sqlFile);
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
            sqlScript = reader.lines().collect(Collectors.joining("\n"));
        }

        String[] statements = sqlScript.split(";");
        try (Statement stmt = conn.createStatement()) {
            for (String sql : statements) {
                String cleanSql = sql.replaceAll("--.*", "").trim();

                if (!cleanSql.isEmpty()) {
                    stmt.execute(cleanSql);
                }
            }
        }
        System.out.println("Baza de date SQLite a fost initializata cu succes!");
        System.out.println("Baza de date SQLite a fost initializata cu succes!");
    }
}