# --- !Ups
CREATE TABLE "book" (
  "id" SERIAL PRIMARY KEY,
  "title" varchar(200),
  "author" varchar(200),
  "year" INTEGER
);

INSERT INTO "book" values (1, 'The Rebel', 'Albert Camus', 1951);

# --- !Downs
DROP TABLE "book";
