# --- !Ups

CREATE TABLE "book" (
  "title" varchar(200) PRIMARY KEY,
  "author" varchar(200),
  "year" INTEGER,
  "accountId" varchar(200)
);

INSERT INTO "book" values ('The Rebel', 'Albert Camus', 1951, 'orestis');

CREATE TABLE "account" (
  "name" varchar(200) PRIMARY KEY,
  "password" varchar(200)
);

INSERT INTO "account" values ('orestis', '1234');

# --- !Downs

DROP TABLE "book";
DROP TABLE "account";
