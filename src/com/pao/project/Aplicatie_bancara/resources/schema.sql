DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS cards;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS clients;

CREATE TABLE clients (
    id VARCHAR(36) PRIMARY KEY,
    nume VARCHAR(100) NOT NULL,
    prenume VARCHAR(100) NOT NULL,
    cnp VARCHAR(13) NOT NULL UNIQUE,
    oras VARCHAR(100),
    strada VARCHAR(100),
    numar INT
);

CREATE TABLE accounts (
    iban VARCHAR(34) PRIMARY KEY,
    sold DOUBLE NOT NULL,
    client_id VARCHAR(36) NOT NULL,
    type VARCHAR(10) NOT NULL,
    limita_descoperire DOUBLE,
    rata_dobanda DOUBLE,
    CONSTRAINT fk_accounts_client FOREIGN KEY (client_id) REFERENCES clients(id)
);

CREATE TABLE cards (
    numar VARCHAR(16) PRIMARY KEY,
    pin VARCHAR(10) NOT NULL,
    data_expirare DATE NOT NULL,
    blocat BOOLEAN NOT NULL,
    account_iban VARCHAR(34) NOT NULL,
    type VARCHAR(10) NOT NULL,
    limita_credit DOUBLE,
    CONSTRAINT fk_cards_account FOREIGN KEY (account_iban) REFERENCES accounts(iban)
);

CREATE TABLE transactions (
    id VARCHAR(36) PRIMARY KEY,
    suma DOUBLE NOT NULL,
    tip VARCHAR(50) NOT NULL,
    data TIMESTAMP NOT NULL,
    account_iban VARCHAR(34) NOT NULL,
    CONSTRAINT fk_transactions_account FOREIGN KEY (account_iban) REFERENCES accounts(iban)
);
