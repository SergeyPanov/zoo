package cz.vut.fit.zoo.dao;

import cz.vut.fit.zoo.model.Animal;
import cz.vut.fit.zoo.model.Plant;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import oracle.ord.im.OrdImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.IntStream;

/**
 * DAO class for communication with "Zvire" table.
 * Implements method declared in AnimalDAO interface.
 * Inherits from the AbstractDao class.
 * @author xpanov00
 */
@Repository
public class AnimalDAOImpl extends AbstractDao implements AnimalDAO {

    /**
     * Date format management.
     */
    private final SimpleDateFormat dateFormat;

    private final PlantDAO plantDAO;

    @Autowired
    public AnimalDAOImpl(@Qualifier("simpleDateFormat") SimpleDateFormat dateFormat, PlantDAO plantDAO) {
        this.dateFormat = dateFormat;
        this.plantDAO = plantDAO;
    }

    /**
     * Method commits the query and return list of "Animal" objects.
     * @param query should should be commit.
     * @return list of "Animal" objects.
     */
    private List<Animal> getWithCondition(String query){
        ArrayList<Animal> animalsl = new ArrayList<>();
        try(Connection connection = getDataSource().getConnection()) {


            try (Statement statement = connection.createStatement()) {

                try (OracleResultSet resultSet = (OracleResultSet) statement.executeQuery(query)) {
                    while (resultSet.next()) {
                        animalsl.add(new Animal(resultSet));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return animalsl;
    }

    /**
     * Uses getWithCondition() method returns list of objects.
     * @return list of all "Animal" objects.
     */
    @Override
    public List<Animal> getAll() {
        return getWithCondition(Animal.GET_ALL);
    }


    /**
     * Store single "Animal" object.
     * @param animal object should be stored
     * @return object was store or null(indicates some problems during storing process).
     */
    @Override
    public Animal store(Animal animal) {

        try(Connection connection = getDataSource().getConnection()) {
            connection.setAutoCommit(false);

            animal.setId(UUID.randomUUID().toString());

            Statement statement = connection.createStatement();

            // Insert new object with empty ORDImage object
            statement.executeUpdate(String.format(Animal.STORE,
                    animal.getId(),
                    animal.getName(),
                    animal.getSpecies(),
                    animal.getFrom() == null ? dateFormat.format(new Date()) : dateFormat.format(animal.getFrom()),
                    animal.getTo() == null ? String.valueOf("null") : "'" + dateFormat.format(animal.getTo()) + "'",
                    animal.getHutchId()
                    ));
            statement.close();

            // retrieve the previously created ORDImage object for future updating
            Statement statement1 = connection.createStatement();
            OracleResultSet resultSet = (OracleResultSet) statement1.executeQuery(String.format(Animal.GET_PHOTO, animal.getId()));
            resultSet.next();
            OrdImage imgProxy = (OrdImage) resultSet.getORAData("fotka", OrdImage.getORADataFactory());
            resultSet.close();
            statement1.close();

            // load the media data from a file to the ORDImage Java object
            imgProxy.loadDataFromByteArray(animal.getPhoto());
            imgProxy.setProperties();

            // update the table with ORDImage Java object (data already loaded)
            OraclePreparedStatement preparedStatement = (OraclePreparedStatement)
                    connection.prepareStatement(String.format(Animal.UPDATE_PHOTO_1, animal.getId()));
            preparedStatement.setORAData(1, imgProxy);
            preparedStatement.executeUpdate();
            preparedStatement.close();

            // update the table with StillImage object and features
            Statement statement2 = connection.createStatement();
            statement2.executeUpdate(String.format(Animal.UPDATE_PHOTO_2, animal.getId()));
            statement2.executeUpdate(String.format(Animal.UPDATE_PHOTO_3, animal.getId()));
            statement2.close();
            connection.commit();

            return animal;

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * Return single "Animal" object.
     * @param id identifier of the object.
     * @return "Animal" object.
     */
    @Override
    public Animal get(String id) {
        Animal animal = null;
        try(Connection connection = getDataSource().getConnection()) {


            try (Statement statement = connection.createStatement()) {

                try (OracleResultSet resultSet = (OracleResultSet) statement.executeQuery(String.format(Animal.GET, id))) {
                    if (resultSet.next()) {
                        animal = new Animal(resultSet);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return animal;
    }


    /**
     * Call parent method "delete" with own query for deleting object.
     * @param id identifier od object needs to be deleted.
     */
    @Override
    public void delete(String id) {
        super.delete(String.format(Animal.DELETE, id));
    }

    /**
     * Use getWithCondition private method for getting list of animals from the hutch.
     * @param id of hitch with animals.
     * @return list of animals in the hutch
     */
    @Override
    public List<Animal> getFromHutch(String id) {
        return getWithCondition(String.format(Animal.GET_FROM_HUTCH, id));
    }

    @Override
    public Map<Long, List<Animal>> getOlderAnimalsThanOldestPlant() {
        Map<Long, List<Animal>> olderAnimals = new HashMap<>();

        Map<Long, List<Plant>> map = plantDAO.getTheOldests();
        if ( ! map.isEmpty() ){
            List<Animal> animals = getWithCondition(Animal.GET_ALIVE_ANIMALS);
            Long liveTime = Collections.max(map.keySet());
            olderAnimals.put(liveTime, new ArrayList<>());
            animals.forEach(a -> {
                if (
                       Math.abs((new Date()) .getTime() - a.getFrom().getTime()) / (1000*60*60*24) >= liveTime
                        ){
                    olderAnimals.get(liveTime).add(a);
                }
            });
       }
        return olderAnimals;
    }
}
