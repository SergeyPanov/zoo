package cz.vut.fit.zoo.service;

import cz.vut.fit.zoo.model.Plant;

import java.time.Period;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Interface declare methods should be implemented for communication with PlantDAO.
 * @author xpanov00
 */
public interface PlantService {
    List<Plant> getAll();

    List<Plant> getAllInPeriod(Date from, Date to);

    List<Plant> getAliveinPeriod(Date from, Date to);

    List<Plant> getHistory(String id);

    List<Plant> getAlive();

    Plant update (Plant newPlant);

    Plant get(String id);

    void delete(String id);

    Plant store(Plant plant);

    void kill(String id);

    Map<Long, List<Plant>> getListOfPlantWithSameLiveTime();

    Map<Long, List<Plant>> getTheOldests();
}
