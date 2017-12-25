package cz.vut.fit.zoo.service;

import cz.vut.fit.zoo.dao.PlantDAO;
import cz.vut.fit.zoo.model.Plant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Class Implements PlantService interface.
 * Is used as service for manipulating with "Plant" objects.
 * @author xpanov00
 */
@Service
public class PlantServiceImpl implements PlantService{

    private final PlantDAO plantDAO;

    @Autowired
    public PlantServiceImpl(PlantDAO plantDAO) {
        this.plantDAO = plantDAO;
    }

    /**

     * @return all plants.
     */
    @Override
    public List<Plant> getAll() {
        return plantDAO.getAll();
    }

    /**
     * @param from beginning of the period.
     * @param to end of the period.
     * @return list of plants in the period.
     */
    @Override
    public List<Plant> getAllInPeriod(Date from, Date to) {
        return plantDAO.getAllInPeriod(from, to);
    }

    /**
     * @param from beginning of the period.
     * @param to end of the period.
     * @return list of alive plants in the period.
     */
    @Override
    public List<Plant> getAliveinPeriod(Date from, Date to) {
        return plantDAO.getAliveinPeriod(from, to);
    }

    /**
     *
     * @param id plant identifier.
     * @return history of changes.
     */
    @Override
    public List<Plant> getHistory(String id) {
        return plantDAO.getHistory(id);
    }

    /**
     *
     * @return all alive plants.
     */
    @Override
    public List<Plant> getAlive() {
        return plantDAO.getAlive();
    }

    /**
     *
     * @param newPlant updated "Plant" object.
     * @return updated plant or null.
     */
    @Override
    public Plant update(Plant newPlant) {
        return plantDAO.update(newPlant);
    }

    /**
     *
     * @param id identifier.
     * @return particular "Plant" object.
     */
    @Override
    public Plant get(String id) {
        return plantDAO.get(id);
    }

    /**
     * Call delete() method of DAO class.
     * @param id id of the object should be deleted.
     */
    @Override
    public void delete(String id) {
        plantDAO.delete(id);
    }

    /**
     *
     * @param plant should be stored.
     * @return stored plant or null.
     */
    @Override
    public Plant store(Plant plant) {
        return plantDAO.store(plant);
    }

    /**
     * Kill single plant.
     * @param id identifier.
     */
    @Override
    public void kill(String id) {
        plantDAO.kill(id);
    }

    @Override
    public Map<Long, List<Plant>> getListOfPlantWithSameLiveTime() {
        return plantDAO.getListOfPlantWithSameLiveTime();
    }

    @Override
    public Map<Long, List<Plant>> getTheOldests() {
        return plantDAO.getTheOldests();
    }
}
