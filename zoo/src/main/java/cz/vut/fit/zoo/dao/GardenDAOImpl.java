package cz.vut.fit.zoo.dao;


import cz.vut.fit.zoo.model.Garden;

import oracle.jdbc.internal.OracleResultSet;
import org.springframework.stereotype.Repository;

import java.sql.Connection;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

/**
 * DAO class for communication with "Zahon" table.
 * Implements method declared in GardenDAO interface.
 * Inherits from the AbstractDao class.
 * @author xpanov00
 */
@Repository
public class GardenDAOImpl extends AbstractDao implements GardenDAO {
    /**
     * Return single "Garden" object.
     * @param id identifier of the object.
     * @return "Garden" object.
     */
    @Override
    public Garden get(String id) {
        Garden garden = null;
        try (Connection connection = getDataSource().getConnection()) {

            try(Statement statement = connection.createStatement()) {
                try(OracleResultSet resultSet = (OracleResultSet) statement.executeQuery(String.format(Garden.GET, id))) {
                    if (resultSet.next()){
                        garden = new Garden(resultSet);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return garden;
    }

    /**
     * Method commits the query and return list of "Garden" objects.
     * @param query should should be commit.
     * @return list of "Garden" objects.
     */
    private List<Garden> getGardensWithCondition(String query){
            ArrayList<Garden> gardens = new ArrayList<>();
            try (Connection connection = getDataSource().getConnection()) {

                try (Statement statement = connection.createStatement()) {

                    try (OracleResultSet resultSet = (OracleResultSet) statement.executeQuery(query)) {
                        while (resultSet.next()) {
                            gardens.add(new Garden(resultSet));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return gardens;
    }

    /**
     * @return list of all "Garden" objects.
     */
    @Override
    public List<Garden> getAll() {
        return getGardensWithCondition(Garden.GET_ALL);
    }

    /**
     * Store single "Garden" object.
     * @param garden object should be stored
     * @return object was store or null(indicates some problems during storing process).
     */
    @Override
    public Garden store(Garden garden) {

        try (Connection connection = getDataSource().getConnection()){

            garden.setId(UUID.randomUUID().toString());
            try(Statement insertStatement = connection.createStatement()){
                insertStatement.executeQuery(String.format(Garden.STORE,
                        garden.getId(),
                        Arrays.stream(garden.getGeometry().getElemInfo()).boxed().map(String::valueOf).collect(Collectors.joining(",")),
                        Arrays.stream(garden.getGeometry().getOrdinatesArray()).boxed().map(String::valueOf).collect(Collectors.joining(",")),
                        garden.getName()
                ));

                return garden;
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
        super.delete(String.format(Garden.DELETE, id));
    }

    /**
     *
     * @return list of gardens in contact with water.
     */
    @Override
    public List<Garden> getGardentInContactWithWater() {
        return getGardensWithCondition(Garden.GET_GARDENS_IN_CONTACT_WITH_WATER);
    }

    /**
     *
     * @return ID of the garden with the biggest Lake in contact.
     */
    @Override
    public String getgardenIDWithTheBiggestLakeInContact() {
        String id = "";

        try (Connection connection = getDataSource().getConnection()) {

            try (Statement statement = connection.createStatement()) {

                try (OracleResultSet resultSet = (OracleResultSet) statement.executeQuery(Garden.GET_GARDEN_WITH_THE_BIGGEST_LAKE_IN_CONTACT)) {
                    while (resultSet.next()) {
                        id = resultSet.getString("ID");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }

    /**
     *
     * @return pair "Garden - Lake" and their intersect area.
     */
    @Override
    public Map<Map<String, String>, Long> getIntersectionWithLakes() {

        Map<Map<String, String>, Long> pairs = new HashMap<>();

        try (Connection connection = getDataSource().getConnection()) {

            try (Statement statement = connection.createStatement()) {

                try (OracleResultSet resultSet = (OracleResultSet) statement.executeQuery(Garden.GET_INTERSECTION_WITH_LAKES)) {
                    while (resultSet.next()) {
                        Map<String, String> pair = new HashMap<>();
                        pair.put(resultSet.getString(1), resultSet.getString(2));
                        pairs.put(pair, resultSet.getLong(3));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pairs;
    }

}
