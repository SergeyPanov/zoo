package cz.vut.fit.zoo.dao;

import cz.vut.fit.zoo.model.Hutch;

import java.util.List;
import java.util.Map;

/**
 * Interface defines method should be implemented for communication with "Kotec" table.
 * @author xpanov00
 */
public interface HutchDAO {
    Hutch get(String id);

    List<Hutch> getAll();

    Hutch store(Hutch hutch);

    void delete(String id);

    Map<Long, List<Hutch>> getTheGiggestHutches();
}
