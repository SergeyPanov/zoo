package cz.vut.fit.zoo.dao;

import cz.vut.fit.zoo.model.Plant;

import java.time.Period;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Interface defines method should be implemented for communication with "Kvetina" table.
 * @author xpanov00
 */
public interface PlantDAO {
    List<Plant> getAll();

    List<Plant> getAllInPeriod(Date from, Date to);

    List<Plant> getAliveinPeriod(Date from, Date to);

    List<Plant> getHistory(String id);

    Plant update (Plant newPlant);

    List<Plant> getAlive();

    Plant store(Plant plant);

    Plant get(String id);

    void delete(String id);

    void kill(String id);

    List<Plant> getFromGarden(String id);

    Map<Long, List<Plant>> getListOfPlantWithSameLiveTime();

    Map<Long, List<Plant>> getTheOldests();
}
