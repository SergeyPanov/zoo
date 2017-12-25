-------------------------------------------Create basic tables

CREATE TABLE Kotec(
  ID VARCHAR(255) PRIMARY KEY,
  geometrie SDO_GEOMETRY NOT NULL
);



CREATE TABLE Jezero(
  ID VARCHAR(255) PRIMARY KEY,
  geometrie SDO_GEOMETRY NOT NULL
);

CREATE TABLE Zvire(
  ID VARCHAR(255) PRIMARY KEY,
  jmeno VARCHAR(15),
  druh VARCHAR(20) NOT NULL,

  fotka ORDSYS.ORDImage,
  fotka_ac ORDSYS.SI_AverageColor,
  fotka_pc ORDSYS.SI_PositionalColor,
  fotka_ch ORDSYS.SI_ColorHistogram,
  fotka_tx ORDSYS.SI_Texture,
  fotka_si ORDSYS.SI_StillImage,

  od date,
  do date,
  kotec_id VARCHAR(255),
  CONSTRAINT fk_zvire_kotec
  FOREIGN KEY (kotec_id)
  REFERENCES Kotec(ID) ON DELETE CASCADE

);

CREATE TABLE Zahon(
  ID VARCHAR(255) PRIMARY KEY,
  geometrie SDO_GEOMETRY NOT NULL,
  jmeno VARCHAR(64)
);

CREATE TABLE Kvetina(
  ID VARCHAR(255) PRIMARY KEY,
  jmeno VARCHAR(15),
  druh VARCHAR(20) NOT NULL,
  geometrie SDO_GEOMETRY NOT NULL,

  fotka ORDSYS.ORDImage,
  fotka_ac ORDSYS.SI_AverageColor,
  fotka_pc ORDSYS.SI_PositionalColor,
  fotka_ch ORDSYS.SI_ColorHistogram,
  fotka_tx ORDSYS.SI_Texture,
  fotka_si ORDSYS.SI_StillImage,

  od DATE,
  do DATE,

  t_od DATE,
  t_do DATE,

  new_plant_id VARCHAR(255),

  zahon_id VARCHAR(255),
  CONSTRAINT fk_kvetina_zahon
  FOREIGN KEY (zahon_id)
  REFERENCES Zahon(ID)
  ON DELETE CASCADE
);

CREATE TABLE Stezka (
  ID VARCHAR(255) PRIMARY KEY,
  geometrie SDO_GEOMETRY NOT NULL
);

COMMIT;
