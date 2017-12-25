package cz.vut.fit.zoo.dao;

import cz.vut.fit.zoo.model.Hutch;
import oracle.jdbc.internal.OracleResultSet;
import org.springframework.stereotype.Repository;

import java.sql.Connection;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

/**
 * DAO class for communication with "Kotec" table.
 * Implements method declared in HutchDAO interface.
 * Inherits from the AbstractDao class.
 * @author xpanov00
 */
@Repository
public class HutchDAOImpl extends AbstractDao implements HutchDAO {
    /**
     * Return single "Hutch" object.
     * @param id identifier of the object.
     * @return "Hutch" object.
     */
    @Override
    public Hutch get(String id) {
        Hutch hutch = null;

        try (Connection connection = getDataSource().getConnection()) {

            try(Statement statement = connection.createStatement()) {
                try(OracleResultSet resultSet = (OracleResultSet) statement.executeQuery(String.format(Hutch.GET, id))) {
                    if (resultSet.next()){
                        hutch = new Hutch(resultSet);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hutch;
    }

    /**
     * @return list of all "Hutch" objects.
     */
    @Override
    public List<Hutch> getAll() {
        ArrayList<Hutch> hutches = new ArrayList<>();

        try (Connection connection = getDataSource().getConnection()) {


            try (Statement statement = connection.createStatement()) {

                try (OracleResultSet resultSet = (OracleResultSet) statement.executeQuery(Hutch.GET_ALL)) {
                    while (resultSet.next()) {
                        hutches.add(new Hutch(resultSet));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hutches;
    }

    /**
     * Store single "Hutch" object.
     * @param hutch object should be stored
     * @return object was store or null(indicates some problems during storing process).
     */
    @Override
    public Hutch store(Hutch hutch) {

        try (Connection connection = getDataSource().getConnection()){

            hutch.setId(UUID.randomUUID().toString());
            try(Statement insertStatement = connection.createStatement()){
                insertStatement.executeQuery(String.format(Hutch.STORE,
                        hutch.getId(),
                        Arrays.stream(hutch.getGeometry().getElemInfo()).boxed().map(String::valueOf).collect(Collectors.joining(",")),
                        Arrays.stream(hutch.getGeometry().getOrdinatesArray()).boxed().map(String::valueOf).collect(Collectors.joining(","))
                ));

                return hutch;
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
        super.delete(String.format(Hutch.DELETE, id));
    }

    @Override
    public Map<Long, List<Hutch>> getTheGiggestHutches() {

        Map<Long, List<Hutch>> theBiggestHutches = new HashMap<>();

        ArrayList<Hutch> hutches = new ArrayList<>();
        long space = -1;

        try (Connection connection = getDataSource().getConnection()) {


            try (Statement statement = connection.createStatement()) {

                try (OracleResultSet resultSet = (OracleResultSet) statement.executeQuery(Hutch.GET_HUTCHES_WITH_THE_BIGGEST_SPACE)) {
                    while (resultSet.next()) {
                        hutches.add(new Hutch(resultSet));
                        if (space == -1){
                            space = resultSet.getLong("plocha");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (space >= 0){
            theBiggestHutches.put(space, hutches);
        }

        return theBiggestHutches;

    }
}
