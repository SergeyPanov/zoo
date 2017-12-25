package cz.vut.fit.zoo.service;

import cz.vut.fit.zoo.model.Animal;


import java.util.List;
import java.util.Map;

/**
 * Interface declare methods should be implemented for communication with AnimalDAO.
 * @author xpanov00
 */
public interface AnimalService {
    List<Animal> getAll();

    Animal get(String id);

    Animal store(Animal animal);

    void delete(String id);

    Map<Long, List<Animal>> getOlderAnimalsThanOldestPlant();
}
