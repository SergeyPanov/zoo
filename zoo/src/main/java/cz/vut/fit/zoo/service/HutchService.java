package cz.vut.fit.zoo.service;

import cz.vut.fit.zoo.model.Animal;
import cz.vut.fit.zoo.model.Hutch;

import java.util.List;
import java.util.Map;

/**
 * Interface declare methods should be implemented for communication with HutchDAO.
 * @author xpanov00
 */
public interface HutchService {
    Hutch get(String id);

    List<Hutch> getAll();

    Hutch store(Hutch hutch);

    void delete(String id);

    List<Animal> getAnimals(String id);

    Map<Long, List<Hutch>> getTheGiggestHutches();
}
