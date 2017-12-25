package cz.vut.fit.zoo.service;

import cz.vut.fit.zoo.model.Lake;

import java.util.List;
/**
 * Interface declare methods should be implemented for communication with LakeDAO.
 * @author xpanov00
 */
public interface LakeService {
    Lake get(String id);
    List<Lake> getAll();
    Lake store(Lake lake);
    void delete(String id);
    double getLakeSpace();
}
