package cz.vut.fit.zoo.service;


import cz.vut.fit.zoo.dao.AnimalDAO;
import cz.vut.fit.zoo.model.Animal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Class Implements AnimalService interface.
 * Is used as service for manipulating with "Animal" objects.
 * @author xpanov00
 */
@Service
public class AnimalServiceImpl implements AnimalService {
    private final AnimalDAO animalDAO;


    @Autowired
    public AnimalServiceImpl(AnimalDAO animalDAO) {
        this.animalDAO = animalDAO;
    }

    /**
     * @return list of all animals.
     */
    @Override
    public List<Animal> getAll() {
        return animalDAO.getAll();
    }

    /**
     * @param id identifier of the object.
     * @return Animal object
     */
    @Override
    public Animal get(String id) {
        return animalDAO.get(id);
    }

    /**
     * Call store() method of DAO class.
     * @param animal object should be stored.
     * @return stored object of null.
     */
    @Override
    public Animal store(Animal animal) {
        return animalDAO.store(animal);
    }

    /**
     * Call delete() method of DAO class.
     * @param id id of the object should be deleted.
     */
    @Override
    public void delete(String id) {
        animalDAO.delete(id);
    }

    @Override
    public Map<Long, List<Animal>> getOlderAnimalsThanOldestPlant() {
        return animalDAO.getOlderAnimalsThanOldestPlant();
    }
}
