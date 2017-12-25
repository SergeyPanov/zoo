package cz.vut.fit.zoo.dao;


import cz.vut.fit.zoo.model.Footpath;
import oracle.jdbc.internal.OracleResultSet;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;
/**
 * DAO class for communication with "Stezka" table.
 * Implements method declared in FootpathDAO interface.
 * Inherits from the AbstractDao class.
 * @author xpanov00
 */
@Repository
public class FootpathDAOImpl extends AbstractDao implements FootpathDAO {
    /**
     * Return single "Footpath" object.
     * @param id identifier of the object.
     * @return "Footpath" object.
     */
    @Override
    public Footpath get(String id) {
        Footpath footpath = null;

        try (Connection connection = getDataSource().getConnection()) {

            try(Statement statement = connection.createStatement()) {
                try(OracleResultSet resultSet = (OracleResultSet) statement.executeQuery(String.format(Footpath.GET, id))) {
                    if (resultSet.next()){
                        footpath = new Footpath(resultSet);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return footpath;
    }

    /**
     * @return list of all "Footpath" objects.
     */
    @Override
    public List<Footpath> getAll() {
        ArrayList<Footpath> footpaths = new ArrayList<>();

        try (Connection connection = getDataSource().getConnection()) {


            try (Statement statement = connection.createStatement()) {

                try (OracleResultSet resultSet = (OracleResultSet) statement.executeQuery(Footpath.GET_ALL)) {
                    while (resultSet.next()) {
                        footpaths.add(new Footpath(resultSet));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return footpaths;
    }

    /**
     * Store single "Footpath" object.
     * @param footpath object should be stored
     * @return object was store or null(indicates some problems during storing process).
     */
    @Override
    public Footpath store(Footpath footpath) {

        try (Connection connection = getDataSource().getConnection()){


            footpath.setId(UUID.randomUUID().toString());
            try(Statement insertStatement = connection.createStatement()){
                insertStatement.executeQuery(String.format(Footpath.STORE,
                        footpath.getId(),
                        Arrays.stream(footpath.getGeometry().getElemInfo()).boxed().map(String::valueOf).collect(Collectors.joining(",")),
                        Arrays.stream(footpath.getGeometry().getOrdinatesArray()).boxed().map(String::valueOf).collect(Collectors.joining(","))
                ));

                return footpath;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Call parent method "delete" with own query for deleting object.
     * @param id identifier od object needs to be deleted.
     */
    @Override
    public void delete(String id) {
        super.delete(String.format(Footpath.DELETE, id));
    }

    @Override
    public List<Map<String, String>> getIntersectFootpathes() {

        List<Map<String, String>> pathes = new ArrayList<>();
        try (Connection connection = getDataSource().getConnection()) {
            try (Statement statement = connection.createStatement()) {

                try (OracleResultSet resultSet = (OracleResultSet) statement.executeQuery(Footpath.GET_INTERSECT_FOOTPATHES)) {
                    while (resultSet.next()) {
                        Map<String, String> pair = new HashMap<>();
                        pair.put(resultSet.getString(1), resultSet.getString(2));
                        pathes.add(pair);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pathes;
    }
}
