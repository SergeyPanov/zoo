package cz.vut.fit.zoo.dao;


import cz.vut.fit.zoo.model.Lake;
import oracle.jdbc.internal.OracleResultSet;

import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * DAO class for communication with "Jezero" table.
 * Implements method declared in HutchDAO interface.
 * Inherits from the AbstractDao class.
 * @author xpanov00
 */
@Repository
public class LakeDAOImpl extends AbstractDao implements LakeDAO {


    /**
     * Method commits the query and return list of "Lake" objects.
     * @param query should should be commit.
     * @return list of "Lake" objects.
     */
    private List<Lake> getWithCondition(String query){
        ArrayList<Lake> lakes = new ArrayList<>();
        try (Connection connection = getDataSource().getConnection()) {

            try (Statement statement = connection.createStatement()) {

                try (OracleResultSet resultSet = (OracleResultSet) statement.executeQuery(query)) {
                    while (resultSet.next()) {
                        lakes.add(new Lake(resultSet));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lakes;

    }

    /**
     * Return single "Lake" object.
     * @param id identifier of the object.
     * @return "Lake" object.
     */
    @Override
    public Lake get(String id) {

        Lake lake = null;
        try (Connection connection = getDataSource().getConnection()) {

            try(Statement statement = connection.createStatement()) {
                try(OracleResultSet resultSet = (OracleResultSet) statement.executeQuery(String.format(Lake.GET, id))) {
                    if (resultSet.next()){
                        lake = new Lake(resultSet);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lake;
    }

    /**
     * Uses getWithCondition() method returns list of objects.
     * @return list of all "Lake" objects.
     */
    @Override
    public List<Lake> getAll() {
        return getWithCondition(Lake.GET_ALL);
    }

    /**
     * Store single "lake" object.
     * @param lake object should be stored
     * @return object was store or null(indicates some problems during storing process).
     */
    @Override
    public Lake store(Lake lake) {
        try (Connection connection = getDataSource().getConnection()){
            lake.setId(UUID.randomUUID().toString());
            try(Statement insertStatement = connection.createStatement()){
                    insertStatement.executeQuery(String.format(Lake.STORE,
                            lake.getId(),
                            Arrays.stream(lake.getGeometry().getElemInfo()).boxed().map(String::valueOf).collect(Collectors.joining(",")),
                            Arrays.stream(lake.getGeometry().getOrdinatesArray()).boxed().map(String::valueOf).collect(Collectors.joining(","))
                            ));
                    return lake;
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
        super.delete(String.format(Lake.DELETE, id));
    }

    /**
     * @return whole space the water covers.
     */
    @Override
    public double getLakeSpace() {

        try(Connection connection = getDataSource().getConnection()){

            try(Statement lakeSpaceStatement = connection.createStatement()){
                try(OracleResultSet resultSet = (OracleResultSet) lakeSpaceStatement.executeQuery(String.format(Lake.GET_LAKE_SPACE))){
                    if (resultSet.next()){
                         return resultSet.getDouble("PLOCHA_JEZER");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


}
