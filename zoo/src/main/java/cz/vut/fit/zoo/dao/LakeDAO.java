package cz.vut.fit.zoo.dao;

import cz.vut.fit.zoo.model.Lake;

import java.util.List;

/**
 * Interface defines method should be implemented for communication with "Jezero" table.
 * @author xpanov00
 */
public interface LakeDAO {
    Lake get(String id);

    List<Lake> getAll();

    Lake store(Lake lake);

    void delete(String id);

    double getLakeSpace();

}
