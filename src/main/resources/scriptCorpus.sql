DROP TABLE "RIJEC";
DROP TABLE "DOKUMENT";
DROP TABLE "SVE_POJAVE_RIJECI";
DROP SEQUENCE "RIJEC_SEQUENCEGENERATOR";
DROP SEQUENCE "DOKUMENT_SEQUENCEGENERATOR";
DROP SEQUENCE "SPR_SEQUENCEGENERATOR";
DROP INDEX rijec_zakon_index;
DROP INDEX rijec_token_index;
DROP INDEX spr_prvi_token_index;

CREATE TABLE "DOKUMENT" (
    "TEKST_ZAKONA_ID" NUMBER NOT NULL, 
    "NAJCESCA_RIJEC_ID" NUMBER
);
/
CREATE TABLE "RIJEC" (

    "TOKEN_ID" NUMBER NOT NULL,
    "TEKST_ZAKONA_ID" NUMBER NOT NULL,
    "POJAVE_U_DOKUMENTU" NUMBER DEFAULT 0,
    "POJAVE_U_KORPUSU" NUMBER DEFAULT 0,
    "TF" NUMBER,
    "IDF" NUMBER,
    "RESULT" NUMBER
);
/
CREATE TABLE "SVE_POJAVE_RIJECI" (
    "SPR_ID" NUMBER NOT NULL,
    "TOKEN_ID_PRVI" NUMBER, 
    "TOKEN_ID_DRUGI" NUMBER,
    CONSTRAINT spr_pk PRIMARY KEY ("SPR_ID")
);
/
CREATE SEQUENCE  "SPR_SEQUENCEGENERATOR"  MINVALUE 1000 MAXVALUE 1000000000 INCREMENT BY 1 START WITH 1000 CACHE 20 ORDER  NOCYCLE;
/
CREATE OR REPLACE TRIGGER "SPR_ON_INSERT"
  BEFORE INSERT ON "SVE_POJAVE_RIJECI"
  FOR EACH ROW
BEGIN
  SELECT "SPR_SEQUENCEGENERATOR".nextval
  INTO :new."SPR_ID"
  FROM dual;
END;
/
CREATE INDEX rijec_zakon_index ON rijec(tekst_zakona_id)
      STORAGE (INITIAL 20K
      NEXT 20k
      PCTINCREASE 75);
/
CREATE INDEX rijec_token_index on rijec(token_id)
      STORAGE (INITIAL 20K
      NEXT 20k
      PCTINCREASE 75);
/
CREATE INDEX spr_prvi_token_index on sve_pojave_rijeci(token_id_prvi)
      STORAGE (INITIAL 20K
      NEXT 20k
      PCTINCREASE 75);
/
COMMIT;