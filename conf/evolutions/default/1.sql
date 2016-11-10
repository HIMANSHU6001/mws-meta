# --- !Ups

CREATE TABLE "book" (
  "id" SERIAL PRIMARY KEY,
  "title" varchar(200),
  "author" varchar(200),
  "year" INTEGER,
  "accountId" varchar(200)
);

INSERT INTO "book" values (1, 'The Rebel', 'Albert Camus', 1951, 'orestis');

CREATE TABLE "account" (
  "name" varchar(200) PRIMARY KEY,
  "password" varchar(200)
);

INSERT INTO "account" values ('orestis', '1234');

# --- !Downs

DROP TABLE "book";
DROP TABLE "account";
