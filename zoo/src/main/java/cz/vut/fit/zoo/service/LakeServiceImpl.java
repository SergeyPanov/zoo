package cz.vut.fit.zoo.service;

import cz.vut.fit.zoo.dao.LakeDAO;
import cz.vut.fit.zoo.model.Lake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Class Implements LakeService interface.
 * Is used as service for manipulating with "lake" objects.
 * @author xpanov00
 */
@Service
public class LakeServiceImpl implements LakeService {

    private final LakeDAO lakeDAO;

    @Autowired
    public LakeServiceImpl(LakeDAO lakeDAO) {
        this.lakeDAO = lakeDAO;
    }

    /**
     * @param id of the object.
     * @return "Lake" object.
     */
    @Override
    public Lake get(String id) {
        return lakeDAO.get(id);
    }

    /**
     *
     * @return all lakes.
     */
    @Override
    public List<Lake> getAll() {
        return lakeDAO.getAll();
    }

    /**
     *
     * @param lake object should be stored.
     * @return stored object or null.
     */
    @Override
    public Lake store(Lake lake) {
        return lakeDAO.store(lake);
    }

    /**
     * Call delete() method of DAO class.
     * @param id id of the object should be deleted.
     */
    @Override
    public void delete(String id) {
        lakeDAO.delete(id);
    }

    @Override
    public double getLakeSpace() {
        return lakeDAO.getLakeSpace();
    }
}
