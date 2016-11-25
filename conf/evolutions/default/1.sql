-- noinspection SqlNoDataSourceInspectionForFile
-- noinspection SqlDialectInspectionForFile

# --- !Ups

CREATE TABLE "account" (
  "name" varchar(200) PRIMARY KEY,
  "password" varchar(200)
);
INSERT INTO "account" values ('orestis', '1234');
# --- !Downs
DROP TABLE "account";