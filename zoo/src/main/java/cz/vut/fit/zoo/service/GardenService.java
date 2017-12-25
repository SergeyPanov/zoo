package cz.vut.fit.zoo.service;

import cz.vut.fit.zoo.model.Garden;
import cz.vut.fit.zoo.model.Plant;

import java.util.List;
import java.util.Map;

/**
 * Interface declare methods should be implemented for communication with GardenDAO.
 * @author xpanov00
 */
public interface GardenService {
    List<Garden> getAll();

    Garden get(String id);

    Garden store(Garden garden);

    void delete(String id);

    List<Plant> getPlants(String id);

    List<Garden> getGardentInContactWithWater();

    String getGardenIDWithTheBiggestLakeInContact();

    Map<Map<String, String>, Long> getIntersectionWithLakes();
}
