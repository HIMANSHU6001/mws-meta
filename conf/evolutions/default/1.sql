# --- !Ups
CREATE TABLE "book" (
  "id" SERIAL PRIMARY KEY,
  "name" varchar(200),
  "author" varchar(200),
  "year" INTEGER
);

INSERT INTO "book" values (1, 'My first book', 'Orestis Melkonian', 2016);

# --- !Downs
DROP TABLE "book";
