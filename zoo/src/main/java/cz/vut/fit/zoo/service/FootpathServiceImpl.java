package cz.vut.fit.zoo.service;

import cz.vut.fit.zoo.dao.FootpathDAO;
import cz.vut.fit.zoo.model.Footpath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Class Implements FootpathService interface.
 * Is used as service for manipulating with "Footpath" objects.
 * @author xpanov00
 */
@Service
public class FootpathServiceImpl implements FootpathService {

    private final FootpathDAO footpathDAO;

    @Autowired
    public FootpathServiceImpl(FootpathDAO footpathDAO) {
        this.footpathDAO = footpathDAO;
    }

    /**
     * @param id identifier of the object.
     * @return Footpath object
     */
    @Override
    public Footpath get(String id) {
        return footpathDAO.get(id);
    }

    /**
     * @return list of all footpaths.
     */
    @Override
    public List<Footpath> getAll() {
        return footpathDAO.getAll();
    }

    /**
     * @param footpath object should be stored.
     * @return stored object or null.
     */
    @Override
    public Footpath store(Footpath footpath) {
        return footpathDAO.store(footpath);
    }

    /**
     * Call delete() method of DAO class.
     * @param id id of the object should be deleted.
     */
    @Override
    public void delete(String id) {
        footpathDAO.delete(id);
    }

    @Override
    public List<Map<String, String>> getIntersectFootpathes() {
        return footpathDAO.getIntersectFootpathes();
    }
}
