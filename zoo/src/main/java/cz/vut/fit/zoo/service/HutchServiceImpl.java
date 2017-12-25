package cz.vut.fit.zoo.service;

import cz.vut.fit.zoo.dao.AnimalDAO;
import cz.vut.fit.zoo.dao.HutchDAO;
import cz.vut.fit.zoo.model.Animal;
import cz.vut.fit.zoo.model.Hutch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Class Implements HutchService interface.
 * Is used as service for manipulating with "Hutch" objects.
 * @author xpanov00
 */
@Service
public class HutchServiceImpl implements HutchService {
    private final HutchDAO hutchDAO;

    private final AnimalDAO animalDAO;

    @Autowired
    public HutchServiceImpl(HutchDAO hutchDAO, AnimalDAO animalDAO) {
        this.hutchDAO = hutchDAO;
        this.animalDAO = animalDAO;
    }

    /**
     * @param id identifier of the object.
     * @return single hutch object.
     */
    @Override
    public Hutch get(String id) {
        return hutchDAO.get(id);
    }

    /**
     * @return all hutches.
     */
    @Override
    public List<Hutch> getAll() {
        return hutchDAO.getAll();
    }

    /**
     * @param hutch object should be stored.
     * @return stored object or null.
     */
    @Override
    public Hutch store(Hutch hutch) {
        return hutchDAO.store(hutch);
    }

    /**
     * Call delete() method of DAO class.
     * @param id id of the object should be deleted.
     */
    @Override
    public void delete(String id) {
        hutchDAO.delete(id);
    }

    /**
     * @param id of the hutch.
     * @return list of animals in the hutch.
     */
    @Override
    public List<Animal> getAnimals(String id) {
        return animalDAO.getFromHutch(id);
    }

    @Override
    public Map<Long, List<Hutch>> getTheGiggestHutches() {
        return hutchDAO.getTheGiggestHutches();
    }
}
