package cz.vut.fit.zoo.service;

import cz.vut.fit.zoo.dao.GardenDAO;
import cz.vut.fit.zoo.dao.PlantDAO;
import cz.vut.fit.zoo.model.Garden;
import cz.vut.fit.zoo.model.Plant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Class Implements GardenService interface.
 * Is used as service for manipulating with "Garden" objects.
 * @author xpanov00
 */
@Service
public class GardenServiceImpl implements GardenService {
    private final GardenDAO gardenDAO;


    private final PlantDAO plantDAO;

    @Autowired
    public GardenServiceImpl(GardenDAO gardenDAO, PlantDAO plantDAO) {
        this.gardenDAO = gardenDAO;
        this.plantDAO = plantDAO;
    }

    /**
     * @return all gardens.
     */
    @Override
    public List<Garden> getAll() {
        return gardenDAO.getAll();
    }

    /**
     * @param id of the garden.
     * @return particular garden.
     */
    @Override
    public Garden get(String id) {
        return gardenDAO.get(id);
    }

    /**
     * @param garden object should be stored.
     * @return stored object or null.
     */
    @Override
    public Garden store(Garden garden) {
        return gardenDAO.store(garden);
    }

    /**
     * Call delete() method of DAO class.
     * @param id id of the object should be deleted.
     */
    @Override
    public void delete(String id) {
        gardenDAO.delete(id);
    }

    /**
     * @param id of the garden.
     * @return all plants in the garden.
     */
    @Override
    public List<Plant> getPlants(String id) {
        return plantDAO.getFromGarden(id);
    }

    /**
     *
     * @return List of Gardens in contact with water.
     */
    @Override
    public List<Garden> getGardentInContactWithWater() {
        return gardenDAO.getGardentInContactWithWater();
    }

    /**
     *
     * @return ID of the garden which is in contact with the biggest lake.
     */
    @Override
    public String getGardenIDWithTheBiggestLakeInContact() {
        return gardenDAO.getgardenIDWithTheBiggestLakeInContact();
    }

    /**
     *
     * @return Pair of lake's iID and garden's ID and their intersect area.
     */
    @Override
    public Map<Map<String, String>, Long> getIntersectionWithLakes() {
        return gardenDAO.getIntersectionWithLakes();
    }
}
