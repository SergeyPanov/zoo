package cz.vut.fit.zoo.dao;

import cz.vut.fit.zoo.model.Animal;

import java.util.List;
import java.util.Map;

/**
 * Interface defines method should be implemented for communication with "Zvire" table.
 * @author xpanov00
 */
public interface AnimalDAO {
    List<Animal> getAll();

    Animal store(Animal animal);

    Animal get(String id);

    void delete(String id);

    List<Animal> getFromHutch(String id);

    Map<Long, List<Animal>> getOlderAnimalsThanOldestPlant();
}
