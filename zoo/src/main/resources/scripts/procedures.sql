CREATE OR REPLACE PROCEDURE animal_generateFeatures IS
  cursor c is select * from ZVIRE for update;
  si ORDSYS.SI_StillImage;
  BEGIN
    for cp in c loop
      si := new SI_StillImage(cp.FOTKA.getContent());
      update ZVIRE z set FOTKA_SI = si,
        FOTKA_AC = SI_AverageColor(si),      FOTKA_ch = SI_ColorHistogram(si),
        FOTKA_PC = SI_PositionalColor(si),   FOTKA_tx = SI_Texture(si)
      where Z.id = cp.id;
    end loop;
  END;
/

CREATE OR REPLACE PROCEDURE plant_generateFeatures IS
  cursor c is select * from KVETINA for update;
  si ORDSYS.SI_StillImage;
  BEGIN
    for cp in c loop
      si := new SI_StillImage(cp.FOTKA.getContent());
      update KVETINA z set FOTKA_SI = si,
        FOTKA_AC = SI_AverageColor(si),      FOTKA_ch = SI_ColorHistogram(si),
        FOTKA_PC = SI_PositionalColor(si),   FOTKA_tx = SI_Texture(si)
      where Z.id = cp.id;
    end loop;
  END;
/
