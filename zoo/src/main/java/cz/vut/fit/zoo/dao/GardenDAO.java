package cz.vut.fit.zoo.dao;

import cz.vut.fit.zoo.model.Garden;


import java.util.List;
import java.util.Map;

/**
 * Interface defines method should be implemented for communication with "Zahon" table.
 * @author xpanov00
 */
public interface GardenDAO {

    Garden get(String id);

    List<Garden> getAll();

    Garden store(Garden garden);

    void delete(String id);

    List<Garden> getGardentInContactWithWater();

    String getgardenIDWithTheBiggestLakeInContact();

    Map<Map<String, String>, Long> getIntersectionWithLakes();
}
