package cz.vut.fit.zoo.service;

import cz.vut.fit.zoo.model.Footpath;

import java.util.List;
import java.util.Map;

/**
 * Interface declare methods should be implemented for communication with FootpathDAO.
 * @author xpanov00
 */
public interface FootpathService {

    Footpath get(String id);

    List<Footpath> getAll();

    Footpath store(Footpath footpath);

    void delete(String id);

    List<Map<String, String>> getIntersectFootpathes();
}
