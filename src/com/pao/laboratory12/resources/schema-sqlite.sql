DROP TABLE IF EXISTS loan;
DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS reader;
DROP TABLE IF EXISTS author;

CREATE TABLE author (
    id      INTEGER PRIMARY KEY AUTOINCREMENT,
    name    TEXT    NOT NULL,
    country TEXT
);

CREATE TABLE book (
    id        INTEGER PRIMARY KEY AUTOINCREMENT,
    title     TEXT    NOT NULL,
    author_id INTEGER NOT NULL,
    available INTEGER NOT NULL DEFAULT 1,       -- 1=disponibil, 0=imprumutat
    FOREIGN KEY (author_id) REFERENCES author(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE reader (
    id    INTEGER PRIMARY KEY AUTOINCREMENT,
    name  TEXT NOT NULL,
    email TEXT
);

CREATE TABLE loan (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    book_id     INTEGER NOT NULL,
    reader_id   INTEGER NOT NULL,
    loan_date   TEXT    NOT NULL,              -- format ISO: "YYYY-MM-DD"
    return_date TEXT,                          -- NULL = imprumut activ
    FOREIGN KEY (book_id)   REFERENCES book(id),
    FOREIGN KEY (reader_id) REFERENCES reader(id)
);

