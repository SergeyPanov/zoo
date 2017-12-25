package cz.vut.fit.zoo.dao;


import cz.vut.fit.zoo.model.Footpath;

import java.util.List;
import java.util.Map;

/**
 * Interface defines method should be implemented for communication with "Stezka" table.
 * @author xpanov00
 */
public interface FootpathDAO {

    Footpath get(String id);

    List<Footpath> getAll();

    Footpath store(Footpath footpath);

    void delete(String id);

    List<Map<String, String>> getIntersectFootpathes();

}
